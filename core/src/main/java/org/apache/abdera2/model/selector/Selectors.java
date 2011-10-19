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

public class Selectors {

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
    return new CategorySchemeSelector(schemes);
  }
  
  public static Selector<Category> withCategoryScheme(IRI... schemes) {
    return new CategorySchemeSelector(schemes);
  }
  
  public static Selector<Collection> accepts(String... types) {
    return new CollectionAcceptSelector(types);
  }
  
  public static Selector<Collection> accepts(MimeType... types) {
    return new CollectionAcceptSelector(types);
  }
  
  public static Selector<Link> withHrefLang(Range range) {
    return new LinkHrefLangSelector(range);
  }
  
  public static Selector<Link> withHrefLang(String... langs) {
    return new LinkHrefLangSelector(langs);
  }
  
  public static Selector<Link> withHrefLang(Lang... langs) {
    return new LinkHrefLangSelector(langs);
  }
  
  public static Selector<Link> withHrefLang(Locale locale) {
    return new LinkHrefLangSelector(Lang.fromLocale(locale));
  }
  
  public static Selector<Link> withRel(String... rels) {
    return new LinkRelSelector(rels);
  }
  
  @SuppressWarnings("rawtypes")
  public static Selector xpath(String path) {
    return new XPathSelector(path);
  }
  
  @SuppressWarnings("rawtypes")
  public static Selector xpath(String path, XPath xpath) {
    return new XPathSelector(path,xpath);
  }
  
  @SuppressWarnings("rawtypes")
  public static Selector xpath(String path, XPath xpath, Map<String, String> namespaces) {
    return new XPathSelector(path,xpath,namespaces);
  }
}
