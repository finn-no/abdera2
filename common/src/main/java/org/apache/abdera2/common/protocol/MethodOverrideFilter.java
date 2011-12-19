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

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.abdera2.common.misc.Chain;
import org.apache.abdera2.common.misc.MoreFunctions;
import org.apache.abdera2.common.misc.Task;

import com.google.common.collect.ImmutableSet;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Abdera Filter implementation that supports the use of the X-HTTP-Method-Override header used by GData.
 */
public class MethodOverrideFilter 
  implements Task<RequestContext,ResponseContext> {

    private Set<String> methods = new HashSet<String>();

    public MethodOverrideFilter() {
        this("DELETE", "PUT", "PATCH");
    }

    public MethodOverrideFilter(String... methods) {
      for (int n = 0; n < checkNotNull(methods).length; n++)
        methods[n] = methods[n].toUpperCase(Locale.US);
      this.methods = ImmutableSet.copyOf(methods);
    }

    public Iterable<String> getMethods() {
      return methods;
    }
    
    public ResponseContext apply(
      RequestContext request, 
      Chain<RequestContext,ResponseContext> chain) {
        return chain.next(
          new MethodOverrideRequestContext(
            request,methods));
    }

    private static class MethodOverrideRequestContext 
      extends BaseRequestContextWrapper {
        private final String method;
        public MethodOverrideRequestContext(
          RequestContext request, 
          Set<String> methods) {
            super(request);
            String method = super.getMethod();
            String xheader = 
              MoreFunctions
                .<String>firstNonNull(
                  getHeader("X-HTTP-Method-Override"),
                  getHeader("X-Method-Override"));
            if (xheader != null)
                xheader = xheader.toUpperCase().trim();
            if (method.equals("POST") && 
                xheader != null && 
                (methods.contains(method) || 
                 methods.contains("*")))
                  method = xheader;
            this.method = method;
        }
        public String getMethod() {
            return method;
        }
    }
}
