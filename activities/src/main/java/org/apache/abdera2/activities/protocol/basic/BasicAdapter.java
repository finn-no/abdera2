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
package org.apache.abdera2.activities.protocol.basic;

import java.util.LinkedHashSet;
import java.util.UUID;
import java.util.logging.Logger;

import javax.activation.MimeType;

import org.apache.abdera2.activities.model.ASBase;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.Collection;
import org.apache.abdera2.activities.model.objects.PersonObject;
import org.apache.abdera2.activities.protocol.AbstractActivitiesProvider;
import org.apache.abdera2.activities.protocol.ActivitiesResponseContext;
import org.apache.abdera2.activities.protocol.ErrorObject;
import org.apache.abdera2.activities.protocol.managed.FeedConfiguration;
import org.apache.abdera2.activities.protocol.managed.ManagedCollectionAdapter;
import org.apache.abdera2.common.mediatype.MimeTypeHelper;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.ResponseContext;
import org.apache.abdera2.common.protocol.Target;
import org.apache.abdera2.common.protocol.TargetType;
import org.apache.abdera2.common.protocol.RequestContext.Scope;
import org.apache.abdera2.common.pusher.ChannelManager;
import org.apache.abdera2.common.pusher.Pusher;
import org.joda.time.DateTime;

import com.google.common.base.Function;
import static com.google.common.base.Preconditions.*;
import static org.apache.abdera2.common.misc.ExceptionHelper.*;
import static org.apache.abdera2.common.protocol.ProviderHelper.*;

/**
 * The BasicAdapter provides a simplistic interface for working with Atompub collections with a restricted set of
 * options/features. The idea of the basic adapter is to make it easy to provide a minimally capable Atompub server
 */
@SuppressWarnings("unchecked")
public abstract class BasicAdapter extends ManagedCollectionAdapter {

    public static Logger logger = Logger.getLogger(BasicAdapter.class.getName());

    protected BasicAdapter(FeedConfiguration config) {
        super(config);
        putHandler(TargetType.TYPE_COLLECTION,"GET",getItemList());
        putHandler(TargetType.TYPE_COLLECTION,"HEAD",getItemList());
        putHandler(TargetType.TYPE_COLLECTION,"POST",postItem());
        putHandler(TargetType.TYPE_ENTRY,"GET",getItem());
        putHandler(TargetType.TYPE_ENTRY,"HEAD",getItem());
        putHandler(TargetType.TYPE_ENTRY,"DELETE",deleteItem());
        putHandler(TargetType.TYPE_ENTRY,"PUT",putItem());
    }

    public String getProperty(String key) throws Exception {
        Object val = config.getProperty(key);
        checkNotNull(
          val,
          "Cannot find property in Adapter properties file for feed ", 
          key, 
          config.getFeedId());
        checked(
          val instanceof String, 
          RuntimeException.class);
        return (String)val;
    }

    protected Collection<ASObject> createCollection() {
      Collection<ASObject> col = 
        Collection
          .makeCollection()
          .id(config.getFeedUri())
          .set("title", config.getFeedTitle())
          .set("updated", DateTime.now())
          .set("author", 
            PersonObject
              .makePerson()
              .displayName(config.getFeedAuthor())
              .get())
          .get();
      col.setItems(new LinkedHashSet<ASObject>());
      return col;
    }
    
    protected void addEditLinkToObject(ASObject object) throws Exception {
      if (AbstractActivitiesProvider.getEditUriFromEntry(object) == null)
        object.setProperty("editLink", object.getId());
    }

    protected void setObjectIdIfNull(ASObject object) throws Exception {
      if (object.getId() != null)
        return;
      String uuidUri = UUID.randomUUID().toString();
      String[] segments = uuidUri.split(":");
      String entryId = segments[segments.length - 1];
      object.setId(createEntryIdUri(entryId));
    }

    protected String createEntryIdUri(String entryId) throws Exception {
        return config.getFeedUri() + "/" + entryId;
    }

    private void push(RequestContext context, String channel, ASObject object) {
      if (context.getAttribute(Scope.CONTAINER, "AbderaChannelManager") != null) {
        ChannelManager cm = (ChannelManager) context.getAttribute(
          Scope.CONTAINER, "AbderaChannelManager");
        if (cm != null) {
          Pusher<ASObject> pusher = 
            cm.getPusher(channel);
          if (pusher != null)
            pusher.push(object);
        }
      }
    }
    
