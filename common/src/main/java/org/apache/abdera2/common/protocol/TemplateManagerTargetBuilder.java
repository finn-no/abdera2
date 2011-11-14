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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import org.apache.abdera2.common.iri.IRI;
import org.apache.abdera2.common.templates.Context;
import org.apache.abdera2.common.templates.MapContext;
import org.apache.abdera2.common.templates.ObjectContext;
import org.apache.abdera2.common.templates.Template;
import org.apache.abdera2.common.templates.TemplateManager;

import com.google.common.collect.ImmutableMap;

public class TemplateManagerTargetBuilder<T> 
  extends TemplateManager<T>
  implements TargetBuilder<T> {

  public static <T>Builder<T> make() {
    return new Builder<T>();
  }
  
  public static <T>TemplateManagerTargetBuilder<T> fromMap(Map<T,Object> map) {
    checkNotNull(map);
    Builder<T> b = make();
    b.add(map);
    return (TemplateManagerTargetBuilder<T>) b.get();
  }
  
  public static class Builder<T> extends TemplateManager.Builder<T> {
    public TemplateManager<T> get() {
      return new TemplateManagerTargetBuilder<T>(
        templates.build(),isiri,base,defaultContexts.get());
    }
    public TemplateManagerTargetBuilder<T> getTargetBuilder() {
      return (TemplateManagerTargetBuilder<T>) get();
    }
  }
  
  TemplateManagerTargetBuilder(
      ImmutableMap<T,Template> templates, 
      boolean isiri, 
      IRI base, 
      Context contextDefaults) {
        super(templates,isiri,base,contextDefaults);
    }
  

  public String urlFor(Request request, T key, Object param) {
    RequestContext rc = (RequestContext) request;
    if (param == null) param = new MapContext(true);
    return expand(key,getContext(rc,param));
  }

  @SuppressWarnings("unchecked")
  public static Context getContext(RequestContext request, Object param) {
    Context context = null;
    if (param != null) {
        if (param instanceof Map) {
            context = new MapContext((Map<String, Object>)param, true);
        } else if (param instanceof Context) {
            context = (Context)param;
        } else {
            context = new ObjectContext(param, true);
        }
    }
    return new RequestTemplateContext(request, context);
  }
}
