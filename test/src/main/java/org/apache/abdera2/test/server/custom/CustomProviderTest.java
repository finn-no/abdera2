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
package org.apache.abdera2.test.server.custom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.model.Base;
import org.apache.abdera2.model.Categories;
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
import org.apache.abdera2.protocol.client.RequestOptions;
import org.apache.abdera2.protocol.client.Session;
import org.apache.abdera2.test.server.JettyServer;
import org.apache.abdera2.common.Constants;
import org.apache.abdera2.common.http.ResponseType;
import org.apache.abdera2.common.mediatype.MimeTypeHelper;
import org.joda.time.DateTime;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CustomProviderTest {

    private static JettyServer server;
    private static Abdera abdera = Abdera.getInstance();
    private static AbderaClient client = new AbderaClient();

    private static String BASE = "http://localhost:9002/atom";

    @BeforeClass
    public static void setUp() throws Exception {
        try {
            server = new JettyServer();
            server.start(CustomProvider.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        client.shutdown();
        server.stop();
    }

    @Test
    public void testGetService() throws IOException {
      AbderaSession session = (AbderaSession) client.newSession();
        AbderaClientResponse resp = (AbderaClientResponse) session.get(BASE);
        assertNotNull(resp);
        assertEquals(ResponseType.SUCCESSFUL, resp.getType());
        assertTrue(MimeTypeHelper.isMatch(resp.getContentType().toString(), Constants.APP_MEDIA_TYPE));
        Document<Service> doc = resp.getDocument();        
        try {
            prettyPrint(doc);
        } catch (Exception e) {
        }
        Service service = doc.getRoot();
        prettyPrint(service);
        assertEquals(1, service.getWorkspaces().size());
        Workspace workspace = service.getWorkspaces().get(0);
        assertEquals(1, workspace.getCollections().size());
        Collection collection = workspace.getCollections().get(0);
        assertEquals(BASE + "/feed?", collection.getResolvedHref().toString());
        assertEquals("A simple feed", collection.getTitle().toString());
        resp.release();
    }

    @Test
    public void testGetCategories() {
      AbderaSession session = (AbderaSession) client.newSession();
        AbderaClientResponse resp = (AbderaClientResponse) session.get(BASE + "/feed;categories");
        assertNotNull(resp);
        assertEquals(ResponseType.SUCCESSFUL, resp.getType());
        assertTrue(MimeTypeHelper.isMatch(resp.getContentType().toString(), Constants.CAT_MEDIA_TYPE));
        Document<Categories> doc = resp.getDocument();
        Categories cats = doc.getRoot();
        assertEquals(3, cats.getCategories().size());
        assertEquals("foo", cats.getCategories().get(0).getTerm());
        assertEquals("bar", cats.getCategories().get(1).getTerm());
        assertEquals("baz", cats.getCategories().get(2).getTerm());
        assertFalse(cats.isFixed());
    }

    @Test
    public void testGetFeed() throws Exception {
      AbderaSession session = (AbderaSession) client.newSession();
        AbderaClientResponse resp = (AbderaClientResponse) session.get(BASE + "/feed");
        assertNotNull(resp);
        assertEquals(ResponseType.SUCCESSFUL, resp.getType());
        assertTrue(MimeTypeHelper.isMatch(resp.getContentType().toString(), Constants.ATOM_MEDIA_TYPE));
        Document<Feed> doc = resp.getDocument();
        Feed feed = doc.getRoot();
        assertEquals("tag:example.org,2008:feed", feed.getId().toString());
        assertEquals("A simple feed", feed.getTitle());
        assertEquals("Simple McGee", feed.getAuthor().getName());
        assertEquals(0, feed.getEntries().size());
        resp.release();
    }

    protected void prettyPrint(Base doc) throws IOException {
        // WriterFactory writerFactory = abdera.getWriterFactory();
        // Writer writer = writerFactory.getWriter("prettyxml");
        // writer.writeTo(doc, System.out);
        // System.out.println();
    }

    @Test
    public void testPostEntry() {
      AbderaSession session = (AbderaSession) client.newSession();
        Entry entry = abdera.newEntry();
        entry.setId(BASE + "/feed/entries/1");
        entry.setTitle("test entry");
        entry.setContent("Test Content");
        entry.addLink("http://example.org");
        entry.setUpdated(DateTime.now());
        entry.addAuthor("James");
        AbderaClientResponse resp = (AbderaClientResponse) session.post(BASE + "/feed", entry);
        assertNotNull(resp);
        assertEquals(ResponseType.SUCCESSFUL, resp.getType());
        assertEquals(201, resp.getStatus());
        assertNotNull(resp.getLocation());
        resp.release();
        resp = (AbderaClientResponse) session.get(BASE + "/feed");
        Document<Feed> feed_doc = resp.getDocument();
        Feed feed = feed_doc.getRoot();
        assertEquals(1, feed.getEntries().size());
    }

    @Test
    public void testPostMedia() {
      Session session = client.newSession();
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[] {0x01, 0x02, 0x03, 0x04});
        RequestOptions options = session.getDefaultRequestOptions();
        options.setContentType("application/octet-stream");
        ClientResponse resp = session.post(BASE + "/feed", in, options);
        assertEquals(ResponseType.CLIENT_ERROR, resp.getType());
        assertEquals(405, resp.getStatus());
        resp.release();
    }

    @Test
    public void testPutEntry() throws IOException {
      AbderaSession session = (AbderaSession) client.newSession();
      AbderaClientResponse resp = (AbderaClientResponse) session.get(BASE + "/feed");
        Document<Feed> feed_doc = resp.getDocument();
        Feed feed = feed_doc.getRoot();
        prettyPrint(feed);
        Entry entry = feed.getEntries().get(0);
        String edit = entry.getEditLinkResolvedHref().toString();
        resp.release();
        resp = (AbderaClientResponse) session.get(edit);
        Document<Entry> doc = resp.getDocument();
        prettyPrint(doc.getRoot());
        entry = doc.getRoot();
        entry.setTitle("This is the modified title");
        resp.release();
        resp = (AbderaClientResponse) session.put(edit, entry);
        assertEquals(ResponseType.SUCCESSFUL, resp.getType());
        assertEquals(204, resp.getStatus());
        resp.release();
        resp = (AbderaClientResponse) session.get(edit);
        doc = resp.getDocument();
        entry = doc.getRoot();
        assertEquals("This is the modified title", entry.getTitle());
        resp.release();
        resp = (AbderaClientResponse) session.get(BASE + "/feed");
        feed_doc = resp.getDocument();
        feed = feed_doc.getRoot();
        assertEquals(1, feed.getEntries().size());
        resp.release();
    }

    @Test
    public void testDeleteEntry() {
      AbderaSession session = (AbderaSession) client.newSession();
      AbderaClientResponse resp = (AbderaClientResponse) session.get(BASE + "/feed");
        Document<Feed> feed_doc = resp.getDocument();
        Feed feed = feed_doc.getRoot();
        Entry entry = feed.getEntries().get(0);
        String edit = entry.getEditLinkResolvedHref().toString();
        resp.release();
        resp = (AbderaClientResponse) session.delete(edit);
        assertEquals(ResponseType.SUCCESSFUL, resp.getType());
        resp.release();
        resp = (AbderaClientResponse) session.get(BASE + "/feed");
        feed_doc = resp.getDocument();
        feed = feed_doc.getRoot();
        assertEquals(0, feed.getEntries().size());
        resp.release();
    }
}
