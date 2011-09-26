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

import java.util.Map;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.common.protocol.AbstractWorkspaceProvider;
import org.apache.abdera2.common.protocol.CollectionRequestProcessor;
import org.apache.abdera2.common.protocol.EntryRequestProcessor;
import org.apache.abdera2.common.protocol.MediaRequestProcessor;
import org.apache.abdera2.common.protocol.ProviderHelper;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.ResponseContext;
import org.apache.abdera2.common.protocol.TargetType;
import org.apache.abdera2.protocol.server.AtompubProvider;
import org.apache.abdera2.protocol.server.context.AtompubRequestContext;
import org.apache.abdera2.protocol.server.model.AtompubWorkspaceManager;
import org.apache.abdera2.protocol.server.processors.CategoriesRequestProcessor;
import org.apache.abdera2.protocol.server.processors.ServiceRequestProcessor;

@SuppressWarnings("unchecked")
public abstract class AbstractAtompubWorkspaceProvider 
  extends AbstractWorkspaceProvider
  implements AtompubProvider, 
             AtompubWorkspaceManager {

 protected Abdera abdera;
  
  protected AbstractAtompubWorkspaceProvider() {
    this.requestProcessors.put(TargetType.TYPE_SERVICE, new ServiceRequestProcessor());
    this.requestProcessors.put(TargetType.TYPE_CATEGORIES, new CategoriesRequestProcessor());
    this.requestProcessors.put(TargetType.TYPE_COLLECTION, new CollectionRequestProcessor() {
      protected boolean isAcceptableItemType(RequestContext context) {
        return ProviderHelper.isAtom(context);
      }
    });
    this.requestProcessors.put(TargetType.TYPE_ENTRY, new EntryRequestProcessor());
    this.requestProcessors.put(TargetType.TYPE_MEDIA, new MediaRequestProcessor());
  }
  
  public void init(Abdera abdera, Map<String, String> properties) {
    super.init(properties);
    this.abdera = abdera;
  }

  public Abdera getAbdera() {
    return abdera;
  }

  public <S extends ResponseContext>S createErrorResponse(int code, String message, Throwable t) {
    return (S)AbstractAtompubProvider.createErrorResponse(abdera,code,message,t);
  }
  
  @Override
  public <S extends ResponseContext> S process(RequestContext request) {
    return (S)super.process(request instanceof AtompubRequestContext?request:new AtompubRequestContext(request));
  }
}