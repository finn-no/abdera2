package org.apache.abdera2.common.misc;

public class ExceptionHelper {

  public static RuntimeException propogate(Throwable t) {
    if (t instanceof RuntimeException)
      throw (RuntimeException)t;
    else if (t instanceof Error)
      throw (Error)t;
    else return new RuntimeException(t);
  }
  
}
