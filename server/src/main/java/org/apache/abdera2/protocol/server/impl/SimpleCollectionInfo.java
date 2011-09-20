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
import java.util.List;

import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.BasicCollectionInfo;
import org.apache.abdera2.model.Collection;
import org.apache.abdera2.protocol.server.model.AtompubCategoriesInfo;
import org.apache.abdera2.protocol.server.model.AtompubCollectionInfo;

public class SimpleCollectionInfo 
  extends BasicCollectionInfo
  implements AtompubCollectionInfo, 
             Serializable {

    private static final long serialVersionUID = 8026455829158149510L;

    private final List<AtompubCategoriesInfo> catinfos = 
      new ArrayList<AtompubCategoriesInfo>();

    public SimpleCollectionInfo(String title, String href, String... accepts) {
        super(title,href,accepts);
    }

    public AtompubCategoriesInfo[] getCategoriesInfo(RequestContext request) {
        return catinfos.toArray(new AtompubCategoriesInfo[catinfos.size()]);
    }

    public void addCategoriesInfo(AtompubCategoriesInfo... catinfos) {
        for (AtompubCategoriesInfo catinfo : catinfos)
            this.catinfos.add(catinfo);
    }

    public void setCategoriesInfo(AtompubCategoriesInfo... catinfos) {
        this.catinfos.clear();
        addCategoriesInfo(catinfos);
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((catinfos == null) ? 0 : catinfos.hashCode());
      return result;
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
        Collection collection = AbstractAtompubProvider.getAbdera(request).getFactory().newCollection();
        collection.setHref(getHref(request));
        collection.setTitle(getTitle(request));
        collection.setAccept(getAccepts(request));
        for (AtompubCategoriesInfo catsinfo : this.catinfos) {
            collection.addCategories(catsinfo.asCategoriesElement(request));
        }
        return collection;
    }

}
