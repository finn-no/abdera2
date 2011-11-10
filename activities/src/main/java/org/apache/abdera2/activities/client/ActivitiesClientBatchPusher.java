package org.apache.abdera2.activities.client;

import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.Collection;
import org.apache.abdera2.protocol.client.RequestOptions;

/**
 * Identical to ActivitiesClientPusher with the exception that the 
 * pushAll method will create a Collection object containing all of 
 * the items and will post the single Collection object to the server
 * rather than sending one post per item
 */
public class ActivitiesClientBatchPusher<T extends ASObject> 
  extends ActivitiesClientPusher<T> {

  public ActivitiesClientBatchPusher(String iri, ActivitiesSession session,
      RequestOptions options) {
    super(iri, session, options);
  }

  public ActivitiesClientBatchPusher(String iri, ActivitiesSession session) {
    super(iri, session);
  }

  public ActivitiesClientBatchPusher(String iri, RequestOptions options) {
    super(iri, options);
  }

  public ActivitiesClientBatchPusher(String iri) {
    super(iri);
  }

  @Override
  public void pushAll(final Iterable<T> t) {
    exec.execute(
        new Runnable() {
          public void run() {
            try {
              handle(
                session.post(
                  iri, 
                  Collection.<T>makeCollection(t), 
                  options));
            } catch (Throwable ex) {
              handle(ex);
            }
          }
        }
      );
  }

}
