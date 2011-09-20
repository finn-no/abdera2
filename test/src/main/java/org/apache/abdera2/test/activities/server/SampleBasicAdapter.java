package org.apache.abdera2.test.activities.server;
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


import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.Collection;
import org.apache.abdera2.activities.protocol.basic.BasicAdapter;
import org.apache.abdera2.activities.protocol.managed.FeedConfiguration;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.ResponseContextException;

public class SampleBasicAdapter extends BasicAdapter {

    private static final String ERROR_INVALID_ENTRY = "No Such Entry in the Feed";

    public static Logger logger = Logger.getLogger(SampleBasicAdapter.class.getName());
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
    
    public SampleBasicAdapter(FeedConfiguration config) {
        super(config);
    }

    @Override
    public Collection<ASObject> getCollection() throws Exception {
        Collection<ASObject> col = createCollection();
        
        for (Item item : entries)
          col.addItem(item.getValue());
        return col;
    }

    @Override
    public ASObject getItem(Object entryId) throws Exception {
      ASObject ret = null;
      for (Item item : entries)
        if (item.getKey().equals(entryId.toString())) {
          ret = item.getValue();
          break;
        }
      return ret;
    }

    @Override
    public ASObject createItem(ASObject object) throws Exception {
        return createItem(object,-1);
    }
    
    @Override
    public ASObject createItem(ASObject object, int c) throws Exception {
        setObjectIdIfNull(object);
        logger.info("assigning id to Object: " + object.getId().toString());
        String entryId = getObjectIdFromUri(object.getId().toString());
        if (c != -1) entryId += c;
        if (object.getUpdated() == null) {
            object.setUpdated(new Date());
        }
        addEditLinkToObject(object);
        storeObject(entryId, object);
        logger.finest("returning this object from sampleadapter.createItem: " + object.toString());
        return object;
    }

    @Override
    public ASObject updateItem(Object entryId, ASObject object) throws Exception {
        if (!deleteItem(entryId))
            throw new Exception(ERROR_INVALID_ENTRY);
        
        if (object.getUpdated() == null) {
            object.setUpdated(new Date());
        }
        addEditLinkToObject(object);
        storeObject((String)entryId, object);
        logger.finest("returning this entry from sampleadapter.updateEntry: " + object.toString());
        return object;
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
