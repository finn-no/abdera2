package org.apache.abdera2.common.misc;

public abstract class AbstractItemManager<T, R> 
  implements ItemManager<T, R> {

  public T apply(R input) {
    return get(input);
  }

}
