package org.apache.abdera2.examples.activities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.abdera2.activities.model.Activity;
import org.apache.abdera2.activities.model.Collection;
import org.apache.abdera2.activities.model.IO;
import static org.apache.abdera2.activities.model.Verb.FOLLOW;
import static org.apache.abdera2.activities.model.Activity.makeActivity;
import static org.apache.abdera2.activities.model.objects.PersonObject.makePerson;

public class Activities {

  public static void main(String... args) throws Exception {
    
    // Simple Activities Example
    
    Activity activity = 
      makeActivity()
        .actor(
          makePerson()
            .displayName("James")
            .get())
        .verb(FOLLOW)
        .object(
          makePerson()
            .email("john.doe@example.org")
            .displayName("John Doe")
            .get())
        .get();
    
    activity.writeTo(System.out);
    
    System.out.println("\n\n\n");
    
    // Activity Stream
    Collection<Activity> collection = 
      Collection.<Activity>makeCollection()
        .item(activity)
        .get();
    
    collection.writeTo(System.out);
    
    System.out.println("\n\n\n");
    
    // Parsing example
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    collection.writeTo(out);
    
    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
    
    collection = IO.get().readCollection(in, "UTF-8");
    
    for (Activity a : collection.getItems()) {
      System.out.println(
        String.format("%s [%s] %s", 
          a.getActor().getDisplayName(), 
          a.getVerb(), 
          a.getObject().getDisplayName()));
    }
    
  }
  
}
