package org.apache.abdera2.common.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.abdera2.common.Discover;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Iterables;

public class MoreFunctions {

  public static <T>T createInstance(Class<T> _class, Object... args) {
    return MoreFunctions.<T>createInstance(_class).apply(args);
  }
  
  public static <T>T createInstance(Class<T> _class, Class<?>[] types, Object... args) {
    return MoreFunctions.<T>createInstance(_class,types).apply(args);
  }
  
  public static <T>Function<Object[],T> createInstance(
    final Class<T> _class,
    final Class<?>... args) {
    return new Function<Object[],T>() {
      public T apply(Object[] input) {
        try {
          if (input != null) {
            return _class.getConstructor(args).newInstance(input);
          } else {
            return _class.newInstance();
          }
        } catch (Throwable t) {
          throw ExceptionHelper.propogate(t);
        }
      }
    };
  }
  
  public static <T>Function<Object[],T> createInstance(
    final Class<T> _class) {
    return new Function<Object[],T>() {
      public T apply(Object[] input) {
        try {
          if (input != null) {
            Class<?>[] _types = new Class[input.length];
            for (int n = 0; n < input.length; n++) 
              _types[n] = input[n].getClass();
            return _class.getConstructor(_types).newInstance(input);
          } else {
            return _class.newInstance();
          }
        } catch (Throwable t) {
          throw ExceptionHelper.propogate(t);
        }
      }
    };
  }
  
  public static <R extends Initializable>Function<Map<String,Object>,R> discoverInitializable(
    final Class<R> _class) {
      return discoverInitializable(_class,null);
  }
  
  public static <R extends Initializable>Function<Map<String,Object>,R> discoverInitializable(
    final Class<R> _class, 
    final Class<? extends R> _instance) {
    return new Function<Map<String,Object>,R>() {
      public R apply(Map<String, Object> input) {
        if (input == null) return null;
        String instance = (String)input.get(_class.getName());
        if (instance == null && _instance != null)
          instance = _instance.getName();
        R r = (R)Discover.locate(_class, instance);
        r.init(input);
        return r;
      }
    };
  }
  
  public static <T>T firstNonNull(T... items) {
    return MoreFunctions.<T>firstNonNullArray().apply(items);
  }
  
  public static <T>T firstNonNull(Iterable<T> items) {
    return MoreFunctions.<T>firstNonNull().apply(items);
  }
  
  /**
   * Returns a Function that returns the first non-null input
   */
  public static <T>Function<Iterable<T>,T> firstNonNull() {
    return new Function<Iterable<T>,T>() {
      public T apply(Iterable<T> input) {
        for (T t : input)
          if (t != null) return t;
        return null;
      }
    };
  }
  
  /**
   * Returns a Function that returns the first non-null input
   */
  public static <T>Function<T[],T> firstNonNullArray() {
    return new Function<T[],T>() {
      public T apply(T[] input) {
        for (T t : input)
          if (t != null) return t;
        return null;
      }
    };
  }
  
  /**
   * Used to build an immutable, thread-safe Function<T,R> wrapping one or more 
   * Predicate<T>'s associated with exactly one Supplier<R>. The input T is 
   * tested against each Predicate<T> in the order defined and returns the 
   * value of the Supplier<R> if Predicate.apply() returns true. A default 
   * return value can be optionally specified, otherwise if no predicates 
   * apply, the function returns null.
   */
  public static <T,R>ChoiceGenerator<T, R> choice() {
    return new ChoiceGenerator<T,R>();
  }
  
  /**
   * Creates an option for use with a Choice Function
   */
  public static <T,R>Choice.Option<T,R> option(
    final Predicate<T> predicate, 
    final Supplier<R> supplier) {
    return new Choice.Option<T,R>() {
      public boolean apply(T input) {
        return predicate.apply(input);
      }
      public R get() {
        return supplier.get();
      }        
    };
  }
  
  /**
   * Creates an option for use with a Choice Function
   */
  public static <T,R>Choice.Option<T,R> option(
    final Predicate<T> predicate,
    final R object) {
      return MoreFunctions.<T,R>option(
        predicate, 
        Suppliers.ofInstance(object));
  }
  
  /**
   * Creates a Function that applies a Predicate to a list of inputs and 
   * returns the first matching item or null
   */
  public static <T>Function<Iterable<T>,T> oneOf(final Predicate<T> predicate) {
    return oneOf(predicate,null);
  }
  
  /**
   * Creates a Function that applies a Predicate to a list of inputs and 
   * returns the first matching item or the specified default value
   */
  public static <T>Function<Iterable<T>,T> oneOf(final Predicate<T> predicate, final T def) {
    return new Function<Iterable<T>,T>() {
      public T apply(Iterable<T> input) {
        try {
          return Iterables.<T>find(input, predicate);
        } catch (Throwable t) {
          return def;
        }
      }
    };
  }
  
