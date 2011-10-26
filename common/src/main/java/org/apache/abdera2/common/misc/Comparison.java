package org.apache.abdera2.common.misc;

/**
 * A Comparison is similiar to Comparable in that it compares
 * the equivalence of two Objects based on some specific condition,
 * however, unlike Comparable which returns either a -1, 0 or 1 
 * for use when Sorting objects, Comparison returns a simple boolean
 * response similar to a Predicate.
 * 
 * A Comparison is similar to the Guava Libraries Equivalence but 
 * differs semantically.. the purpose of an Equivalence is to test
 * whether two items are semantically equivalent to one another, either
 * in terms of equality, identity, or identical meaning. A 
 * Comparison, on the other hand, checks only to see if the two objects
 * adhere to some arbitrary comparison logic regardless of whether the
 * two objects are semantically equivalent.
 */
public abstract class Comparison<R> {

  public abstract boolean apply(R r1, R r2);
  
  public Comparison<R> negate() {
    final Comparison<R> _this = this;
    return new Comparison<R>() {
      public boolean apply(R r1, R r2) {
        return !_this.apply(r1, r2);
      }
    };
  }
  
  public Comparison<R> and(final Comparison<R> other) {
    final Comparison<R> _this = this;
    return new Comparison<R>() {
      public boolean apply(R r1, R r2) {
        return _this.apply(r1, r2) && other.apply(r1, r2);
      }      
    };
  }
  
  public Comparison<R> or(final Comparison<R> other) {
    final Comparison<R> _this = this;
    return new Comparison<R>() {
      public boolean apply(R r1, R r2) {
        return _this.apply(r1, r2) || other.apply(r1, r2);
      }
    };
  }
  
  public Comparison<R> not(final Comparison<R> other) {
    final Comparison<R> _this = this;
    return new Comparison<R>() {
      public boolean apply(R r1, R r2) {
        return _this.apply(r1, r2) && !other.apply(r1, r2);
      }
    };
  } 

}
