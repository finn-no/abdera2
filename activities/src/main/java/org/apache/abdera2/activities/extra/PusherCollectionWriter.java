package org.apache.abdera2.activities.extra;

import org.apache.abdera2.activities.model.ASBase;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.CollectionWriter;
import org.apache.abdera2.common.pusher.Pusher;

/**
 * Simple CollectionWriter implemention that wraps a Pusher object. 
 * Calls to writeObject/writeObjects are sent through to the pusher.push 
 * and pusher.pushAll methods. The writeHeader and complete methods are 
 * ignored.
 */
@SuppressWarnings("unchecked")
public class PusherCollectionWriter 
  implements CollectionWriter {

  private final Pusher<ASObject> pusher;
  
  public PusherCollectionWriter(Pusher<ASObject> pusher) {
    this.pusher = pusher;
  }

  /**
   * Ignored in this implementation
   */
  public <X extends CollectionWriter>X writeHeader(ASBase base) {
    // We ignore this in the pusher...
    return (X)this;
  }

  public <X extends CollectionWriter>X writeObject(ASObject object) {
    pusher.push(object);
    return (X)this;
  }

  public <X extends CollectionWriter>X writeObjects(ASObject... objects) {
    for (ASObject object : objects)
      pusher.push(object);
    return (X)this;
  }
  
  public <X extends CollectionWriter>X writeObjects(Iterable<ASObject> objects) {
    pusher.pushAll(objects);
    return (X)this;
  }

  /**
   * Ignored by this implementation
   */
  public void complete() {
    // ignored
  }
  
}