  /**
   * Creates a Function that applies a Predicate to a list of inputs and 
   * returns the first matching item or null
   */
  public static <T>Function<T[],T> oneOfArray(final Predicate<T> predicate) {
    return oneOfArray(predicate,null);
  }
  
  /**
   * Creates a Function that applies a Predicate to a list of inputs and 
   * returns the first matching item or the specified default value
   */
  public static <T>Function<T[],T> oneOfArray(final Predicate<T> predicate, final T def) {
    return new Function<T[],T>() {
      public T apply(T[] input) {
        try {
          return Iterables.<T>find(Arrays.asList(input), predicate);
        } catch (Throwable t) {
          return def;
        }
      }
    };
  }
  
  /**
   * Returns a function that takes an array of input T and applies a 
   * function to each member to produce an array of output X.
   */
  public static <T,X>Function<T[],X[]> eachArray(
    final Function<T,X> apply, final Class<X> _class) {
      return new Function<T[],X[]>() {
        public X[] apply(T[] input) {
          return each(input, apply, _class);
        }
      };
  }
    
  /**
   * Returns a function that takes an array of input T and applies a 
   * function to each member for which the Predicate<T> evaluates true
   * to produce an array of output X.
   */
  public static <T,X>Function<T[],X[]> eachArray(
    final Function<T,X> apply, 
    final Predicate<T> predicate,
    final Class<X> _class) {
      return new Function<T[],X[]>() {
        public X[] apply(T[] input) {
          return each(input, apply, predicate, _class);
        }
      };
  }
  
  /**
   * Returns a function that takes an Iterable of input T and applies a 
   * function to each member to produce an Iterable of output X.
   */
  public static <T,X>Function<Iterable<T>,Iterable<X>> each(
    final Function<T,X> apply) {
      return new Function<Iterable<T>,Iterable<X>>() {
        public Iterable<X> apply(Iterable<T> input) {
          return each(input, apply);
        }
      };
  }
  
  /**
   * Returns a function that takes an Iterable of input T and applies a 
   * function to each member for which the Predicate<T> evaluates true
   * to produce an Iterable of output X.
   */
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
    Iterable<X> x = MoreFunctions.<T,X>each(Arrays.<T>asList(i),apply);
    return Iterables.<X>toArray(x, _class);
  }
  
  public static <T,X>X[] each(T[] i, Function<T,X> apply,Predicate<T> pred, Class<X> _class) {
    Iterable<X> x = MoreFunctions.<T,X>each(Arrays.<T>asList(i),apply,pred);
    return Iterables.<X>toArray(x, _class);
  }
  
  public static <T>T[] array(T...t){
    return t;
  }
  
  public static class ChoiceGenerator<T,R> implements Supplier<Function<T,R>> {
    private final Set<Choice.Option<T,R>> options = 
      new LinkedHashSet<Choice.Option<T,R>>();
    private Supplier<R> otherwise;
    public ChoiceGenerator<T,R> of(Choice.Option<T,R> option) {
      this.options.add(option);
      return this;
    }
    public ChoiceGenerator<T,R> of(Predicate<T> predicate, Supplier<R> supplier) {
      return of(option(predicate, supplier));
    }
    public ChoiceGenerator<T,R> of(Predicate<T> predicate, R instance) {
      return of(option(predicate, instance));
    }
    public ChoiceGenerator<T,R> otherwise(Supplier<R> supplier) {
      this.otherwise = supplier;
      return this;
    }
    public ChoiceGenerator<T,R> otherwise(R instance) {
      return otherwise(Suppliers.ofInstance(instance));
    }
    public Function<T,R> get() {
      return new Choice<T,R>(options,otherwise);
    }
  }
  
  private static class Choice<T,R> implements Function<T,R> {    
    public static interface Option<T,R>
    extends Predicate<T>, Supplier<R> {}
    private final Set<Choice.Option<T,R>> options = 
      new LinkedHashSet<Choice.Option<T,R>>();
    private final Supplier<R> otherwise;
    private Choice(Set<Choice.Option<T,R>> options, Supplier<R> otherwise) {
      this.options.addAll(options);
      this.otherwise = otherwise;
    }
    public R apply(T input) {
      for (Choice.Option<T,R> option : options) {
        if (option.apply(input))
          return option.get();
      }
      return otherwise != null ? otherwise.get() : null;
    }
  }
  
  public static final Function<String,Long> parseLong =
    new Function<String,Long>() {
      public Long apply(String input) {
        try {
          return (input != null) ? Long.valueOf(input) : -1;
        } catch (NumberFormatException e) {
          return -1L;
        }
      }
  };
}
