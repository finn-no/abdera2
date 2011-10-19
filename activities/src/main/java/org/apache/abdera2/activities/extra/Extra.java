package org.apache.abdera2.activities.extra;



import java.util.Comparator;

import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.Activity;
import org.apache.abdera2.activities.model.IO;
import org.apache.abdera2.activities.model.Verb;
import org.apache.abdera2.common.date.DateTimes;
import org.apache.abdera2.common.selector.AbstractSelector;
import org.apache.abdera2.common.selector.PropertySelector;
import org.apache.abdera2.common.selector.Selector;
import org.joda.time.DateTime;

import com.google.common.base.Equivalence;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import static com.google.common.base.Preconditions.*;

/**
 * Miscellaneous extensions
 */
@SuppressWarnings("unchecked")
public class Extra {

  public static Selector<Activity> usesVerb(Verb verb) {
    return PropertySelector.<Activity>create(
      Activity.class, 
      "getVerb", 
      Predicates.equalTo(verb));
  }
  
  public static <A extends ASObject>Selector<A> published(Class<A> _class, Predicate<?> predicate) {
    return PropertySelector.<A>create(
      _class,
      "getPublished",
      predicate);
  }
  
  public static Selector<Activity> activityPublished(Predicate<DateTime> predicate) {
    return published(Activity.class,predicate);
  }
  
  public static Selector<ASObject> objectPublished(Predicate<DateTime> predicate) {
    return published(ASObject.class,predicate);
  }
  
  public static <A extends ASObject>Selector<A> updated(Class<A> _class, Predicate<DateTime> predicate) {
    return PropertySelector.<A>create(
      _class,
      "getUpdated",
      predicate);
  }
  
  public static Selector<Activity> activityUpdated(Predicate<DateTime> predicate) {
    return updated(Activity.class,predicate);
  }
  
  public static Selector<ASObject> objectUpdated(Predicate<DateTime> predicate) {
    return updated(ASObject.class,predicate);
  }
  
  // As in "Sally purchased the app"
  public static final Verb PURCHASE = new Verb("purchase") {};
  
  // As in: "Joe is hosting a meeting"
  public static final Verb HOST = new Verb("host") {};
  
  // As in: "Mark read the book" ... this is related to "play", but saying that
  // someone "played" a book just doesn't make much sense. A user can 
  // "play" and audio book, but they must "read" the physical or ebook,
  // also works for "Mark read the note", "Sally read the question", etc
  public static final Verb READ = new Verb("read") {};
  
  // As in "Sally approved the line item"
  public static final Verb APPROVE = new Verb("approve") {};
  
  // As in "Sally rejected the line item"
  public static final Verb REJECT = new Verb("reject") {};
  
  // As in "Sally archived the document"
  public static final Verb ARCHIVE = new Verb("archive") {};
  
  // As in "Mark installed the app"
  public static final Verb INSTALL = new Verb("install") {};
  
  // As in "Mark closed the issue"
  public static final Verb CLOSE = new Verb("close") {};
  
  // As in "Mark opened the issue" .. careful not to confuse this with
  // creating an issue, for instance. For example, in source code 
  // management, creating a new issue and "opening" it are two separate
  // tasks. An item can be opened automatically when it is created,
  // closed, and then opened again if it is determined to not have been
  // resolved, etc.
  public static final Verb OPEN = new Verb("open") {};
  
  // As in "Mark resolved the issue" .. careful not to confuse this with
  // updating the issue or closing it. 
  public static final Verb RESOLVE = new Verb("resolve") {};
  
  
  /**
   * Registers the "extra" object types with the IO instance
   * for serialization/deserialization.
   */
  public static void initExtras(IO io) {

    io.addObjectMapping(
      BookObject.class,
      MovieObject.class,
      OfferObject.class,
      TvEpisodeObject.class,
      TvSeasonObject.class,
      TvSeriesObject.class,
      VersionObject.class,
      BinaryObject.class);
  }
  
  /**
   * Special AS Object that represents the authenticated user
   */
  public static ASObject SELF() {
    return new ASObject("@self");
  }
  
  /**
   * Special AS Object that represents the authenticated user.
   * synonymous with @self
   */
  public static ASObject ME() {
    return new ASObject("@me");
  }
  
  /**
   * Special AS Object that represents the authenticated users 
   * collection of direct contacts
   */
  public static ASObject FRIENDS() {
    return new ASObject("@friends");
  }
  
