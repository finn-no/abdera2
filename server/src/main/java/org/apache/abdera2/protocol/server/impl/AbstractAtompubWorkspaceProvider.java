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
import org.apache.abdera2.common.protocol.ResponseContext;
import org.apache.abdera2.common.protocol.TargetType;
import org.apache.abdera2.common.protocol.WorkspaceManager;
import org.apache.abdera2.protocol.server.AtompubProvider;
import org.apache.abdera2.protocol.server.processors.CategoriesRequestProcessor;
import org.apache.abdera2.protocol.server.processors.ServiceRequestProcessor;

public abstract class AbstractAtompubWorkspaceProvider 
  extends AbstractWorkspaceProvider
  implements AtompubProvider, 
             WorkspaceManager {

 protected Abdera abdera;
  
  protected AbstractAtompubWorkspaceProvider() {
    addRequestProcessor(
      TargetType.TYPE_SERVICE, 
      ServiceRequestProcessor.class,
      this);
    addRequestProcessor(
      TargetType.TYPE_CATEGORIES, 
      CategoriesRequestProcessor.class, 
      this);
    addRequestProcessor(
      TargetType.TYPE_COLLECTION,
      CollectionRequestProcessor.class,
      ProviderHelper.isAtom(),
      this);
    addRequestProcessor(
      TargetType.TYPE_ENTRY, 
      EntryRequestProcessor.class,
      this);
    addRequestProcessor(
      TargetType.TYPE_MEDIA, 
      MediaRequestProcessor.class,
      this);
  }
  
  public void init(Map<String, Object> properties) {
    this.abdera = 
      properties != null && 
      properties.containsKey("abdera") ?
        (Abdera)properties.get("abdera") :
        Abdera.getInstance();
    super.init(properties);
  }

  public Abdera getAbdera() {
    return abdera;
  }

  public ResponseContext createErrorResponse(int code, String message, Throwable t) {
    return AbstractAtompubProvider.createErrorResponse(abdera,code,message,t);
  }
  
}