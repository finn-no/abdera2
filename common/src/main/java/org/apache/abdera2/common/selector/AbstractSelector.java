package org.apache.abdera2.common.selector;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Constraint;

public abstract class AbstractSelector<X> 
  implements Selector<X> {

  public boolean apply(X item) {
    return select(item);
  }
  
  public X checkElement(X element) {
    if (apply(element))
      return element;
    throw new IllegalArgumentException();
  }

  public Function<X,Boolean> asFunction() {
    return Utils.asFunction(this);
  }
  
  public Predicate<X> asPredicate() {
    return this;
  }
  public Constraint<X> asConstraint() {
    return this;
  }
  public <Y>Selector<Y> compose(Function<Y,X> transform) {
    return Utils.compose(this, transform);
  }
  
}
