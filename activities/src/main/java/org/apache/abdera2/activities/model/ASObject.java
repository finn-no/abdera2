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

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.abdera2.activities.extra.Extra;
import org.apache.abdera2.activities.model.objects.EmbeddedExperience;
import org.apache.abdera2.activities.model.objects.Mood;
import org.apache.abdera2.activities.model.objects.PersonObject;
import org.apache.abdera2.activities.model.objects.PlaceObject;
import org.apache.abdera2.common.anno.AnnoUtil;
import org.apache.abdera2.common.iri.IRI;
import org.apache.abdera2.common.selector.Selector;

import com.google.common.collect.Iterables;

/**
 * Base class for all Activity Streams Objects.
 */
@SuppressWarnings("unchecked")
public class ASObject extends ASBase {

  private static final long serialVersionUID = -6969558559101109831L;
  public static final String ATTACHMENTS = "attachments";
  public static final String AUTHOR = "author";
  public static final String CONTENT = "content";
  public static final String DISPLAYNAME = "displayName";
  public static final String DOWNSTREAMDUPLICATES = "downstreamDuplicates";
  public static final String ID = "id";
  public static final String IMAGE = "image";
  public static final String OBJECTTYPE = "objectType";
  public static final String PUBLISHED = "published";
  public static final String SUMMARY = "summary";
  public static final String UPDATED = "updated";
  public static final String UPSTREAMDUPLICATES = "upstreamDuplicates";
  public static final String URL = "url";
  
  public static final String INREPLYTO = "inReplyTo";
  public static final String LOCATION = "location";
  public static final String SOURCE = "source";
  public static final String MOOD = "mood";
  public static final String TAGS = "tags";
  public static final String RATING = "rating";
  
  public static final String EMBED = "embed";
  
  public ASObject() {
    setObjectType(AnnoUtil.getName(this));
  }
  
  public ASObject(String objectType) {
    setObjectType(objectType);
  }
  
  /**
   * Returns the value of the "attachments" property
   */
  public Iterable<ASObject> getAttachments() {
    return checkEmpty((Iterable<ASObject>)getProperty(ATTACHMENTS));
  }
  
  /**
   * Sets the value of the attachments property... note... internally, the
   * list of attachments does not allow for duplicate entries so the collection
   * passed in is changed to a LinkedHashSet, maintaining the order of the 
   * entries but eliminating duplicates. 
   */
  public void setAttachments(java.util.Collection<ASObject> attachments) {;
    setProperty(ATTACHMENTS, new LinkedHashSet<ASObject>(attachments));
  }
  
  /**
   * Adds an attachment to the "attachments" property.
   */
  public void addAttachment(ASObject... attachments) {
    Set<ASObject> list = getProperty(ATTACHMENTS);
    if (list == null) {
      list = new LinkedHashSet<ASObject>();
      setProperty(ATTACHMENTS, list);
    }
    for (ASObject attachment : attachments)
      list.add(attachment); 
  }
  
  /**
   * Return the author of this object
   */
  public <E extends ASObject>E getAuthor() {
    return (E)getProperty(AUTHOR);
  }
  
  /**
   * Return the author of this object, if the author has not been
   * set and create==true, creates a default PersonObject and 
   * returns that.
   */
  public <E extends ASObject>E getAuthor(boolean create) {
    ASObject obj = getAuthor();
    if (obj == null && create) {
      obj = new PersonObject();
      setAuthor(obj);
    }
    return (E)obj;
  }
  
  /**
   * Set the author of the object
   */
  public void setAuthor(ASObject author) {
    setProperty(AUTHOR, author);
  }
  
  /**
   * Set the author of the object
   */
  public <E extends ASObject>E setAuthor(String displayName) {
    ASObject obj = getAuthor(true);
    obj.setDisplayName(displayName);
    return (E)obj;
  }
  
  /**
   * Get the content of the object
   */
  public String getContent() {
    return getProperty(CONTENT);
  }
  
  /**
   * Set the content of the object
   */
  public void setContent(String content) {
    setProperty(CONTENT, content);
    
  }
  
  /**
   * Get the displayName of the object
   */
  public String getDisplayName() {
    return getProperty(DISPLAYNAME);
  }
  
  /**
   * Set the displayName of the object
   */
  public void setDisplayName(String displayName) {
    setProperty(DISPLAYNAME, displayName);
    
  }
  
