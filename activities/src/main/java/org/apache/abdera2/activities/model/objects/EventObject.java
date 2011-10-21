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
import org.apache.abdera2.activities.model.Collection;
import org.apache.abdera2.common.anno.Name;
import org.joda.time.DateTime;

@Name("event")
public class EventObject 
  extends ASObject {

  private static final long serialVersionUID = -7607925831414516450L;
  public static final String ATTENDING = "attending";
  public static final String ENDTIME = "endTime";
  public static final String MAYBEATTENDING = "maybeAttending";
  public static final String NOTATTENDING = "notAttending";
  public static final String STARTTIME = "startTime";
  
  public EventObject() {
  }
  
  public EventObject(String displayName) {
    setDisplayName(displayName);
  }
  
  protected Collection<ASObject> getcol(String name, boolean create) {
    Collection<ASObject> col = getProperty(name);
    if (col == null && create) {
      col = new Collection<ASObject>();
      setProperty(name,col);
    }
    return col;
  }
  
  public Collection<ASObject> getAttending() {
    return getcol(ATTENDING,false);
  }
  
  public Collection<ASObject> getAttending(boolean create) {
    return getcol(ATTENDING,create);
  }
  
  public void setAttending(Collection<ASObject> attending) {
    setProperty(ATTENDING, attending);
  }
  
  public DateTime getEndTime() {
    return getProperty(ENDTIME);
  }
  
  public void setEndTime(DateTime endTime) {
    setProperty(ENDTIME, endTime);
  }
  
  public Collection<ASObject> getMaybeAttending() {
    return getcol(MAYBEATTENDING,false);
  }
  
  public Collection<ASObject> getMaybeAttending(boolean create) {
    return getcol(MAYBEATTENDING,create);
  }
  
  public void setMaybeAttending(Collection<ASObject> maybeAttending) {
    setProperty(MAYBEATTENDING, maybeAttending);
  }
  
  public Collection<ASObject> getNotAttending() {
    return getcol(NOTATTENDING,false);
  }
  
  public Collection<ASObject> getNotAttending(boolean create) {
    return getcol(NOTATTENDING,create);
  }
  
  public void setNotAttending(Collection<ASObject> notAttending) {
    setProperty(NOTATTENDING, notAttending);
  }
  
  public DateTime getStartTime() {
    return getProperty(STARTTIME);
  }
  
  public void setStartTime(DateTime startTime) {
    setProperty(STARTTIME, startTime);
  }

  public static <T extends EventObject>EventObjectGenerator<T> makeEvent() {
    return new EventObjectGenerator<T>();
  }
  
  @SuppressWarnings("unchecked")
  public static class EventObjectGenerator<T extends EventObject> extends ASObjectGenerator<T> {

    public EventObjectGenerator() {
      super((Class<? extends T>) EventObject.class);
    }
    
    public EventObjectGenerator(Class<? extends T> _class) {
      super(_class);
    }
    
    public <X extends EventObjectGenerator<T>>X attending(Collection<ASObject> col) {
      item.setAttending(col);
      return (X)this;
    }
    
    public <X extends EventObjectGenerator<T>>X endTime(DateTime dt) {
      item.setEndTime(dt);
      return (X)this;
    }
    
    public <X extends EventObjectGenerator<T>>X maybeAttending(Collection<ASObject> col) {
      item.setMaybeAttending(col);
      return (X)this;
    }
    
    public <X extends EventObjectGenerator<T>>X notAttending(Collection<ASObject> col) {
      item.setNotAttending(col);
      return (X)this;
    }
    
    public <X extends EventObjectGenerator<T>>X startTime(DateTime t) {
      item.setStartTime(t);
      return (X)this;
    }
  }
}
