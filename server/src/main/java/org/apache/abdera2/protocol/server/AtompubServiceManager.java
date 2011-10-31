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

import java.util.Map;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.common.misc.MoreFunctions;
import org.apache.abdera2.common.protocol.AbstractServiceManager;
import org.apache.abdera2.common.protocol.Provider;
import org.apache.abdera2.protocol.server.impl.DefaultAtompubProvider;

/**
 * The ServiceManager is used by the AbderaServlet to bootstrap the server instance.
 */
public class AtompubServiceManager 
  extends AbstractServiceManager {

    private final Abdera abdera;

    public AtompubServiceManager() {
      this.abdera = Abdera.getInstance();
    }
    
    public AtompubServiceManager(Abdera abdera) {
      this.abdera = abdera;
    }

    @SuppressWarnings("unchecked")
    public <P extends Provider>P newProvider(
      Map<String, Object> properties) {
      properties.put("abdera",abdera);
      return (P)MoreFunctions
        .discoverInitializable(
          Provider.class,
          DefaultAtompubProvider.class).apply(properties);
    }    
}
