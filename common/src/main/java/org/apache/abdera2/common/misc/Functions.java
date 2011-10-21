package org.apache.abdera2.common.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class Functions {
  
  public static <T,X>Function<Iterable<T>,Iterable<X>> each(
    final Function<T,X> apply) {
      return new Function<Iterable<T>,Iterable<X>>() {
        public Iterable<X> apply(Iterable<T> input) {
          return each(input, apply);
        }
      };
  }
  
  public static <T,X>Function<Iterable<T>,Iterable<X>> each(
    final Function<T,X> apply,
    final Predicate<T> predicate) {
      return new Function<Iterable<T>,Iterable<X>>() {
        public Iterable<X> apply(Iterable<T> input) {
          return each(input, apply, predicate);
        }
      };
  }
  
  public static <T,X>Iterable<X> each(Iterable<T> i, Function<T,X> apply) {
    List<X> list = new ArrayList<X>();
    for (T t : i) {
      try {
        list.add(apply.apply(t));
      } catch (Throwable e) {
        throw ExceptionHelper.propogate(e);
      }
    }
    return Iterables.<X>unmodifiableIterable(list);
  }
  
  public static <T,X>Iterable<X> each(Iterable<T> i, Function<T,X> apply, Predicate<T> test) {
    List<X> list = new ArrayList<X>();
    for (T t : i) {
      try {
        if (test.apply(t))
          list.add(apply.apply(t));
      } catch (Throwable e) {
        throw ExceptionHelper.propogate(e);
      }
    }
    return Iterables.<X>unmodifiableIterable(list);
  }
  
  public static <T,X>X[] each(T[] i, Function<T,X> apply, Class<X> _class) {
    Iterable<X> x = Functions.<T,X>each(Arrays.<T>asList(i),apply);
    return Iterables.<X>toArray(x, _class);
  }
  
  public static <T,X>X[] each(T[] i, Function<T,X> apply,Predicate<T> pred, Class<X> _class) {
    Iterable<X> x = Functions.<T,X>each(Arrays.<T>asList(i),apply,pred);
    return Iterables.<X>toArray(x, _class);
  }
}
