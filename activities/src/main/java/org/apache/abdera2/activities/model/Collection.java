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
package org.apache.abdera2.activities.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.abdera2.common.anno.Name;
import org.apache.abdera2.common.iri.IRI;
import org.apache.abdera2.common.selector.Selector;

/**
 * An Activity Streams Collection... used as the root object of
 * JSON Activity Streams documents and as the value for a variety 
 * of properties (such as "replies"). Encapsulates an array of 
 * items with additional metadata. As an alternative to specifying
 * the array of items inline, the "url" property can be used to 
 * reference an external Collection document. 
 */
@Name("collection")
public class Collection<T extends ASObject> 
  extends ASObject {

  private static final long serialVersionUID = 1530068180553259077L;
  public static final String TOTAL_ITEMS = "totalItems";
  public static final String URL = "url";
  public static final String ITEMS = "items";
  public static final String OBJECT_TYPES = "objectTypes";

  /**
   * Return the value of the "totalItems" property... this does not 
   * necessarily reflect the actual number of items in the "items" 
   * iterator.
   */
  public int getTotalItems() {
    return (Integer)getProperty(TOTAL_ITEMS);
  }
  
  /**
   * Set the value of the "totalItems" property
   */
  public Collection<T> setTotalItems(int totalItems) {
    setProperty(TOTAL_ITEMS, totalItems);
    return this;
  }
  
  /**
   * Get the url of this collection
   */
  public IRI getUrl() {
    return getProperty(URL);
  }
  
  /**
   * Set the url of this collection
   */
  public void setUrl(IRI url) {
    setProperty(URL, url);
  }

  /**
   * Get the list of objectTypes expected to be found in this collection
   */
  public Iterable<String> getObjectTypes() {
    return checkEmpty(this.<Iterable<String>>getProperty(OBJECT_TYPES));
  }
  
  /**
   * Set the list of objectTypes expected to be found in this collection
   */
  public void setObjectTypes(java.util.Collection<String> types) {
    setProperty(OBJECT_TYPES,new LinkedHashSet<String>(types));
  }
  
  /**
   * Add a new objectType to the list of objectTypes expected to be found
   * in this collection
   */
  public void addObjectType(String... objectTypes) {
    Set<String> list = getProperty(OBJECT_TYPES);
    if (list == null) {
      list = new LinkedHashSet<String>();
      setProperty(OBJECT_TYPES, list);
    }
    for (String objectType : objectTypes)
      list.add(objectType);
  }
  
  /**
   * get the items collection using the specified selector as a filter
   */
  public Iterable<T> getItems(Selector<T> selector) {
    List<T> list = new ArrayList<T>();
    for (T item : getItems()) 
      if (selector.apply(item))
        list.add(item);
    return list;
  }
  
  /**
   * get the items contained in this collection
   */
  @SuppressWarnings("unchecked")
  public Iterable<T> getItems() {
    return checkEmpty((Iterable<T>)getProperty(ITEMS));
  }
  
  /**
   * Get the items contained in this collection. If no "items" 
   * property exists, a new LinkedHashSet will be created, set 
   * and returned if create == true;
   */
  public Iterable<T> getItems(boolean create) {
    Iterable<T> items = getItems();
    if (items == null && create) {
      items = new LinkedHashSet<T>();
      setProperty(ITEMS,items);
    }
    return items;
  }
  
  /**
   * Set the items in this collection, overwriting the existing value.
   * setting this will change the value of the "totalItems" property 
   * to reflect the number of items passed in.
   */
  public void setItems(java.util.Collection<T> items) {
    setProperty(ITEMS, new LinkedHashSet<T>(items));
    setTotalItems(items.size());
  }
  
  /**
   * Set the items in this collection, overwriting the existing value.
   * setting this will change the value of the "totalItems" property 
   * to reflect the number of items passed in.
   */
  public void setItems(Iterable<T> items) {
    Set<T> set = new LinkedHashSet<T>();
    for (T item : items) set.add(item);
    setItems(set);
  }
  
  /**
   * Add an item to this collection
   * setting this will change the value of the "totalItems" property 
   * to reflect the number of items passed in.
   */
  public void addItem(T... items) {
    Set<T> list = getProperty(ITEMS);
    if (list == null) {
      list = new LinkedHashSet<T>();
      setProperty(ITEMS, list);
    }
    for (T item : items)
      list.add(item);
    setTotalItems(list.size());
  }
  
  /**
   * Begin making a new collection object using the fluent factory api
   */
  public static <T extends ASObject>CollectionGenerator<T> makeCollection() {
    return new CollectionGenerator<T>();
  }
  
  @SuppressWarnings("unchecked")
  public static class CollectionGenerator<T extends ASObject> 
    extends ASObjectGenerator<Collection<T>> {
    
    @SuppressWarnings("rawtypes")
    private static <T extends ASObject>Class<? extends Collection<T>> t(Class _class) {
      return (Class<? extends Collection<T>>) _class;
    }
    
    public CollectionGenerator() {
      super(CollectionGenerator.<T>t(Collection.class));
    }
    
    public CollectionGenerator(Class<? extends Collection<T>> _class) {
      super(_class);
    }
    
    public <X extends CollectionGenerator<T>>X totalItems(int items) {
      item.setTotalItems(items);
      return (X)this;
    }
    
    public <X extends CollectionGenerator<T>>X item(T item) {
      this.item.addItem(item);
      return (X)this;
    }
  }
}
