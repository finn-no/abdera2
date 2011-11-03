package org.apache.abdera2.examples.activities;

import org.apache.abdera2.activities.model.IO;
import org.apache.abdera2.activities.model.objects.EventObject;
import org.apache.abdera2.activities.model.objects.AdditionalEventProperties;
import org.apache.abdera2.activities.model.objects.PersonObject;

/**
 * Quick example that shows how new object types can be
 * created by extending the base object types.. in this
 * case, we create a hypothetical representation of a 
 * Google+ style "hangout" being hosted by user "james"
 * with one other user ("joe") in attendance.
 */
public class ExtendingBaseObjectExample {

  public static void main(String... args) throws Exception {
    IO io = IO.get();
    EventObject event = new EventObject();
    event.setObjectType("hangout");
    // the extend method dynamically attaches a new interface
    // to the object that can be used to specify extension
    // properties in a typesafe manner
    AdditionalEventProperties ext = 
      event.extend(
        AdditionalEventProperties.class);
    ext.setHost(new PersonObject("james"));
    event.getAttending(true).addItem(new PersonObject("joe"));
    io.write(event,System.out,"UTF-8");
  }
  
}
