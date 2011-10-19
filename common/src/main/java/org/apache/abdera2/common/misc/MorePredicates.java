package org.apache.abdera2.common.misc;

import com.google.common.base.Predicate; 
import static com.google.common.base.Preconditions.*;

public final class MorePredicates {

  private MorePredicates() {}
  
  public static Predicate<String> equalsIgnoreCase(final String val) {
    return new Predicate<String>() {
      public boolean apply(String input) {
        checkNotNull(input);
        checkNotNull(val);
        return input.equalsIgnoreCase(val);
      }
    };
  }
  
  public static Predicate<String> notNullOrEmpty() {
    return new Predicate<String>() {
      public boolean apply(String input) {
        return input != null && input.length() > 0;
      }
    };
  }
}
