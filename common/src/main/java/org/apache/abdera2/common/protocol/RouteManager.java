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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.abdera2.common.templates.CachingContext;
import org.apache.abdera2.common.templates.Context;
import org.apache.abdera2.common.templates.MapContext;
import org.apache.abdera2.common.templates.ObjectContext;
import org.apache.abdera2.common.templates.Route;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.sun.xml.internal.xsom.impl.scd.Iterators;

/**
 * This is a largely experimental implementation of a Target Resolver and Target Builder based on URL patterns similar
 * (but not identical) to Ruby on Rails style routes. For instance:
 * 
 * <pre>
 * RouteManager rm =
 *     new RouteManager().addRoute(&quot;entry&quot;, &quot;:collection/:entry&quot;, TargetType.TYPE_ENTRY)
 *         .addRoute(&quot;feed&quot;, &quot;:collection&quot;, TargetType.TYPE_COLLECTION);
 * </pre>
 * 
 * The RouteManager can be used by Provider implementations as the target resolver and target builder
 */
public class RouteManager<T,X extends RequestContext,R> 
  implements Function<X,Target>, 
             TargetBuilder<R> {

    public static <T,X extends RequestContext,R>Generator<T,X,R> make() {
      return new Generator<T,X,R>();
    }
  
    public static class Generator<T, X extends RequestContext,R> 
      implements Supplier<RouteManager<T,X,R>> {

      protected final List<RouteTargetType<R>> targets = 
        new ArrayList<RouteTargetType<R>>();
      protected final Map<R, Route<R>> routes = 
        new HashMap<R, Route<R>>();
      protected final Map<Route<R>, CollectionAdapter> route2CA = 
        new HashMap<Route<R>, CollectionAdapter>();
      public Generator<T,X,R> withAll(RouteManager<T,X,R> other) {
        this.targets.addAll(other.targets);
        this.routes.putAll(other.routes);
        this.route2CA.putAll(other.route2CA);
        return this;
      }
      public Generator<T,X,R> with(Route<R>... routes) {
        for (Route<R> route : routes)
          with(route, null);
        return this;
      }
      public Generator<T,X,R> with(R key, String pattern) {
        return with(key, pattern, null);
      }
      public Generator<T,X,R> with(
        Route<R> route, 
        TargetType type) {
        routes.put(route.getKey(), route);
        if (type != null)
          targets.add(new RouteTargetType<R>(route, type));
        return this;
      }
      public Generator<T,X,R> with(
        R key, 
        String pattern, 
        TargetType type) {
        return with(new Route<R>(key, pattern), type);
      }
      public Generator<T,X,R> with(
        R key, 
        String pattern, 
        TargetType type, 
        CollectionAdapter collectionAdapter) {
        Route<R> route = new Route<R>(key, pattern);
        route2CA.put(route, collectionAdapter);
        return with(route, type);
      }
      public RouteManager<T, X, R> get() {
        return new RouteManager<T,X,R>(this);
      }
    }
  
    protected static class RouteTargetType<T> {
      protected Route<T> route;
      protected TargetType targetType;
      RouteTargetType(Route<T> route, TargetType targetType) {
        this.route = route;
        this.targetType = targetType;
      }
      public Route<T> getRoute() {
        return route;
      }
      public TargetType getTargetType() {
        return targetType;
      }
    }

    protected final List<RouteTargetType<R>> targets = 
      new ArrayList<RouteTargetType<R>>();
    protected final Map<R, Route<R>> routes = 
      new HashMap<R, Route<R>>();
    protected final Map<Route<R>, CollectionAdapter> route2CA = 
      new HashMap<Route<R>, CollectionAdapter>();

    private RouteManager(Generator<T,X,R> gen) {
      this.targets.addAll(gen.targets);
      this.routes.putAll(gen.routes);
      this.route2CA.putAll(gen.route2CA);
    }
    
    public Target apply(X request) {
      String uri = request.getTargetPath();
      int idx = uri.indexOf('?');
      if (idx != -1) {
          uri = uri.substring(0, idx);
      }

      RouteTargetType<R> target = get(uri);
      if (target == null) {
          target = match(uri);
      }

      if (target != null) {
          return getTarget(request, target, uri);
      }

      return null;
    }

    private RouteTargetType<R> get(String uri) {
      for (RouteTargetType<R> target : targets) {
        if (target.route.getPattern().equals(uri)) {
          return target;
        }
      }
      return null;
    }

    private RouteTargetType<R> match(String uri) {
      for (RouteTargetType<R> target : targets) {
        if (target.route.match(uri)) {
          return target;
        }
      }
      return null;
    }

    private Target getTarget(RequestContext context, RouteTargetType<R> target, String uri) {
        CollectionAdapter ca = route2CA.get(target.route);
        if (ca != null) {
            context.setAttribute(AbstractWorkspaceManager.COLLECTION_ADAPTER_ATTRIBUTE, ca);
        }
        return getTarget(context, target.route, uri, target.targetType);
    }

    private Target getTarget(RequestContext context, Route<R> route, String uri, TargetType type) {
        return new RouteTarget(type, context, route, uri);
    }

    public String urlFor(Request context, R key, Object param) {
        RequestContext rc = (RequestContext) context;
        Route<R> route = routes.get(key);
        return route != null ? rc.getContextPath() + route.expand(getContext(param)) : null;
    }

    private Context getContext(Object param) {
        Context context = new EmptyContext();
        if (param != null) {
            if (param instanceof Map) {
                context = new MapContext(cleanMapCtx(param), true);
            } else if (param instanceof Context) {
                context = (Context)param;
            } else {
                context = new ObjectContext(param, true);
            }
        }
        return context;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> cleanMapCtx(Object param) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : ((Map<String, Object>)param).entrySet()) {
            map.put(entry.getKey().replaceFirst("^:", ""), entry.getValue());
        }
        ((Map<String, Object>)param).clear();
        ((Map<String, Object>)param).putAll(map);
        return (Map<String, Object>)param;
    }

    private static class EmptyContext 
      extends CachingContext {
      private static final long serialVersionUID = 4681906592987534451L;
      public EmptyContext() {
        super(false);
      }
      public boolean contains(String var) {
        return false;
      }  
      protected <T> T resolveActual(String var) {
        return null;
      }
      public Iterator<String> iterator() {
        return Iterators.<String>empty();
      }
    }

    public static class RouteTarget extends SimpleTarget {
        private final Map<String, String> params;
        private final Route<?> route;

        public RouteTarget(
          TargetType type, 
          RequestContext context, 
          Route<?> route, String uri) {
            super(type, context);
            this.route = route;
            this.params = route.parse(uri);
        }

        public Route<?> getRoute() {
            return route;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T getMatcher() {
            return (T)getRoute();
        }

        public String getParameter(String name) {
            return params.containsKey(name) ? params.get(name) : super.getParameter(name);
        }

        public Iterable<String> getParameterNames() {
            Iterable<String> ns = super.getParameterNames();
            Set<String> names = new HashSet<String>();
            for (String name : ns)
              names.add(name);
            for (String name : params.keySet())
                names.add(name);
            return names;
        }
    }
}
