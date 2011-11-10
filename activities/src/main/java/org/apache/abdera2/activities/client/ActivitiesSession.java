package org.apache.abdera2.activities.client;

import javax.activation.MimeType;

import org.apache.abdera2.activities.model.ASBase;
import org.apache.abdera2.activities.model.ASDocument;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.Activity;
import org.apache.abdera2.activities.model.Collection;
import org.apache.abdera2.activities.model.IO;
import org.apache.abdera2.common.http.EntityTag;
import org.apache.abdera2.common.protocol.ProtocolException;
import org.apache.abdera2.protocol.client.Client;
import org.apache.abdera2.protocol.client.ClientResponse;
import org.apache.abdera2.protocol.client.RequestOptions;
import org.apache.abdera2.protocol.client.Session;
import org.joda.time.DateTime;

/**
 * Extension of the base Abdera Client Session that provides utility 
 * methods for working with Activity Streams objects.
 */
@SuppressWarnings("unchecked")
public class ActivitiesSession 
  extends Session {

  private final IO io;
  
  protected ActivitiesSession(Client client) {
    super(client);
    this.io = IO.get();
  }

  public IO getIO() {
    return io;
  }
  
  protected ActivitiesClient getActivitiesClient() {
    return (ActivitiesClient) client;
  }

  public <T extends Collection<?>>ASDocument<T> getCollection(String uri) {
    return this.<T>getCollection(uri, this.getDefaultRequestOptions());
  }
  
  public <T extends ClientResponse>T post(String uri, ASBase base) {
    return this.<T>post(uri,base, this.getDefaultRequestOptions());
  }
  
  public <T extends ClientResponse>T post(String uri, ASBase base, RequestOptions options) {
    ActivityEntity entity = new ActivityEntity(base);
    return this.<T>post(uri, entity, options);
  }

  public <T extends ClientResponse>T put(String uri, ASBase base) {
    return this.<T>put(uri,base, this.getDefaultRequestOptions());
  }
  
  public <T extends ClientResponse>T put(String uri, ASBase base, RequestOptions options) {
    ActivityEntity entity = new ActivityEntity(base);
    return this.<T>put(uri, entity, options);
  }
  
  public <T extends Collection<?>>ASDocument<T> getCollection(String uri, RequestOptions options) {
    ClientResponse cr = get(uri, options);
    try {
      if (cr != null) {
        switch(cr.getType()) {
        case SUCCESSFUL:
          try {
            T t = (T)io.readCollection(cr.getReader());
            return getDoc(cr,t);
          } catch (Throwable t) {
            throw new ProtocolException(601, t.getMessage());
          }
        default:
          throw new ProtocolException(cr.getStatus(), cr.getStatusText());
        }
      } else {
        throw new ProtocolException(600, "Null Response");
      }
    } finally {
      if (cr != null) cr.release();
    }
  }
  
  public <T extends Activity>ASDocument<T> getActivity(String uri) {
    return this.<T>getActivity(uri,this.getDefaultRequestOptions());
  }
  
  public <T extends Activity>ASDocument<T> getActivity(String uri, RequestOptions options) {
    ClientResponse cr = get(uri, options);
    try {
      if (cr != null) {
        switch(cr.getType()) {
        case SUCCESSFUL:
          try {
            T t = (T)io.readActivity(cr.getReader());
            return getDoc(cr,t);
          } catch (Throwable t) {
            throw new ProtocolException(601, t.getMessage());
          }
        default:
          throw new ProtocolException(cr.getStatus(), cr.getStatusText());
        }
      } else {
        throw new ProtocolException(600, "Null Response");
      }
    } finally {
      if (cr != null) cr.release();
    }
  }
  
  public <T extends ASObject>ASDocument<T> getObject(String uri) {
    return this.<T>getObject(uri, this.getDefaultRequestOptions());
  }

  public <T extends ASObject>ASDocument<T> getObject(String uri, RequestOptions options) {
    ClientResponse cr = get(uri, options);
    try {
      if (cr != null) {
        switch(cr.getType()) {
        case SUCCESSFUL:
          try {
            T t = (T)io.readObject(cr.getReader());
            return getDoc(cr,t);
          } catch (Throwable t) {
            throw new ProtocolException(601, t.getMessage());
          }
        default:
          throw new ProtocolException(cr.getStatus(), cr.getStatusText());
        }
      } else {
        throw new ProtocolException(600, "Null Response");
      }
    } finally {
      if (cr != null) cr.release();
    }
  }
  
  private <T extends ASBase>ASDocument<T> getDoc(ClientResponse resp, T base) {
    ASDocument.Builder<T> builder = 
      ASDocument.make(base);
    EntityTag etag = resp.getEntityTag();
    if (etag != null)
        builder.entityTag(etag);
    DateTime lm = resp.getLastModified();
    if (lm != null)
        builder.lastModified(lm);
    MimeType mt = resp.getContentType();
    if (mt != null)
        builder.contentType(mt.toString());
    String language = resp.getContentLanguage();
    if (language != null)
        builder.language(language);
    String slug = resp.getSlug();
    if (slug != null)
        builder.slug(slug);
    return builder.get();
  }
}
