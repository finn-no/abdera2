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
package org.apache.abdera2.activities.model;

import org.apache.abdera2.common.iri.IRI;

/**
 * Represents the Activity Streams Media Link construct.
 */
public class MediaLink extends ASBase {

  private static final long serialVersionUID = -2003166656259290419L;
  public static final String DURATION = "duration";
  public static final String HEIGHT = "height";
  public static final String WIDTH = "width";
  public static final String URL = "url";
  
  public MediaLink() {}
  
  public MediaLink(IRI url) {
    setUrl(url);
  }
  
  public MediaLink(
    IRI url, 
    int height, 
    int width, 
    int duration) {
      setUrl(url);
      setHeight(height);
      setWidth(width);
      setDuration(duration);
  }
  
  public int getDuration() {
    return (Integer)getProperty(DURATION);
  }
  
  public void setDuration(int duration) {
    setProperty(DURATION, duration < 0 ? null : duration);
  }
  
  public int getHeight() {
    return (Integer)getProperty(HEIGHT);
  }
  
  public void setHeight(int height) {
    setProperty(HEIGHT, height < 0 ? null : height);
  }
  
  public int getWidth() {
    return (Integer)getProperty(WIDTH);
  }
  
  public void setWidth(int width) {
    setProperty(WIDTH, width < 0 ? null : width);
  }
  
  public IRI getUrl() {
    return getProperty(URL);
  }
  
  public void setUrl(IRI url) {
    setProperty(URL, url);
  }
  
  public void setUrl(String url) {
    setUrl(new IRI(url));
  }
  
  public static MediaLinkGenerator makeMediaLink() {
    return new MediaLinkGenerator();
  }
  
  public static class MediaLinkGenerator extends Generator<MediaLink> {

    public MediaLinkGenerator() {
      super(MediaLink.class);
    }
    
    public MediaLinkGenerator duration(int duration) {
      item.setDuration(duration);
      return this;
    }
    
    public MediaLinkGenerator height(int height) {
      item.setHeight(height);
      return this;
    }
    
    public MediaLinkGenerator width(int width) {
      item.setWidth(width);
      return this;
    }
    
    public MediaLinkGenerator url(IRI iri) {
      item.setUrl(iri);
      return this;
    }
    
    public MediaLinkGenerator url(String uri) {
      item.setUrl(new IRI(uri));
      return this;
    }
  }
  
}
