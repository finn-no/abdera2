package org.apache.abdera2.test.server.multipart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.factory.Factory;
import org.apache.abdera2.common.iri.IRI;
import org.apache.abdera2.common.protocol.CollectionAdapter;
import org.apache.abdera2.common.protocol.RequestProcessor;
import org.apache.abdera2.common.protocol.TargetType;
import org.apache.abdera2.common.protocol.servlet.AbderaServlet;
import org.apache.abdera2.model.Entry;
import org.apache.abdera2.protocol.client.AbderaClient;
import org.apache.abdera2.protocol.client.AbderaClientResponse;
import org.apache.abdera2.protocol.client.AbderaSession;
import org.apache.abdera2.protocol.client.Client;
import org.apache.abdera2.protocol.server.AtompubProvider;
import org.apache.abdera2.protocol.server.impl.DefaultAtompubProvider;
import org.apache.abdera2.protocol.server.impl.SimpleWorkspaceInfo;
import org.apache.abdera2.protocol.server.processors.MultipartRelatedServiceRequestProcessor;
import org.apache.abdera2.test.JettyUtil;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.Test;

import com.google.common.base.Function;

@SuppressWarnings("serial")
public class MultipartRelatedTest {

    private void initializeJetty(String contextPath) throws Exception {

        JettyUtil.addServlet(new ServletHolder(new AbderaServlet() {
            @Override
            protected AtompubProvider createProvider() {
                DefaultAtompubProvider provider = new DefaultAtompubProvider("/");
                Map<TargetType,Function<CollectionAdapter,? extends RequestProcessor>> map = 
                  new HashMap<TargetType,Function<CollectionAdapter,? extends RequestProcessor>>();
                map.put(TargetType.TYPE_SERVICE, 
                  RequestProcessor.forClass(
                    MultipartRelatedServiceRequestProcessor.class, 
                    provider.getWorkspaceManager()));

                provider.addRequestProcessors(map);            
                MultipartRelatedAdapter ca = 
                  new MultipartRelatedAdapter("media");  
                provider
                  .addWorkspace(
                    SimpleWorkspaceInfo
                      .make()
                      .title("multipart/related Workspace")
                      .collection(ca)
                      .get());
                provider.init(null);
                return provider;
            }
        }), "/*");
        JettyUtil.setContextPath(contextPath);
        JettyUtil.start();
    }

    @After
    public void tearDown() throws Exception {
        JettyUtil.stop();
    }

    @Test
    public void testServiceDocument() throws Exception {
        initializeJetty("/");
        Client client = new AbderaClient(Abdera.getInstance());
        AbderaSession session = (AbderaSession) client.newSession();
        AbderaClientResponse res = (AbderaClientResponse) session.get("http://localhost:9002/");
        assertEquals(200, res.getStatus());
        StringWriter sw = new StringWriter();
        res.getDocument().writeTo(sw);
        res.release();
        assertTrue(sw.toString().contains("accept alternate=\"multipart-related\">image/png"));
        assertTrue(sw.toString().contains("accept>video/*"));
        assertTrue(sw.toString().contains("accept>image/jpg"));
        client.shutdown();
    }

    @Test
    public void testPostMedia() throws Exception {
        execTest(201, "image/png");
    }

    @Test
    public void testPostMediaInvalidContentType() throws Exception {
        // collection doesn't accept multipart files with this content type
        execTest(415, "image/jpg");
    }

    private void execTest(int status, String contentType) throws Exception {
        initializeJetty("/");

        Abdera abdera = Abdera.getInstance();
        Factory factory = abdera.getFactory();

        Client client = new AbderaClient(abdera);
        AbderaSession session = (AbderaSession) client.newSession();

        Entry entry = factory.newEntry();

        entry.setTitle("my image");
        entry.addAuthor("david");
        entry.setId("tag:apache.org,2008:234534344");
        entry.setSummary("multipart test");
        entry.setContent(new IRI("cid:234234@example.com"), contentType);

        AbderaClientResponse res =
            (AbderaClientResponse) session.post(
          "http://localhost:9002/media", 
          entry, 
          new InputStreamBody(this.getClass().getResourceAsStream("/info.png"),contentType,null));
        assertEquals(status, res.getStatus());
        
        res.release();
        client.shutdown();
    }
}
