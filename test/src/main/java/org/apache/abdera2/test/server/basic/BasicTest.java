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
package org.apache.abdera2.test.server.basic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.model.Base;
import org.apache.abdera2.model.Collection;
import org.apache.abdera2.model.Document;
import org.apache.abdera2.model.Entry;
import org.apache.abdera2.model.Feed;
import org.apache.abdera2.model.Service;
import org.apache.abdera2.model.Workspace;
import org.apache.abdera2.protocol.client.AbderaClient;
import org.apache.abdera2.protocol.client.AbderaClientResponse;
import org.apache.abdera2.protocol.client.AbderaSession;
import org.apache.abdera2.protocol.client.ClientResponse;
import org.apache.abdera2.protocol.client.Session;
import org.apache.abdera2.test.server.JettyServer;
import org.apache.abdera2.common.Constants;
import org.apache.abdera2.common.http.ResponseType;
import org.apache.abdera2.common.mediatype.MimeTypeHelper;
import org.apache.abdera2.writer.Writer;
import org.apache.abdera2.writer.WriterFactory;
import org.joda.time.DateTime;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class BasicTest {

    private static JettyServer server;
    private static Abdera abdera = Abdera.getInstance();
    private static AbderaClient client = new AbderaClient(abdera);

    @BeforeClass
    public static void setUp() throws Exception {
        if (server == null) {
            server = new JettyServer();
            server.start(BasicAtompubServiceManager.class);
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        client.shutdown();
        server.stop();
    }

    protected void prettyPrint(Base doc) throws IOException {
        WriterFactory factory = abdera.getWriterFactory();
        Writer writer = factory.getWriter("prettyxml");
        writer.writeTo(doc, System.out);
        System.out.println();
    }

    @Test
    public void testGetService() throws IOException {
        Document<Service> doc = client.get("http://localhost:9002/");
        Service service = doc.getRoot();
        assertEquals(1, service.getWorkspaces().size());
        Workspace workspace = service.getWorkspace("Abdera");
        assertEquals(1, workspace.getCollections().size());
        Collection collection = workspace.getCollection("title for any sample feed");
        assertNotNull(collection);
        assertTrue(collection.acceptsEntry());
        assertEquals("http://localhost:9002/sample", collection.getResolvedHref().toString());
    }

    @Test
    public void testGetFeed() throws IOException {
        Document<Feed> doc = client.get("http://localhost:9002/sample");
        Feed feed = doc.getRoot();
        assertEquals("http://localhost:9002/sample", feed.getId().toString());
        assertEquals("title for any sample feed", feed.getTitle());
        assertEquals("rayc", feed.getAuthor().getName());
        assertEquals(0, feed.getEntries().size());
    }

    @Test
    public void testPostEntry() {
        Entry entry = abdera.newEntry();
        entry.setId("http://localhost:9002/sample/foo");
        entry.setTitle("test entry");
        entry.setContent("Test Content");
        entry.addLink("http://example.org");
        entry.setUpdated(DateTime.now());
        entry.addAuthor("James");
        AbderaSession session = (AbderaSession) client.newSession();
        AbderaClientResponse resp = (AbderaClientResponse) session.post("http://localhost:9002/sample", entry);
        assertNotNull(resp);
        assertEquals(ResponseType.SUCCESSFUL, resp.getType());
        assertEquals(201, resp.getStatus());
        assertEquals("http://localhost:9002/sample/foo", resp.getLocation().toString());
        resp.release();
        resp = (AbderaClientResponse) session.get("http://localhost:9002/sample");
        Document<Feed> feed_doc = resp.getDocument();
        Feed feed = feed_doc.getRoot();
        assertEquals(1, feed.getEntries().size());
        resp.release();
    }

    @Test
    public void testPostMedia() {
        Session session = client.newSession();
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[] {0x01, 0x02, 0x03, 0x04});
        ClientResponse resp = session.post(
          "http://localhost:9002/sample", 
          in, session.getDefaultRequestOptions()
            .contentType("application/octet-stream").get());
        assertEquals(ResponseType.CLIENT_ERROR, resp.getType());
        assertEquals(405, resp.getStatus());
        resp.release();
    }

    @Test
    public void testPutEntry() {
      AbderaSession session = (AbderaSession) client.newSession();
        AbderaClientResponse resp = (AbderaClientResponse) session.get("http://localhost:9002/sample/foo");
        assertTrue(MimeTypeHelper.isMatch(resp.getContentType().toString(), Constants.ENTRY_MEDIA_TYPE));
        Document<Entry> doc = resp.getDocument();
        Entry entry = doc.getRoot();
        entry.setTitle("This is the modified title");
        resp.release();
        resp = (AbderaClientResponse) session.put("http://localhost:9002/sample/foo", entry);
        assertEquals(ResponseType.SUCCESSFUL, resp.getType());
        assertEquals(200, resp.getStatus());
        resp.release();
        resp = (AbderaClientResponse) session.get("http://localhost:9002/sample/foo");
        doc = resp.getDocument();
        entry = doc.getRoot();
        assertEquals("This is the modified title", entry.getTitle());
        resp.release();
        resp = (AbderaClientResponse) session.get("http://localhost:9002/sample");
        Document<Feed> feed_doc = resp.getDocument();
        Feed feed = feed_doc.getRoot();
        assertEquals(1, feed.getEntries().size());
        resp.release();
    }

    @Test
    public void testDeleteEntry() {
      AbderaSession session = (AbderaSession) client.newSession();
        AbderaClientResponse resp = (AbderaClientResponse) session.delete("http://localhost:9002/sample/foo");
        assertEquals(ResponseType.SUCCESSFUL, resp.getType());
        resp.release();
        resp = (AbderaClientResponse) session.get("http://localhost:9002/sample");
        Document<Feed> feed_doc = resp.getDocument();
        Feed feed = feed_doc.getRoot();
        assertEquals(0, feed.getEntries().size());
        resp.release();
    }
}
