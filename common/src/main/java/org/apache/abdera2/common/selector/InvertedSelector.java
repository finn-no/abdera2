package org.apache.abdera2.common.selector;

/**
 * Selector that inverts the results of the wrapped selector
 */
public class InvertedSelector<X> 
  extends AbstractSelector<X>
  implements Selector<X> {

  private final Selector<X> selector;
  
  public InvertedSelector(Selector<X> selector) {
    this.selector = selector;
  }
  
  public boolean select(Object item) {
    return !selector.select(item);
  }

}
