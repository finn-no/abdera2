package org.apache.abdera2.activities.extra;

import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.AbstractCollectionWriter;
import org.apache.abdera2.activities.model.Collection;

/**
 * Simple implementation of the CollectionWriter interface that builds
 * an in-memory Collection using the CollectionWriter's streaming interface.
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class SimpleCollectionWriter 
  extends AbstractCollectionWriter {

  private final Collection collection = new Collection();
  
  @Override
  protected void write(String name, Object val) {
    collection.setProperty(name, val);
  }

  @Override
  protected void startItems() {
    collection.getItems(true);
  }

  @Override
  protected void writeItem(ASObject object) {
    collection.addItem(object);
  }

  @Override
  public void complete() {}

  public <T extends ASObject>Collection<T> getCollection() {
    return (Collection<T>) collection;
  }
}
