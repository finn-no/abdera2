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
package org.apache.abdera2.model.selector;

import java.util.HashSet;
import java.util.Set;

import org.apache.abdera2.common.iri.IRI;
import org.apache.abdera2.common.selector.AbstractSelector;
import org.apache.abdera2.common.selector.Selector;
import org.apache.abdera2.model.Category;

/**
 * Selector implementation that selects Category elements using the 
 * specified scheme(s).
 * @see org.apache.abdera2.common.selector.Selector
 */
public class CategorySchemeSelector 
  extends AbstractSelector<Category>
  implements Selector<Category> {

  private static final long serialVersionUID = 7008363856043465676L;
  private final Set<IRI> schemes = new HashSet<IRI>();
  
  public CategorySchemeSelector(String... schemes) {
    for (String scheme : schemes)
      this.schemes.add(new IRI(scheme));
  }
  
  public CategorySchemeSelector(IRI... schemes) {
    for (IRI scheme : schemes) 
      this.schemes.add(scheme);
  }
  
  public boolean select(Object item) {
    if (!(item instanceof Category)) return false;
    return schemes.isEmpty() || schemes.contains(((Category)item).getScheme());
  }

}
