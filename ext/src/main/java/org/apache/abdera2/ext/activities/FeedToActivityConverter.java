package org.apache.abdera2.ext.activities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.abdera2.activities.model.ASBase;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.Activity;
import org.apache.abdera2.activities.model.Collection;
import org.apache.abdera2.activities.model.CollectionWriter;
import org.apache.abdera2.activities.model.MediaLink;
import org.apache.abdera2.activities.model.Verb;
import org.apache.abdera2.activities.model.objects.PersonObject;
import org.apache.abdera2.activities.model.objects.ServiceObject;
import org.apache.abdera2.common.Constants;
import org.apache.abdera2.common.iri.IRI;
import org.apache.abdera2.common.selector.Selector;
import org.apache.abdera2.ext.history.FeedPagingHelper;
import org.apache.abdera2.model.Category;
import org.apache.abdera2.model.Entry;
import org.apache.abdera2.model.ExtensibleElement;
import org.apache.abdera2.model.Feed;
import org.apache.abdera2.model.Link;
import org.apache.abdera2.model.Person;
import org.apache.abdera2.model.Generator;
import org.apache.abdera2.model.Text;
import org.apache.abdera2.model.selector.LinkRelSelector;

/**
 * A default Atom Feed to Activity Stream Conversion implementation. 
 * Applications can customize the conversion process by subclassing
 * this class and overriding the various conversion methods.
 */
public class FeedToActivityConverter {

  public static final String NS = "http://activitystrea.ms/spec/1.0/";
  
  public static final QName VERB = new QName(NS,"verb");
  public static final QName TARGET = new QName(NS,"target");
  public static final QName OBJECT = new QName(NS,"object");
  public static final QName OBJECTTYPE = new QName(NS,"object-type");
  
  public void convert(Feed feed, CollectionWriter writer) {
    convert(feed,writer,null);
  }
  
  public Activity convert(Entry entry) {
    return item(entry);
  }
  
  @SuppressWarnings("rawtypes")
  public void convert(
    Feed feed, 
    CollectionWriter writer, 
    Selector selector) {
    ASBase header = header(feed);
    if (header != null)
      writer.writeHeader(header);
    Iterable<Entry> entries = 
      selector != null ? 
        feed.getEntries(selector) : 
        feed.getEntries();
    for (Entry entry : entries) {
      Activity activity = item(entry);
      if (activity != null)
        writer.writeObject(activity);
    }
    writer.complete();
  }

  protected ASBase header(Feed feed) {
    Collection<Activity> col = 
      new Collection<Activity>();
    col.setId(feed.getId().toString());
    col.setAuthor(authors(feed.getAuthors()));
    col.setProperty("contributors", authors(feed.getContributors()));
    categories(feed.getCategories(), col);
    col.setProperty("generator", generator(feed.getGenerator()));
    col.setImage(image(feed.getIcon()));
    col.setUrl(feed.getAlternateLinkResolvedHref());
    col.setDisplayName(feed.getTitle());
    col.setSummary(feed.getSubtitle());
    col.setUpdated(feed.getUpdated());
    col.setProperty("nextLink", FeedPagingHelper.getNext(feed));
    col.setProperty("previousLink", FeedPagingHelper.getPrevious(feed));
    col.setProperty("firstLink", FeedPagingHelper.getFirst(feed));
    col.setProperty("lastLink", FeedPagingHelper.getLast(feed));
    col.setProperty("selfLink", feed.getSelfLinkResolvedHref());
    return col;
  }
  
  protected Verb verb(Entry entry) {
    Verb verb = Verb.POST;
    if (entry.has(VERB)) {
      String val = entry.getSimpleExtension(VERB);
      // might be a url, just get the last component
      int n = val.indexOf('/');
      if (n > -1) val = val.substring(n+1);
      verb = Verb.get(val);
    }
    return verb;
  }
  
  protected Activity item(Entry entry) {
    Activity activity = new Activity();
    if (entry.getId() != null)
      activity.setId(entry.getId().toString());
    if (entry.getUpdated() != null)
      activity.setUpdated(entry.getUpdated());
    if (entry.getPublished() != null)
      activity.setPublished(entry.getPublished());
    activity.setUrl(entry.getAlternateLinkResolvedHref());
    activity.setTitle(entry.getTitle());
    activity.setSummary(entry.getSummary());
    categories(entry.getCategories(), activity);
    activity.setLang(entry.getLanguageTag());
    activity.setVerb(verb(entry));
    List<Person> authors = entry.getAuthorsInherited();
    activity.setActor(authors(authors));
    activity.setProperty("contributors", authors(entry.getContributors()));
    activity.setObject(object(entry));
    activity.setTarget(target(entry));
    List<Link> enclosures = entry.getLinks("enclosure");
    for (Link link : enclosures) {
      activity.addAttachment(
        attachment(link));
    }
    return activity;
  }
  
