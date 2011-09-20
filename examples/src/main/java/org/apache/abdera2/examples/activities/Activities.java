package org.apache.abdera2.examples.activities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.abdera2.activities.model.Activity;
import org.apache.abdera2.activities.model.Collection;
import org.apache.abdera2.activities.model.IO;
import org.apache.abdera2.activities.model.Verb;
import org.apache.abdera2.activities.model.objects.PersonObject;

public class Activities {

  public static void main(String... args) throws Exception {
    
    // Simple Activities Example
    
    Activity activity = new Activity();
    
    activity.setActor("James");     // Subject
    activity.setVerb(Verb.FOLLOW);  // Verb
     
                                    // Object
    PersonObject person = new PersonObject();
    person.setDisplayName("John Doe");
    person.setProperty("email", "john.doe@example.org");
    activity.setObject(person);
    
    activity.writeTo(System.out);
    
    System.out.println("\n\n\n");
    
    // Activity Stream
    Collection<Activity> collection = new Collection<Activity>();
    collection.addItem(activity);
    
    collection.writeTo(System.out);
    
    
    System.out.println("\n\n\n");
    
    // Parsing example
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    collection.writeTo(out);
    
    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
    
    collection = IO.get().readCollection(in, "UTF-8");
    
    for (Activity a : collection.getItems()) {
      System.out.println(String.format("%s [%s] %s", 
          a.getActor().getDisplayName(), 
          a.getVerb(), 
          a.getObject().getDisplayName()));
    }
    
  }
  
}
