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
package org.apache.abdera2.protocol.server.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.abdera2.common.misc.MoreFunctions;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.model.Categories;
import org.apache.abdera2.protocol.server.model.AtompubCategoriesInfo;
import org.apache.abdera2.protocol.server.model.AtompubCategoryInfo;

import com.google.common.base.Supplier;
import com.google.common.collect.Iterators;

public class SimpleCategoriesInfo implements AtompubCategoriesInfo, Serializable {

    public static Generator make() {
      return new Generator();
    }
  
    public static class Generator implements Supplier<AtompubCategoriesInfo> {
      private String href;
      private String scheme;
      private boolean fixed;
      private final List<AtompubCategoryInfo> list = 
        new ArrayList<AtompubCategoryInfo>();
      public Generator href(String href) {
        this.href = href;
        return this;
      }
      public Generator scheme(String scheme) {
        this.scheme = scheme;
        return this;
      }
      public Generator fixed() {
        this.fixed = true;
        return this;
      }
      public Generator category(AtompubCategoryInfo info) {
        this.list.add(info);
        return this;
      }
      public AtompubCategoriesInfo get() {
        return new SimpleCategoriesInfo(this);
      }
      
    }
  
    private static final long serialVersionUID = 8732335394387909260L;

    private final String href;
    private final String scheme;
    private final boolean fixed;
    private final List<AtompubCategoryInfo> list = new ArrayList<AtompubCategoryInfo>();

    protected SimpleCategoriesInfo(Generator gen) {
      this.href = gen.href;
      this.scheme = gen.scheme;
      this.fixed = gen.fixed;
      this.list.addAll(gen.list);
    }
    
    public String getHref(RequestContext request) {
        return href;
    }

    public String getScheme(RequestContext request) {
        return scheme;
    }

    public boolean isFixed(RequestContext request) {
        return fixed;
    }

    public Iterator<AtompubCategoryInfo> iterator() {
        return Iterators.<AtompubCategoryInfo>unmodifiableIterator(list.iterator());
    }

    public int hashCode() {
      return MoreFunctions.genHashCode(1, fixed, href, list, scheme);
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SimpleCategoriesInfo other = (SimpleCategoriesInfo)obj;
        if (fixed != other.fixed)
            return false;
        if (href == null) {
            if (other.href != null)
                return false;
        } else if (!href.equals(other.href))
            return false;
        if (list == null) {
            if (other.list != null)
                return false;
        } else if (!list.equals(other.list))
            return false;
        if (scheme == null) {
            if (other.scheme != null)
                return false;
        } else if (!scheme.equals(other.scheme))
            return false;
        return true;
    }

    public Categories asCategoriesElement(RequestContext request) {
        Categories cats = AbstractAtompubProvider.getAbdera(request).getFactory().newCategories();
        if (href != null)
            cats.setHref(href);
        else {
            cats.setFixed(fixed);
            cats.setScheme(scheme);
            for (AtompubCategoryInfo cat : this)
                cats.addCategory(cat.asCategoryElement(request));
        }
        return cats;
    }
}
