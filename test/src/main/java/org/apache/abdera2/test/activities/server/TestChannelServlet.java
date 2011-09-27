package org.apache.abdera2.test.activities.server;

import java.io.Writer;

import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.IO;
import org.apache.abdera2.common.protocol.servlet.async.AbderaChannelServlet;

public class TestChannelServlet 
  extends AbderaChannelServlet {

  private static final long serialVersionUID = 4020428773015858214L;

  protected String getChannel(AsyncContext context) {
    HttpServletRequest req = (HttpServletRequest) context.getRequest();
    String pi = req.getPathInfo();
    return pi.substring(pi.lastIndexOf('/')+1);
  }

  @Override
  protected AsyncListener<ASObject> createListener(final AsyncContext context) {
    return new AsyncListener<ASObject>(context) {

      public void beforeItems() {}
      protected void finish() {}
      public void onItem(ASObject t) {
        try {
          HttpServletResponse response = 
            (HttpServletResponse) context.getResponse();
          Writer writer = response.getWriter();
          IO.get().write(t,writer);
          response.flushBuffer();
          context.complete(); // close out the request, make the user come back for more
        } catch (Throwable e) {}
      }
    };
  }

  @Override
  protected long getTimeout(ServletConfig config, ServletContext context) {
    return 60 * 1000;
  }

}
