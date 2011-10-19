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
    public <Y>Selector<Y> compose(Function<Y,X> transform);
    
    public static class Utils {
      
      public static <X,Y>Selector<Y> compose(Selector<X> selector, Function<Y,X> transform) {
        TransformSelector<Y,X> tsel = 
          new TransformSelector<Y,X>(selector,transform);
        return tsel;
      }
      
      public static <X>Function<X,Boolean> asFunction(final Selector<X> selector) {
        return new Function<X,Boolean>() {
          public Boolean apply(X input) {
            return selector.apply(input);
          }
        };
      }
      
      public static <X>Selector<X> negate(Selector<X> selector) {
        return new InvertedSelector<X>(selector);
      }
      
      public static <X>Selector<X> forPredicate(
        Predicate<X> predicate) {
          return new PredicateSelector<X>(predicate);
      }
      
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
