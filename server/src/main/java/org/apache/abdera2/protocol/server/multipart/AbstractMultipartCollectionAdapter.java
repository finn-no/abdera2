/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  For additional information regarding
 * copyright in this work, please see the NOTICE file in the top level
 * directory of this distribution.
 */
package org.apache.abdera2.protocol.server.multipart;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.activation.MimeType;
import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.internet.InternetHeaders;

import org.apache.abdera2.model.Document;
import org.apache.abdera2.model.Element;
import org.apache.abdera2.model.Entry;
import org.apache.abdera2.parser.ParseException;
import org.apache.abdera2.parser.Parser;
import org.apache.abdera2.protocol.server.impl.AbstractAtompubCollectionAdapter;
import org.apache.abdera2.protocol.server.impl.AbstractAtompubProvider;
import org.apache.abdera2.common.Constants;
import org.apache.abdera2.common.io.MultipartInputStream;
import org.apache.abdera2.common.protocol.AbstractCollectionAdapter;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.commons.codec.binary.Base64;
import static org.apache.abdera2.common.mediatype.MimeTypeHelper.*;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

import static org.apache.abdera2.common.misc.ExceptionHelper.*;
import static com.google.common.base.Preconditions.*;

@SuppressWarnings("unchecked")
public abstract class AbstractMultipartCollectionAdapter 
  extends AbstractAtompubCollectionAdapter 
  implements MultipartRelatedCollectionInfo {

    public AbstractMultipartCollectionAdapter(String href) {
      super(href);
    }

    private static final String CONTENT_TYPE_HEADER = "content-type";
    private static final String CONTENT_ID_HEADER = "content-id";
    private static final String START_PARAM = "start";
    private static final String TYPE_PARAM = "type";
    private static final String BOUNDARY_PARAM = "boundary";

    protected Map<String, String> accepts;

    public Predicate<RequestContext> acceptable() {
      return Predicates.or(
        AbstractCollectionAdapter.HAS_NO_ENTITY,
        new Predicate<RequestContext>() {
        public boolean apply(RequestContext input) {
          MimeType mt = input.getContentType();
          if (mt == null) return false;
          return isMultipart(mt.toString()) ||
                 isAtom(mt.toString());
        }
      });
    }
    
    public Iterable<String> getAccepts(RequestContext request) {
        Collection<String> acceptKeys = getAlternateAccepts(request).keySet();
        return Iterables.unmodifiableIterable(acceptKeys);
    }

    protected MultipartRelatedPost getMultipartRelatedData(
      RequestContext request) 
        throws IOException, 
               ParseException,
               MessagingException {
      MultipartInputStream multipart = getMultipartStream(request);
      multipart.skipBoundary();
      String start = request.getContentType().getParameter(START_PARAM);
      Document<Entry> entry = null;
      Map<String, String> entryHeaders = new HashMap<String, String>();
      InputStream data = null;
      Map<String, String> dataHeaders = new HashMap<String, String>();
      Map<String, String> headers = getHeaders(multipart);
        // check if the first boundary is the media link entry
      if (start == null || start.length() == 0 || 
         (headers.containsKey(CONTENT_ID_HEADER) && 
          start.equals(headers.get(CONTENT_ID_HEADER))) || 
         (headers.containsKey(CONTENT_TYPE_HEADER) && 
          isAtom(headers.get(CONTENT_TYPE_HEADER)))) {
        entry = getEntry(multipart, request);
        entryHeaders.putAll(headers);
      } else {
        data = getDataInputStream(multipart);
        dataHeaders.putAll(headers);
      }
      multipart.skipBoundary();
      headers = getHeaders(multipart);

      if (start != null && 
          (headers.containsKey(CONTENT_ID_HEADER) && 
           start.equals(headers.get(CONTENT_ID_HEADER))) && 
          (headers.containsKey(CONTENT_TYPE_HEADER) && 
           isAtom(headers.get(CONTENT_TYPE_HEADER)))) {
        entry = getEntry(multipart, request);
        entryHeaders.putAll(headers);
      } else {
        data = getDataInputStream(multipart);
        dataHeaders.putAll(headers);
      }
      checkMultipartContent(entry, dataHeaders, request);
      return new MultipartRelatedPost(
        entry, 
        data, 
        entryHeaders, 
        dataHeaders);
    }

    private MultipartInputStream getMultipartStream(
      RequestContext request) 
        throws IOException, ParseException {
      String boundary = request.getContentType().getParameter(BOUNDARY_PARAM);
      checked(
        boundary != null,
        ParseException.class,
        "multipart/related stream invalid, boundary parameter is missing.");
      boundary = "--" + boundary;
      String type = request.getContentType().getParameter(TYPE_PARAM);     
      checked(
        type != null && isAtom(type),
        ParseException.class,
        "multipart/related stream invalid, type parameter should be ",
        Constants.ATOM_MEDIA_TYPE);
      PushbackInputStream pushBackInput = 
        new PushbackInputStream(request.getInputStream(), 2);
      pushBackInput.unread("\r\n".getBytes());
      return new MultipartInputStream(pushBackInput, boundary.getBytes());
    }

    private void checkMultipartContent(
      Document<Entry> entry, 
      Map<String, String> dataHeaders, 
      RequestContext request)
        throws ParseException {
      checked(
        entry != null, 
        ParseException.class, 
        "multipart/related stream invalid, media link entry is missing");
      checked(
        dataHeaders.containsKey(CONTENT_TYPE_HEADER), 
        ParseException.class,
        "multipart/related stream invalid, data content-type is missing");
      checked(
        isContentTypeAccepted(dataHeaders.get(CONTENT_TYPE_HEADER),request),
        ParseException.class,
        "multipart/related stream invalid, content-type is not acceptable", 
        dataHeaders.get(CONTENT_TYPE_HEADER));
    }

    private Map<String, String> getHeaders(
      MultipartInputStream multipart) 
        throws IOException, 
               MessagingException {
        Map<String, String> mapHeaders = new HashMap<String, String>();
        moveToHeaders(multipart);
        InternetHeaders headers = new InternetHeaders(multipart);
        Enumeration<Header> allHeaders = headers.getAllHeaders();
        if (allHeaders != null) {
          while (allHeaders.hasMoreElements()) {
            Header header = allHeaders.nextElement();
            mapHeaders.put(header.getName().toLowerCase(), header.getValue());
          }
        }
        return mapHeaders;
    }

    private boolean moveToHeaders(
      InputStream stream) 
        throws IOException {
      boolean dash = false;
      boolean cr = false;
      int byteReaded;
      while ((byteReaded = stream.read()) != -1) {
          switch (byteReaded) {
              case '\r':
                  cr = true;
                  dash = false;
                  break;
              case '\n':
                  if (cr == true)
                      return true;
                  dash = false;
                  break;
              case '-':
                  if (dash == true) { // two dashes
                      stream.close();
                      return false;
                  }
                  dash = true;
                  cr = false;
                  break;
              default:
                  dash = false;
                  cr = false;
          }
      }
      return false;
    }

    private InputStream getDataInputStream(
      InputStream stream) 
        throws IOException {
        Base64 base64 = new Base64();
        ByteArrayOutputStream bo = 
          new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (stream.read(buffer) != -1)
          bo.write(buffer);
        return new ByteArrayInputStream(
          base64.decode(bo.toByteArray()));
    }

    private <T extends Element> Document<T> getEntry(
      InputStream stream, 
      RequestContext request) 
        throws ParseException,
               IOException {
        Parser parser = AbstractAtompubProvider.getAbdera(request).getParser();
        checkNotNull(parser,"No parser implementation provided");
        Document<?> document =
          parser.parse(
            stream, 
            request.getResolvedUri().toString(), 
            parser.getDefaultParserOptions());
        return (Document<T>)document;
    }

    private boolean isContentTypeAccepted(
      String contentType, 
      RequestContext request) {
        if (getAlternateAccepts(request) == null)
          return false;
        for (Map.Entry<String, String> accept : getAlternateAccepts(request).entrySet())
          if (isMatch(contentType, accept.getKey()) &&
              accept.getValue() != null && 
              accept.getValue().equalsIgnoreCase(
                Constants.LN_ALTERNATE_MULTIPART_RELATED))
              return true;
        return false;
    }

    protected class MultipartRelatedPost {
        private final Document<Entry> entry;
        private final InputStream data;
        private final Map<String, String> entryHeaders;
        private final Map<String, String> dataHeaders;

        public MultipartRelatedPost(
          Document<Entry> entry,
          InputStream data,
          Map<String, String> entryHeaders,
          Map<String, String> dataHeaders) {
            this.entry = entry;
            this.data = data;
            this.entryHeaders = entryHeaders;
            this.dataHeaders = dataHeaders;
        }

        public Document<Entry> getEntry() {
          return entry;
        }

        public InputStream getData() {
          return data;
        }

        public Map<String, String> getEntryHeaders() {
          return entryHeaders;
        }

        public Map<String, String> getDataHeaders() {
          return dataHeaders;
        }
    }
}
