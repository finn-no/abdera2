package org.apache.abdera2.examples.activities;

import org.apache.abdera2.activities.extra.Extra;
import org.apache.abdera2.activities.model.Activity;
import org.apache.abdera2.activities.model.Collection;
import org.apache.abdera2.activities.model.Generator;
import org.apache.abdera2.activities.model.IO;
import org.apache.abdera2.activities.model.Verb;

import static org.apache.abdera2.activities.model.Activity.makeActivity;
import static org.apache.abdera2.activities.model.objects.PersonObject.makePerson;
import static org.apache.abdera2.activities.extra.BookObject.makeBook;
import static org.apache.abdera2.activities.model.Collection.makeCollection;

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
    
    Generator<Activity> gen = 
      makeActivity()
        .actor(
          makePerson()
            .displayName("James")
            .get())
        .get().newGenerator();

    Collection.CollectionGenerator<Activity> builder = 
      makeCollection();
    
    // Add a book we want to read
    builder.item(
      gen.startNew()
         .set("verb", Verb.SAVE)
         .set("object", 
           makeBook()
             .displayName("The Cat in the Hat")
             .get())
         .set("format", Extra.EBOOK())
         .complete());
    
    // Add a book we just finished
    builder.item(
      gen.startNew()
         .set("verb", Extra.READ)
         .set("object", 
           makeBook()
             .displayName("Meditations on the Method")
             .author(
               makePerson()
                 .displayName("Rene Descartes")
                 .get())
             .get())
         .set("format", Extra.HARDCOVER())
         .complete());
    
    builder.get().writeTo(io,System.out);
    
  }
  
}
