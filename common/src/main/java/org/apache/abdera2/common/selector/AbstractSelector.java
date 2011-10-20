package org.apache.abdera2.common.selector;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Constraint;

@SuppressWarnings("unchecked")
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
  
  public Selector<X> limit(int limit) {
    return (Selector<X>)and(Utils.<X>counting(limit));
  }
  
  public <Y>Selector<Y> compose(Function<Y,X> transform) {
    return Utils.compose(this, transform);
  }
  
  public Selector<X> negate() {
    return Utils.negate(this);
  }
  
  public Selector<X> and(Selector<X> selector) {
    return MultiSelector.<X>and(this,selector);
  }
  public Selector<X> or(Selector<X> selector) {
    return MultiSelector.<X>or(this,selector);
  }
  public Selector<X> andNot(Selector<X> selector) {
    return MultiSelector.<X>and(this, selector.negate());
  }
  public Selector<X> orNot(Selector<X> selector) {
    return MultiSelector.<X>or(this, selector.negate());
  }
}
