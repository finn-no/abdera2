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
import java.util.Set;

import org.apache.abdera2.common.misc.MoreFunctions;
import org.apache.abdera2.common.protocol.CollectionInfo;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.BasicCollectionInfo;
import org.apache.abdera2.model.Collection;
import org.apache.abdera2.protocol.server.model.AtompubCategoriesInfo;
import org.apache.abdera2.protocol.server.model.AtompubCollectionInfo;

import com.google.common.collect.ImmutableSet;

public class SimpleCollectionInfo 
  extends BasicCollectionInfo
  implements AtompubCollectionInfo, 
             Serializable {

    private static final long serialVersionUID = 8026455829158149510L;

    public static Generator make() {
      return new Generator();
    }
    
    public static class Generator extends BasicCollectionInfo.Generator {
      final ImmutableSet.Builder<AtompubCategoriesInfo> catinfos =
        ImmutableSet.builder();
      public Generator category(AtompubCategoriesInfo info) {
        this.catinfos.add(info);
        return this;
      }
      public CollectionInfo get() {
        return new SimpleCollectionInfo(this);
      }
    }
    
    private final Set<AtompubCategoriesInfo> catinfos;

    protected SimpleCollectionInfo(Generator generator) {
      super(generator);
      this.catinfos = generator.catinfos.build();
    }

    public Iterable<AtompubCategoriesInfo> getCategoriesInfo(RequestContext request) {
        return catinfos;
    }

    @Override
    public int hashCode() {
      return MoreFunctions.genHashCode(super.hashCode(), catinfos);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (!super.equals(obj))
        return false;
      if (getClass() != obj.getClass())
        return false;
      SimpleCollectionInfo other = (SimpleCollectionInfo) obj;
      if (catinfos == null) {
        if (other.catinfos != null)
          return false;
      } else if (!catinfos.equals(other.catinfos))
        return false;
      return true;
    }

    public Collection asCollectionElement(RequestContext request) {
      Collection collection = 
        AbstractAtompubProvider
          .getAbdera(request)
          .getFactory()
          .newCollection();
      collection.setHref(getHref(request));
      collection.setTitle(getTitle(request));
      collection.setAccept(getAccepts(request));
      for (AtompubCategoriesInfo catsinfo : this.catinfos)
        collection.addCategories(catsinfo.asCategoriesElement(request));
      return collection;
    }

}
