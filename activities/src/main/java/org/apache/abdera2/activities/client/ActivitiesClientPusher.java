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

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.common.pusher.Pusher;
import org.apache.abdera2.protocol.client.ClientResponse;
import org.apache.abdera2.protocol.client.RequestOptions;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility that wraps an ActivitiesClient and uses the pusher 
 * interface to asynchronously send those off to somewhere else
 * using POST requests. 
 */
public class ActivitiesClientPusher<T extends ASObject> 
  implements Pusher<T> {
  private final static Log log = LogFactory.getLog(ActivitiesClientPusher.class);
  
  protected final ActivitiesSession session;
  protected final String iri;
  protected final Executor exec;
  protected final RequestOptions options;

  public ActivitiesClientPusher(String iri) {
    this(iri, initSession());
  }
  
  public ActivitiesClientPusher(String iri, RequestOptions options) {
    this(iri, initSession(), options);
  }
  
  private static ActivitiesSession initSession() {
    return new ActivitiesClient().newSession();
  }
  
  public ActivitiesClientPusher(
    String iri, 
    ActivitiesSession session) {
      this(iri,session,session.getDefaultRequestOptions());
  }
  
  public ActivitiesClientPusher(
    String iri, 
    ActivitiesSession session,
    RequestOptions options) {
    this.session = session;
    this.iri = iri;
    this.options = options;
    this.exec = initExecutor();
  }
  
  protected Executor initExecutor() {
    return Executors.newSingleThreadExecutor();
  }
  
  public void push(final T t) {
    exec.execute(
      new Runnable() {
        public void run() {
          try {
            handle(session.post(iri, t, options));
          } catch (Throwable ex) {
            handle(ex);
          }
        }
      }
    );
  }
  
  protected void handle(ClientResponse resp) {
    // by default, do nothing, fire'n'forget
    resp.release();
  }
  
  protected void handle(Throwable t) {
    // by default, do nothing
    log.error(t);
  }

  public void pushAll(Iterable<T> t) {
    for (T i : t) push(i);
  }

}
