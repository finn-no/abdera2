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
package org.apache.abdera2.activities.client;

import org.apache.abdera2.activities.model.ASDocument;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.Activity;
import org.apache.abdera2.activities.model.Collection;
import org.apache.abdera2.protocol.client.BasicClient;
import org.apache.abdera2.protocol.client.Client;
import org.apache.abdera2.protocol.client.ClientWrapper;
import org.apache.abdera2.protocol.client.RequestOptions;
import org.apache.abdera2.protocol.client.Session;

/**
 * Extension of the base Abdera Client that provides utility methods
 * for working with Activity Stream objects. The ActivityClient acts 
 * as a decorator for the base Abdera Client. 
 */
@SuppressWarnings("unchecked")
public class ActivitiesClient 
  extends ClientWrapper {

  public ActivitiesClient() {
    super(new BasicClient());
  }
  
  public ActivitiesClient(Client client) {
    super(client);
  }

  @Override
  public <T extends Session> T newSession() {
    return (T)new ActivitiesSession(this);
  }

  public <M extends ASObject,T extends Collection<M>>ASDocument<T> getCollection(String uri) {
    ActivitiesSession session = newSession();
    return session.<M,T>getCollection(uri);
  }
  
  public <M extends ASObject,T extends Collection<M>>ASDocument<T> getCollection(String uri, RequestOptions options) {
    ActivitiesSession session = newSession();
    return session.<M,T>getCollection(uri,options);
  }
  
  public <T extends Activity>ASDocument<T> getActivity(String uri) {
    ActivitiesSession session = newSession();
    return session.<T>getActivity(uri);
  }
  
  public <T extends Activity>ASDocument<T> getActivity(String uri, RequestOptions options) {
    ActivitiesSession session = newSession();
    return session.<T>getActivity(uri,options);
  }
  
  public <T extends ASObject>ASDocument<T> getObject(String uri) {
    ActivitiesSession session = newSession();
    return session.<T>getObject(uri);
  }
  
  public <T extends ASObject>ASDocument<T> getObject(String uri, RequestOptions options) {
    ActivitiesSession session = newSession();
    return session.<T>getObject(uri,options);
  }
}
