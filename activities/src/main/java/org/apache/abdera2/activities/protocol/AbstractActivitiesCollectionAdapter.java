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
import org.apache.abdera2.activities.model.objects.PersonObject;
import org.apache.abdera2.activities.model.objects.ServiceObject;
import org.apache.abdera2.common.misc.ExceptionHelper;
import org.apache.abdera2.common.protocol.AbstractCollectionAdapter;
import org.apache.abdera2.common.protocol.CollectionAdapter;
import org.apache.abdera2.common.protocol.CollectionInfo;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.ResponseContext;
import org.apache.abdera2.common.protocol.ResponseContextException;
import org.joda.time.DateTime;

import com.google.common.base.Predicate;

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
  
  protected ResponseContext buildCreateEntryResponse(String link, ASBase base) {
    return
      new ActivitiesResponseContext<ASBase>(base)
        .setLocation(link)
        .setContentLocation(link)
        .setEntityTag(AbstractActivitiesProvider.calculateEntityTag(base))
        .setStatus(201);
  }

  protected ResponseContext buildGetEntryResponse(RequestContext request, ASObject base)
      throws ResponseContextException {
      base.setSource(createSourceObject(request));
      return 
        new ActivitiesResponseContext<ASObject>(base)
         .setEntityTag(AbstractActivitiesProvider.calculateEntityTag(base));
  }

  protected ResponseContext buildGetFeedResponse(Collection<ASObject> collection) {
      return 
        new ActivitiesResponseContext<Collection<ASObject>>(collection)
          .setEntityTag(AbstractActivitiesProvider.calculateEntityTag(collection));
  }

  protected ServiceObject createSourceObject(RequestContext request) throws ResponseContextException {
    return 
      ServiceObject
        .makeService()
        .displayName(getTitle(request))
        .id(getId(request))
        .author(
          PersonObject
            .makePerson()
            .displayName(getAuthor(request))
            .get())
        .get();
  }
  
  /**
   * Create the base feed for the requested collection.
   */
  protected Collection<ASObject> createCollectionBase(RequestContext request) throws ResponseContextException {
    return 
      Collection
        .makeCollection()
        .id(getId(request))
        .set("title", getTitle(request))
        .set("updated", DateTime.now())
        .set("author", 
          PersonObject
            .makePerson()
            .displayName(getAuthor(request))
            .get()) 
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
    return AbstractActivitiesWorkspaceProvider.isJson();
  }
}
