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
package org.apache.abdera2.common.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.abdera2.common.text.CharUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

/**
 * Provides parsing and properly handling of the HTTP Cache-Control header.
 */
public final class CacheControlUtil {

    private CacheControlUtil() {}
  
    private static long value(String val) {
        return (val != null) ? Long.parseLong(val) : -1;
    }

    private static void append(StringBuilder buf, String value) {
        if (buf.length() > 0)
            buf.append(", ");
        buf.append(value);
    }

    private static final Joiner joiner = Joiner.on(',').skipNulls();
    
    /**
     * Construct the Cache-Control header from info in the request object
     */
    public static String buildCacheControl(CacheControl cacheControl) {
        StringBuilder buf = new StringBuilder();
        if (cacheControl.isPrivate()) {
            append(buf, "private");
            Iterable<String> headers = cacheControl.getPrivateHeaders();
            if (!Iterables.isEmpty(headers)) {
              buf.append("=\"");
              joiner.appendTo(buf, headers);
              buf.append("\"");
            }
        }
        if (cacheControl.isPublic())
            append(buf, "public");
        if (cacheControl.isNoCache()) {
            append(buf, "no-cache");
            Iterable<String> headers = cacheControl.getNoCacheHeaders();
            if (!Iterables.isEmpty(headers)) {
              buf.append("=\"");
              joiner.appendTo(buf,headers);
              buf.append("\"");
            }   
        }
        if (cacheControl.isNoStore())
            append(buf, "no-store");
        if (cacheControl.isNoTransform())
            append(buf, "no-transform");
        if (cacheControl.isOnlyIfCached())
            append(buf, "only-if-cached");
        if (cacheControl.isMustRevalidate())
            append(buf, "must-revalidate");
        if (cacheControl.isProxyRevalidate())
            append(buf, "proxy-revalidate");
        if (cacheControl.getMaxAge() != -1)
            append(buf, String.format("max-age=%d", cacheControl.getMaxAge()));
        if (cacheControl.getMaxStale() != -1)
            append(buf, String.format("max-stale=%d", cacheControl.getMaxStale()));
        if (cacheControl.getMinFresh() != -1)
            append(buf, String.format("min-fresh=%d", cacheControl.getMinFresh()));
        if (cacheControl.getStaleIfError() != -1)
            append(buf, String.format("stale-if-error=%d", cacheControl.getStaleIfError()));
        if (cacheControl.getStaleWhileRevalidate() != -1)
            append(buf, String.format("stale-while-revalidate=%d", cacheControl.getStaleWhileRevalidate()));
        for (String ext : cacheControl.listExtensions()) {
          append(buf, ext);
          Object val = cacheControl.getExtension(ext);
          if (val instanceof Long || val instanceof Integer || val instanceof Short || val instanceof Byte) {
            buf.append('=')
               .append(val);            
          } else {
            String v = val.toString();
            if (val != null && v.length() > 0)
              buf.append('=')
                 .append('"')
                 .append(val)
                 .append('"');
          }
        }
        return buf.toString();
    }

    /**
     * Parse the Cache-Control header
     */
    public static CacheControl.Builder parseCacheControl(String cc, CacheControl.Builder builder) {
        if (cc == null) return builder;
        builder.defaults();
        CacheControlParser parser = new CacheControlParser(cc);
        for (Directive directive : parser)
          directive.set(builder, parser);
        builder.extensions(parser.getExtensions());
        return builder;
    }

    /**
     * Cache Control Directives
     */
    public enum Directive {
        MAXAGE,
        MAXSTALE, 
        MINFRESH, 
        NOCACHE, 
        NOSTORE, 
        NOTRANSFORM, 
        ONLYIFCACHED, 
        MUSTREVALIDATE, 
        PROXYREVALIDATE,
        PRIVATE,
        PUBLIC, 
        STALEWHILEREVALIDATE,
        STALEIFERROR,
        UNKNOWN;

        public static Directive select(String d) {
            try {
                d = d.toUpperCase().replaceAll("-", "");
                return Directive.valueOf(d);
            } catch (Exception e) {
            }
            return UNKNOWN;
        }
        
        public void set(CacheControl.Builder builder, CacheControlParser parser) {
          switch (this) {
          case NOCACHE:
              builder.noCache(true);
              builder.noCacheHeaders(parser.getValues(this));
              break;
          case NOSTORE:
              builder.noStore(true);
              break;
          case NOTRANSFORM:
              builder.noTransform(true);
              break;
          case ONLYIFCACHED:
              builder.onlyIfCached(true);
              break;
          case MAXAGE:
              builder.maxAge(value(parser.getValue(this)));
              break;
          case MAXSTALE:
              builder.maxStale(value(parser.getValue(this)));
              break;
          case MINFRESH:
              builder.minFresh(value(parser.getValue(this)));
              break;
          case STALEIFERROR:
              builder.staleIfError(value(parser.getValue(this)));
              break;
          case MUSTREVALIDATE:
              builder.mustRevalidate(true);
              break;
          case PROXYREVALIDATE:
              builder.proxyRevalidate(true);
              break;
          case PUBLIC:
              builder.isPublic(true);
              break;
          case PRIVATE:
              builder.isPrivate(true);
              builder.privateHeaders(parser.getValues(this));
            break;
          case STALEWHILEREVALIDATE:
            builder.staleWhileRevalidate(value(parser.getValue(this)));
            break;
          }
        }
    }

    /**
     * Parser for the Cache-Control header
     */
    public static class CacheControlParser 
      implements Iterable<Directive> {

        private static final String REGEX =
            "\\s*([\\w\\-]+)\\s*(=)?\\s*(\\d+|\\\"([^\"\\\\]*(\\\\.[^\"\\\\]*)*)+\\\")?\\s*";

        private static final Pattern pattern = Pattern.compile(REGEX);

        private final HashMap<Directive, String> values = new HashMap<Directive, String>();
        private final HashMap<String,Object> exts = new HashMap<String,Object>();

        public CacheControlParser(String value) {
            Matcher matcher = pattern.matcher(value);
            while (matcher.find()) {
                String d = matcher.group(1);
                Directive directive = Directive.select(d);
                if (directive != Directive.UNKNOWN) {
                    values.put(directive, matcher.group(3));
                } else {
                  String val = matcher.group(3);
                  try {
                    Long l = Long.parseLong(val);
                    exts.put(d, l);
                  } catch (Throwable t) {
                    exts.put(d, val != null ? CharUtils.unquote(val) : "");
                  }
                }
            }
        }

        public Map<String,Object> getExtensions() {
          return exts;
        }
        
        public Map<Directive, String> getValues() {
            return values;
        }

        public String getValue(Directive directive) {
            return values.get(directive);
        }

        public Iterator<Directive> iterator() {
            return values.keySet().iterator();
        }

        public String[] getValues(Directive directive) {
            String value = getValue(directive);
            return value == null ?
                null :
                CharUtils.splitAndTrim(value);
        }

    }
}
