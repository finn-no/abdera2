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
package org.apache.abdera2.activities.protocol;

import java.util.HashSet;
import java.util.Set;

import org.apache.abdera2.activities.model.ASBase;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.Activity;
import org.apache.abdera2.activities.model.Collection;
import org.apache.abdera2.activities.model.TypeAdapter;
import org.apache.abdera2.common.date.DateTimes;
import org.apache.abdera2.common.http.EntityTag;
import org.apache.abdera2.common.protocol.BaseProvider;
import org.apache.abdera2.common.protocol.CollectionRequestProcessor;
import org.apache.abdera2.common.protocol.EntryRequestProcessor;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.RequestProcessor;
import org.apache.abdera2.common.protocol.ResponseContext;
import org.apache.abdera2.common.protocol.TargetType;
import org.apache.abdera2.common.protocol.WorkspaceManager;

public abstract class AbstractActivitiesProvider 
  extends BaseProvider
  implements ActivitiesProvider {

  protected Set<TypeAdapter<?>> typeAdapters = new HashSet<TypeAdapter<?>>();
  protected final WorkspaceManager workspaceManager;
  
  protected AbstractActivitiesProvider(
    WorkspaceManager workspaceManager) {
    this.workspaceManager = workspaceManager;
    this.requestProcessors.put(
      TargetType.TYPE_COLLECTION, 
      RequestProcessor.forClass(
        CollectionRequestProcessor.class,
        workspaceManager,
        AbstractActivitiesWorkspaceProvider.isJson()));
    this.requestProcessors.put(
      TargetType.TYPE_ENTRY, 
      RequestProcessor.forClass(
        EntryRequestProcessor.class,
        workspaceManager));
  }
  
  public void addTypeAdapter(TypeAdapter<?> typeAdapter) {
    typeAdapters.add(typeAdapter);
  }
  
  public void removeTypeAdapter(TypeAdapter<?> typeAdapter) {
    typeAdapters.remove(typeAdapter);
  }
  
  public Set<TypeAdapter<?>> getTypeAdapters() {
    return typeAdapters;
  }
  
  public ResponseContext createErrorResponse(
    int code,
    String message, 
    Throwable t) {
      return
        new ActivitiesResponseContext<ErrorObject>(
            ErrorObject
            .makeError()
            .code(code)
            .displayName(message)
            .get())
        .setStatus(code)
        .setStatusText(message);
  }

  @Override
  public ResponseContext apply(RequestContext request) {
    return super.apply(
      request instanceof ActivitiesRequestContext?
        request:
        new ActivitiesRequestContext(request));
  }

  public static EntityTag calculateEntityTag(ASBase base) {
    String id = null;
    String modified = null;
    if (base instanceof Activity) {
        Activity ac = (Activity)base;
        id = ac.getId();
        modified = DateTimes.format(ac.getUpdated() != null ? ac.getUpdated() : ac.getPublished());
    } else if (base instanceof Collection) {
        Collection<?> col = (Collection<?>)base;
        id = col.getProperty("id");
        if (id == null) id = java.util.UUID.randomUUID().toString();
        modified = col.getProperty("updated");
    } else if (base instanceof ASObject) {
        ASObject as = (ASObject)base;
        id = as.getId().toString();
        modified = DateTimes.format(as.getUpdated() != null ? as.getUpdated() : as.getPublished());
    }
    if (modified == null) modified = DateTimes.formatNow();
    return EntityTag.generate(id, modified);
  }
  
  public static String getEditUriFromEntry(ASObject object) {
    String editLink = object.getProperty("editLink");
    return editLink;
  }
}
