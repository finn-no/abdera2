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

import org.apache.abdera2.common.lang.Lang;
import org.apache.abdera2.common.lang.Range;
import org.apache.abdera2.common.selector.AbstractSelector;
import org.apache.abdera2.common.selector.Selector;
import org.apache.abdera2.model.Link;

/**
 * Selector implementation that selects Link elements specifying
 * a matching hreflang attribute.
 * @see org.apache.abdera2.common.selector.Selector
 */
public class LinkHrefLangSelector 
extends AbstractSelector<Link>
implements Selector<Link> {

  private static final long serialVersionUID = 7008363856043465676L;
  private final Set<Lang> langs = new HashSet<Lang>();
  private final Range range;
  
  public LinkHrefLangSelector(Range range) {
    this.range = range;
  }
  
  public LinkHrefLangSelector(String... langs) {
    this.range = null;
    for (String lang : langs)
      this.langs.add(new Lang(lang));
  }
  
  public LinkHrefLangSelector(Lang... langs) {
    this.range = null;
    for (Lang lang : langs)
      this.langs.add(lang);
  }

  public boolean select(Object item) {
    if (!(item instanceof Link)) return false;
    Link link = (Link)item;
    Lang lang = new Lang(link.getHrefLang());
    return range != null ? 
      range.matches(lang) : 
        langs.isEmpty() || 
        langs.contains(lang);
  }


}
