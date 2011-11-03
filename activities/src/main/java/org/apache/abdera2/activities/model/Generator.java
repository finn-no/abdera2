package org.apache.abdera2.activities.model;

import com.google.common.base.Supplier;

/**
 * The Generator is used to create instances of specific
 * types of Activity Objects. They are typically best 
 * used when generating multiple objects from a single
 * base template, for instance, when producing multiple
 * activity objects that share a base common set of 
 * properties (e.g. same actor, same provider, same verb, etc)
 */
public class Generator<T extends ASBase> implements Supplier<T> {

  private final ASBase template;
  private final Class<? extends T> _class;
  
  protected T item;
  
  public Generator(Class<? extends T> _class) {
    this(_class,null);
  }
  
  public Generator(Class<? extends T> _class, ASBase template) {
    this._class = _class;
    this.template = template;
  }
  
  @SuppressWarnings("unchecked")
  public <X extends Generator<T>>X startNew() {
    if (item != null) 
      throw new IllegalStateException();
    try {
      item = _class.newInstance();
      if (template != null) {
        for (String name : template) {
          Object obj = template.getProperty(name);
          item.setProperty(
            name, 
            obj instanceof Copyable ? 
              ((Copyable)obj).copy() : 
              obj);
        }
      }
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
    return (X)this;
  }
  
  @SuppressWarnings("unchecked")
  public <X extends Generator<T>>X set(String name, Object value) {
    if (item == null)
      throw new IllegalStateException();
    item.setProperty(name,value);
    return (X)this;
  }
  
  public T complete() {
    T t = item;
    item = null;
    return t;
  }
  
  public T get() {
    return complete();
  }
  
  public static interface Copyable {
    Object copy();
  }
  
  public static Generator<Activity> activityGenerator() {
    return new Generator<Activity>(Activity.class);
  }
  
  public static Generator<MediaLink> mediaLinkGenerator() {
    return new Generator<MediaLink>(MediaLink.class);
  }
  
  public static Generator<ASObject> objectGenerator() {
    return new Generator<ASObject>(ASObject.class);
  }
}
