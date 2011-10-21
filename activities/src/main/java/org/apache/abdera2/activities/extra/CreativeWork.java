package org.apache.abdera2.activities.extra;

import org.apache.abdera2.activities.model.ASObject;

/**
 * Abstract base class for several extension ASObject types
 */
@SuppressWarnings("unchecked")
public abstract class CreativeWork extends ASObject {

  private static final long serialVersionUID = -178336535850006357L;

  public CreativeWork() {}
  
  public CreativeWork(String displayName) {
    setDisplayName(displayName);
  }
  
  public <T extends ASObject>T getAbout() {
    return (T)getProperty("about");
  }
  
  public void setAbout(ASObject about) {
    setProperty("about", about);
  }
  
  public <T extends ASObject>T getGenre() {
    return (T)getProperty("genre");
  }
  
  public void setGenre(ASObject genre) {
    setProperty("genre", genre);
  }
  
  public <T extends ASObject>T getPublisher() {
    return (T)getProperty("publisher");
  }
  
  public void setPublisher(ASObject publisher) {
    setProperty("publisher", publisher);
  }
  
  public <T extends ASObject>T getProvider() {
    return (T)getProperty("provider");
  }
  
  public void setProvider(ASObject provider) {
    setProperty("provider", provider);
  }
  
  public <T extends ASObject>T getContributor() {
    return (T)getProperty("contributor");
  }
  
  public void setContributor(ASObject contributor) {
    setProperty("contributor", contributor);
  }
  
  public <T extends ASObject>T getEditor() {
    return (T)getProperty("editor");
  }
  
  public void setEditor(ASObject editor) {
    setProperty("editor", editor);
  }
  
  public static abstract class CreativeWorkGenerator<T extends CreativeWork> extends ASObjectGenerator<T> {
    protected CreativeWorkGenerator(Class<T> _class) {
      super(_class);
    }
    public <X extends CreativeWorkGenerator<T>>X about(ASObject object) {
      item.setAbout(object);
      return (X)this;
    }
    public <X extends CreativeWorkGenerator<T>>X contributor(ASObject object) {
      item.setContributor(object);
      return (X)this;
    }
    public <X extends CreativeWorkGenerator<T>>X editor(ASObject object) {
      item.setEditor(object);
      return (X)this;
    }
    public <X extends CreativeWorkGenerator<T>>X genre(ASObject object) {
      item.setGenre(object);
      return (X)this;
    }
    public <X extends CreativeWorkGenerator<T>>X provider(ASObject object) {
      item.setProvider(object);
      return (X)this;
    }
    public <X extends CreativeWorkGenerator<T>>X publisher(ASObject object) {
      item.setPublisher(object);
      return (X)this;
    }
  }
}
