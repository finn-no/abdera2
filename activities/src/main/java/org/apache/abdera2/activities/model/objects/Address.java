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
import org.apache.abdera2.common.anno.Name;

@Name("address")
public class Address extends ASObject {

  private static final long serialVersionUID = -6352046844998504401L;
  public static final String FORMATTED = "formatted";
  public static final String STREETADDRESS = "streetAddress";
  public static final String LOCALITY = "locality";
  public static final String REGION = "region";
  public static final String POSTALCODE = "postalCode";
  public static final String COUNTRY = "country";
  
  public String getFormatted() {
    return getProperty(FORMATTED);
  }
  
  public void setFormatted(String formatted) {
    setProperty(FORMATTED, formatted);
  }
  
  public String getStreetAddress() {
    return getProperty(STREETADDRESS);
  }
  
  public void setStreetAddress(String streetAddress) {
    setProperty(STREETADDRESS, streetAddress);
  }
  
  public String getLocality() {
    return getProperty(LOCALITY);
  }
  
  public void setLocality(String locality) {
    setProperty(LOCALITY, locality);
  }
  
  public String getRegion() {
    return getProperty(REGION);
  }
  
  public void setRegion(String region) {
    setProperty(REGION, region);
  }
  
  public String getPostalCode() {
    return getProperty(POSTALCODE);
  }
  
  public void setPostalCode(String postalCode) {
    setProperty(POSTALCODE, postalCode);
  }
  
  public String getCountry() {
    return getProperty(COUNTRY);
  }
  
  public void setCountry(String country) {
    setProperty(COUNTRY, country);
  }
  
  public String toString() {
    StringBuilder buf = new StringBuilder();
    if (getFormatted() != null) {
      buf.append(getFormatted());
    } else {
      buf.append("an address");
    }
    return buf.toString();
  }
  
  public static <T extends Address>AddressGenerator<T> makeAddress() {
    return new AddressGenerator<T>();
  }
  
  @SuppressWarnings("unchecked")
  public static class AddressGenerator<T extends Address> extends ASObjectGenerator<T> {
    public AddressGenerator() {
      super((Class<? extends T>) Address.class);
    }
    public AddressGenerator(Class<T> _class) {
      super(_class);
    }
    public <X extends AddressGenerator<T>>X country(String country) {
      item.setCountry(country);
      return (X)this;
    }
    public <X extends AddressGenerator<T>>X formatted(String formatted) {
      item.setFormatted(formatted);
      return (X)this;
    }
    public <X extends AddressGenerator<T>>X locality(String locality) {
      item.setLocality(locality);
      return (X)this;
    }
    public <X extends AddressGenerator<T>>X postalCode(String postalCode) {
      item.setPostalCode(postalCode);
      return (X)this;
    }
    public <X extends AddressGenerator<T>>X region(String region) {
      item.setRegion(region);
      return (X)this;
    }
    public <X extends AddressGenerator<T>>X streetAddress(String address) {
      item.setStreetAddress(address);
      return (X)this;
    }
  }
}
