package org.apache.abdera2.activities.extra;

import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.objects.EventObject;

/**
 * Abstract extension of the basic event object type that adds
 * additional useful fields. Subclasses of this object MUST
 * define their own objectType names using the Name annotation
 * (@see org.apache.abdera2.common.anno.Name). 
 */
public abstract class ExtendedEventObject extends EventObject {

  private static final long serialVersionUID = 8368535995814591315L;

  public <T extends ASObject>T getHost() {
    return getProperty("host");
  }
  
  public void setHost(ASObject host) {
    setProperty("host", host);
  }
  
  public <T extends ASObject>T getOffers() {
    return getProperty("offers");
  }
  
  public void setOffers(ASObject offers) {
    setProperty("offers", offers);
  }
  
  public <T extends ASObject>T getSubEvents() {
    return getProperty("subEvents");
  }
  
  public void setSubEvents(ASObject subEvents) {
    setProperty("subEvents", subEvents);
  }
  
  public <T extends ASObject>T getSuperEvent() {
    return getProperty("superEvent");
  }
  
  public void setSuperEvent(ASObject superEvent) {
    setProperty("superEvent", superEvent);
  }
  
  public <T extends ASObject>T getPerformers() {
    return getProperty("performers");
  }
  
  public void setPerformers(ASObject performers) {
    setProperty("performers", performers);
  }
  
}
