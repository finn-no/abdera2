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
package org.apache.abdera2.examples.appserver.employee;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.common.protocol.Provider;
import org.apache.abdera2.common.protocol.servlet.AbderaServlet;
import org.apache.abdera2.protocol.server.impl.DefaultAtompubProvider;
import org.apache.abdera2.protocol.server.impl.SimpleWorkspaceInfo;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


public class AppServer {

    public static void main(String... args) throws Exception {
        int port = 9002;
        try {
            port = args.length > 0 ? Integer.parseInt(args[0]) : 9002;
        } catch (Exception e) {
        }
        Server server = new Server(port);
        ServletContextHandler handler = 
          new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/");
        server.setHandler(handler);
        ServletHolder servletHolder = new ServletHolder(new EmployeeProviderServlet());
        handler.addServlet(servletHolder, "/*");
        server.start();
        server.join();
    }

    // START SNIPPET: servlet
    public static final class EmployeeProviderServlet extends AbderaServlet {
        private static final long serialVersionUID = -549428240693531463L;

        protected Provider createProvider() {
            EmployeeCollectionAdapter ca = new EmployeeCollectionAdapter();
            ca.setHref("employee");

            SimpleWorkspaceInfo wi = new SimpleWorkspaceInfo();
            wi.setTitle("Employee Directory Workspace");
            wi.addCollection(ca);

            DefaultAtompubProvider provider = new DefaultAtompubProvider("/");
            provider.addWorkspace(wi);

            provider.init(Abdera.getInstance(), null);
            return provider;
        }
    }
    // END SNIPPET: servlet
}
