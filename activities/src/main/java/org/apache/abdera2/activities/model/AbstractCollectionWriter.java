package org.apache.abdera2.activities.model;

/**
 * Base implementation of the CollectionWriter interface.. handles
 * basic flow and state management
 */
public abstract class AbstractCollectionWriter 
  implements CollectionWriter {

  protected boolean _items = false;
  protected boolean _header = false;
  
  protected void flush() {}
  
  protected abstract void write(String name, Object val);
  
  protected abstract void startItems();
  
  protected abstract void writeItem(ASObject object);
  
  public abstract void complete();
  
  public void writeHeader(ASBase base) {
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
  }
  
  public void writeObject(ASObject object) {
    if (!_items) {
      startItems();
      _items = true;
    }
    writeItem(object);
    flush();
  }
  
  public void writeObjects(ASObject... objects) {
    for (ASObject object : objects)
      writeObject(object);
  }
  
  public void writeObjects(Iterable<ASObject> objects) {
    for (ASObject object : objects)
      writeObject(object);
  }
 
}
