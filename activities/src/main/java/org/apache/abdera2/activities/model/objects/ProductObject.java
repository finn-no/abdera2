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

@Name("product")
public class ProductObject 
  extends ASObject {

  private static final long serialVersionUID = 8485599939786580223L;
  public static final String FULLIMAGE = "fullImage";
  
  public ProductObject() {}
  
  public ProductObject(String displayName) {
    setDisplayName(displayName);
  }
  
  public MediaLink getFullImage() {
    return getProperty(FULLIMAGE);
  }
  
  public void setFullImage(MediaLink fullImage) {
    setProperty(FULLIMAGE, fullImage);
  }

  public static <T extends ProductObject>ProductObjectGenerator<T> makeProduct() {
    return new ProductObjectGenerator<T>();
  }
  
  @SuppressWarnings("unchecked")
  public static class ProductObjectGenerator<T extends ProductObject> extends ASObjectGenerator<T> {
    public ProductObjectGenerator() {
      super((Class<? extends T>) ProductObject.class);
    }
    public ProductObjectGenerator(Class<T> _class) {
      super(_class);
    }
    public <X extends ProductObjectGenerator<T>>X fullImage(MediaLink fullImage) {
      item.setFullImage(fullImage);
      return (X)this;
    }
  }
}
