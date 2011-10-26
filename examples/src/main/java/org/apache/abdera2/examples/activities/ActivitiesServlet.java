package org.apache.abdera2.examples.activities;

import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebInitParam;
import org.apache.abdera2.common.protocol.servlet.AbderaServlet;

@WebServlet(
  urlPatterns="/*",
  initParams={
    @WebInitParam(
      name="org.apache.abdera2.common.protocol.ServiceManager",
      value="org.apache.abdera2.activities.protocol.basic.BasicServiceManager"
    )
  }
)
public class ActivitiesServlet
  extends AbderaServlet {
  private static final long serialVersionUID = -2969428720501378351L;
}
