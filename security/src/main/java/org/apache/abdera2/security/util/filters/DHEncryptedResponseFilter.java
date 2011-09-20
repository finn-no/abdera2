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

import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.ResponseContext;
import org.apache.abdera2.common.protocol.FilterChain;
import org.apache.abdera2.security.Encryption;
import org.apache.abdera2.security.EncryptionOptions;
import org.apache.abdera2.security.util.Constants;
import org.apache.abdera2.security.util.DHContext;

/**
 * A Servlet Filter that uses Diffie-Hellman Key Exchange to encrypt Atom documents. The HTTP request must include an
 * Accept-Encryption header in the form: Accept-Encryption: DH p={dh_p}, g={dh_g}, l={dh_l}, k={base64_pubkey} Example
 * AbderaClient Code:
 * 
 * <pre>
 * DHContext context = new DHContext();
 * Abdera abdera = Abdera.getInstance();
 * CommonsClient client = new CommonsClient(abdera);
 * RequestOptions options = client.getDefaultRequestOptions();
 * options.setHeader(&quot;Accept-Encryption&quot;, context.getRequestString());
 * 
 * ClientResponse response = client.get(&quot;http://localhost:8080/TestWeb/test&quot;, options);
 * Document&lt;Element&gt; doc = response.getDocument();
 * 
 * String dh_ret = response.getHeader(&quot;Content-Encrypted&quot;);
 * if (dh_ret != null) {
 *     context.setPublicKey(dh_ret);
 *     AbderaSecurity absec = new AbderaSecurity(abdera);
 *     Encryption enc = absec.getEncryption();
 *     EncryptionOptions encoptions = context.getEncryptionOptions(enc);
 *     doc = enc.decrypt(doc, encoptions);
 * }
 * 
 * doc.writeTo(System.out);
 * </pre>
 * 
 * Webapp Deployment:
 * 
 * <pre>
 * &lt;filter>
 *   &lt;filter-name>enc filter&lt;/filter-name>
 *   &lt;filter-class>com.test.EncryptedResponseFilter&lt;/filter-class>
 * &lt;/filter>
 * &lt;filter-mapping>
 *   &lt;filter-name>enc filter&lt;/filter-name>
 *   &lt;servlet-name>TestServlet&lt;/servlet-name>
 * &lt;/filter-mapping>
 * </pre>
 */
public class DHEncryptedResponseFilter extends AbstractEncryptedResponseFilter {

    protected boolean doEncryption(RequestContext request, Object arg) {
        return arg != null;
    }

    protected Object initArg(RequestContext request) {
        return getDHContext(request);
    }

    protected EncryptionOptions initEncryptionOptions(RequestContext request,
                                                      ResponseContext response,
                                                      Encryption enc,
                                                      Object arg) {
        EncryptionOptions options = null;
        try {
            DHContext context = (DHContext)arg;
            options = context.getEncryptionOptions(enc);
            returnPublicKey(response, context);
        } catch (Exception e) {
        }
        return options;

    }

    @SuppressWarnings("unchecked")
    public <S extends ResponseContext>S filter(RequestContext request, FilterChain chain) {
        ResponseContext response = super.filter(request, chain);
        DHContext context = getDHContext(request);
        response.setHeader(Constants.CONTENT_ENCRYPTED, context.getResponseString());
        return (S)response;
    }

    private void returnPublicKey(ResponseContext response, DHContext context) {
        response.setHeader(Constants.CONTENT_ENCRYPTED, context.getResponseString());
    }

    private DHContext getDHContext(RequestContext request) {
        try {
            String dh_req = request.getHeader(Constants.ACCEPT_ENCRYPTION);
            if (dh_req == null || dh_req.length() == 0)
                return null;
            return new DHContext(dh_req);
        } catch (Exception e) {
            return null;
        }
    }

}
