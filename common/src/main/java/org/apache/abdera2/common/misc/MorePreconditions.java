package org.apache.abdera2.common.misc;

public final class MorePreconditions  {

  private MorePreconditions() {}
  
  public static void checkArguments(boolean... expressions) {
    for (boolean expression : expressions)
      if (!expression)
        throw new IllegalArgumentException();
  }
  
  public static void checkArgumentTypes(Object obj, Class<?>... types) {
    for (Class<?> type : types) 
      if (type.isInstance(obj))
        throw new IllegalArgumentException();
  }
  
  public static boolean checkArgument(boolean expression, String message) {
    if (expression) return expression;
    throw new IllegalArgumentException(message);
  }
  
}
