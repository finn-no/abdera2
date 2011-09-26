package org.apache.abdera2.activities.client;

import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.Activity;
import org.apache.abdera2.activities.model.Collection;
import org.apache.abdera2.protocol.client.BasicClient;
import org.apache.abdera2.protocol.client.Client;
import org.apache.abdera2.protocol.client.ClientWrapper;
import org.apache.abdera2.protocol.client.RequestOptions;
import org.apache.abdera2.protocol.client.Session;

/**
 * Extension of the base Abdera Client that provides utility methods
 * for working with Activity Stream objects. The ActivityClient acts 
 * as a decorator for the base Abdera Client. 
 */
@SuppressWarnings("unchecked")
public class ActivitiesClient 
  extends ClientWrapper {

  public ActivitiesClient() {
    super(new BasicClient());
  }
  
  public ActivitiesClient(Client client) {
    super(client);
  }

  @Override
  public <T extends Session> T newSession() {
    return (T)new ActivitiesSession(this);
  }

  public <T extends Collection<?>>T getCollection(String uri) {
    ActivitiesSession session = newSession();
    return (T)session.getCollection(uri);
  }
  
  public <T extends Collection<?>>T getCollection(String uri, RequestOptions options) {
    ActivitiesSession session = newSession();
    return (T)session.getCollection(uri,options);
  }
  
  public <T extends Activity>T getActivity(String uri) {
    ActivitiesSession session = newSession();
    return (T)session.getActivity(uri);
  }
  
  public <T extends Activity>T getActivity(String uri, RequestOptions options) {
    ActivitiesSession session = newSession();
    return (T)session.getActivity(uri,options);
  }
  
  public <T extends ASObject>T getObject(String uri) {
    ActivitiesSession session = newSession();
    return (T)session.getObject(uri);
  }
  
  public <T extends ASObject>T getObject(String uri, RequestOptions options) {
    ActivitiesSession session = newSession();
    return (T)session.getObject(uri,options);
  }
}
