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

import java.io.IOException;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.ResponseContext;
import org.apache.abdera2.common.protocol.Filter;
import org.apache.abdera2.common.protocol.FilterChain;
import org.apache.abdera2.model.Document;
import org.apache.abdera2.model.Element;
import org.apache.abdera2.parser.ParseException;
import org.apache.abdera2.parser.Parser;
import org.apache.abdera2.parser.ParserOptions;
import org.apache.abdera2.protocol.server.context.AtompubRequestContext;
import org.apache.abdera2.security.Encryption;
import org.apache.abdera2.security.EncryptionOptions;

@SuppressWarnings("unchecked")
public abstract class AbstractEncryptedRequestFilter implements Filter {

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

    public <S extends ResponseContext>S filter(RequestContext request, FilterChain chain) {
        bootstrap(request);
        String method = request.getMethod();
        if (methods.contains(method.toUpperCase())) {
            return (S)chain.next(new DecryptingRequestContextWrapper(request));
        } else
            return (S)chain.next(request);
    }

    protected abstract void bootstrap(RequestContext request);

    protected abstract Object initArg(RequestContext request);

    protected abstract EncryptionOptions initEncryptionOptions(RequestContext request, Encryption encryption, Object arg);

    private class DecryptingRequestContextWrapper extends AtompubRequestContext {
        public DecryptingRequestContextWrapper(RequestContext request) {
            super(request);
        }
        public <T extends Element> Document<T> getDocument(Parser parser, ParserOptions options) throws ParseException,
            IOException {
            Document<Element> doc = super.getDocument();
            try {
                if (doc != null) {
                    Abdera abdera = getAbdera();
                    Encryption enc = new org.apache.abdera2.security.Security(abdera).getEncryption();
                    if (enc.isEncrypted(doc)) {
                        Object arg = initArg((AtompubRequestContext)request);
                        EncryptionOptions encoptions = initEncryptionOptions((AtompubRequestContext)request, enc, arg);
                        doc = enc.decrypt(doc, encoptions);
                    }
                }
            } catch (Exception e) {
            }
            return (Document<T>)doc;
        }
    }
}
