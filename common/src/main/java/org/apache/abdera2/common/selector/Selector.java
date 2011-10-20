/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  For additional information regarding
 * copyright in this work, please see the NOTICE file in the top level
 * directory of this distribution.
 */
package org.apache.abdera2.common.selector;

import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Constraint;

/**
 * Utility interface that is used as a Filter in several places 
 * throughout the Abdera API.
 */
public interface Selector<X>
  extends Predicate<X>, Constraint<X> {

    /** Returns true the item is to be selected **/
    boolean select(Object item);

    public Function<X,Boolean> asFunction();
    public Predicate<X> asPredicate();
    public Constraint<X> asConstraint();
    
    /**
     * Returns a Selector that matches this selector up to a specific number of times
     */
    public Selector<X> limit(int limit);
    
    /**
     * Returns a Selector<Y> A using this Selector B and Funtion<Y,X> C such that
     * X = C(Y),
     * B(X),
     * A(Y) = B(C(Y))
     */
    public <Y>Selector<Y> compose(Function<Y,X> transform);
    /**
     * Returns a Selector that selects the opposite of this selector
     */
    public Selector<X> negate();
    /**
     * Returns a Selector that is the union of this and the specified selector
     */
    public Selector<X> and(Selector<X> selector);
    /**
     * Returns a Selector that matches either this or the specified selector
     */
    public Selector<X> or(Selector<X> selector);
    /**
     * Returns a Selector that matches this, but not the specified selector
     */
    public Selector<X> andNot(Selector<X> selector);
    /**
     * Returns a Selector that matches this or the inverse of the specified selector
     */
    public Selector<X> orNot(Selector<X> selector);
    
    public static class Utils {
      
      /**
       * Creates a selector that will select at most the given number 
       * of items. Once the threshold has been met, the selector will
       * return false;
       */
      public static <X>Selector<X> counting(int limit) {
        return new CountingSelector<X>(limit);
      }
      
      private static class CountingSelector<X> 
        extends AbstractSelector<X> {
          private final AtomicInteger counter = new AtomicInteger();
          private final int limit;
          private boolean done = false;
          public CountingSelector(int limit) {
            this.limit = limit;
          }
          public boolean select(Object object) {
            if (done) return false;
            if (counter.incrementAndGet() <= limit)
              return true;
            else done = true;
            return false;
          }
      }
      
      /**
       * Creates a new Selector A using Selector B and Function C such that
       * X = C(Y),
       * B(X),
       * A(Y) = B(C(Y)). 
       * That is, for instance, supposing we have a 
       * Selector<Long> B and Function<String,Long> C, this creates a 
       * Selector<String> that will first pass the input string to the 
       * Function, which returns a Long, and in turn passes that to selector
       * B.
       */
      public static <X,Y>Selector<Y> compose(Selector<X> b, Function<Y,X> c) {
        return new TransformSelector<Y,X>(b,c);
      }
      
      /**
       * Returns the Selector<X> as a Function<X,Boolean>
       */
      public static <X>Function<X,Boolean> asFunction(final Selector<X> selector) {
        return new Function<X,Boolean>() {
          public Boolean apply(X input) {
            return selector.apply(input);
          }
        };
      }
      
      /**
       * Returns a Selector<X> that selects the inverse of the provided
       * Selector<X>
       */
      public static <X>Selector<X> negate(Selector<X> selector) {
        return new InvertedSelector<X>(selector);
      }
      
      /**
       * Returns a Selector<X> that wraps the specified Predicate<X>
       */
      public static <X>Selector<X> forPredicate(
        Predicate<X> predicate) {
          return new PredicateSelector<X>(predicate);
      }
      
      /**
       * Returns a Selector<X> that wraps a Constraint<X>
       */
      public static <X>Selector<X> forConstraint(
        Constraint<X> constraint) {
          return new ConstraintSelector<X>(constraint);
      }
      
    }
    
    public static class ConstraintSelector<X>
      extends AbstractSelector<X> {
      private final Constraint<X> internal;
      ConstraintSelector(Constraint<X> internal) {
        this.internal = internal;
      }
      @SuppressWarnings("unchecked")
      public boolean select(Object item) {
        return internal.checkElement((X)item) == item;
      }
      public X checkElement(X element) {
        return internal.checkElement(element);
      }
    }
    
    public static class PredicateSelector<X> 
      extends AbstractSelector<X> {
      private final Predicate<X> internal;
      PredicateSelector(Predicate<X> internal) {
        this.internal = internal;
      }
      @SuppressWarnings("unchecked")
      public boolean select(Object item) {
        return this.internal.apply((X)item);
      }
    }
}
