package org.apache.abdera2.examples.activities;
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

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.abdera2.activities.model.ASBase;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.Collection.CollectionBuilder;
import org.apache.abdera2.activities.protocol.basic.BasicAdapter;
import org.apache.abdera2.activities.protocol.managed.FeedConfiguration;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.ResponseContextException;


public class MyActivitiesAdapter extends BasicAdapter {

    private static final String ERROR_INVALID_ENTRY = "No Such Entry in the Feed";

    public static Logger logger = Logger.getLogger(MyActivitiesAdapter.class.getName());
    protected List<Item> entries = new LinkedList<Item>();

    private static class Item {
      private final String key;
      private ASObject value;
      Item(String key, ASObject value) {
        this.key = key;
        this.value = value;
      }
      String getKey() {
        return key;
      }
      ASObject getValue() {
        return value;
      }
      void setValue(ASObject value) {
        this.value = value;
      }
    }
    
    public MyActivitiesAdapter(FeedConfiguration config) {
        super(config);
    }

    @Override
    public CollectionBuilder<ASObject> getCollection() throws Exception {
        CollectionBuilder<ASObject> col = createCollection();
        for (Item item : entries)
          col.item(item.getValue());
        return col;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public ASObject.Builder<ASObject,?> getItem(Object entryId) throws Exception {
      ASObject ret = null;
      for (Item item : entries)
        if (item.getKey().equals(entryId.toString())) {
          ret = item.getValue();
          break;
        }
      if (ret == null) return null;
      return ret.<ASObject,ASObject.Builder>template();
    }

    @Override
    public <T extends ASObject>ASObject.Builder<T,?> createItem(ASObject object) throws Exception {
        return createItem(object,-1);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <T extends ASObject>ASObject.Builder<T,?> createItem(ASObject object, int c) throws Exception {
      ASObject.Builder<T, ?> builder = object.<ASObject,ASObject.Builder>template(ASBase.withoutFields("editLink","updated"));
      if (!object.has("id"))
        setObjectId(builder);
      logger.info("assigning id to Object: " + object.getId().toString());
      String entryId = getObjectIdFromUri(object.getId().toString());
      if (c != -1) entryId += c;
      builder.updatedNow();
      addEditLinkToObject(builder,object.getId());
      storeObject(entryId, object);
      logger.finest("returning this object from sampleadapter.createItem: " + object.toString());
      return builder;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <T extends ASObject>ASObject.Builder<T,?> updateItem(Object entryId, ASObject object) throws Exception {
        if (!deleteItem(entryId))
            throw new Exception(ERROR_INVALID_ENTRY);
        ASObject.Builder<T, ?> builder = object.<ASObject,ASObject.Builder>template(ASBase.withoutFields("editLink","updated"));
        builder.updatedNow();
        addEditLinkToObject(builder,object.getId());
        storeObject((String)entryId, builder.get());
        logger.finest("returning this entry from sampleadapter.updateEntry: " + object.toString());
        return builder;
    }

    @Override
    public boolean deleteItem(Object entryId) throws Exception {
      Item item = null;
      for (Item i : entries) {
        if (i.getKey().equals(entryId.toString())) {
          item = i;
          break;
        }
      }
      if (item != null) {
        entries.remove(item);
        return true;
      } else return false;
    }

    protected String getObjectIdFromUri(String uri) {
        String[] segments = uri.split("/");
        return segments[segments.length - 1];
    }

    protected void storeObject(String entryId, ASObject object) throws Exception {
      Item item = null;
      for (Item i : entries) {
        if (i.getKey().equals(entryId)) {
          item = i;
          break;
        }
      }
      if (item == null) { 
        entries.add(0,new Item(entryId,object));
      } else {
        item.setValue(object);
      }
    }

    @Override
    public String getAuthor(RequestContext request) throws ResponseContextException {
        return config.getFeedAuthor();
    }

    @Override
    public String getId(RequestContext request) {
        return config.getFeedId();
    }

    @Override
    public String getTitle(RequestContext request) {
        return config.getFeedTitle();
    }
}
