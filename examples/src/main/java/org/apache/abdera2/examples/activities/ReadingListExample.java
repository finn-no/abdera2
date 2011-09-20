package org.apache.abdera2.examples.activities;

import org.apache.abdera2.activities.extra.BookObject;
import org.apache.abdera2.activities.extra.Extra;
import org.apache.abdera2.activities.model.Activity;
import org.apache.abdera2.activities.model.Collection;
import org.apache.abdera2.activities.model.Generator;
import org.apache.abdera2.activities.model.IO;
import org.apache.abdera2.activities.model.Verb;
import org.apache.abdera2.activities.model.objects.PersonObject;

/**
 * Example that shows a simple practical use of an activity stream
 * to represent a user activity on their reading list. For instance,
 * the "save" verb can be used to indicate that a user has added a 
 * book to their reading list, while the "read" verb can indicate 
 * that the user has read a book from their reading list
 */
public class ReadingListExample {

  public static void main(String... args) throws Exception {
    
    // Building an activity stream for a reading list
    IO io = IO.get();
    Extra.initExtras(io);
    
    Collection<Activity> stream = 
      new Collection<Activity>();
    
    Activity template = 
      new Activity();
    PersonObject actor = 
      new PersonObject("James");
    template.setActor(actor);
    Generator<Activity> gen = 
      template.newGenerator();
    
    // Add a book we want to read
    BookObject book1 = 
      new BookObject("The Cat in the Hat");
    stream.addItem(
      gen.startNew()
         .set("verb", Verb.SAVE)
         .set("object", book1)
         .set("format", Extra.EBOOK())
         .complete());
    
    // Add a book we just finished
    BookObject book2 =
      new BookObject("Meditations on the Method");
    book2.setAuthor(new PersonObject("Rene Descartes"));
    stream.addItem(
      gen.startNew()
         .set("verb", Extra.READ)
         .set("object", book2)
         .set("format", Extra.HARDCOVER())
         .complete());
    
    stream.writeTo(io,System.out);
    
  }
  
}
