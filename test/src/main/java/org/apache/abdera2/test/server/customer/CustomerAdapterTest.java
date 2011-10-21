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
package org.apache.abdera2.test.server.customer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.factory.Factory;
import org.apache.abdera2.common.Constants;
import org.apache.abdera2.common.http.ResponseType;
import org.apache.abdera2.common.iri.IRI;
import org.apache.abdera2.model.Base;
import org.apache.abdera2.model.Collection;
import org.apache.abdera2.model.Document;
import org.apache.abdera2.model.Element;
import org.apache.abdera2.model.Entry;
import org.apache.abdera2.model.Service;
import org.apache.abdera2.model.Workspace;
import org.apache.abdera2.protocol.client.AbderaClient;
import org.apache.abdera2.protocol.client.AbderaClientResponse;
import org.apache.abdera2.protocol.client.AbderaSession;
import org.apache.abdera2.protocol.client.RequestOptions;
import org.apache.abdera2.protocol.server.AtompubProvider;
import org.apache.abdera2.protocol.server.impl.DefaultAtompubProvider;
import org.apache.abdera2.protocol.server.impl.SimpleWorkspaceInfo;
import org.apache.abdera2.test.JettyUtil;
import org.apache.abdera2.common.mediatype.MimeTypeHelper;
import org.apache.abdera2.common.protocol.servlet.AbderaServlet;
import org.apache.abdera2.writer.Writer;
import org.apache.abdera2.writer.WriterFactory;
import org.eclipse.jetty.servlet.ServletHolder;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;

public class CustomerAdapterTest {

    private DefaultAtompubProvider customerProvider;

    private void setupAbdera(String base) throws Exception {
        customerProvider = new DefaultAtompubProvider(base);
        CustomerAdapter ca = new CustomerAdapter("customers");
        customerProvider
          .addWorkspace(
            SimpleWorkspaceInfo
              .make()
              .title("Customer Workspace")
              .collection(ca)
              .get());
    }

    @Test
    public void testCustomerProvider() throws Exception {
        setupAbdera("/");
        initializeJetty("/");

        runTests("/");
    }

    @Test
    public void testCustomerProviderWithNonRootContextPath() throws Exception {
        setupAbdera("/");
        initializeJetty("/foo");
        runTests("/foo/");
    }

    private void runTests(String base) throws IOException {
        Abdera abdera = Abdera.getInstance();
        Factory factory = abdera.getFactory();

        AbderaClient client = new AbderaClient(abdera);
        AbderaSession session = (AbderaSession) client.newSession();

        String uri = "http://localhost:9002" + base;

        // Service document test.

        AbderaClientResponse res = (AbderaClientResponse) session.get(uri);
        assertNotNull(res);
        try {
            assertEquals(200, res.getStatus());
            assertEquals(ResponseType.SUCCESSFUL, res.getType());
            assertTrue(MimeTypeHelper.isMatch(res.getContentType().toString(), Constants.APP_MEDIA_TYPE));

            Document<Service> doc = res.getDocument();
            Service service = doc.getRoot();
            assertEquals(1, service.getWorkspaces().size());

            Workspace workspace = service.getWorkspaces().get(0);
            assertEquals(1, workspace.getCollections().size());

            // Keep the loop in case we add other collections to the test.

            for (Collection collection : workspace.getCollections()) {
                if (collection.getTitle().equals("Acme Customer Database")) {
                    String expected = uri + "customers";
                    String actual = collection.getResolvedHref().toString();
                    assertEquals(expected, actual);
                }
            }
        } finally {
            res.release();
        }

        // Testing of entry creation
        IRI colUri = new IRI(uri).resolve("customers");

        Entry entry = factory.newEntry();
        entry.setTitle("This is ignored right now");
        entry.setUpdated(DateTime.now());
        entry.addAuthor("Acme Industries");
        entry.setId(factory.newUuidUri());
        entry.setSummary("Customer document");

        Element customerEl = factory.newElement(new QName("customer"));
        customerEl.setAttributeValue(new QName("name"), "Dan Diephouse");
        entry.setContent(customerEl);

        RequestOptions opts = new RequestOptions();
        opts.setContentType("application/atom+xml;type=entry");
        res = (AbderaClientResponse) session.post(colUri.toString() + "?test=foo", entry, opts);
        assertEquals(201, res.getStatus());

        // prettyPrint(abdera, res.getDocument());

        IRI location = res.getLocation();
        assertEquals(uri + "customers/1001-Dan_Diephouse", location.toString());

        // GET the entry
        res = (AbderaClientResponse) session.get(location.toString());
        assertEquals(200, res.getStatus());
        org.apache.abdera2.model.Document<Entry> entry_doc = res.getDocument();
        // prettyPrint(abdera, entry_doc);
        entry = entry_doc.getRoot();
        assertEquals(uri + "customers/1001-Dan_Diephouse", entry_doc.getRoot().getEditLinkResolvedHref().toString());
        res.release();
        
        // HEAD
        res = (AbderaClientResponse) session.head(location.toString());
        assertEquals(200, res.getStatus());
        assertEquals(0, res.getContentLength());
        res.release();

        // Try invalid resources
        res = (AbderaClientResponse) session.get(colUri + "/foobar");
        assertEquals(404, res.getStatus());
        res.release();

        res = (AbderaClientResponse) session.head(colUri + "/foobar");
        assertEquals(404, res.getStatus());
        assertEquals(0, res.getContentLength());
        res.release();

        IRI badColUri = new IRI(uri).resolve("customersbad");
        // GET the service doc
        res = (AbderaClientResponse) session.get(colUri.toString());
        assertEquals(200, res.getStatus());
        res.release();
        res = (AbderaClientResponse) session.get(badColUri.toString());
        assertEquals(404, res.getStatus());
        res.release();
        client.shutdown();
    }

    protected void prettyPrint(Abdera abdera, Base doc) throws IOException {
        WriterFactory factory = abdera.getWriterFactory();
        Writer writer = factory.getWriter("prettyxml");
        writer.writeTo(doc, System.out);
        System.out.println();
    }

    @SuppressWarnings("serial")
    private void initializeJetty(String contextPath) throws Exception {

        JettyUtil.addServlet(new ServletHolder(new AbderaServlet() {
            @Override
            protected AtompubProvider createProvider() {
                customerProvider.init(Abdera.getInstance(), null);
                return customerProvider;
            }
        }), "/*");
        JettyUtil.setContextPath(contextPath);
        JettyUtil.start();
    }

    @After
    public void tearDown() throws Exception {
        JettyUtil.stop();
    }

}
