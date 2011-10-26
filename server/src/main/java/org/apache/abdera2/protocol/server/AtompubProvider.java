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
package org.apache.abdera2.protocol.server;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.common.protocol.Provider;
import org.apache.abdera2.common.protocol.TargetType;

/**
 * Providers are responsible for processing all requests to the Atompub server.<br>
 * Actual request processing is delegated to {@link AtompubRequestProcessor} implementations, depending on the request
 * {@link TargetType}.
 */
public interface AtompubProvider extends Provider {

    /**
     * Retrieve the Abdera instance associated with this provider
     */
    Abdera getAbdera();

}
