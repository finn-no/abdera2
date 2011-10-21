package org.apache.abdera2.test.server.multipart;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.abdera2.parser.ParseException;
import org.apache.abdera2.protocol.server.impl.AbstractAtompubProvider;
import org.apache.abdera2.protocol.server.multipart.AbstractMultipartCollectionAdapter;
import org.apache.abdera2.common.Constants;
import org.apache.abdera2.common.mediatype.MimeTypeHelper;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.ResponseContext;
import org.apache.abdera2.common.protocol.EmptyResponseContext;
import org.apache.abdera2.common.protocol.TargetType;

import com.google.common.base.Function;

public class MultipartRelatedAdapter 
  extends AbstractMultipartCollectionAdapter {

    public MultipartRelatedAdapter(String href) {
      super(href);
      putHandler(TargetType.TYPE_COLLECTION,"POST",handlePost());
    }
  
    @Override
    public String getAuthor(RequestContext request) {
        return "Acme Industries";
    }

    @Override
    public String getId(RequestContext request) {
        return "tag:example.org,2008:feed";
    }

    private Function<RequestContext,ResponseContext> handlePost() {
      return new Function<RequestContext,ResponseContext>() {
        public ResponseContext apply(RequestContext input) {
          return AbstractAtompubProvider.IS_ATOM.apply(input) ?
            NOT_ALLOWED.apply(input) :
            postMedia().apply(input);
        }
      };
    }
    
    public Function<RequestContext,ResponseContext> postMedia() {
      return new Function<RequestContext,ResponseContext>() {
        public ResponseContext apply(RequestContext input) {
          try {
              if (MimeTypeHelper.isMultipart(input.getContentType().toString())) {
                  getMultipartRelatedData(input);
              }
              return new EmptyResponseContext(201);
          } catch (ParseException pe) {
              return new EmptyResponseContext(415, pe.getLocalizedMessage());
          } catch (IOException ioe) {
              return new EmptyResponseContext(500, ioe.getLocalizedMessage());
          } catch (MessagingException e) {
              return new EmptyResponseContext(500, e.getLocalizedMessage());
          }
        }
      };
    }

    public String getTitle(RequestContext request) {
        return "Acme Multipart/related adapter";
    }

    @SuppressWarnings("serial")
    public Map<String, String> getAlternateAccepts(RequestContext request) {
        if (accepts == null) {
            accepts = new HashMap<String, String>() {
                {
                    put("video/*", null); /* doesn't accept multipart related */
                    put("image/jpg", ""); /* doesn't accept multipart related */
                    put("image/png", Constants.LN_ALTERNATE_MULTIPART_RELATED /* multipart-related */);
                }
            };
        }
        return accepts;
    }

}
