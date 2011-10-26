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
package org.apache.abdera2.test.server;

import java.util.EventListener;

import org.apache.abdera2.common.protocol.Provider;
import org.apache.abdera2.common.protocol.ServiceManager;
import org.apache.abdera2.common.protocol.servlet.AbderaServlet;
import org.apache.abdera2.common.protocol.servlet.async.AbderaAsyncService;
import org.apache.abdera2.common.protocol.servlet.async.AbderaChannelServlet;
import org.apache.abdera2.common.protocol.servlet.async.AsyncAbderaServlet;
import org.apache.abdera2.test.JettyUtil;
import org.eclipse.jetty.servlet.ServletHolder;

public class JettyServer {

    public void start(
        Class<? extends ServiceManager> _smclass) throws Exception {
      
      ServletHolder servletHolder = new ServletHolder(new AbderaServlet());
      servletHolder.setInitParameter(ServiceManager.class.getName(), _smclass.getName());
      JettyUtil.addServlet(servletHolder, "/*");
      JettyUtil.start();
    }
    
    public void startAsync(
        Class<? extends ServiceManager> _smclass, 
        AbderaChannelServlet acs) throws Exception {
      
      ServletHolder servletHolder = new ServletHolder(new AsyncAbderaServlet());
      JettyUtil.addServlet(servletHolder, "/*");
      
      if (acs != null) {
        servletHolder = new ServletHolder(acs);
        JettyUtil.addServlet(servletHolder, "/stream/*");
      }
      
      EventListener[] listeners = 
        new EventListener[] {
          new AbderaAsyncService()
      };
      JettyUtil.getSch().setInitParameter("AbderaAtompubService", "true");
      JettyUtil.getSch().setInitParameter("AbderaChannelService", "true");
      JettyUtil.getSch().setInitParameter(ServiceManager.class.getName(), _smclass.getName());
      JettyUtil.getSch().setEventListeners(listeners);
      JettyUtil.start();
    }

    public void stop() throws Exception {
       // server.stop();
      JettyUtil.stop();
    }

}
