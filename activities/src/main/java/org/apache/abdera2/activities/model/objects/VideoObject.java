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
package org.apache.abdera2.activities.model.objects;

import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.MediaLink;
import org.apache.abdera2.common.anno.Name;
@Name("video")
public class VideoObject 
  extends ASObject {

  private static final long serialVersionUID = 5394642824444623984L;
  public static final String EMBEDCODE = "embedCode";
  public static final String STREAM = "stream";
  
  public VideoObject() {}
  
  public VideoObject(String displayName) {
    setDisplayName(displayName);
  }
  
  public String getEmbedCode() {
    return getProperty(EMBEDCODE);
  }
  
  public void setEmbedCode(String embedCode) {
    setProperty(EMBEDCODE, embedCode);
  }
  
  public MediaLink getStream() {
    return getProperty(STREAM);
  }
  
  public void setStream(MediaLink stream) {
    setProperty(STREAM, stream);
  }
  
  public static <T extends VideoObject>VideoObjectGenerator<T> makeVideo() {
    return new VideoObjectGenerator<T>();
  }
  
  @SuppressWarnings("unchecked")
  public static class VideoObjectGenerator<T extends VideoObject> extends ASObjectGenerator<T> {
    public VideoObjectGenerator() {
      super((Class<? extends T>) VideoObject.class);
    }
    public VideoObjectGenerator(Class<T> _class) {
      super(_class);
    }
    public <X extends VideoObjectGenerator<T>>X embedCode(String code) {
      item.setEmbedCode(code);
      return (X)this;
    }
    public <X extends VideoObjectGenerator<T>>X stream(MediaLink stream) {
      item.setStream(stream);
      return (X)this;
    }
  }
}
