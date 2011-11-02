package org.apache.abdera2.examples.activities;

import org.apache.abdera2.activities.model.Activity;
import org.apache.abdera2.activities.model.objects.NoteObject;
import org.apache.abdera2.activities.model.objects.PersonObject;
import static org.apache.abdera2.activities.model.objects.Objects.FRIENDS;
import static org.apache.abdera2.activities.model.objects.Objects.NETWORK;

public class RepliesAndTargetingExample {

  public static void main(String... args) throws Exception {
    
    Activity activity = 
      Activity.makeActivity()
        .to(new PersonObject("bob"))
        .cc(FRIENDS("Colleagues"))
        .bto(new PersonObject("sally"))
        .bcc(NETWORK())
        .inReplyTo(
          NoteObject
            .makeNote()
            .id("urn:foo:note:1")
          .get())
        .get();
    
    activity.writeTo(System.out);
    
    /**
     * {"to":[
     *   {"displayName":"bob",
     *    "objectType":"person"}
     *  ],
     *  "inReplyTo":[
     *    {"id":"urn:foo:note:1",
     *     "objectType":"note"}
     *  ],
     *  "bto":[
     *    {"displayName":"sally",
     *     "objectType":"person"}
     *  ],
     *  "bcc":[
     *    {"objectType":"@network"}
     *  ],
     *  "objectType":"activity",
     *  "cc":[
     *    {"id":"Colleagues",
     *     "objectType":"@friends"}
     *  ]
     * }
     */
  }
  
}
