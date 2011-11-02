package org.apache.abdera2.examples.activities;

import org.apache.abdera2.activities.model.Activity;
import org.apache.abdera2.activities.model.Collection;
import org.apache.abdera2.activities.model.Generator;
import org.apache.abdera2.activities.model.IO;

import static org.apache.abdera2.activities.model.Verb.SAVE;
import static org.apache.abdera2.activities.model.Verb.CONSUME;
import static org.apache.abdera2.activities.model.objects.Objects.EBOOK;
import static org.apache.abdera2.activities.model.objects.Objects.HARDCOVER;
import static org.apache.abdera2.activities.model.Activity.makeActivity;
import static org.apache.abdera2.activities.model.objects.BookObject.makeBook;
import static org.apache.abdera2.activities.model.objects.PersonObject.makePerson;
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
         .set("verb", SAVE)
         .set("object", 
           makeBook()
             .displayName("The Cat in the Hat")
             .get())
         .set("format", EBOOK())
         .complete());
    
    // Add a book we just finished
    builder.item(
      gen.startNew()
         .set("verb", CONSUME)
         .set("object", 
           makeBook()
             .displayName("Meditations on the Method")
             .author(
               makePerson()
                 .displayName("Rene Descartes")
                 .get())
             .get())
         .set("format", HARDCOVER())
         .complete());
    
    builder.get().writeTo(io,System.out);
    
  }
  
}
