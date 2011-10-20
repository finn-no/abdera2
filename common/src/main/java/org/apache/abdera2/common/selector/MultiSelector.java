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

/**
 * Selector that is based on an internal array of Selectors that are
 * invoked in order. By default, the selector will accept the item unless one 
 * of the selectors rejects it. 
 */
public abstract class MultiSelector<X> 
  extends AbstractSelector<X>
  implements Selector<X> {

  private static final long serialVersionUID = 5257601171344714824L;
  protected final Selector<X>[] selectors;
  
  public MultiSelector(Selector<X>... selectors) {
    this.selectors = selectors;
  }

  public static <X>Selector<X> not(Selector<X>...selectors) {
    return Selector.Utils.negate(and(selectors));
  }

  public static <X>Selector<X> or(Selector<X>...selectors) {
    return new MultiSelector<X>(selectors) {
      public boolean select(Object item) {
        for (Selector<X> selector : selectors)
          if (selector.select(item))
            return true;
        return false;
      }
    };
  }
  
  public static <X>Selector<X> and(Selector<X>...selectors) {
    return new MultiSelector<X>(selectors) {
      public boolean select(Object item) {
        for (Selector<X> selector : selectors)
          if (!selector.select(item))
            return false;
        return true;
      }
    };
  }
}
