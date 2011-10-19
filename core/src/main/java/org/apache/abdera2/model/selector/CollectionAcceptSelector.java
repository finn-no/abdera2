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

import javax.activation.MimeType;

import org.apache.abdera2.common.selector.AbstractSelector;
import org.apache.abdera2.common.selector.Selector;
import org.apache.abdera2.model.Collection;

/**
 * Selector implementation that selects Collections elements that contain
 * specific mimetypes, or compatible equivalent mimetimes within child
 * accept elements
 */
public class CollectionAcceptSelector 
extends AbstractSelector<Collection>
  implements Selector<Collection> {

  private static final long serialVersionUID = 1821941024155067263L;
  private final Set<MimeType> types = 
    new HashSet<MimeType>();
  
  public CollectionAcceptSelector(String... types) {
    try {
      for (String type:types)
        this.types.add(new MimeType(type));
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }
  
  public CollectionAcceptSelector(MimeType... types) {
    for (MimeType type : types)
      this.types.add(type);
  }
  
  public boolean select(Object item) {
    if (!(item instanceof Collection)) return false;
    Collection citem = (Collection) item;
    for (MimeType type : types) 
      if (!citem.accepts(type))
        return false;
    return true;
  }

}
