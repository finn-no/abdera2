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

import org.apache.abdera2.Abdera;
import org.apache.abdera2.common.Localizer;
import org.apache.abdera2.common.misc.Chain;
import org.apache.abdera2.common.misc.Task;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.ResponseContext;
import org.apache.abdera2.common.protocol.ProviderHelper;
import org.apache.abdera2.model.Document;
import org.apache.abdera2.model.Element;
import org.apache.abdera2.protocol.server.impl.AbstractAtompubProvider;
import org.apache.abdera2.security.Security;
import org.apache.abdera2.security.Signature;

/**
 * Servlet Filter that verifies that an Atom document received by the server via PUT or POST contains a valid XML
 * Digital Signature.
 */
public class SignedRequestFilter implements Task<RequestContext,ResponseContext> {

    public static final String VALID = "org.apache.abdera.security.util.servlet.SignedRequestFilter.valid";
    public static final String CERTS = "org.apache.abdera.security.util.servlet.SignedRequestFilter.certs";

    public ResponseContext apply(RequestContext request, Chain<RequestContext,ResponseContext> chain) {
        Security security = new Security(Abdera.getInstance());
        Signature sig = security.getSignature();
        String method = request.getMethod();
        if (method.equals("POST") || method.equals("PUT")) {
            try {
                Document<Element> doc = AbstractAtompubProvider.getDocument(request);
                if (security.notVerified(doc))
                    return ProviderHelper.badrequest(
                      request, 
                      Localizer.get("VALID.SIGNATURE.REQUIRED"));
                request.setAttribute(
                  VALID, true);
                request.setAttribute(
                  CERTS, 
                  sig.getValidSignatureCertificates(doc.getRoot(), null));
            } catch (Exception e) {
            }
        }
        return chain.next(request);
    }

}
