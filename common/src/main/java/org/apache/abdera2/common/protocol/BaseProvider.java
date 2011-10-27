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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;

import org.apache.abdera2.common.misc.Chain;
import org.apache.abdera2.common.misc.Task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.base.Function;

/**
 * Base Provider implementation that provides the core implementation details for all Providers. This class provides the
 * basic request routing logic.
 */
@SuppressWarnings({"unchecked","rawtypes"})
public abstract class BaseProvider
  implements Provider {

    private final static Log log = LogFactory.getLog(BaseProvider.class);
    protected Map<String, Object> properties;
    protected Set<Task<RequestContext,ResponseContext>> filters = 
      new LinkedHashSet<Task<RequestContext,ResponseContext>>();
    protected Map<TargetType, Function<CollectionAdapter,? extends RequestProcessor>> requestProcessors = 
      new HashMap<TargetType, Function<CollectionAdapter,? extends RequestProcessor>>();

    public void init(Map<String,Object> properties) {
      this.properties = properties != null ? properties : new HashMap<String,Object>();
    }

    public String getProperty(String name) {
        return (String)properties.get(name);
    }

    public Iterable<String> getPropertyNames() {
        return properties.keySet();
    }

    public Subject resolveSubject(RequestContext request) {
        Function<Request,Subject> subjectResolver = getSubjectResolver(request);
        return subjectResolver != null ? subjectResolver.apply(request) : null;
    }

    public Target resolveTarget(RequestContext request) {
        Function<RequestContext,Target> targetResolver = getTargetResolver(request);
        return targetResolver != null ? targetResolver.apply(request) : null;
    }

    public String urlFor(Request request, Object key, Object param) {
        TargetBuilder tm = getTargetBuilder(request);
        return tm != null ? tm.urlFor(request, key, param) : null;
    }

    protected Function<Request,Subject> getSubjectResolver(RequestContext request) {
        return new SimpleSubjectResolver();
    }

    protected abstract TargetBuilder getTargetBuilder(Request request);

    protected abstract Function<RequestContext,Target> getTargetResolver(RequestContext request);

    public ResponseContext apply(RequestContext request) {
      Target target = request.getTarget();
      if (Target.NOT_FOUND.apply(target))
          return ProviderHelper.notfound(request);
      TargetType type = target.getType();
      log.debug(String.format(
        "Processing [%s] request for Target [%s] of Type [%s]",
        request.getMethod(),
        target.getIdentity(),
        type.toString()));
      CollectionAdapter adapter = 
        getWorkspaceManager()
          .getCollectionAdapter(request);
      if (adapter == null && type != TargetType.TYPE_SERVICE)
        return ProviderHelper.servererror(request, null);
      RequestProcessor processor = 
        (RequestProcessor) this.requestProcessors
          .get(type)
          .apply(adapter);
      if (processor == null)
          return ProviderHelper.notfound(request);
      Chain<RequestContext,ResponseContext> chain = 
        Chain.<RequestContext,ResponseContext>make()
        .to(processor)
        .via(getFilters(request))
        .get();
      ResponseContext response = null;
      try {
        response = chain.apply(request);
      } catch (Throwable t) {
        response = createErrorResponse(request, t);
      }
      return response != null ? 
        response : 
        ProviderHelper.badrequest(request);
    }

    /**
     * Subclass to customize the kind of error response to return
     */
    protected ResponseContext createErrorResponse(RequestContext request, Throwable e) {
        return ProviderHelper.servererror(request, e);
    }

    protected abstract WorkspaceManager getWorkspaceManager();

    public void setFilters(Collection<Task<RequestContext,ResponseContext>> filters) {
        this.filters = new LinkedHashSet<Task<RequestContext,ResponseContext>>(filters);
    }

    public Iterable<Task<RequestContext,ResponseContext>> getFilters(RequestContext request) {
        return filters;
    }

    public void addFilter(Task<RequestContext,ResponseContext>... filters) {
        for (Task<RequestContext,ResponseContext> filter : filters) {
            this.filters.add(filter);
        }
    }

    public void setRequestProcessors(Map<TargetType, Function<CollectionAdapter,? extends RequestProcessor>> requestProcessors) {
        this.requestProcessors.clear();
        this.requestProcessors.putAll(requestProcessors);
    }

    public void addRequestProcessors(Map<TargetType, Function<CollectionAdapter,? extends RequestProcessor>> requestProcessors) {
        this.requestProcessors.putAll(requestProcessors);
    }

    public Map<TargetType, Function<CollectionAdapter,? extends RequestProcessor>> getRequestProcessors() {
        return Collections.unmodifiableMap(this.requestProcessors);
    }
    
}
