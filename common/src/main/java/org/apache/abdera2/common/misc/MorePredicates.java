package org.apache.abdera2.common.misc;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

import org.apache.abdera2.common.selector.PropertySelector;
import org.apache.abdera2.common.text.CodepointMatcher;

import com.google.common.base.Predicate; 
import com.google.common.base.Predicates;

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
  
  /**
   * Returns a Predicate that checks if the named property of instances of
   * the specified class are null. The named property MUST NOT be private 
   * and MUST NOT require any input parameters. The method name is case
   * sensitive. 
   */
  public static <T>Predicate<T> isNull(Class<T> _class, String method) {
    return PropertySelector.<T>create(_class, method, Predicates.isNull());
  }
  
  /**
   * Returns a Predicate that checks if the named property of instances of
   * the specified class are not null. The named property MUST NOT be private 
   * and MUST NOT require any input parameters. The method name is case
   * sensitive. 
   */
  public static <T>Predicate<T> isNotNull(Class<T> _class, String method) {
    return PropertySelector.<T>create(_class, method, Predicates.not(Predicates.isNull()));
  }
  
  /**
   * Returns a Predicate that checks if the value of a named property of 
   * instances of the specified class is an instance of the given test class 
   */
  public static <T>Predicate<T> instanceOf(Class<T> _class, String method, Class<?> _test) {
    return PropertySelector.<T>create(_class, method, Predicates.instanceOf(_test));
  }
  
  public static <T>Predicate<T> assignableFrom(Class<T> _class, String method, Class<?> _test) {
    return PropertySelector.<T>create(_class, method, Predicates.assignableFrom(_test));
  }
  
  public static <T>Predicate<T> containsPattern(Class<T> _class, String method, Pattern pattern) {
    return PropertySelector.<T>create(_class, method, Predicates.contains(pattern));
  }
  
  public static <T>Predicate<T> containsPattern(Class<T> _class, String method, String pattern) {
    return containsPattern(_class,method,Pattern.compile(pattern));
  }
  
  public static <T>Predicate<T> matches(Class<T> _class, String method, CodepointMatcher matcher) {
    return PropertySelector.<T>create(_class, method, matcher);
  }
  
  public static <T>Predicate<T> equalTo(Class<T> _class, String method, Object obj) {
    return PropertySelector.<T>create(_class, method, Predicates.equalTo(obj));
  }
  
  public static <T>Predicate<T> in(Class<T> _class, String method, Collection<T> items) {
    return PropertySelector.<T>create(_class, method, Predicates.in(items));
  }
  
  public static <T>Predicate<T> in(Class<T> _class, String method, T... items) {
    return in(_class,method,Arrays.asList(items));
  }
  
}
