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
package org.apache.abdera2.test.security.filter;

import org.apache.abdera2.common.protocol.Provider;
import org.apache.abdera2.common.protocol.servlet.AbderaServlet;
import org.apache.abdera2.protocol.server.AtompubServiceManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


public class JettyServer {

    public static final int DEFAULT_PORT = 9002;

    private final int port;
    private Server server;

    public JettyServer() {
        this(DEFAULT_PORT);
    }

    public JettyServer(int port) {
        this.port = port;
    }

    public void start(Class<? extends Provider> _class) throws Exception {
        server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        ServletHolder servletHolder = new ServletHolder(new AbderaServlet());
        servletHolder.setInitParameter(AtompubServiceManager.PROVIDER, _class.getName());
        context.addServlet(servletHolder, "/*");
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public void join() throws Exception {
        server.join();
    }

}
