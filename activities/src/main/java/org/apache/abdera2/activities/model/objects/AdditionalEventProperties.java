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
  <T extends ASObject>T getOffers();
  <T extends ASObject>T getSubEvents();
  <T extends ASObject>T getSuperEvent();
  <T extends ASObject>T getPerformers();
  
  public static interface Builder {
    Builder host(ASObject host);
    Builder offers(ASObject offers);
    Builder subEvents(ASObject subEvents);
    Builder superEvent(ASObject superEvent);
    Builder performers(ASObject performers);
  }
  
}