  /**
   * Return the list of downstream duplicate ids for this object. 
   * When an object is redistributed by third parties, the value of the "id"
   * property may change. When such changes do occur, it becomes difficult
   * to track duplicate versions of the same object. The "downstreamDuplicates"
   * and "upstreamDuplicates" properties on the object can be used to track 
   * modifications that occur in the "id" of the object in order to make
   * duplication detection easier
   */
  public Iterable<String> getDownstreamDuplicates() {
    return checkEmpty((Iterable<String>)getProperty(DOWNSTREAMDUPLICATES));
  }
  
  /**
   * Set the list of downstream duplicate ids for this object. 
   * When an object is redistributed by third parties, the value of the "id"
   * property may change. When such changes do occur, it becomes difficult
   * to track duplicate versions of the same object. The "downstreamDuplicates"
   * and "upstreamDuplicates" properties on the object can be used to track 
   * modifications that occur in the "id" of the object in order to make
   * duplication detection easier
   */
  public void setDownstreamDuplicates(Set<String> downstreamDuplicates) {
    setProperty(DOWNSTREAMDUPLICATES, downstreamDuplicates);
    
  }
  
  /**
   * Add an entry to the list of downstream duplicate ids for this object. 
   * When an object is redistributed by third parties, the value of the "id"
   * property may change. When such changes do occur, it becomes difficult
   * to track duplicate versions of the same object. The "downstreamDuplicates"
   * and "upstreamDuplicates" properties on the object can be used to track 
   * modifications that occur in the "id" of the object in order to make
   * duplication detection easier
   */
  public void addDownstreamDuplicate(String... duplicates) {
    Set<String> downstreamDuplicates = getProperty(DOWNSTREAMDUPLICATES);
    if (downstreamDuplicates == null) {
      downstreamDuplicates = new HashSet<String>();
      setProperty(DOWNSTREAMDUPLICATES, downstreamDuplicates);
    }
    for (String downstreamDuplicate : duplicates)
      downstreamDuplicates.add(downstreamDuplicate);  
  }
  
  /**
   * Get the id of this object
   */
  public String getId() {
    return getProperty(ID);
  }
  
  /**
   * Set the id of this object
   */
  public void setId(String id) {
    setProperty(ID, id);
    
  }
  
  /**
   * Get the "image" property
   */
  public MediaLink getImage() {
    return getProperty(IMAGE);
  }
  
  /**
   * Set the "image" property
   */
  public void setImage(MediaLink image) {
    setProperty(IMAGE, image);
  }
  
  /**
   * Set the "image" property
   */
  public void setImage(String uri) {
    if (uri == null) 
      setImage((MediaLink)null);
    else {
      MediaLink link = getImage();
      if (link == null) {
        link = new MediaLink();
        setProperty(IMAGE,link);
      }
      link.setUrl(uri);
    }
  }
  
  /**
   * Set the "image" property
   */
  public void setImage(IRI uri) {
    setImage(uri != null ? uri.toString() : null);
  }
  
  /**
   * Get the objectType
   */
  public String getObjectType() {
    return getProperty(OBJECTTYPE);
  }
  
  /**
   * Set the objectType
   */
  public void setObjectType(String objectType) {
    if (objectType != null && 
        ASObject.class.getSimpleName().equalsIgnoreCase(objectType))
      objectType = null;
    setProperty(OBJECTTYPE, objectType);
  }
  
  /**
   * Get the "published" datetime
   */
  public DateTime getPublished() {
    return getProperty(PUBLISHED);
  }
  
  /**
   * Set the "published" datetime
   */
  public void setPublished(DateTime published) {
    setProperty(PUBLISHED, published);
  }
  
  /**
   * Set the "published" property to the current date, time and default timezone
   */
  public void setPublishedNow() {
    setPublished(DateTime.now());
  }
  
  /**
   * Get the "summary" property
   */
  public String getSummary() {
    return getProperty(SUMMARY);
  }
  
  /**
   * Set the "summary" property
   */
  public void setSummary(String summary) {
    setProperty(SUMMARY, summary);
  }
  
  /**
   * Get the "updated" property
   */
  public DateTime getUpdated() {
    return getProperty(UPDATED);
  }
  
  /**
   * Set the "updated" property 
   */
  public void setUpdated(DateTime updated) {
    setProperty(UPDATED, updated);
  }
  
  /**
   * Set the "updated" property to the current date,time and default timezone
   */
  public void setUpdatedNow() {
    setUpdated(DateTime.now());
  }
  