  /**
   * Special AS Object that represents a subset of the authenticated users
   * collection of direct contacts
   */
  public static ASObject FRIENDS(String id) {
    ASObject obj = FRIENDS();
    obj.setId(id);
    return obj;
  }
  
  /**
   * Special AS Object that represents the authenticated users collection
   * of extended contacts (e.g. friends of friends)
   */
  public static ASObject NETWORK() {
    return new ASObject("@network");
  }
  
  /**
   * Special AS Object that represents everyone. synonymous with @public
   */
  public static ASObject ALL() {
    return new ASObject("@all");
  }
  
  /**
   * Special AS Object that represents everyone
   */
  public static ASObject PUBLIC() {
    return new ASObject("@public");
  }
  
  /**
   * Create an anonymous AS Object (no objectType property)
   */
  public static ASObject anonymousObject(String id) {
    ASObject obj = new ASObject();
    obj.setObjectType(null);
    obj.setId(id);
    return obj;
  }
  
  public static ASObject DISCONTINUED() {
    return anonymousObject("discontinued");
  }
  
  public static ASObject INSTOCK() {
    return anonymousObject("in-stock");
  }
  
  public static ASObject INSTOREONLY() {
    return anonymousObject("in-store-only");
  }
  
  public static ASObject ONLINEONLY() {
    return anonymousObject("online-only");
  }
  
  public static ASObject OUTOFSTOCK() {
    return anonymousObject("out-of-stock");
  }
  
  public static ASObject PREORDER() {
    return anonymousObject("pre-order");
  }
  
  public static ASObject EBOOK() {
    return anonymousObject("ebook");
  }
  
  public static ASObject HARDCOVER() {
    return anonymousObject("hardcover");
  }
  
  public static ASObject PAPERBACK() {
    return anonymousObject("paperback");
  }
  
  public static ASObject DAMAGED() {
    return anonymousObject("damaged");
  }
  
  public static ASObject NEW() {
    return anonymousObject("new");
  }
  
  public static ASObject REFURBISHED() {
    return anonymousObject("refurbished");
  }
  
  public static ASObject USED() {
    return anonymousObject("used");
  }
  
  public static Selector<ASObject> sameIdentity(final ASObject obj) {
    return new AbstractSelector<ASObject>() {
      public boolean select(Object item) {
        checkArgument(item instanceof ASObject);
        ASObject other = (ASObject) item;
        return IDENTITY_EQUIVALENCE.equivalent(obj, other);
      }     
    };
  }
  
  public static final Equivalence<ASObject> IDENTITY_EQUIVALENCE = identity();
  
  /**
   * Two ASObject's are considered equivalent in identity if 
   * they share the same objectType and id property
   * values. 
   */
  private static Equivalence<ASObject> identity() {
    return new Equivalence<ASObject>() {
      protected boolean doEquivalent(ASObject a, ASObject b) {
        if (a != null && b == null) return false;
        if (a == null && b != null) return false;
        String aot = a.getObjectType();
        String bot = b.getObjectType();
        if (aot != null && bot == null) return false;
        if (aot == null && bot != null) return false; 
        if (aot != null)
          if (!aot.equalsIgnoreCase(bot)) return false;
        String aid = a.getId();
        String bid = b.getId();
        if (aid != null && bid == null) return false;
        if (aid == null && bid != null) return false;
        if (aid != null)
          if (!aid.equals(bid)) return false;
        return true;
      }
      protected int doHash(ASObject t) {
        String id = t.getId();
        String objectType = t.getObjectType();
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
            + ((objectType == null) ? 0 : objectType.hashCode());
        return result;
      }
    };
  }
  
  public static final Comparator<ASObject> UPDATED_COMPARATOR = 
    new UpdatedComparator();
  public static final Comparator<ASObject> PUBLISHED_COMPARATOR = 
    new PublishedComparator();
  
  private static class UpdatedComparator 
    extends DateTimes.DateTimeComparator<ASObject> {
      public int compare(ASObject a1, ASObject a2) {
        DateTime d1 = a1.getUpdated();
        DateTime d2 = a2.getUpdated();
        return innerCompare(d1,d2);
      }
  }
  
  private static class PublishedComparator 
    extends DateTimes.DateTimeComparator<ASObject> {
      public int compare(ASObject a1, ASObject a2) {
        DateTime d1 = a1.getPublished();
        DateTime d2 = a2.getPublished();
        return innerCompare(d1,d2);
      }
  }
}
