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
package org.apache.abdera2.common.protocol.servlet.async;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.abdera2.common.http.Preference;
import org.apache.abdera2.common.protocol.Provider;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.ServiceManager;
import org.apache.abdera2.common.protocol.servlet.ServletRequestContext;

@WebServlet(asyncSupported=true)
public class AsyncAbderaServlet 
  extends HttpServlet {

      protected Map<String, Object> getProperties(ServletConfig config) {
        Map<String, Object> properties = new HashMap<String, Object>();
        Enumeration<String> e = config.getInitParameterNames();
        while (e.hasMoreElements()) {
            String key = e.nextElement();
            String val = config.getInitParameter(key);
            properties.put(key, val);
        }
        return properties;
    }
  
    private static final long serialVersionUID = 2086707888078611321L;
    @Override
    protected void service(
        final HttpServletRequest request, 
        final HttpServletResponse response) 
          throws ServletException, IOException {
      ServletContext sc = getServletContext();
      Processor proc = (Processor) sc.getAttribute(Processor.NAME);
      if (proc != null && !proc.isShutdown()) {
        final AsyncContext context = request.startAsync(request, response);
        ServiceManager sm = (ServiceManager) sc.getAttribute(ServiceManager.class.getName());
        Provider provider = sm.newProvider(getProperties(getServletConfig()));
        ServletRequestContext reqcontext = new ServletRequestContext(provider, request, sc);
        long timeout = getTimeout(reqcontext);
        if (timeout > -1) context.setTimeout(timeout);
        proc.submit(context,provider,reqcontext);
      } else {
        response.sendError(
          HttpServletResponse.SC_SERVICE_UNAVAILABLE, 
          "Abdera Service in unavailable");
      }
    }
    
    public static long getTimeout(RequestContext req) {
      return Math.min(getMaxTimeout(req),timeout(req));
    }
    
    public static long getMaxTimeout(RequestContext req) {
      return 30 * 1000;
    }
    
    private static long timeout(RequestContext req) {
      try {
        Iterable<Preference> i = req.getPrefer();
        Preference waitPref = Preference.get(i, Preference.WAIT);
        long wait = waitPref != null ? waitPref.getLongValue() : -1;
        return Math.max(0, wait);
      } catch (Throwable t) {
        return -1;
      }
    }
}
