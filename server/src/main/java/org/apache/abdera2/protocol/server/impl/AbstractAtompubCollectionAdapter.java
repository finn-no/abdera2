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
package org.apache.abdera2.protocol.server.impl;

import java.util.Arrays;
import java.util.Collections;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.factory.Factory;
import org.apache.abdera2.common.protocol.AbstractCollectionAdapter;
import org.apache.abdera2.common.protocol.ProviderHelper;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.ResponseContext;
import org.apache.abdera2.common.protocol.ResponseContextException;
import org.apache.abdera2.model.Collection;
import org.apache.abdera2.model.Document;
import org.apache.abdera2.model.Entry;
import org.apache.abdera2.model.Feed;
import org.apache.abdera2.parser.ParseException;
import org.apache.abdera2.parser.Parser;
import org.apache.abdera2.protocol.server.context.FOMResponseContext;
import org.apache.abdera2.protocol.server.model.AtompubCategoriesInfo;
import org.apache.abdera2.protocol.server.model.AtompubCollectionInfo;
import org.joda.time.DateTime;

import com.google.common.base.Predicate;

/**
 * Base CollectionAdapter implementation that provides a number of helper utility methods for adapter implementations.
 */
public abstract class AbstractAtompubCollectionAdapter 
  extends AbstractCollectionAdapter
  implements AtompubCollectionInfo {

    public AbstractAtompubCollectionAdapter(String href) {
    super(href);
  }

    public Iterable<String> getAccepts(RequestContext request) {
      return Arrays.<String>asList("application/atom+xml;type=entry");
    }
    
    public Iterable<AtompubCategoriesInfo> getCategoriesInfo(RequestContext request) {
      return Collections.<AtompubCategoriesInfo>emptySet();
    }
    
    /**
     * Creates the ResponseContext for a newly created entry. By default, a BaseResponseContext is returned. The
     * Location, Content-Location, Etag and status are set appropriately.
     */
    protected ResponseContext buildCreateEntryResponse(String link, Entry entry) {
        return new FOMResponseContext<Entry>(entry)
          .setLocation(link)
          .setContentLocation(link)
          .setEntityTag(AbstractAtompubProvider.calculateEntityTag(entry))
          .setStatus(201);
    }

    /**
     * Creates the ResponseContext for a newly created entry. By default, a BaseResponseContext is returned. The
     * Location, Content-Location, Etag and status are set appropriately.
     */
    protected ResponseContext buildPostMediaEntryResponse(String link, Entry entry) {
      return buildCreateEntryResponse(link, entry);
    }

    /**
     * Creates the ResponseContext for a GET entry request. By default, a BaseResponseContext is returned. The Entry
     * will contain an appropriate atom:source element and the Etag header will be set.
     */
    protected ResponseContext buildGetEntryResponse(RequestContext request, Entry entry)
        throws ResponseContextException {
        Feed feed = createFeedBase(request);
        entry.setSource(feed.getAsSource());
        Document<Entry> entry_doc = entry.getDocument();
        return new FOMResponseContext<Document<Entry>>(entry_doc)
          .setEntityTag(AbstractAtompubProvider.calculateEntityTag(entry));
    }

    /**
     * Creates the ResponseContext for a GET feed request. By default, a BaseResponseContext is returned. The Etag
     * header will be set.
     */
    protected ResponseContext buildGetFeedResponse(Feed feed) {
        Document<Feed> document = feed.getDocument();
        return new FOMResponseContext<Document<Feed>>(document)
          .setEntityTag(AbstractAtompubProvider.calculateEntityTag(document.getRoot()));
    }

    /**
     * Create the base feed for the requested collection.
     */
    protected Feed createFeedBase(RequestContext request) throws ResponseContextException {
        Factory factory = AbstractAtompubProvider.getAbdera(request).getFactory();
        Feed feed = factory.newFeed();
        feed.setId(getId(request));
        feed.setTitle(getTitle(request));
        feed.addLink("");
        feed.addLink("", "self");
        feed.addAuthor(getAuthor(request));
        feed.setUpdated(DateTime.now());
        return feed;
    }

    /**
     * Retrieves the FOM Entry object from the request payload.
     */
    protected Entry getEntryFromRequest(RequestContext request) throws ResponseContextException {
        Abdera abdera = AbstractAtompubProvider.getAbdera(request);
        Parser parser = abdera.getParser();

        Document<Entry> entry_doc;
        try {
          entry_doc = AbstractAtompubProvider.getDocument(parser,request);
        } catch (ParseException e) {
          throw new ResponseContextException(400, e);
        } catch (Throwable t) {
          throw new ResponseContextException(500, t);
        }
        return entry_doc == null ? null : entry_doc.getRoot();
    }

    public Collection asCollectionElement(RequestContext request) {
        Collection collection = AbstractAtompubProvider.getAbdera(request).getFactory().newCollection();
        collection.setHref(getHref(request));
        collection.setTitle(getTitle(request));
        collection.setAccept(getAccepts(request));
        for (AtompubCategoriesInfo catsinfo : getCategoriesInfo(request)) {
            collection.addCategories(catsinfo.asCategoriesElement(request));
        }
        return collection;
    }
    
    public Predicate<RequestContext> acceptable() {
      return ProviderHelper.isAtom();
    }
}
