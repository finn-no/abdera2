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
package org.apache.abdera2.security.util.filters;

import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.common.misc.Chain;
import org.apache.abdera2.common.misc.ExceptionHelper;
import org.apache.abdera2.common.misc.Task;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.ResponseContext;
import org.apache.abdera2.model.Document;
import org.apache.abdera2.model.Element;
import org.apache.abdera2.protocol.server.impl.AbstractAtompubProvider;
import org.apache.abdera2.security.Encryption;
import org.apache.abdera2.security.EncryptionOptions;

public abstract class AbstractEncryptedRequestFilter implements Task<RequestContext,ResponseContext> {

    // The methods that allow encrypted bodies
    protected final List<String> methods = new ArrayList<String>();

    protected AbstractEncryptedRequestFilter() {
        this("POST", "PUT");
    }

    protected AbstractEncryptedRequestFilter(String... methods) {
        for (String method : methods)
            this.methods.add(method);
        initProvider();
    }

    protected void initProvider() {
    }

    protected void addProvider(Provider provider) {
        if (Security.getProvider(provider.getName()) == null)
            Security.addProvider(provider);
    }

    public ResponseContext apply(RequestContext request, Chain<RequestContext,ResponseContext> chain) {
        bootstrap(request);
        String method = request.getMethod();
        if (methods.contains(method.toUpperCase())) {
            return chain.next(setDecryptedDocument(request));
        } else
            return chain.next(request);
    }

    protected RequestContext setDecryptedDocument(RequestContext request) {
      try {
        Document<Element> doc = AbstractAtompubProvider.getDocument(request);
        if (doc != null) {
          Abdera abdera = Abdera.getInstance();
          Encryption enc = new org.apache.abdera2.security.Security(abdera).getEncryption();
          if (enc.isEncrypted(doc)) {
            Object arg = initArg(request);
            EncryptionOptions encoptions = 
              initEncryptionOptions(request, enc, arg);
            doc = enc.decrypt(doc, encoptions);
            if (doc != null) 
              request.setAttribute(
                Document.class.getName(), doc);
          }
        }
      } catch (Exception e) {
        throw ExceptionHelper.propogate(e);
      }
      return request;
    }
    
    protected abstract void bootstrap(RequestContext request);

    protected abstract Object initArg(RequestContext request);

    protected abstract EncryptionOptions initEncryptionOptions(
      RequestContext request, 
      Encryption encryption, 
      Object arg);

}
