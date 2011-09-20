package org.apache.abdera2.test.selector;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.model.Entry;
import org.apache.abdera2.model.Link;
import org.apache.abdera2.model.selector.LinkHrefLangSelector;
import org.apache.abdera2.model.selector.LinkRelSelector;
import org.junit.Test;

public class SelectorTest {

  @Test
  public void selectorTest() {
    
    Abdera abdera = Abdera.getInstance();
    Entry entry = abdera.newEntry();
    entry.addLink("foo1", "bar").setHrefLang("en");
    entry.addLink("foo2", "bar").setHrefLang("fr");
    entry.addLink("foo3", "baz").setHrefLang("fr");
    
    LinkRelSelector lrs = new LinkRelSelector("bar");
    
    List<Link> links = entry.getLinks(lrs);
    assertEquals(2,links.size());
    assertEquals("foo1",links.get(0).getHref().toString());
    assertEquals("foo2",links.get(1).getHref().toString());
    
    LinkHrefLangSelector lhls = new LinkHrefLangSelector("fr");
    links = entry.getLinks(lhls);
    assertEquals(2,links.size());
    assertEquals("foo2",links.get(0).getHref().toString());
    assertEquals("foo3",links.get(1).getHref().toString());
    
  }
  
}
