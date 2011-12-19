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
package org.apache.abdera2.common.protocol;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.abdera2.common.text.UrlEncoding;
import org.apache.abdera2.common.date.DateTimes;
import org.apache.abdera2.common.http.EntityTag;
import org.apache.abdera2.common.misc.ExceptionHelper;
import org.apache.abdera2.common.misc.MoreFunctions;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

/**
 * Base CollectionAdapter implementation that provides a number of helper 
 * utility methods for adapter implementations.
 */
public abstract class AbstractCollectionAdapter 
  implements CollectionAdapter, 
             CollectionInfo {

    private final static Log log = 
      LogFactory.getLog(AbstractCollectionAdapter.class);

    private String href;
    private final Map<String, Object> hrefParams = 
      new HashMap<String, Object>();
    
    private final Map<HandlerKey,Function<RequestContext,ResponseContext>> handlers = 
      new HashMap<HandlerKey,Function<RequestContext,ResponseContext>>();

    private static class HandlerKey implements Serializable {
      private static final long serialVersionUID = -580349554867112812L;
      final TargetType type;
      final String method;
      HandlerKey(TargetType type, String method) {
        this.type = type;
        this.method = method.toUpperCase();
      }
      public int hashCode() {
        return MoreFunctions.genHashCode(1, method,type);
      }
      public boolean equals(Object obj) {
        if (this == obj)
          return true;
        if (obj == null)
          return false;
        if (getClass() != obj.getClass())
          return false;
        HandlerKey other = (HandlerKey) obj;
        if (method == null) {
          if (other.method != null)
            return false;
        } else if (!method.equals(other.method))
          return false;
        if (type == null) {
          if (other.type != null)
            return false;
        } else if (!type.equals(other.type))
          return false;
        return true;
      }
    }
    
    @SuppressWarnings("unchecked")
    protected synchronized <X extends AbstractCollectionAdapter>X putHandler(
      TargetType targetType,
      String method,
      Function<RequestContext,ResponseContext> handler) {
        HandlerKey key = new HandlerKey(targetType,method);
        handlers.put(key, handler);
        return (X)this;
    }
    
    public AbstractCollectionAdapter(String href) {
        super();
        putHandler(TargetType.TYPE_CATEGORIES, "GET", NOT_FOUND);
        putHandler(TargetType.TYPE_CATEGORIES, "HEAD", NOT_FOUND);
        setHref(href);
    }

    public String getHref() {
        return href;
    }

    private void setHref(String href) {
        this.href = href;
        if (href != null)
          hrefParams.put("collection", href);
    }

    public String getHref(RequestContext request) {
        return request.urlFor("feed", hrefParams);
    }

    public abstract String getAuthor(
      RequestContext request) 
        throws ResponseContextException;

    public abstract String getId(
      RequestContext request);

    /**
     * Creates the ResponseContext for a HEAD entry request. By default, an EmptyResponseContext is returned. The Etag
     * header will be set.
     */
    protected ResponseContext buildHeadEntryResponse(
      RequestContext request, 
      String id, 
      DateTime updated)
        throws ResponseContextException {
        return new EmptyResponseContext(200)
          .setEntityTag(
            EntityTag.generate(
              id, 
              DateTimes.format(updated)));
    }

    /**
     * Create a ResponseContext (or take it from the Exception) for an exception that occurred in the application.
     * 
     * @param e
     * @return
     */
    protected ResponseContext createErrorResponse(
      ResponseContextException e) {
        ExceptionHelper.responseLog(log,e);
        return e.getResponseContext();
    }

    /**
     * Get's the name of the specific resource requested
     */
    protected String getResourceName(RequestContext request) {
        String path = request.getTargetPath();
        int q = path.indexOf("?");
        if (q != -1) {
            path = path.substring(0, q);
        }
        String[] segments = path.split("/");
        String id = segments[segments.length - 1];
        return UrlEncoding.decode(id);
    }

    protected String[] getMethods(RequestContext request) {
        return ProviderHelper.getDefaultMethods(request);
    }

    private static final Predicate<HandlerKey> COLLECTION_POST_CHECK = 
      new Predicate<HandlerKey>() {
        public boolean apply(HandlerKey input) {
          return input.method.equalsIgnoreCase("POST") && 
                 input.type == TargetType.TYPE_COLLECTION;
        }
    };
    
    public Function<RequestContext,ResponseContext> handlerFor(
      Target target, 
      String method) {
        HandlerKey key = new HandlerKey(target.getType(),method);
        Function<RequestContext,ResponseContext> handler = handlers.get(key);
        return handler != null ?
          handler :
          COLLECTION_POST_CHECK.apply(key) ? // if this is a post to a collection, and the handler is null
            UNSUPPORTED_TYPE :               // return an unsupported_type reponse since POST is always 
            NOT_ALLOWED;                     // allowed on the collection
    }
    
    protected final Function<RequestContext,ResponseContext> UNSUPPORTED_TYPE = 
      new Function<RequestContext,ResponseContext>() {
        public ResponseContext apply(RequestContext input) {
          return ProviderHelper.notsupported(input);
        }
    };
    
    protected final Function<RequestContext,ResponseContext> NOT_ALLOWED =
      new Function<RequestContext,ResponseContext>() {
        public ResponseContext apply(RequestContext input) {
          return ProviderHelper.notallowed(input,getMethods(input));
        }
    };
    
    protected final Function<RequestContext,ResponseContext> NOT_FOUND = 
      new Function<RequestContext,ResponseContext>() {
        public ResponseContext apply(RequestContext input) {
          return ProviderHelper.notfound(input);
        }
    };
    
    public final static Predicate<RequestContext> HAS_NO_ENTITY = 
      new Predicate<RequestContext>() {
        public boolean apply(RequestContext input) {
          String method = input.getMethod();
          return method.equalsIgnoreCase("GET") ||
                 method.equalsIgnoreCase("DELETE") ||
                 method.equalsIgnoreCase("HEAD") ||
                 method.equalsIgnoreCase("OPTIONS");
        }
    };
    
    public Predicate<RequestContext> acceptable() {
      return HAS_NO_ENTITY;
    }
}
