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
package org.apache.abdera2.common.protocol;

import java.io.Serializable;
import java.util.Set;

import org.apache.abdera2.common.misc.MoreFunctions;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;

public class BasicCollectionInfo 
  implements CollectionInfo, 
             Serializable {

    public static Generator make() {
      return new Generator();
    }
  
    public static class Generator implements Supplier<CollectionInfo> {
      String title;
      String href;
      final ImmutableSet.Builder<String> accepts = 
        ImmutableSet.builder();
      Generator title(String title) {
        this.title = title;
        return this;
      }
      public Generator href(String href) {
        this.href = href;
        return this;
      }
      public Generator accept(String type) {
        this.accepts.add(type);
        return this;
      }
      public CollectionInfo get() {
        return new BasicCollectionInfo(this);
      }
    }
  
    private static final long serialVersionUID = 8026455829158149510L;

    private final String title;
    private final String href;
    private final Set<String> accepts;

    protected BasicCollectionInfo(Generator gen) {
      this.title = gen.title;
      this.accepts = gen.accepts.build();
      this.href = gen.href;
    }

    public Iterable<String> getAccepts(RequestContext request) {
        return accepts;
    }

    public String getHref(RequestContext request) {
        return href;
    }

    public String getTitle(RequestContext request) {
        return title;
    }

    @Override
    public int hashCode() {
      return MoreFunctions.genHashCode(1, accepts,href,title);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      BasicCollectionInfo other = (BasicCollectionInfo) obj;
      if (accepts == null) {
        if (other.accepts != null)
          return false;
      } else if (!accepts.equals(other.accepts))
        return false;
      if (href == null) {
        if (other.href != null)
          return false;
      } else if (!href.equals(other.href))
        return false;
      if (title == null) {
        if (other.title != null)
          return false;
      } else if (!title.equals(other.title))
        return false;
      return true;
    }
}
