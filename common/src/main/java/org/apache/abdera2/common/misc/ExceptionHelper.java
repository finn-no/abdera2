package org.apache.abdera2.common.misc;

import org.apache.abdera2.common.mediatype.MimeTypeParseException;

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
  
}
