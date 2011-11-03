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
    IO io = IO.get();
    
    
    // A simple fluent factory model can be used to create 
    // activities and their associated objects... several
    // of the methods here are statically imported so be
    // sure to take a look at the import statements at 
    // the top of the class to see where various bits are 
    // coming from.
    //
    // this activity basically says: "James is following John"
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
    
    // All activities objects can handle their own serialization 
    // using the writeTo method, or you can use the IO object 
    // directly... reusing the IO object will be more efficient
    // if you're doing a lot of serialization of objects
    activity.writeTo(io,System.out);
    
    System.out.println("\n\n\n");
    
    // Now that we have a single activity, let's create the 
    // "Activity Stream"... a stream is essentially a 
    // Collection Object whose items are all Activities. 
    // It's possible to have Collections of other types 
    // of objects too..
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
    
    collection = io.readCollection(in, "UTF-8");
    
    for (Activity a : collection.getItems()) {
      System.out.println(
        String.format("%s [%s] %s", 
          a.getActor().getDisplayName(), 
          a.getVerb(), 
          a.getObject().getDisplayName()));
    }
    
  }
  
}
