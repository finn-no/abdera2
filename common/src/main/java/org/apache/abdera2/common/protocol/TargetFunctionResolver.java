/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  For additional information regarding
 * copyright in this work, please see the NOTICE file in the top level
 * directory of this distribution.
 */
package org.apache.abdera2.common.protocol;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.abdera2.common.iri.IRI;
import org.apache.abdera2.common.misc.ExceptionHelper;
import org.apache.abdera2.common.misc.MoreFunctions;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;

/**
 * A TargetResolver based on a Set of Function objects. Each 
 * Function is attempted in the order it is added to the 
 * resolver. If the Function returns a Target, execution 
 * stops and the target is returned, otherwise the next 
 * function in the list is tried
 */
public class TargetFunctionResolver<R extends RequestContext>
  implements Function<R,Target> {

  public static <R extends RequestContext>TargetFunctionResolver<R> create(
    TargetFunction<R>... functions) {
      return create(Arrays.asList(functions));
  }
  
  public static <R extends RequestContext>TargetFunctionResolver<R> create( 
    Iterable<TargetFunction<R>> functions) {
      return new TargetFunctionResolver<R>(functions);
  }
  
  protected final Set<TargetFunction<R>> functions = 
    new LinkedHashSet<TargetFunction<R>>();

  private TargetFunctionResolver(
    Iterable<TargetFunction<R>> functions) {
      Iterables.addAll(this.functions, functions);
  }

  public TargetFunctionResolver<R> addFunction(
    TargetFunction<R> function) {
      this.functions.add(function);
      return this;
  }

  public Target apply(R request) {
    for (TargetFunction<R> f : functions) {
      Target target = f.apply(request);
      if (target != null)
        return target;
    }
    return null;
  }

  @Override
  public int hashCode() {
    return MoreFunctions.genHashCode(1, functions);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TargetFunctionResolver<?> other = 
      (TargetFunctionResolver<?>) obj;
    if (functions == null) {
      if (other.functions != null)
        return false;
    } else if (!functions.equals(other.functions))
      return false;
    return true;
  }

  /**
   * A Function that Resolves a Target from a RequestContext
   */
  public static abstract class TargetFunction<R extends RequestContext>
    implements Function<R,Target> {
        
  }
 
  /**
   * Returns a Generator used to build PredicateTargetfunction instances
   */
  public static <R extends RequestContext>PredicateTargetFunction.Generator<R> make() {
    return new PredicateTargetFunction.Generator<R>();
  }
  
  /**
   * A TargetFunction that is based on a map of Predicates and Functions.
   * The RequestContext is tested against each Predicate in the order the
   * Predicate was added. If apply() returns true, the associated Function
   * is used to return the appropriate Target instance.
   */
  public static class PredicateTargetFunction<R extends RequestContext>
    extends TargetFunction<R> {    
    public static class Generator<R extends RequestContext> implements Supplier<TargetFunction<R>> {
      private final Map<Predicate<R>,Function<R,Target>> map =
        new LinkedHashMap<Predicate<R>,Function<R,Target>>();
      public Generator<R> set(Predicate<R> test, Function<R,Target> function) {
        map.put(test,function);
        return this;
      }
      public TargetFunction<R> get() {
        return new PredicateTargetFunction<R>(map);
      }
    }
    
    private final Map<Predicate<R>,Function<R,Target>> map =
      new HashMap<Predicate<R>,Function<R,Target>>();
    
    protected PredicateTargetFunction(
      Map<Predicate<R>,Function<R,Target>> map) {
        this.map.putAll(map);
    }
    
    public Target apply(R input) { 
      try {
        for (Predicate<R> test : map.keySet())
          if (test.apply(input))
            return map.get(test).apply(input);
        return null;
      } catch (Throwable t) {
        throw ExceptionHelper.propogate(t);
      }
    }
  }
  
  /**
   * A TargetFunction that resolves Targets based primarily off the 
   * Request URI
   */
  public static abstract class RequestUriFunction<R extends RequestContext>
    extends TargetFunction<R>
    implements Function<R,Target> {
    public Target apply(R input) {
      return apply(input,input.getResolvedUri());
    }
    protected abstract Target apply(R context,IRI uri);
  }
  
  /**
   * A Predicate that tests the Request URI
   */
  public static abstract class RequestUriPredicate<R extends RequestContext> 
    implements Predicate<R> {
    public boolean apply(R input) {
      return apply(input.getResolvedUri());
    }
    protected abstract boolean apply(IRI uri);
  }
  
  /**
   * A TargetFunction that performs a Regex Pattern match on the 
   * Request URI. If the Pattern matches, a RegexTarget instance
   * is returned. This Function can be used either directly by 
   * the TargetFunctionResolver or as part of the PredicateTargetFunction
   */
  public static class RegexUriFunction<R extends RequestContext>
    extends RequestUriFunction<R> {
    private final Pattern pattern;
    private final TargetType type;
    private final Iterable<String> fields;
    public RegexUriFunction(TargetType type, Pattern pattern, String... fields) {
      this.type = type;
      this.pattern = pattern;
      this.fields = Arrays.<String>asList(fields);
    }
    public RegexUriFunction(TargetType type, String pattern, String... fields) {
      this(type,Pattern.compile(pattern),fields);
    }
    protected Target apply(R context,IRI uri) {
      Matcher matcher = pattern.matcher(uri.toString());
      return matcher.matches() ?
        new RegexTargetResolver.RegexTarget(type,context,matcher,fields) : 
        null;
    }  
  }
  
  /**
   * Predicate that tests the Request URI against a Regex Pattern
   */
  public static class RegexUriPredicate<R extends RequestContext> 
    extends RequestUriPredicate<R> {
    private final Pattern pattern;
    public RegexUriPredicate(Pattern pattern) {
      this.pattern = pattern;
    }
    public RegexUriPredicate(String pattern) {
      this(Pattern.compile(pattern));
    }
    protected boolean apply(IRI uri) {
      return pattern.matcher(uri.toString()).matches();
    }
  }
  
  public static <R extends RequestContext>Function<R,Target> functionForRegex(Pattern pattern, TargetType type, String... fields) {
    return new RegexUriFunction<R>(type,pattern,fields);
  }
  
  public static <R extends RequestContext>Function<R,Target> functionForRegex(String pattern, TargetType type, String... fields) {
    return new RegexUriFunction<R>(type,pattern,fields);
  }
  
  public static <R extends RequestContext>Predicate<R> predicateForRegex(Pattern pattern) {
    return new RegexUriPredicate<R>(pattern);
  }
  
  public static <R extends RequestContext>Predicate<R> predicateForRegex(String pattern) {
    return new RegexUriPredicate<R>(pattern);
  }
}
