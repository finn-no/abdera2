package org.apache.abdera2.activities.model;

/**
 * Base implementation of the CollectionWriter interface.. handles
 * basic flow and state management
 */
@SuppressWarnings("unchecked")
public abstract class AbstractCollectionWriter 
  implements CollectionWriter {

  protected boolean _items = false;
  protected boolean _header = false;
  
  protected void flush() {}
  
  protected abstract void write(String name, Object val);
  
  protected abstract void startItems();
  
  protected abstract void writeItem(ASObject object);
  
  public abstract void complete();
  
  public <X extends CollectionWriter>X writeHeader(ASBase.Builder<?,?> base) {
    return writeHeader(base.get());
  }
  
  public <X extends CollectionWriter>X writeHeader(ASBase base) {
    if (_items || _header)
    throw new IllegalStateException();
    if (base != null) {
      for (String name : base) {
        if (!"items".equals(name)) {
          Object val = base.getProperty(name);
          write(name,val);
        }
      }
    }
    _header = true;
    flush();
    return (X)this;
  }
  
  public <X extends CollectionWriter>X writeObject(ASObject.Builder<?, ?> object) {
    return writeObject(object.get());
  }
  
  public <X extends CollectionWriter>X writeObject(ASObject object) {
    if (!_items) {
      startItems();
      _items = true;
    }
    writeItem(object);
    flush();
    return (X)this;
  }
  
  public <X extends CollectionWriter>X writeObjects(ASObject... objects) {
    for (ASObject object : objects)
      writeObject(object);
    return (X)this;
  }
  
  public <X extends CollectionWriter>X writeObjects(Iterable<ASObject> objects) {
    for (ASObject object : objects)
      writeObject(object);
    return (X)this;
  }
 
}
