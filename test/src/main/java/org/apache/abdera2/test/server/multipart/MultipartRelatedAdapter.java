package org.apache.abdera2.test.server.multipart;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.abdera2.parser.ParseException;
import org.apache.abdera2.protocol.server.multipart.AbstractMultipartCollectionAdapter;
import org.apache.abdera2.common.Constants;
import org.apache.abdera2.common.mediatype.MimeTypeHelper;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.ResponseContext;
import org.apache.abdera2.common.protocol.EmptyResponseContext;
import org.apache.abdera2.common.protocol.ProviderHelper;

@SuppressWarnings("unchecked")
public class MultipartRelatedAdapter extends AbstractMultipartCollectionAdapter {

    @Override
    public String getAuthor(RequestContext request) {
        return "Acme Industries";
    }

    @Override
    public String getId(RequestContext request) {
        return "tag:example.org,2008:feed";
    }

    public <S extends ResponseContext>S deleteItem(RequestContext request) {
        return (S)ProviderHelper.notallowed(request);
    }

    public <S extends ResponseContext>S getItem(RequestContext request) {
        return (S)ProviderHelper.notallowed(request);
    }

    public <S extends ResponseContext>S getItemList(RequestContext request) {
        return (S)ProviderHelper.notallowed(request);
    }

    public <S extends ResponseContext>S postItem(RequestContext request) {
        return (S)ProviderHelper.notallowed(request);
    }

    public <S extends ResponseContext>S putItem(RequestContext request) {
        return (S)ProviderHelper.notallowed(request);
    }

    public <S extends ResponseContext>S postMedia(RequestContext request) {
        try {
            if (MimeTypeHelper.isMultipart(request.getContentType().toString())) {
                getMultipartRelatedData(request);
                // Post object is a wrapper for the media resource and the media link entry.
                // Once we get it we can save them following the rfc specification.
            }

            return (S)new EmptyResponseContext(201);
        } catch (ParseException pe) {
            return (S)new EmptyResponseContext(415, pe.getLocalizedMessage());
        } catch (IOException ioe) {
            return (S)new EmptyResponseContext(500, ioe.getLocalizedMessage());
        } catch (MessagingException e) {
            return (S)new EmptyResponseContext(500, e.getLocalizedMessage());
        }
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
