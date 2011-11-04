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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.lang.Iterable;

import org.apache.abdera2.activities.model.objects.PersonObject;
import org.apache.abdera2.activities.model.objects.ServiceObject;
import org.apache.abdera2.common.anno.Name;
import org.apache.abdera2.common.iri.IRI;
import org.apache.abdera2.common.selector.Selector;
import org.joda.time.DateTime;

/**
 * An Activity. Represents some action that has been taken. At it's core,
 * every Activity consists of an Actor (who performed the action), a Verb
 * (what action was performed) and an Object (what was acted upon), and a 
 * Target (to which the action is directed). For instance, if I said
 * "John posted a photo to his album", the Actor is "John", the Verb is 
 * "post", the Object is "a photo" and the Target is "his album". 
 */
@SuppressWarnings("unchecked")
@Name("activity")
public class Activity extends ASObject {

  private static final long serialVersionUID = -3284781784555866672L;
  public static final String ACTOR = "actor";
  public static final String CONTENT = "content";
  public static final String GENERATOR = "generator";
  public static final String ICON = "icon";
  public static final String ID = "id";
  public static final String OBJECT = "object";
  public static final String PUBLISHED = "published";
  public static final String PROVIDER = "provider";
  public static final String TARGET = "target";
  public static final String TITLE = "title";
  public static final String UPDATED = "updated";
  public static final String URL = "url";
  public static final String VERB = "verb";
  
  /**
   * Used to specify the target audience for an Activity.
   */
  public enum Audience { 
    /** Identifies the public primary target audience of the Activity **/
    TO,
    /** Identifies the private primary target audience of the Activity **/
    BTO, 
    /** Identifies the public secondary target audience of the Activity **/
    CC, 
    /** Identifies the private secondary target audience of the Activity **/
    BCC; 
    String label() {
      return name().toLowerCase();
    }
  };
  
  public Activity() {}
  
  public Activity(
    ASObject actor, 
    Verb verb) {
      setActor(actor);
      setVerb(verb);
  }
  
  public Activity(
    ASObject actor, 
    Verb verb, 
    ASObject object) {
      setActor(actor);
      setVerb(verb);
      setObject(object);
  }
  
  public Activity(
    ASObject actor, 
    Verb verb, 
    ASObject object, 
    ASObject target) {
      setActor(actor);
      setVerb(verb);
      setObject(object);
      setTarget(target);
  }
  
  public ASObject getActor() {
    return getProperty(ACTOR);
  }
  
  /**
   * Returns the Actor Object... if an actor object 
   * does not yet exist and create==true, a default
   * PersonObject will be created and set as the 
   * object and returned.
   */
  public <E extends ASObject>E getActor(boolean create) {
    ASObject obj = getActor();
    if (obj == null && create) {
      obj = new PersonObject();
      setActor(obj);
    }
    return (E)obj;
  }
  
  /**
   * Set the Actor for this Activity.
   */
  public void setActor(ASObject actor) {
    setProperty(ACTOR, actor);
  }
  
  /**
   * Set the Actor's displayName for this activity. 
   * If the Actor has not yet been set, a default
   * PersonObject will be created with the specified
   * displayName. If the Actor object has already 
   * been set, this will change the displayName to
   * that specified.
   */
  public <E extends ASObject>E setActor(String displayName) {
    ASObject obj = getActor(true);
    obj.setDisplayName(displayName);
    return (E)obj;
  }
  
  /**
   * Return the value of the "content" property for this activity
   */
  public String getContent() {
    return getProperty(CONTENT);
  }
  
  /**
   * Set the value of the "content" property for this activity
   */
  public void setContent(String content) {
    setProperty(CONTENT, content);
  }
  
  /**
   * Return the ASObject value of the "generator" property for this
   * activity.
   */
  public <E extends ASObject>E getGenerator() {
    return (E)getProperty(GENERATOR);
  }
  
  /**
   * Return the ASObject value for the "generator" property for this
   * activity. If the generator has not yet been set and create==true,
   * a default ServiceObject will be created, set and returned.
   */
  public <E extends ASObject>E getGenerator(boolean create) {
    ASObject obj = getGenerator();
    if (obj == null && create) {
      obj = new ServiceObject();
      setGenerator(obj);
    }
    return (E)obj;
  }
  
  /**
   * Set the value of the "generator" property for this Activity
   */
  public void setGenerator(ASObject generator) {
    setProperty(GENERATOR, generator); 
  }
  
  /**
   * Set the value of the "generator" properties displayName.
   * If the generator has not yet been set, a default ServiceObject
   * will be created, set and returned. Otherwise, the displayName
   * of the existing object will be changed to that specified.
   */
  public <E extends ASObject>E setGenerator(String displayName) {
    ASObject obj = getGenerator(true);
    obj.setDisplayName(displayName);
    return (E)obj;
  }
  
  /**
   * Return the value of the "icon" property 
   */
  public MediaLink getIcon() {
    return getProperty(ICON);
  }
  
  /**
   * Set the value of the "icon" property
   */
  public void setIcon(MediaLink icon) {
    setProperty(ICON, icon);  
  }
  
