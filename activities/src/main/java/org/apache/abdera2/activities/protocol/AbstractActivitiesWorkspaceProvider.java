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

import javax.activation.MimeType;

import org.apache.abdera2.activities.model.TypeAdapter;
import org.apache.abdera2.common.mediatype.MimeTypeHelper;
import org.apache.abdera2.common.protocol.AbstractWorkspaceProvider;
import org.apache.abdera2.common.protocol.CollectionRequestProcessor;
import org.apache.abdera2.common.protocol.EntryRequestProcessor;
import org.apache.abdera2.common.protocol.Provider;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.ResponseContext;
import org.apache.abdera2.common.protocol.TargetType;
import org.apache.abdera2.common.protocol.WorkspaceManager;

import com.google.common.base.Predicate;

public abstract class AbstractActivitiesWorkspaceProvider 
  extends AbstractWorkspaceProvider
  implements Provider, 
             WorkspaceManager,
             ActivitiesProvider {
  
  protected final Set<TypeAdapter<?>> typeAdapters = 
    new HashSet<TypeAdapter<?>>();
  
  public static Predicate<RequestContext> isJson() {
    return new Predicate<RequestContext>() {
      public boolean apply(RequestContext input) {
        MimeType ct = input.getContentType();
        if (ct == null) return false;
        return MimeTypeHelper.isJson(ct.toString());
      }
    };
  }
  
  protected AbstractActivitiesWorkspaceProvider() {
    addRequestProcessor(
      TargetType.TYPE_COLLECTION, 
      CollectionRequestProcessor.class, 
      isJson(),
      this);
    addRequestProcessor(
      TargetType.TYPE_ENTRY,
      EntryRequestProcessor.class,
      this);
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
}