  /**
   * Return the list of upstream duplicate ids for this object. 
   * When an object is redistributed by third parties, the value of the "id"
   * property may change. When such changes do occur, it becomes difficult
   * to track duplicate versions of the same object. The "downstreamDuplicates"
   * and "upstreamDuplicates" properties on the object can be used to track 
   * modifications that occur in the "id" of the object in order to make
   * duplication detection easier
   */
  public Iterable<String> getUpstreamDuplicates() {
    return checkEmpty((Iterable<String>)getProperty(UPSTREAMDUPLICATES));
  }
  
  /**
   * Set the list of upstream duplicate ids for this object. 
   * When an object is redistributed by third parties, the value of the "id"
   * property may change. When such changes do occur, it becomes difficult
   * to track duplicate versions of the same object. The "downstreamDuplicates"
   * and "upstreamDuplicates" properties on the object can be used to track 
   * modifications that occur in the "id" of the object in order to make
   * duplication detection easier
   */
  public void setUpstreamDuplicates(Set<String> upstreamDuplicates) {
    setProperty(UPSTREAMDUPLICATES, upstreamDuplicates);
    
  }
  
  /**
   * Add to the list of upstream duplicate ids for this object. 
   * When an object is redistributed by third parties, the value of the "id"
   * property may change. When such changes do occur, it becomes difficult
   * to track duplicate versions of the same object. The "downstreamDuplicates"
   * and "upstreamDuplicates" properties on the object can be used to track 
   * modifications that occur in the "id" of the object in order to make
   * duplication detection easier
   */
  public void addUpstreamDuplicate(String... duplicates) {
    Set<String> upstreamDuplicates = getProperty(UPSTREAMDUPLICATES);
    if (upstreamDuplicates == null) {
      upstreamDuplicates = new HashSet<String>();
      setProperty(UPSTREAMDUPLICATES, upstreamDuplicates);
    }
    for (String upstreamDuplicate : duplicates)
      upstreamDuplicates.add(upstreamDuplicate);
  }
  
  /**
   * Get the url of this object
   */
  public IRI getUrl() {
    return getProperty(URL);
  }
  
  /**
   * Set the url of this object 
   */
  public void setUrl(IRI url) {
    setProperty(URL,url);
  }
  
  /**
   * Get the collection of objects this object is considered a response to
   */
  public Iterable<ASObject> getInReplyTo() {
    return checkEmpty((Iterable<ASObject>)getProperty(INREPLYTO));
  }
  
  /**
   * Get the collection of objects this object is considered a response to
   * using the specified selector to filter the results
   */
  public Iterable<ASObject> getInReplyTo(Selector<ASObject> selector) {
    List<ASObject> list= new ArrayList<ASObject>();
    for (ASObject obj : getInReplyTo())
      if (selector.apply(obj))
        list.add(obj);
    return list;
  }
  
  /**
   * Set the collection of objects this object is considered a response to.
   * Note that duplicates are removed
   */
  public void setInReplyTo(java.util.Collection<ASObject> inReplyTo) {
    setProperty(INREPLYTO, new LinkedHashSet<ASObject>(inReplyTo));
  }
  
  /**
   * Add a new object this object is considered a response to
   */
  public void addInReplyTo(ASObject... inReplyTos) {
    Set<ASObject> list = getProperty(INREPLYTO);
    if (list == null) {
      list = new LinkedHashSet<ASObject>();
      setProperty(INREPLYTO, list);
    }
    for (ASObject inReplyTo : inReplyTos)
      list.add(inReplyTo);
  }
  
  /**
   * Get the "location" property
   */
  public PlaceObject getLocation() {
    return getProperty(LOCATION);
  }
  
  /**
   * Set the "location" property
   */
  public void setLocation(PlaceObject location) {
    setProperty(LOCATION, location);
    location.setObjectType(null);
  }
  
  /**
   * Get the "mood" property
   */
  public Mood getMood() {
    return getProperty(MOOD);
  }
  
  /**
   * Set the "mood" property
   */
  public void setMood(Mood mood) {
    setProperty(MOOD, mood);
  }
  
  /**
   * Get the "source" property
   */
  public <E extends ASObject>E getSource() {
    return (E)getProperty(SOURCE);
  }
  
  /**
   * Set the "source" property
   */
  public void setSource(ASObject source) {
    setProperty(SOURCE, source);
  }

  /**
   * Get the collection of tags for this object
   */
  public Iterable<ASObject> getTags() {
    return checkEmpty((Iterable<ASObject>)getProperty(TAGS));
  }
  
  /**
   * Set the collection of tags for this object. Duplicates 
   * will be removed.
   */
  public void setTags(java.util.Collection<ASObject> tags) {
    setProperty(TAGS, new LinkedHashSet<ASObject>(tags));
  }
  
