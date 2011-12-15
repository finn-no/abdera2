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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.activation.MimeType;


import org.apache.abdera2.common.Localizer;
import org.apache.abdera2.common.text.Codec;
import org.apache.abdera2.common.text.UrlEncoding;
import org.apache.abdera2.common.text.CharUtils.Profile;
import org.apache.abdera2.common.http.CacheControl;
import org.apache.abdera2.common.http.EntityTag;
import org.apache.abdera2.common.http.Preference;
import org.apache.abdera2.common.http.WebLink;
import org.apache.abdera2.common.iri.IRI;
import org.apache.abdera2.common.mediatype.MimeTypeHelper;
import org.apache.abdera2.common.misc.MoreFunctions;
import org.joda.time.DateTime;

import com.google.common.collect.Iterables;

@SuppressWarnings("unchecked")
public abstract class AbstractResponseContext extends AbstractResponse implements ResponseContext {

    protected static final String[] EMPTY = new String[0];

    protected int status = 0;
    protected String status_text = null;
    protected boolean binary = false;

    protected final Map<String, Iterable<Object>> headers = 
      new HashMap<String,Iterable<Object>>();

    public <T extends ResponseContext>T setBinary(boolean binary) {
        this.binary = binary;
        return (T)this;
    }

    public boolean isBinary() {
        return binary;
    }

    public <T extends ResponseContext>T setCacheControl(CacheControl cc) {
      return (T)setCacheControl(cc.toString());
    }
    
    public <T extends ResponseContext>T setCacheControl(String cc) {
      return (T)this.setHeader("Cache-Control", cc);
    }
    
    public <T extends ResponseContext>T removeHeader(String name) {
        headers.remove(name);
        return (T)this;
    }

    public <T extends ResponseContext>T setEncodedHeader(String name, String charset, String value) {
        return (T)setHeader(name, Codec.encode(value, charset));
    }

    public <T extends ResponseContext>T setEncodedHeader(String name, String charset, String... vals) {
        String[] evals = 
          MoreFunctions.<String,String>each(
            vals, Codec.encodeStar(charset), String.class);
        return (T)setHeader(name, (Object[])evals);
    }

    public <T extends ResponseContext>T setEscapedHeader(String name, Profile profile, String value) {
        return (T)setHeader(name, UrlEncoding.encode(value, profile));
    }

    public <T extends ResponseContext>T setHeader(String name, Object value) {
        return (T)setHeader(name, new Object[] {value});
    }

    public <T extends ResponseContext>T setHeader(String name, Object... vals) {
        Set<Object> values = new HashSet<Object>();
        values.addAll(Arrays.asList(vals));
        headers.put(name, values);
        return (T)this;
    }

    public <T extends ResponseContext>T addEncodedHeader(String name, String charset, String value) {
        return (T)addHeader(name, Codec.encode(value, charset));
    }

    public <T extends ResponseContext>T addEncodedHeaders(String name, String charset, String... vals) {
      String[] evals = MoreFunctions.<String,String>each(vals, Codec.encodeStar(charset),String.class);
      addHeaders(name,(Object[])evals);
      return (T)this;
    }

    public <T extends ResponseContext>T addHeader(String name, Object value) {
        return (T)addHeaders(name, new Object[] {value});
    }

    public <T extends ResponseContext>T addHeaders(String name, Object... vals) {
        Iterable<Object> values = headers.get(name);
        Set<Object> l = 
          values == null ? 
            new HashSet<Object>() :
            (Set<Object>)values;
        l.addAll(Arrays.asList(vals));
        headers.put(name, l);
        return (T)this;
    }

    public Map<String, Iterable<Object>> getHeaders() {
        return headers;
    }

    public DateTime getDateHeader(String name) {
        Iterable<Object> values = headers.get(name);
        if (values != null) {
            for (Object value : values) {
                if (value instanceof Date)
                    return new DateTime(value);
                else if (value instanceof DateTime)
                    return (DateTime)value;
                else if (value instanceof Long)
                    return new DateTime((Long)value);
                else if (value instanceof String)
                    return new DateTime(value);
                else if (value instanceof Calendar)
                    return new DateTime(value);
            }
        }
        return null;
    }