  /**
   * Set the value of the "icon" property. If the
   * property has not yet been set, the MediaLink
   * will be created.
   */
  public void setIcon(String uri) {
    if (uri == null) 
      setProperty(ICON,null);
    else {
      MediaLink link = getIcon();
      if (link == null) {
        link = new MediaLink();
        setIcon(link);
      }
      link.setUrl(uri);
    }
  }
  
  /**
   * Set the value of the "icon" property. If the
   * property has not yet been set, the MediaLink 
   * will be created.
   */
  public void setIcon(IRI uri) {
    setIcon(uri != null ? uri.toString() : null);
  }
  
  /**
   * Get the value of the "id" property
   */
  public String getId() {
    return getProperty(ID);
  }
  
  /**
   * Set the value of the "id" property
   */
  public void setId(String id) {
    setProperty(ID, id);
  }
  
  /**
   * set the value of the "id" property
   */
  public void setId(IRI id) {
    setId(id != null ? id.toString() : null);
  }
  
  /**
   * Get the Activities Object property
   */
  public <E extends ASObject>E getObject() {
    return (E)getProperty(OBJECT);
  }
  
  /**
   * Set the Activities Object property
   */
  public void setObject(ASObject object) {
    setProperty(OBJECT, object);
  }
  
  /**
   * Get the value of the Activities "published" property
   */
  public DateTime getPublished() {
    return getProperty(PUBLISHED);
  }
  
  /**
   * Set the value of the Activities "published" property
   */
  public void setPublished(DateTime published) {
    setProperty(PUBLISHED, published);
  }
  
  /**
   * Set the value of the Activities "published" property using the current date, time and default timezone
   */
  public void setPublishedNow() {
    setPublished(DateTime.now());
  }
  
  /**
   * Get the value of the Activities "provider" property
   */
  public <E extends ASObject>E getProvider() {
    return (E)getProperty(PROVIDER);
  }
  
  /**
   * Set the value of the Activities "provider" property.
   * If the value has not yet been set, a default ServiceObject
   * will be created, set and returned
   */
  public <E extends ASObject>E getProvider(boolean create) {
    ASObject obj = getProvider();
    if (obj == null && create) {
      obj = new ServiceObject();
      setProvider(obj);
    }
    return (E)obj;
  }
  
  /**
   * Set the value of the Activities "provider" property
   */
  public void setProvider(ASObject provider) {
    setProperty(PROVIDER, provider);
  }
  
  /**
   * Set the displayName of the Activities "provider" property.
   * If the object has not yet been created, a default 
   * ServiceObject will be created, otherwise the displayName
   * will be changed to the provided value
   */
  public <E extends ASObject>E setProvider(String displayName) {
    ASObject obj = getProvider(true);
    obj.setDisplayName(displayName);
    return (E)obj;
  }
  
  /**
   * Get the value of Activities "target" property
   */
  public <E extends ASObject>E getTarget() {
    return (E)getProperty(TARGET);
  }
  
  /**
   * Set the value of the Activities "target" property
   */
  public void setTarget(ASObject target) {
    setProperty(TARGET, target);
  }
  
  /**
   * Get the value of the "title" property
   */
  public String getTitle() {
    return getProperty(TITLE);
  }
  
  /**
   * Set the value of the "title" property
   */
  public void setTitle(String title) {
    setProperty(TITLE, title);
    
  }
  
  /**
   * Get the value of the "updated" property
   */
  public DateTime getUpdated() {
    return getProperty(UPDATED);
  }
  
  /**
   * Set the value of the "updated" property
   */
  public void setUpdated(DateTime updated) {
    setProperty(UPDATED, updated);
  }
  
  /**
   * Set the value of the "updated" property to the current date, time and default timezone
   */
  public void setUpdatedNow() {
    setUpdated(DateTime.now());
  }
  
  /**
   * Get the value of the "url" property
   */
  public IRI getUrl() {
    return getProperty(URL);
  }
  
  /**
   * Set the value of the "url" property
   */
  public void setUrl(IRI url) {
    setProperty(URL, url);
  }
  
  /**
   * Get the value of the "verb" property
   */
  public Verb getVerb() {
    return getProperty(VERB);
  }
  
  /**
   * Set the value of the "verb" property
   */
  public void setVerb(Verb verb) {
    setProperty(VERB, verb);
  }
  
  /**
   * Set the value of the "verb" property
   */
  public void setVerb(String verb) {
    setVerb(verb != null ? Verb.get(verb) : null);
  }

  /**
   * Get the value of the "author" property.. for Activity 
   * objects, the "author" property is mapped to the "actor"
   * property in order to avoid duplication of content. 
   * If you want to get the actual "author" property, use
   * getProperty("author")...
   */
  public <E extends ASObject>E getAuthor() {
    return (E)getActor();
  }
  
  /**
   * Set the value of the "author" property.. for Activity
   * objects, the "author" property is mapped to the "actor"
   * property in orde to avoid duplication of content. 
   * If you want to set the actual "author" property,  use
   * setProperty("author",val)
   */
  public <E extends ASObject>E getAuthor(boolean create) {
    return (E)getActor(create);
  }

