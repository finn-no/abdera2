package org.apache.abdera2.common.pusher;

public abstract class SimpleListener<T> 
  implements Listener<T> {

  public void beforeItems() {}

  public void afterItems() {}

}
