package org.apache.abdera2.common.selector;

import com.google.common.base.Function;


public class TransformSelector<X,Y>
  extends AbstractSelector<X> {

  private final Selector<Y> inner;
  private final Function<X,Y> function;
  
  public TransformSelector(
    Selector<Y> selector, 
    Function<X,Y> transform) {
      this.inner = selector;
      this.function = transform;
  }
  
  @SuppressWarnings("unchecked")
  public boolean select(Object item) {
    return inner.select((Y)function.apply((X)item));
  }

}