  /**
   * Add an object to the collection of tags for this object
   */
  public void addTag(ASObject... tags) {
    Set<ASObject> list = getProperty(TAGS);
    if (list == null) {
      list = new LinkedHashSet<ASObject>();
      setProperty(TAGS, list);
    }
    for (ASObject tag : tags)
      list.add(tag); 
  }
  
  /**
   * @see org.apache.abdera2.activities.model.ASObject.getEmbeddedExperience()
   * {"embed":{...}}
   */
  public <T extends ASObject>T getEmbed() {
    return (T)getProperty(EMBED);
  }
  
  /**
   * @see org.apache.abdera2.activities.model.ASObject.setEmbeddedExperience()
   * {"embed":{...}}
   */
  public void setEmbed(ASObject embed) {
    setProperty(EMBED,embed);
  }
  
  /**
   * Get the "rating" property
   */
  public double getRating() {
    return (Double)getProperty(RATING);
  }
  
  /**
   * Set the "rating" property
   */
  public void setRating(double rating) {
    setProperty(RATING, rating);
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    String objectType = getObjectType();
    String displayName = getDisplayName();
    if (displayName != null)
      sb.append(displayName);
    else if (objectType != null) {
      char s = objectType.charAt(0);
      if ("aeiou".indexOf(s) > -1) 
        sb.append("an ");
      else sb.append("a ");
      sb.append(objectType);
    } else {
      sb.append("an object");
    }
    return sb.toString();
  }

  /**
   * "Embedded Experiences" were introduced to Activity Streams 
   * by the OpenSocial 2.0 specification, while functionally not 
   * specific to OpenSocial, the spec defines that the "Embedded 
   * Experience" document has to be wrapped within an "openSocial"
   * extension property. Other applications, such as Google+, however,
   * use the "embed" property directly within an object without the
   * "openSocial" wrapper. To use the "embed" property without 
   * the "openSocial" wrapper, use the setEmbed/getEmbed properties
   * on ASObject. To use OpenSocial style Embedded Experiences, 
   * use the setEmbeddedExperience/getEmbeddedExperience/hasEmbeddedExperience
   * methods. The OpenSocial style Embedded Experience should be used
   * primarily to associate OpenSocial Gadgets with an activity 
   * object while the alternative "embed" can be used to reference
   * any kind of embedded content.
   * 
   * {"openSocial":{"embed":{...}}}
   */
  public void setEmbeddedExperience(EmbeddedExperience embed) {
    ASBase os = getProperty("openSocial");
    if (os == null) {
      os = new ASBase();
      setProperty("openSocial", os);
    }
    os.setProperty("embed", embed);
  }
  
  /**
   * "Embedded Experiences" were introduced to Activity Streams 
   * by the OpenSocial 2.0 specification, while functionally not 
   * specific to OpenSocial, the spec defines that the "Embedded 
   * Experience" document has to be wrapped within an "openSocial"
   * extension property. Other applications, such as Google+, however,
   * use the "embed" property directly within an object without the
   * "openSocial" wrapper. To use the "embed" property without 
   * the "openSocial" wrapper, use the setEmbed/getEmbed properties
   * on ASObject. To use OpenSocial style Embedded Experiences, 
   * use the setEmbeddedExperience/getEmbeddedExperience/hasEmbeddedExperience
   * methods. The OpenSocial style Embedded Experience should be used
   * primarily to associate OpenSocial Gadgets with an activity 
   * object while the alternative "embed" can be used to reference
   * any kind of embedded content.
   * 
   * {"openSocial":{"embed":{...}}}
   */
  public EmbeddedExperience getEmbeddedExperience() {
    if (!has("openSocial")) return null;
    ASBase os = getProperty("openSocial");
    if (!os.has("embed")) return null;
    ASBase e = os.getProperty("embed");
    if (!(e instanceof EmbeddedExperience)) {
      e = e.as(EmbeddedExperience.class);
      os.setProperty("embed", e);
    }
    return (EmbeddedExperience) e;
  }
  
  /**
   * Checks to see if the "openSocial":{"embed":{...}} property exists
   */
  public boolean hasEmbeddedExperience() {
    if (!has("openSocial")) return false;
    ASBase os = getProperty("openSocial");
    return os.has("embed");
  }
  
  /**
   * Performs an equivalence check. Two ASObjects are equivalent if they
   * share the same objectType and id property values. All other properties
   * can be different (e.g. they are different versions of the same object)
   */
  public boolean is(ASObject obj) {
    return Extra.sameIdentity(this).apply(obj);
  }
  
