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

import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

import javax.activation.MimeType;

import org.apache.abdera2.activities.model.ASBase;
import org.apache.abdera2.activities.model.ASDocument;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.Activity;
import org.apache.abdera2.activities.model.Collection;
import org.apache.abdera2.activities.model.IO;
import org.apache.abdera2.activities.model.TypeAdapter;
import org.apache.abdera2.activities.model.objects.ErrorObject;
import org.apache.abdera2.common.date.DateTimes;
import org.apache.abdera2.common.http.EntityTag;
import org.apache.abdera2.common.misc.ExceptionHelper;
import org.apache.abdera2.common.protocol.AbstractProvider;
import org.apache.abdera2.common.protocol.CollectionRequestProcessor;
import org.apache.abdera2.common.protocol.EntryRequestProcessor;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.ResponseContext;
import org.apache.abdera2.common.protocol.TargetType;
import org.apache.abdera2.common.protocol.WorkspaceManager;
import org.apache.abdera2.common.protocol.RequestContext.Scope;
import org.joda.time.DateTime;

public abstract class AbstractActivitiesProvider 
  extends AbstractProvider
  implements ActivitiesProvider {

  protected Set<TypeAdapter<?>> typeAdapters = 
    new HashSet<TypeAdapter<?>>();
  protected final WorkspaceManager workspaceManager;
  
  protected AbstractActivitiesProvider(
    WorkspaceManager workspaceManager) {
    this.workspaceManager = workspaceManager;
    addRequestProcessor(
      TargetType.TYPE_COLLECTION,
      CollectionRequestProcessor.class,
      AbstractActivitiesWorkspaceProvider.isJson(),
      workspaceManager
      );
    addRequestProcessor(
      TargetType.TYPE_ENTRY,
      EntryRequestProcessor.class,
      workspaceManager);
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
            .displayName(message))
        .setStatus(code)
        .setStatusText(message);
  }

  public static EntityTag calculateEntityTag(ASBase base) {
    String id = null;
    String modified = null;
    if (base instanceof Activity) {
        Activity ac = (Activity)base;
        id = ac.getId();
        modified = DateTimes.format(
          ac.getUpdated() != null ? 
            ac.getUpdated() : 
            ac.getPublished());
    } else if (base instanceof Collection) {
        Collection<?> col = (Collection<?>)base;
        id = col.getProperty("id");
        if (id == null) id = java.util.UUID.randomUUID().toString();
        modified = col.getProperty("updated");
    } else if (base instanceof ASObject) {
        ASObject as = (ASObject)base;
        id = as.getId().toString();
        modified = DateTimes.format(
          as.getUpdated() != null ? 
            as.getUpdated() : 
            as.getPublished());
    }
    if (modified == null) modified = DateTimes.formatNow();
    return EntityTag.generate(id, modified);
  }
  
  public static String getEditUriFromEntry(ASObject object) {
    String editLink = object.getProperty("editLink");
    return editLink;
  }
  
  
  
  public static IO getIO(
    ActivitiesProvider provider, 
    TypeAdapter<?>... adapters) {
    Set<TypeAdapter<?>> as = 
      new HashSet<TypeAdapter<?>>(provider.getTypeAdapters());
    for (TypeAdapter<?> ta : adapters)
      as.add(ta);
    return IO.get(as.toArray(new TypeAdapter[as.size()]));
  }
  
  @SuppressWarnings("unchecked")
  public static <T extends ASBase>T getASBaseFromRequestContext(
    RequestContext context) 
      throws IOException {
    ASBase entity = context.getAttribute(Scope.REQUEST, ASBase.class.getName());
      try {
      if (entity == null) {
        Reader reader = context.getReader();
        IO io = getIO(context.<ActivitiesProvider>getProvider());
        if (reader != null)
          entity = io.read(reader);
        else // try input stream, but this should've worked
          entity = io.read(context.getInputStream(), "UTF-8");
      }
      } catch (Throwable t) {
        throw ExceptionHelper.propogate(t);
      }
    if (entity != null)
      context.setAttribute(ASBase.class.getName(), getDoc(entity,context));
    return (T)entity;
  }
  
  private static <T extends ASBase>ASDocument<T> getDoc(T base, RequestContext context) {
    ASDocument.Builder<T> builder = 
      ASDocument.make(base);
    String etag = context.getHeader("ETag");
    if (etag != null)
        builder.entityTag(etag);
    DateTime lm = context.getDateHeader("Last-Modified");
    if (lm != null)
        builder.lastModified(lm);
    MimeType mt = context.getContentType();
    if (mt != null)
        builder.contentType(mt.toString());
    String language = context.getContentLanguage();
    if (language != null)
        builder.language(language);
    String slug = context.getSlug();
    if (slug != null)
        builder.slug(slug);
    return builder.get();
  }
}
