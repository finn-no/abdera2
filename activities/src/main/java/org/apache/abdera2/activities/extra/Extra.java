package org.apache.abdera2.activities.extra;


import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.IO;
import org.apache.abdera2.activities.model.Verb;

/**
 * Miscellaneous extensions
 */
@SuppressWarnings("unchecked")
public class Extra {

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
      VersionObject.class);
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
  
}
