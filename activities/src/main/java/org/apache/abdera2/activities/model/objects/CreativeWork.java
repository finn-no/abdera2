package org.apache.abdera2.activities.model.objects;

import java.util.Map;

import org.apache.abdera2.activities.model.ASObject;

/**
 * Abstract base class for several extension ASObject types
 */
@SuppressWarnings("unchecked")
public abstract class CreativeWork extends ASObject {


  protected <X extends CreativeWork, M extends Builder<X,M>>CreativeWork(Map<String,Object> map, Class<M> _class, Class<X>_obj) {
    super(map,_class,_obj);
  }
  
  public <T extends ASObject>T getAbout() {
    return (T)getProperty("about");
  }
  
  public <T extends ASObject>T getGenre() {
    return (T)getProperty("genre");
  }
  
  public <T extends ASObject>T getPublisher() {
    return (T)getProperty("publisher");
  }
  
  public <T extends ASObject>T getProvider() {
    return (T)getProperty("provider");
  }
  
  public <T extends ASObject>T getContributor() {
    return (T)getProperty("contributor");
  }
  
  public <T extends ASObject>T getEditor() {
    return (T)getProperty("editor");
  }
  
  public static abstract class Builder<X extends CreativeWork, M extends Builder<X,M>> 
    extends ASObject.Builder<X,M> {
    protected Builder(Class<X>_class,Class<M>_builder) {
      super(_class,_builder);
    }
    protected Builder(String objectType,Class<X>_class,Class<M>_builder) {
      super(objectType,_class,_builder);
    }
    protected Builder(Map<String,Object> map,Class<X>_class,Class<M>_builder) {
      super(map,_class,_builder);
    }
    public M about(ASObject object) {
      set("about",object);
      return (M)this;
    }
    public M contributor(ASObject object) {
      set("contributor",object);
      return (M)this;
    }
    public M editor(ASObject object) {
      set("editor",object);
      return (M)this;
    }
    public M genre(ASObject object) {
      set("genre",object);
      return (M)this;
    }
    public M provider(ASObject object) {
      set("provider",object);
      return (M)this;
    }
    public M publisher(ASObject object) {
      set("publisher",object);
      return (M)this;
    }
  }
}
