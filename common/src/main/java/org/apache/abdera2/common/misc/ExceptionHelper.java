package org.apache.abdera2.common.misc;

import org.apache.abdera2.common.http.ResponseType;
import org.apache.abdera2.common.mediatype.MimeTypeParseException;
import org.apache.abdera2.common.protocol.ResponseContextException;
import org.apache.commons.logging.Log;

public class ExceptionHelper {

  public static RuntimeException propogate(Throwable t) {
    if (t instanceof RuntimeException)
      throw (RuntimeException)t;
    else if (t instanceof Error)
      throw (Error)t;
    else if (t instanceof javax.activation.MimeTypeParseException) 
      throw MimeTypeParseException.wrap(
        (javax.activation.MimeTypeParseException)t);
    else return new RuntimeException(t);
  }
  
  public static void log(Log log, Throwable t) {
    if (t instanceof ResponseContextException) {
      ResponseContextException rce = 
        (ResponseContextException)t;
        if (rce.getResponseType() == ResponseType.CLIENT_ERROR)
            log.info(t);
        else
            log.error(t);
    } else log.error(t);
  }
  
  public static void responseLog(Log log, ResponseContextException t) {
    if (log.isDebugEnabled())
      log.debug("A ResponseException was thrown.", t);
    else if (t.getResponseContext().getType() == ResponseType.SERVER_ERROR)
      log.warn("A ResponseException was thrown.", t);
  }
}
