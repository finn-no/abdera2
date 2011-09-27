package org.apache.abdera2.activities.extra;

import java.util.Iterator;

import org.apache.abdera2.activities.model.ASBase;
import org.apache.abdera2.common.templates.AbstractContext;

/**
 * URI Templates Context implementation based on an Activity Streams
 * object. Makes it easier to construct new URLs based on the properties
 * of an Activity Streams object. For example:
 * 
 * Template template = new Template("{?nextPageToken}"};
 * ASObject obj = new ASObject();
 * obj.setProperty("nextPageToken");
 * ASContext ctx = new ASContext(obj);
 * String the_new_iri = template.expand(ctx);
 * 
 */
public final class ASContext 
  extends AbstractContext {
  private static final long serialVersionUID = 4445623432125049535L;
  private final ASBase base;
  
  public ASContext(ASBase base) {
    this.base = base;
  }
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public <T> T resolve(String var) {
    Object obj = base.getProperty(var);
    if (obj instanceof Iterable) {
      return (T)new IterableWrapper((Iterable)obj);
    }
    return obj instanceof ASBase ? 
      (T)new ASContext((ASBase)obj) :
      (T)base.getProperty(var);
  }

  public void clear() {
    throw new UnsupportedOperationException();
  }

  public boolean contains(String var) {
    return base.has(var);
  }

  public Iterator<String> iterator() {
    return base.iterator();
  }
  
  @SuppressWarnings("rawtypes")
  public static class IterableWrapper 
    implements Iterable {

    private final Iterable i;
    
    IterableWrapper(Iterable i) {
      this.i = i;
    }
    
    public Iterator iterator() {
      return new IteratorWrapper(i.iterator());
    }
    
  }
  
  @SuppressWarnings("rawtypes")
  public static class IteratorWrapper 
    implements Iterator {

    private final Iterator i;
    
    IteratorWrapper(Iterator i) {
      this.i = i;
    }
    
    public boolean hasNext() {
      return i.hasNext();
    }

    public Object next() {
      Object obj = i.next();
      if (obj instanceof ASBase) 
        return new ASContext((ASBase)obj);
      if (obj instanceof Iterable)
        return new IterableWrapper((Iterable)obj);
      return obj;
    }

    public void remove() {
      i.remove();
    }
    
  }
}