    private ResponseContext createOrUpdateObject(RequestContext request, boolean createFlag) {
      try {
        MimeType mimeType = request.getContentType();
        String contentType = mimeType == null ? null : mimeType.toString();
        if (contentType != null && !MimeTypeHelper.isJson(contentType))
          return notsupported(request);
           
        ASBase base = getEntryFromRequest(request);
        Target target = request.getTarget();

        if (base instanceof Collection && 
            createFlag && 
            target.getType() == TargetType.TYPE_COLLECTION) {
            // only allow multiposts on collections.. these always create, never update
            Collection<ASObject> coll = (Collection<ASObject>) base;
            Collection<ASObject> retl = new Collection<ASObject>();
            int c = 0;
            for (ASObject inputEntry : coll.getItems()) {
              ASObject newEntry = createItem(inputEntry,c++);
              if (newEntry != null) {
                push(request,target.getParameter(BasicProvider.PARAM_FEED),newEntry);
                retl.addItem(newEntry);
              } else {
                ErrorObject err = new ErrorObject();
                err.setCode(-100);
                err.setDisplayName("Error adding object");
                retl.addItem(err);
              }
            }
            return
              new ActivitiesResponseContext<Collection<ASObject>>(retl)
                .setStatus(createFlag?201:200);
        } else if (base instanceof ASObject){
          String entryId = !createFlag ? 
            target.getParameter(BasicProvider.PARAM_ENTRY) : 
              null;
          ASObject inputEntry = (ASObject) base;
          ASObject newEntry = createFlag ? 
            createItem(inputEntry) : 
            updateItem(entryId, inputEntry);
          if (newEntry != null) {
            push(request,target.getParameter(BasicProvider.PARAM_FEED),newEntry);
            String loc = newEntry.getProperty("editLink");
            return 
              new ActivitiesResponseContext<ASObject>(newEntry)
                .setStatus(createFlag?201:200)
                .setLocation(loc);
          } else
            return notfound(request);
        } else
          return notallowed(request);
      } catch (Exception e) {
        return servererror(request, e.getMessage(), e);
      }
    }

    private Function<RequestContext,ResponseContext> postItem() {
      return new Function<RequestContext,ResponseContext>() {
        public ResponseContext apply(RequestContext input) {
          return createOrUpdateObject(input,true);
        }
      };
    }
    
    private Function<RequestContext,ResponseContext> deleteItem() {
      return new Function<RequestContext,ResponseContext>() {
        public ResponseContext apply(RequestContext input) {
          Target target = input.getTarget();
          String entryId = target.getParameter(BasicProvider.PARAM_ENTRY);
          try {
              return deleteItem(entryId) ? 
                nocontent() : 
                notfound(input);
          } catch (Exception e) {
              return servererror(
                input, e.getMessage(), e);
          }
        }
      };
    }

    private Function<RequestContext,ResponseContext> putItem() {
      return new Function<RequestContext,ResponseContext>() {
        public ResponseContext apply(RequestContext input) {
          return createOrUpdateObject(input,false);
        }
      };
    }

    private Function<RequestContext,ResponseContext> getItem() {
      return new Function<RequestContext,ResponseContext>() {
        public ResponseContext apply(RequestContext input) {
          Target target = input.getTarget();
          String entryId = target.getParameter(BasicProvider.PARAM_ENTRY);
          try {
              ASObject object = getItem(entryId);
              if (object != null) {
                return 
                  new ActivitiesResponseContext<ASObject>(object)
                    .setStatus(200);
              } else return notfound(input);       
          } catch (Exception e) {
              return servererror(input, e.getMessage(), e);
          }
        }
      };
    }

    public Function<RequestContext,ResponseContext> getItemList() {
      return new Function<RequestContext,ResponseContext>() {
        public ResponseContext apply(RequestContext input) {
          try {
            Collection<ASObject> collection = 
              getCollection();

            if (collection != null) { 
              return 
                new ActivitiesResponseContext<Collection<ASObject>>(collection)
                  .setStatus(200);
            } else return notfound(input);
          } catch (Exception e) {
            return servererror(input, e.getMessage(), e);
          }
        }
      };
    }

    public abstract Collection<ASObject> getCollection() throws Exception;

    public abstract ASObject getItem(Object objectId) throws Exception;

    public abstract ASObject createItem(ASObject object) throws Exception;

    public abstract ASObject createItem(ASObject object, int c) throws Exception;
    
    public abstract ASObject updateItem(Object objectId, ASObject object) throws Exception;

    public abstract boolean deleteItem(Object objectId) throws Exception;

}
