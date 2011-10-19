package org.apache.abdera2.common.misc;

public abstract class AbstractResolver<T,R> 
  implements Resolver<T, R> {

  public T apply(R input) {
    return resolve(input);
  }
}
