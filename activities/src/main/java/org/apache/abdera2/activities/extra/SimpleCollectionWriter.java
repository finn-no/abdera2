package org.apache.abdera2.activities.extra;

import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.AbstractCollectionWriter;
import org.apache.abdera2.activities.model.Collection;

/**
 * Simple implementation of the CollectionWriter interface that builds
 * an in-memory Collection using the CollectionWriter's streaming interface.
 */
@SuppressWarnings({"unchecked"})
public class SimpleCollectionWriter<T extends ASObject> 
  extends AbstractCollectionWriter {

  private final Collection.CollectionBuilder<T> builder = 
    Collection.makeCollection();
  
  @Override
  protected void write(String name, Object val) {
    builder.set(name, val);
  }

  protected void startItems() {}

  protected void writeItem(ASObject object) {
    builder.item((T)object);
  }

  public void complete() {}

  public Collection<T> getCollection() {
    return builder.get();
  }
}
