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

import org.apache.abdera2.common.misc.ExceptionHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.base.Predicate;

public abstract class TransactionalRequestProcessor 
  extends RequestProcessor {

  private final static Log log = 
    LogFactory.getLog(
      TransactionalRequestProcessor.class);
  
  protected TransactionalRequestProcessor(
    WorkspaceManager workspaceManager,
    CollectionAdapter adapter) {
      super(workspaceManager, adapter);
  }
  
  protected TransactionalRequestProcessor(
    WorkspaceManager workspaceManager,
    CollectionAdapter adapter,
    Predicate<RequestContext> predicate) {
      super(workspaceManager,adapter,predicate);
  }

  public void start(RequestContext request) {
    // the default is to do nothing here
  }

  public void end(RequestContext request, ResponseContext response) {
    // the default is to do nothing here
  }

  public void compensate(RequestContext request, Throwable t) {
    // the default is to do nothing here
  }

  public ResponseContext apply(RequestContext input) {
    ResponseContext response = null;
    try {
      start(input);
      response = actuallyApply(input);
      return response;
    } catch (Throwable e) {
      ExceptionHelper.log(log,e);
      compensate(input,e);
      throw ExceptionHelper.propogate(e);
    } finally {
      end(input, response);
    }
  }

}
