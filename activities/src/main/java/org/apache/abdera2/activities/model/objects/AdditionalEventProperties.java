package org.apache.abdera2.activities.model.objects;

import org.apache.abdera2.activities.model.ASObject;

/**
 * Additional extension properties for the EventObject...
 * attach this interface to an EventObject using the 
 * extend method... e.g.
 * 
 * AdditionalEventProperties avp = event.extend(AdditionaEventProperties.class);
 * avp.setHost(...);
 * avp.setOffers(...);
 * ...
 */
public interface AdditionalEventProperties {
  <T extends ASObject>T getHost();
  void setHost(ASObject host);
  <T extends ASObject>T getOffers();
  void setOffers(ASObject offers);
  <T extends ASObject>T getSubEvents();
  void setSubEvents(ASObject subEvents);
  <T extends ASObject>T getSuperEvent();
  void setSuperEvent(ASObject superEvent);
  <T extends ASObject>T getPerformers();
  void setPerformers(ASObject performers);
}
