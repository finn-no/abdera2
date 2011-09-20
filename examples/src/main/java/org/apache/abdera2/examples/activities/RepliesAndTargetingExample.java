package org.apache.abdera2.examples.activities;

import org.apache.abdera2.activities.extra.Extra;
import org.apache.abdera2.activities.model.Activity;
import org.apache.abdera2.activities.model.Activity.Audience;
import org.apache.abdera2.activities.model.objects.NoteObject;
import org.apache.abdera2.activities.model.objects.PersonObject;

public class RepliesAndTargetingExample {

  public static void main(String... args) throws Exception {
    
    
    Activity activity = new Activity();
    
    // Specify the audience
    activity.addAudience(Audience.TO, new PersonObject("bob"));
    activity.addAudience(Audience.CC, Extra.FRIENDS("Colleagues"));
    activity.addAudience(Audience.BTO, new PersonObject("sally"));
    activity.addAudience(Audience.BCC, Extra.NETWORK());
    
    // Indicate that this is a reply to something else
    NoteObject note = new NoteObject();
    note.setId("urn:foo:note:1");
    activity.addInReplyTo(note);
    
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
