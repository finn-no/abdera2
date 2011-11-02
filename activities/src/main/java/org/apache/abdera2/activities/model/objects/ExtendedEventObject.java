package org.apache.abdera2.activities.model.objects;

import org.apache.abdera2.activities.model.ASObject;

/**
 * Abstract extension of the basic event object type that adds
 * additional useful fields. Subclasses of this object MUST
 * define their own objectType names using the Name annotation
 * (@see org.apache.abdera2.common.anno.Name). 
 */
@SuppressWarnings("unchecked")
public abstract class ExtendedEventObject extends EventObject {

  private static final long serialVersionUID = 8368535995814591315L;

  public <T extends ASObject>T getHost() {
    return (T)getProperty("host");
  }
  
  public void setHost(ASObject host) {
    setProperty("host", host);
  }
  
  public <T extends ASObject>T getOffers() {
    return (T)getProperty("offers");
  }
  
  public void setOffers(ASObject offers) {
    setProperty("offers", offers);
  }
  
  public <T extends ASObject>T getSubEvents() {
    return (T)getProperty("subEvents");
  }
  
  public void setSubEvents(ASObject subEvents) {
    setProperty("subEvents", subEvents);
  }
  
  public <T extends ASObject>T getSuperEvent() {
    return (T)getProperty("superEvent");
  }
  
  public void setSuperEvent(ASObject superEvent) {
    setProperty("superEvent", superEvent);
  }
  
  public <T extends ASObject>T getPerformers() {
    return (T)getProperty("performers");
  }
  
  public void setPerformers(ASObject performers) {
    setProperty("performers", performers);
  }
  
  public static <T extends ExtendedEventObject>ExtendedEventObjectGenerator<T> makeExtendedEvent() {
    return new ExtendedEventObjectGenerator<T>();
  }
  
  public static class ExtendedEventObjectGenerator<T extends ExtendedEventObject> extends EventObjectGenerator<T> {
    public ExtendedEventObjectGenerator() {
      super((Class<? extends T>) ExtendedEventObject.class);
    }
    public ExtendedEventObjectGenerator(Class<T> _class) {
      super(_class);
    }
    public <X extends ExtendedEventObjectGenerator<T>>X host(ASObject object) {
      item.setHost(object);
      return (X)this;
    }
    public <X extends ExtendedEventObjectGenerator<T>>X offers(ASObject object) {
      item.setOffers(object);
      return (X)this;
    }
    public <X extends ExtendedEventObjectGenerator<T>>X performers(ASObject object) {
      item.setPerformers(object);
      return (X)this;
    }
    public <X extends ExtendedEventObjectGenerator<T>>X subEvents(ASObject object) {
      item.setSubEvents(object);
      return (X)this;
    }
    public <X extends ExtendedEventObjectGenerator<T>>X superEvent(ASObject object) {
      item.setSuperEvent(object);
      return (X)this;
    }
  }
}