    private Object getFirst(Iterable<Object> i, Object def) {
      if (i == null) return def;
      return Iterables.getFirst(i, def);
    }
    
    public String getHeader(String name) {
      Iterable<Object> values = headers.get(name);
      Object obj = getFirst(values,null);
      return obj != null ? obj.toString() : null;
    }

    public Iterable<Object> getHeaders(String name) {
        return headers.get(name);
    }

    public Iterable<String> getHeaderNames() {
        return headers.keySet();
    }

    public <T extends ResponseContext>T setAge(long age) {
        return (T)(age <= -1 ? 
          removeHeader("Age") : 
            setHeader("Age", String.valueOf(age)));
    }

    public <T extends ResponseContext>T setContentLanguage(String language) {
        return (T)(language == null ? 
          removeHeader("Content-Language") : 
          setHeader("Content-Language", language));
    }

    public <T extends ResponseContext>T setContentLength(long length) {
        return (T)(length <= -1 ? 
          removeHeader("Content-Length") : 
          setHeader("Content-Length", String.valueOf(length)));
    }

    public <T extends ResponseContext>T setContentLocation(String uri) {
        return (T)(uri == null ? 
          removeHeader("Content-Location") : 
          setHeader("Content-Location", uri));
    }

    public <T extends ResponseContext>T setSlug(String slug) {
        if (slug == null)
            return (T)removeHeader("Slug");
        if (slug.indexOf((char)10) > -1 || 
            slug.indexOf((char)13) > -1)
          throw new IllegalArgumentException(
            Localizer.get("SLUG.BAD.CHARACTERS"));
        return (T)setEscapedHeader(
          "Slug", 
          Profile.PATHNODELIMS, slug);
    }

    public <T extends ResponseContext>T setContentType(String type) {
        return (T)setContentType(type, null);
    }

    public <T extends ResponseContext>T setContentType(String type, String charset) {
        if (type == null)
            return (T)removeHeader("Content-Type");
        MimeType mimeType = MimeTypeHelper.unmodifiableMimeType(type);
        if (charset != null)
            mimeType.setParameter("charset", charset);
        return (T)setHeader("Content-Type", mimeType.toString());
    }

    public <T extends ResponseContext>T setEntityTag(String etag) {
        return (T)(etag != null ? 
          setEntityTag(new EntityTag(etag)) : 
          removeHeader("ETag"));
    }

    public <T extends ResponseContext>T setEntityTag(EntityTag etag) {
        return (T)(etag == null ? 
          removeHeader("ETag") : 
          setHeader("ETag", etag.toString()));
    }

    public <T extends ResponseContext>T setExpires(DateTime date) {
        return (T)(date == null ? 
          removeHeader("Expires") : 
          setHeader("Expires", date));
    }

    public <T extends ResponseContext>T setLastModified(DateTime date) {
        return (T)(date == null ? 
          removeHeader("Last-Modified") : 
          setHeader("Last-Modified", date));
    }

    public <T extends ResponseContext>T setLocation(String uri) {
        return (T)(uri == null ? 
          removeHeader("Location") : 
          setHeader("Location", uri));
    }
    public <T extends ResponseContext>T setLocation(IRI iri) {
      return (T)setLocation(iri == null ? null : iri.toString());
    }

    public int getStatus() {
        return status;
    }

    public <T extends ResponseContext>T setStatus(int status) {
        this.status = status;
        return (T)this;
    }

    public String getStatusText() {
        return status_text;
    }

    public <T extends ResponseContext>T setStatusText(String text) {
        this.status_text = text;
        return (T)this;
    }

    public <T extends ResponseContext>T setAllow(String method) {
        return (T)setHeader("Allow", method);
    }

    public <T extends ResponseContext>T setAllow(String... methods) {
        StringBuilder buf = new StringBuilder();
        for (String method : methods) {
            if (buf.length() > 0)
                buf.append(", ");
            buf.append(method);
        }
        return (T)setAllow(buf.toString());
    }

    public <T extends ResponseContext>T setWebLinks(WebLink link, WebLink... links) {
      return (T)setHeader("Link", WebLink.toString(link,links));
    }
    
    public <T extends ResponseContext>T setPrefer(Preference pref, Preference... prefs) {
      return (T)setHeader("Prefer", Preference.toString(pref,prefs));
    }

}
