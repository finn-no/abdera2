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

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.common.anno.Name;
import org.apache.abdera2.common.iri.IRI;

@Name("file")
public class FileObject 
  extends ASObject {

  private static final long serialVersionUID = -5449136149228249949L;
  public static final String FILEURL = "fileUrl";
  
  public FileObject() {
  }
  
  public FileObject(String displayName) {
    setDisplayName(displayName);
  }
  
  public IRI getFileUrl() {
    return getProperty(FILEURL);
  }
  
  public void setFileUrl(IRI fileUrl) {
    setProperty(FILEURL, fileUrl);
  }
  
  public void setFileUrl(String fileUrl) {
    setFileUrl(new IRI(fileUrl));
  }
  
  public MimeType getMimeType() {
    return getProperty("mimeType");
  }
  
  public void setMimeType(MimeType mimeType) {
    setProperty("mimeType", mimeType);
  }
  
  public void setMimeType(String mimeType) {
    try {
      setProperty("mimeType", new MimeType(mimeType));
    } catch (MimeTypeParseException e) {
      throw new org.apache.abdera2.common.mediatype.MimeTypeParseException(e);
    }
  }
  
  public static <T extends FileObject>FileObjectGenerator<T> makeFile() {
    return new FileObjectGenerator<T>();
  }
  
  @SuppressWarnings("unchecked")
  public static class FileObjectGenerator<T extends FileObject> extends ASObjectGenerator<T> {
    public FileObjectGenerator() {
      super((Class<? extends T>) FileObject.class);
    }
    public FileObjectGenerator(Class<T> _class) {
      super(_class);
    }
    public <X extends FileObjectGenerator<T>>X fileUrl(IRI iri) {
      item.setFileUrl(iri);
      return (X)this;
    }
    public <X extends FileObjectGenerator<T>>X fileUrl(String uri) {
      item.setFileUrl(uri);
      return (X)this;
    }
    public <X extends FileObjectGenerator<T>>X mimeType(MimeType mimeType) {
      item.setMimeType(mimeType);
      return (X)this;
    }
    public <X extends FileObjectGenerator<T>>X mimeType(String mimeType) {
      item.setMimeType(mimeType);
      return (X)this;
    }
  }
}