  protected String objectType(ExtensibleElement ext) {
    String objtype = null;
    if (ext.has(OBJECTTYPE)) {
      String val = ext.getSimpleExtension(OBJECTTYPE);
      int n = val.lastIndexOf('/');
      if (n > -1) val = val.substring(n+1);
      objtype = val;
    }
    return objtype;
  }
  
  protected ASObject object(ExtensibleElement ext) {
    ASObject obj = new ASObject();
    if (ext.has(Constants.ID))
      obj.setId(ext.getSimpleExtension(Constants.ID));
    if (ext.has(Constants.TITLE)) {
      Text text = ext.getExtension(Constants.TITLE);
      obj.setDisplayName(text.getValue());
    }
    if (ext.has(Constants.SUMMARY)) {
      Text text = ext.getExtension(Constants.SUMMARY);
      obj.setSummary(text.getValue());
    }
    obj.setObjectType(objectType(ext));
    LinkRelSelector sel = 
      new LinkRelSelector("alternate","preview");
    List<Link> links = ext.getExtensions(Constants.LINK, sel);
    for (Link link : links) {
      String rel = link.getCanonicalRel();
      if (Link.REL_ALTERNATE.equalsIgnoreCase(rel)) {
        obj.setUrl(link.getResolvedHref());
      } else if ("preview".equalsIgnoreCase(rel)) {
        obj.setImage(image(link.getResolvedBaseUri()));
      }
    }
    return obj;
  }
  
  protected ASObject target(Entry entry) {
    return entry.has(TARGET) ?
        object((ExtensibleElement)entry.getExtension(TARGET)) :
        null;
  }
  
  protected ASObject object(Entry entry) {    
    return entry.has(OBJECT) ?
      object((ExtensibleElement)entry.getExtension(OBJECT)) :
      object((ExtensibleElement)entry);
  }
  
  protected ASObject attachment(Link link) {
    ASObject obj = new ASObject();
    obj.setUrl(link.getResolvedHref());
    obj.setProperty("mimeType", link.getMimeType());
    obj.setDisplayName(link.getTitle());
    obj.setProperty("hreflang", link.getHrefLang());
    obj.setObjectType("link");
    return obj;
  }
  
  protected MediaLink image(IRI iri) {
    MediaLink link = null;
    if (iri != null) {
      link = new MediaLink();
      link.setUrl(iri);
    }
    return link;
  }
  
  protected ASObject generator(Generator generator) {
    ASObject _generator = null;
    if (generator != null) {
      _generator = new ServiceObject();
      _generator.setDisplayName(generator.getText());
      _generator.setProperty("version", generator.getVersion());
      _generator.setUrl(generator.getUri());
    }
    return _generator;
  }
  
  protected void categories(List<Category> categories, ASObject obj) {
    for (Category category : categories)
      obj.addTag(category(category));
  }
  
  protected ASObject category(Category category) {
    ASObject _category = new ASObject("category");
    _category.setDisplayName(category.getTerm());
    if (category.getScheme() != null)
      _category.setProperty("scheme", category.getScheme());
    return _category;
  }
  
  protected ASObject authors(List<Person> authors) {
    if (authors.size() == 1) {
      return person(authors.get(0));
    } else if (authors.size() > 1) {
      Collection<PersonObject> _authors = 
        new Collection<PersonObject>();
      for (Person person : authors) {
        PersonObject _person = person(person);
        if (_person != null) 
          _authors.addItem(_person);
      }
      return _authors;
    } else return null;
  }
  
  protected PersonObject person(Person person) {
    PersonObject _person = new PersonObject();
    _person.setDisplayName(person.getName());
    if (person.getUri() != null)
      _person.setUrl(person.getUri());
    if (person.getEmail() != null) {
      Set<String> emails = new HashSet<String>();
      emails.add(person.getEmail());
      _person.setEmails(emails);
    }
    return _person;
  }
}
