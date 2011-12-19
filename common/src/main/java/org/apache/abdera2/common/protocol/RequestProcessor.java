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
import org.apache.abdera2.common.misc.MoreFunctions;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

/**
 * Request processors implement the actual business logic for handling requests to the Atompub server and producing the
 * related response.
 */
public abstract class RequestProcessor 
  implements Function<RequestContext,ResponseContext> {
 
  protected final WorkspaceManager workspaceManager;
  protected final CollectionAdapter adapter;
  protected final Predicate<RequestContext> predicate;
  
  protected RequestProcessor(
    WorkspaceManager workspaceManager, 
    CollectionAdapter adapter) {
    this.workspaceManager = workspaceManager;
    this.adapter = adapter;
    this.predicate = null;
  }
  
  protected RequestProcessor(
      WorkspaceManager workspaceManager, 
      CollectionAdapter adapter,
      Predicate<RequestContext> predicate) {
      this.workspaceManager = workspaceManager;
      this.adapter = adapter;
      this.predicate = predicate;
    }
  
  private boolean applies(RequestContext request, Predicate<RequestContext> predicate) {
    return predicate != null && predicate.apply(request);
  }
  
  protected ResponseContext actuallyApply(RequestContext request) {
    boolean adapter_ok = applies(request, adapter.acceptable());
    boolean pred_ok = applies(request, predicate);
    if (!adapter_ok && !pred_ok) // fail only if both are false
      return ProviderHelper.notallowed(request);
    Function<RequestContext,ResponseContext> handler = 
      adapter.handlerFor(
        request.getTarget(),
        request.getMethod());
    return handler != null ?
      handler.apply(request) :
      ProviderHelper.notfound(request);
  }
  
  public ResponseContext apply(RequestContext request) {
    return actuallyApply(request);
  }

  public static abstract class RequestProcessorSupplier<T extends RequestProcessor> 
    implements Function<CollectionAdapter,T> {
    protected final WorkspaceManager workspaceManager;
    protected RequestProcessorSupplier(
      WorkspaceManager workspaceManager) {
      this.workspaceManager = workspaceManager;
    }
  }
  
  public static <T extends RequestProcessor>Function<CollectionAdapter,T> forClass(
      final Class<T> _class, 
      final WorkspaceManager workspaceManager,
      final Predicate<RequestContext> predicate) {
      try {
        final Function<Object[],T> c =
          MoreFunctions.<T>createInstance(
            _class, 
            WorkspaceManager.class, 
            CollectionAdapter.class, 
            Predicate.class);
        return new RequestProcessorSupplier<T>(workspaceManager) {
          public T apply(CollectionAdapter adapter) {
            try {
              return c.apply(
                MoreFunctions.array(
                  workspaceManager,
                  adapter,
                  predicate));
            } catch (Throwable t) {
              throw ExceptionHelper.propogate(t);
            }
          }
        };
      } catch (Throwable t) {
        throw ExceptionHelper.propogate(t);
      }
    }
  
  public static <T extends RequestProcessor>Function<CollectionAdapter,T> forClass(
    final Class<T> _class, 
    final WorkspaceManager workspaceManager) {
    try {
      final Function<Object[],T> c =
        MoreFunctions.<T>createInstance(
          _class, 
          WorkspaceManager.class, 
          CollectionAdapter.class);
      return new RequestProcessorSupplier<T>(workspaceManager) {
        public T apply(CollectionAdapter adapter) {
          try {
            return c.apply(
              MoreFunctions.array(
                workspaceManager,
                adapter));
          } catch (Throwable t) {
            throw ExceptionHelper.propogate(t);
          }
        }
      };
    } catch (Throwable t) {
      throw ExceptionHelper.propogate(t);
    }
  }
}