  /**
   * Set the value of the "author" property.. for Activity
   * objects, the "author" property is mapped to the "actor"
   * property in orde to avoid duplication of content. 
   * If you want to set the actual "author" property,  use
   * setProperty("author",val)
   */
  public void setAuthor(ASObject author) {
    setActor(author); 
  }
  
  /**
   * Set the value of the "author" property.. for Activity
   * objects, the "author" property is mapped to the "actor"
   * property in order to avoid duplication of content. 
   * If you want to set the actual "author" property,  use
   * setProperty("author",val)
   */
  public <E extends ASObject>E setAuthor(String displayName) {
    return (E)setActor(displayName);
  }

  /**
   * Get the "displayName" property. For Activity objects, the
   * "displayName" property is mapped to the "title" property.
   * If you want to get the actual "displayName" property,
   * us getProperty("displayName")
   */
  public String getDisplayName() {
    return getTitle();
  }

  /**
   * Set the "displayName" property. For Activity objects, the
   * "displayName" property is mapped to the "title" property.
   * If you want to set the actual "displayName" property,
   * us setProperty("displayName",val)
   */
  public void setDisplayName(String displayName) {
    setTitle(displayName);
  }

  /**
   * Get the "image" property. For Activity objects, the
   * "image" property is mapped to the "icon" property.
   * If you want to get the actual "image" property,
   * us getProperty("image")
   */
  public MediaLink getImage() {
    return getIcon();
  }

  /**
   * Set the "image" property. For Activity objects, the
   * "image" property is mapped to the "icon" property.
   * If you want to set the actual "image" property,
   * us setProperty("image",val)
   */
  public void setImage(MediaLink image) {
    setIcon(image);
  }
  
  /**
   * Get the specified target audience for the activity
   */
  public Iterable<ASObject> getAudience(Audience audience) {
    return checkEmpty((Iterable<ASObject>)getProperty(audience.label()));
  }
  
  /**
   * Get the specified target audience for the activity using the 
   * specified selector as a filter to limit the results. This 
   * can be used, for instance, to quickly determine if a particular 
   * entity is included in the audience of the activity
   */
  public Iterable<ASObject> getAudience(Audience audience, Selector<ASObject> selector) {
    List<ASObject> list = new ArrayList<ASObject>();
    for (ASObject obj : getAudience(audience))
      if (selector.apply(obj))
        list.add(obj);
    return list;
  }
  
  /**
   * Set the target audience for the activity
   */
  public void setAudience(Audience audience, Set<ASObject> set) {
    setProperty(audience.label(), set);
  }
  
  /**
   * Add one or more entities to the target audience of the activity.
   * Unlike setAudience, this will not overwrite the existing audience
   * property values.
   */
  public void addAudience(Audience audience, ASObject... objs) {
    Set<ASObject> list = getProperty(audience.label());
    if (list == null) {
      list = new HashSet<ASObject>();
      setProperty(audience.label(),list);
    }
    for (ASObject obj : objs)
      list.add(obj);
  }

  /**
   * Begin creating a new Activity object using the fluent factory API
   */
  public static <T extends Activity>ActivityGenerator<T> makeActivity() {
    return new ActivityGenerator<T>();
  }
  
  public static class ActivityGenerator<T extends Activity> extends ASObjectGenerator<T> {
    ActivityGenerator() {
      super((Class<? extends T>) Activity.class);
    }
    protected ActivityGenerator(Class<? extends T> _class) {
      super(_class);
    }
    public <X extends ActivityGenerator<T>>X to(ASObject object) {
      item.addAudience(Audience.TO, object);
      return (X)this;
    }
    public <X extends ActivityGenerator<T>>X cc(ASObject object) {
      item.addAudience(Audience.CC, object);
      return (X)this;
    }
    public <X extends ActivityGenerator<T>>X bcc(ASObject object) {
      item.addAudience(Audience.BCC, object);
      return (X)this;
    }
    public <X extends ActivityGenerator<T>>X bto(ASObject object) {
      item.addAudience(Audience.BTO, object);
      return (X)this;
    }
    public <X extends ActivityGenerator<T>>X actor(ASObject object) {
      item.setActor(object);
      return (X)this;
    }
    public <X extends ActivityGenerator<T>>X generator(ASObject object) {
      item.setGenerator(object);
      return (X)this;
    }
    public <X extends ActivityGenerator<T>>X icon(MediaLink link) {
      item.setIcon(link);
      return (X)this;
    }
    public <X extends ActivityGenerator<T>>X object(ASObject object) {
      item.setObject(object);
      return (X)this;
    }
    public <X extends ActivityGenerator<T>>X provider(ASObject object) {
      item.setProvider(object);
      return (X)this;
    }
    public <X extends ActivityGenerator<T>>X target(ASObject object) {
      item.setTarget(object);
      return (X)this;
    }
    public <X extends ActivityGenerator<T>>X title(String title) {
      item.setTitle(title);
      return (X)this;
    }
    public <X extends ActivityGenerator<T>>X verb(Verb verb) {
      item.setVerb(verb);
      return (X)this;
    }
  }
}