  /**
   * Returns a union of all known IDs for this object.. specifically,
   * this is a union of the "id", "downstreamDuplicates" and "upstreamDuplicates"
   * properties
   */
  public Iterable<String> getKnownIds() {
    Set<String> list = new LinkedHashSet<String>();
    if (has("id")) list.add(getId());
    Iterables.addAll(list, checkEmpty(getDownstreamDuplicates()));
    Iterables.addAll(list, checkEmpty(getUpstreamDuplicates()));
    return list;
  }
  
  /**
   * Begins creating a new object using the fluent factory api
   */
  public static <X extends ASObjectGenerator<T>,T extends ASObject>X make() {
    return (X)new ASObjectGenerator<T>();
  }
  
  public static class ASObjectGenerator<T extends ASObject> extends Generator<T> {

    public ASObjectGenerator() {
      super((Class<? extends T>) ASObject.class);
      startNew();
    }
    
    public ASObjectGenerator(Class<? extends T> _class) {
      super(_class);
      startNew();
    }

    public <X extends ASObjectGenerator<T>>X attachment(ASObject object) {
      item.addAttachment(object);
      return (X)this;
    }
    
    public <X extends ASObjectGenerator<T>>X downstreamDuplicate(String id) {
      item.addDownstreamDuplicate(id);
      return (X)this;
    }
    
    public <X extends ASObjectGenerator<T>>X inReplyTo(ASObject object) {
      item.addInReplyTo(object);
      return (X)this;
    }
    
    public <X extends ASObjectGenerator<T>>X tag(ASObject object) {
      item.addTag(object);
      return (X)this;
    }
    
    public <X extends ASObjectGenerator<T>>X upstreamDuplicate(String id) {
      item.addUpstreamDuplicate(id);
      return (X)this;
    }
    
    public <X extends ASObjectGenerator<T>>X author(ASObject object) {
      item.setAuthor(object);
      return (X)this;
    }
    
    public <X extends ASObjectGenerator<T>>X content(String content) {
      item.setContent(content);
      return (X)this;
    }
    
    public <X extends ASObjectGenerator<T>>X displayName(String displayName) {
      item.setDisplayName(displayName);
      return (X)this;
    }
    
    public <X extends ASObjectGenerator<T>>X embed(ASObject object) {
      item.setEmbed(object);
      return (X)this;
    }
    
    public <X extends ASObjectGenerator<T>>X embeddedExperience(EmbeddedExperience ee) {
      item.setEmbeddedExperience(ee);
      return (X)this;
    }
    
    public <X extends ASObjectGenerator<T>>X id(String id) {
      item.setId(id);
      return (X)this;
    }
    
    public <X extends ASObjectGenerator<T>>X image(MediaLink link) {
      item.setImage(link);
      return (X)this;
    }
    
    public <X extends ASObjectGenerator<T>>X location(PlaceObject object) {
      item.setLocation(object);
      return (X)this;
    }
    
    public <X extends ASObjectGenerator<T>>X mood(Mood mood) {
      item.setMood(mood);
      return (X)this;
    }
    
    public <X extends ASObjectGenerator<T>>X objectType(String type) {
      item.setObjectType(type);
      return (X)this;
    }
    
    public <X extends ASObjectGenerator<T>>X published(DateTime dt) {
      item.setPublished(dt);
      return (X)this;
    }
    
    public <X extends ASObjectGenerator<T>>X publishedNow() {
      item.setPublishedNow();
      return (X)this;
    }
    
    public <X extends ASObjectGenerator<T>>X rating(double rating) {
      item.setRating(rating);
      return (X)this;
    }
    
    public <X extends ASObjectGenerator<T>>X source(ASObject object) {
      item.setSource(object);
      return (X)this;
    }
    
    public <X extends ASObjectGenerator<T>>X summary(String summary) {
      item.setSummary(summary);
      return (X)this;
    }
    
    public <X extends ASObjectGenerator<T>>X updated(DateTime dt) {
      item.setUpdated(dt);
      return (X)this;
    }
    
    public <X extends ASObjectGenerator<T>>X updatedNow() {
      item.setUpdatedNow();
      return (X)this;
    }
    
    public <X extends ASObjectGenerator<T>>X url(IRI url) {
      item.setUrl(url);
      return (X)this;
    }
    
    public <X extends ASObjectGenerator<T>>X url(String url) {
      return url(new IRI(url));
    }
  }
}
