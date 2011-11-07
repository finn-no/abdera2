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
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.abdera2.common.misc.MoreFunctions;

import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;


public class BasicWorkspaceInfo
  implements WorkspaceInfo, 
             Serializable {

    private static final long serialVersionUID = -8459688584319762878L;
    
    public static class Generator implements Supplier<WorkspaceInfo> {

      private String title;
      private final Set<CollectionInfo> collections = 
        new LinkedHashSet<CollectionInfo>();
      
      public Generator title(String title) {
        this.title = title;
        return this;
      }
      
      public Generator collection(CollectionInfo info) {
        this.collections.add(info);
        return this;
      }
      
      public WorkspaceInfo get() {
        return new BasicWorkspaceInfo(this);
      }
      
    }
    
    protected final String title;
    protected final Set<CollectionInfo> collections =
      new LinkedHashSet<CollectionInfo>();

    protected BasicWorkspaceInfo(Generator generator) {
      this.title = generator.title;
      this.collections.addAll(generator.collections);
    }

    public BasicWorkspaceInfo(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getTitle(RequestContext request) {
        return title;
    }

    public Iterable<CollectionInfo> getCollections(RequestContext request) {
        return Iterables.unmodifiableIterable(collections);
    }

    public int hashCode() {
      return MoreFunctions.genHashCode(1, collections,title);
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final BasicWorkspaceInfo other = (BasicWorkspaceInfo)obj;
        if (collections == null) {
            if (other.collections != null)
                return false;
        } else if (!collections.equals(other.collections))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }


}
