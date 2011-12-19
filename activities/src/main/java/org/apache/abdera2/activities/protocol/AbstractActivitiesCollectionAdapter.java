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
import java.util.Arrays;

import org.apache.abdera2.activities.model.ASBase;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.Collection;
import org.apache.abdera2.activities.model.Collection.CollectionBuilder;
import org.apache.abdera2.activities.model.objects.ServiceObject;
import org.apache.abdera2.common.misc.ExceptionHelper;
import org.apache.abdera2.common.protocol.AbstractCollectionAdapter;
import org.apache.abdera2.common.protocol.CollectionAdapter;
import org.apache.abdera2.common.protocol.CollectionInfo;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.ResponseContext;
import org.apache.abdera2.common.protocol.ResponseContextException;

import static org.apache.abdera2.activities.protocol.AbstractActivitiesProvider.*;
import static org.apache.abdera2.activities.model.objects.PersonObject.makePerson;
import static org.apache.abdera2.activities.model.objects.ServiceObject.makeService;
import static org.apache.abdera2.activities.model.Collection.makeCollection;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public abstract class AbstractActivitiesCollectionAdapter
  extends AbstractCollectionAdapter
  implements CollectionAdapter, 
             CollectionInfo {

  public AbstractActivitiesCollectionAdapter(String href) {
    super(href);
  }

  public Iterable<String> getAccepts(RequestContext request) {
    return Arrays.asList("application/json");
  }
  
  protected <T extends ASBase>ResponseContext buildCreateEntryResponse(
    String link, 
    ASBase.Builder<T,?> builder) {
    return
      new ActivitiesResponseContext<T>(builder)
        .setLocation(link)
        .setContentLocation(link)
        .setEntityTag(calculateEntityTag(builder.get()))
        .setStatus(201);
  }

  protected <T extends ASObject>ResponseContext buildGetEntryResponse(
    RequestContext request, 
    ASObject.Builder<T,?> builder)
      throws ResponseContextException {
      builder.source(createSourceObject(request));
      return 
        new ActivitiesResponseContext<T>(builder)
         .setEntityTag(calculateEntityTag(builder.get()));
  }

  protected <T extends ASObject>ResponseContext buildGetFeedResponse(
    CollectionBuilder<T> builder) {
      return 
        new ActivitiesResponseContext<Collection<T>>(builder)
          .setEntityTag(calculateEntityTag(builder.get()));
  }

  protected ServiceObject createSourceObject(
    RequestContext request) 
      throws ResponseContextException {
    return 
      makeService()
        .displayName(getTitle(request))
        .id(getId(request))
        .author(
          makePerson()
            .displayName(getAuthor(request)))
        .get();
  }
  
  /**
   * Create the base feed for the requested collection.
   */
  protected Collection<ASObject> createCollectionBase(
    RequestContext request) 
      throws ResponseContextException {
    return 
      makeCollection()
        .id(getId(request))
        .updatedNow()
        .author( 
          makePerson()
            .displayName(getAuthor(request)))
        .displayName(getTitle(request))
        .get();
  }

  protected ASObject getEntryFromRequest(
    RequestContext request) 
      throws ResponseContextException {
      ASObject object;
      try {
        object = 
          AbstractActivitiesProvider
            .getASBaseFromRequestContext(request);
      } catch (IOException e) {
       throw ExceptionHelper.propogate(e);
      }
      return object;
  }

  public Predicate<RequestContext> acceptable() {
    return Predicates.or(
      super.acceptable(),
      AbstractActivitiesWorkspaceProvider.isJson());
  }
}
