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
package org.apache.abdera2.activities.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.activation.MimeType;

import org.apache.abdera2.activities.model.ASBase;
import org.apache.abdera2.common.mediatype.MimeTypeHelper;
import org.apache.abdera2.common.protocol.AbstractResponseContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ActivitiesResponseContext<T extends ASBase>    
  extends AbstractResponseContext  {
  
  private final static Log log = LogFactory.getLog(ActivitiesResponseContext.class);
  
  private final ASBase.Builder<T,?> builder;
  private final boolean chunked;
  
  public ActivitiesResponseContext(ASBase.Builder<T,?> builder) {
    this(builder, true);
  }
  
  public ActivitiesResponseContext(ASBase.Builder<T,?> builder, boolean chunked) {
    this.builder = builder;
    setStatus(200);
    setStatusText("OK");
    this.chunked = chunked;
    try {
        MimeType type = getContentType();
        String charset = type.getParameter("charset");
        if (charset == null)
          type.setParameter("charset", "UTF-8");
        setContentType(type.toString());
    } catch (Exception e) {
      // it's ok to ignore this exception
      log.debug("Error setting charset parameter",e);
    }
    log.debug(String.format("Content-Type: %s", getContentType()));
  }
   
  @SuppressWarnings("unchecked")
  public <X extends ASBase.Builder<T,?>>X getBuilder() {
    return (X)builder;
  }
  
  public T getEntity() {
    return builder.get();
  }

  public boolean hasEntity() {
    return (builder != null);
  }

  public void writeTo(java.io.Writer javaWriter) throws IOException {
    log.debug("Writing...");
    if (hasEntity())
      getEntity().writeTo(javaWriter);
  }

  public void writeTo(OutputStream out) throws IOException {
    log.debug("Writing...");
    if (hasEntity())
      getEntity().writeTo(out);
  }

  public MimeType getContentType() {
    return MimeTypeHelper.unmodifiableMimeType("application/json");
  }

  public long getContentLength() {
    long len = super.getContentLength();
    if (hasEntity() && len == -1 && !chunked) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            getEntity().writeTo(out);
            len = out.size();
            super.setContentLength(len);
        } catch (Exception e) {
        }
    }
    log.debug(String.format("Content-Length: %d", len));
    return len;
  }

}
