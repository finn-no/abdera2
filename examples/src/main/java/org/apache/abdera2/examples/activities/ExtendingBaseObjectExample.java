package org.apache.abdera2.examples.activities;

import org.apache.abdera2.activities.model.IO;
import org.apache.abdera2.activities.model.objects.ExtendedEventObject;
import org.apache.abdera2.activities.model.objects.PersonObject;
import org.apache.abdera2.common.anno.Name;

/**
 * Quick example that shows how new object types can be
 * created by extending the base object types.. in this
 * case, we create a hypothetical representation of a 
 * Google+ style "hangout" being hosted by user "james"
 * with one other user ("joe") in attendance.
 */
public class ExtendingBaseObjectExample {

  @SuppressWarnings("unchecked")
  public static void main(String... args) throws Exception {

    // Building an activity stream for a reading list
    IO io = IO.get();
    io.addObjectMapping(Hangout.class);
    
    Hangout hangout = new Hangout();
    hangout.setHost(new PersonObject("james"));
    hangout.getAttending(true).addItem(new PersonObject("joe"));

    // another way to extend the Activity Stream object 
    // is by using the extend() method and passing in a 
    // simple Interface...
    OtherExtensions oe = hangout.extend(OtherExtensions.class);
    oe.setFoo("this is the value");
    
    // this is a type-safe alternative to using hangout.setProperty("foo","this is the value");
    
    io.write(hangout,System.out,"UTF-8");
  }

  @Name("hangout")
  public static class Hangout 
    extends ExtendedEventObject {
    private static final long serialVersionUID = -5466869609152673390L;
  }
  
  public static interface OtherExtensions {
    public String getFoo();
    public void setFoo(String bar);
  }
}
