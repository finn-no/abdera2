package org.apache.abdera2.model.selector;

import java.util.Locale;
import java.util.Map;

import javax.activation.MimeType;

import org.apache.abdera2.common.iri.IRI;
import org.apache.abdera2.common.lang.Lang;
import org.apache.abdera2.common.lang.Range;
import org.apache.abdera2.common.selector.PropertySelector;
import org.apache.abdera2.common.selector.Selector;
import org.apache.abdera2.model.Category;
import org.apache.abdera2.model.Collection;
import org.apache.abdera2.model.Entry;
import org.apache.abdera2.model.Link;
import org.apache.abdera2.xpath.XPath;
import org.joda.time.DateTime;

import com.google.common.base.Predicate;

public final class Selectors {

  private Selectors() {}
  
  public static Selector<Entry> updated(Predicate<DateTime> predicate) {
    return 
      PropertySelector.<Entry>create(
        Entry.class, 
        "getUpdated",
        predicate);
  }
  
  public static Selector<Entry> edited(Predicate<DateTime> predicate) {
    return
      PropertySelector.<Entry>create(
        Entry.class,
        "getEdited",
        predicate);
  }
  
  public static Selector<Entry> published(Predicate<DateTime> predicate) {
    return
      PropertySelector.<Entry>create(
        Entry.class,
        "getPublished",
        predicate);
  }
  
  public static Selector<Category> withCategoryScheme(String... schemes) {
    return CategorySchemeSelector.of(schemes);
  }
  
  public static Selector<Category> withCategoryScheme(IRI... schemes) {
    return CategorySchemeSelector.of(schemes);
  }
  
  public static Selector<Collection> accepts(String... types) {
    return CollectionAcceptSelector.of(types);
  }
  
  public static Selector<Collection> accepts(MimeType... types) {
    return CollectionAcceptSelector.of(types);
  }
  
  public static Selector<Link> withHrefLang(Range range) {
    return LinkHrefLangSelector.of(range);
  }
  
  public static Selector<Link> withHrefLang(String... langs) {
    return LinkHrefLangSelector.of(langs);
  }
  
  public static Selector<Link> withHrefLang(Lang... langs) {
    return LinkHrefLangSelector.of(langs);
  }
  
  public static Selector<Link> withHrefLang(Locale locale) {
    return LinkHrefLangSelector.of(Lang.fromLocale(locale));
  }
  
  public static Selector<Link> withRel(String... rels) {
    return LinkRelSelector.of(rels);
  }
  
  public static Selector<Object> xpath(String path) {
    return XPathSelector.make().path(path).get();
  }
  
  public static Selector<Object> xpath(String path, XPath xpath) {
    return XPathSelector.make(xpath).path(path).get();
  }
  
  public static Selector<Object> xpath(String path, XPath xpath, Map<String, String> namespaces) {
    return XPathSelector.make(xpath).path(path).with(namespaces).get();
  }
  
  public static XPathSelector.Builder xpath() {
    return XPathSelector.make();
  }
}
