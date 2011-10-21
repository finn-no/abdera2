package org.apache.abdera2.common.protocol;

import org.apache.abdera2.common.misc.ExceptionHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.base.Predicate;

public abstract class TransactionalRequestProcessor 
  extends RequestProcessor
  implements Transactional {

  private final static Log log = 
    LogFactory.getLog(
      TransactionalRequestProcessor.class);
  
  protected TransactionalRequestProcessor(
    WorkspaceManager workspaceManager,
    CollectionAdapter adapter) {
      super(workspaceManager, adapter);
  }
  
  protected TransactionalRequestProcessor(
    WorkspaceManager workspaceManager,
    CollectionAdapter adapter,
    Predicate<RequestContext> predicate) {
      super(workspaceManager,adapter,predicate);
  }

  public void start(RequestContext request) {
    // the default is to do nothing here
  }

  public void end(RequestContext request, ResponseContext response) {
    // the default is to do nothing here
  }

  public void compensate(RequestContext request, Throwable t) {
    // the default is to do nothing here
  }

  public ResponseContext apply(RequestContext input) {
    ResponseContext response = null;
    try {
      start(input);
      response = actuallyApply(input);
      return response;
    } catch (Throwable e) {
      ExceptionHelper.log(log,e);
      compensate(input,e);
      throw ExceptionHelper.propogate(e);
    } finally {
      end(input, response);
    }
  }

}
