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


/**
 * Filters are invoked by AbderaServlet immediately before passing the request off to the Provider for processing The
 * filters use a model generally identical to that of Servlet Filters, with each filter forwarding the request on to the
 * next filter in the chain.
 * 
 * @author jasnell
 */
public interface Filter {

    /**
     * Process the filter request. The filter must call chain.next(request) to pass the request on to the next filter or
     * the provider.
     */
  <S extends ResponseContext>S filter(RequestContext request, FilterChain chain);

}
