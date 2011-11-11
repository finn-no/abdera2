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
package org.apache.abdera2.activities.io.gson;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

import org.apache.abdera2.activities.model.ASBase;
import org.apache.abdera2.common.misc.ExceptionHelper;

import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

@SuppressWarnings({"rawtypes","unchecked"})
@AdaptedType(Multimap.class)
class MultimapAdapter
  implements GsonTypeAdapter<Multimap> {
  
  public Class<Multimap> getAdaptedClass() {
    return Multimap.class;
  }

  public JsonElement serialize(
    Multimap src, 
    Type typeOfSrc,
    JsonSerializationContext context) {
      return context.serialize(src.asMap(), Map.class);
  }

  public Multimap deserialize(
    JsonElement json, 
    Type typeOfT,
    JsonDeserializationContext context) 
      throws JsonParseException {
    Multimap mm = create(typeOfT);
    JsonObject obj = json.getAsJsonObject();
    for (Map.Entry<String,JsonElement> entry : obj.entrySet()) {
      String key = entry.getKey();
      JsonElement val = entry.getValue();
      if (val.isJsonArray()) {
        JsonArray array = val.getAsJsonArray();
        for (JsonElement el : array) {
          if (el.isJsonArray()) {       
          } else if (el.isJsonObject()) {
            mm.put(key, context.deserialize(el, ASBase.class));
          } else if (el.isJsonNull()) {
            mm.put(key, null);
          } else if (el.isJsonPrimitive()) {
            JsonPrimitive jp = el.getAsJsonPrimitive();
            if (jp.isBoolean()) {
              mm.put(key, jp.getAsBoolean());
            } else if (jp.isNumber()) {
              mm.put(key, jp.getAsNumber());
            } else if (jp.isString()) {
              mm.put(key, jp.getAsString());
            }
          }
        }
      }
    }
    return mm;
  }  
  
  private static Multimap create(Type typeOfT) {
    try {
      Class<Multimap> _class = (Class<Multimap>)typeOfT;
      Method method = _class.getMethod("create");
      return (Multimap) method.invoke(_class);
    } catch (Throwable t) {
      throw ExceptionHelper.propogate(t);
    }
  }
}
