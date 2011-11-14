package org.apache.abdera2.test.activities.server;
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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.abdera2.activities.client.ActivityEntity;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.Activity;
import org.apache.abdera2.activities.model.Collection;
import org.apache.abdera2.activities.model.IO;
import org.apache.abdera2.activities.model.Verb;
import org.apache.abdera2.activities.model.objects.NoteObject;
import org.apache.abdera2.activities.model.objects.PersonObject;
import org.apache.abdera2.common.http.ResponseType;
import org.apache.abdera2.protocol.client.BasicClient;
import org.apache.abdera2.protocol.client.Client;
import org.apache.abdera2.protocol.client.ClientResponse;
import org.apache.abdera2.protocol.client.Session;
import org.apache.abdera2.test.server.JettyServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AsyncBasicTest {

    private static JettyServer server;
    private static Client client = new BasicClient();
    private static IO io = IO.get();

    @BeforeClass
    public static void setUp() throws Exception {
        if (server == null) {
            server = new JettyServer();
            server.startAsync(
              BasicActivitiesServiceManager.class,
              new TestChannelServlet());
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
      try {
         Thread.sleep(20 * 1000);
      } catch (Throwable t) {}
        client.shutdown();
        server.stop();
    }

    @Test
    public void testGetFeed() throws IOException {
        Session session = client.newSession();
        ClientResponse resp = session.get("http://localhost:9002/sample");
        Collection<ASObject> coll = io.readCollection(resp.getReader());
        assertEquals("http://localhost:9002/sample", coll.getProperty("id").toString());
        assertEquals("title for any sample feed", coll.getProperty("title"));
        PersonObject person = coll.getProperty("author");
        assertNotNull(person);
        assertEquals("rayc", person.getDisplayName());
        resp.release();
    }
 
    @Test
    public void testPostEntry() throws IOException {
      
        try {
          Thread.sleep(20 * 1000);
        } catch (Throwable t) {}
      
        Activity activity = 
          Activity.makeActivity()
           .id("http://localhost:9002/sample/foo")
           .title("test entry")
           .verb(Verb.POST)
           .publishedNow()
           .actor(PersonObject.makePerson().displayName("James").get())
           .object(NoteObject.makeNote().content("Test Content").get())
           .get();
        Session session = client.newSession();
        ActivityEntity ae = new ActivityEntity(activity);
        ClientResponse resp = session.post("http://localhost:9002/sample", ae);
        assertNotNull(resp);
        assertEquals(ResponseType.SUCCESSFUL, resp.getType());
        assertEquals(201, resp.getStatus());
        assertEquals("http://localhost:9002/sample/foo", resp.getLocation().toString());
        resp.release();
        resp = session.get("http://localhost:9002/sample");
        Collection<ASObject> coll = io.readCollection(resp.getReader());
        Iterable<ASObject> items = coll.getItems();
        int n = 0;
        for (ASObject obj : items) {
          assertTrue(obj instanceof Activity);
          activity = (Activity) obj;
          assertEquals(Verb.POST, activity.getVerb());
          n++;
        }
        assertEquals(1, n);
        resp.release();
    }

}
