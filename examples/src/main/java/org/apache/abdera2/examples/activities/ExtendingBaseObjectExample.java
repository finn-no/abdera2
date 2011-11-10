package org.apache.abdera2.examples.activities;

import org.apache.abdera2.activities.model.Collection;
import org.apache.abdera2.activities.model.IO;
import org.apache.abdera2.activities.model.objects.EventObject;
import org.apache.abdera2.activities.model.objects.AdditionalEventProperties;
import org.apache.abdera2.activities.model.objects.EventObject.EventBuilder;
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
    EventBuilder builder = 
      EventObject.makeEvent("hangout");
    // the extend method dynamically attaches a new interface
    // to the object that can be used to specify extension
    // properties in a typesafe manner
    builder.extend(
      AdditionalEventProperties.Builder.class)
       .host(PersonObject.makePerson().displayName("James").get());
    builder.attending(
      Collection.makeCollection()
        .item(
           PersonObject
             .makePerson()
               .displayName("Joe")
                 .get())
                   .get());
    io.write(builder.get(),System.out,"UTF-8");
  }
  
}
