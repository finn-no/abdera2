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

import java.util.HashSet;
import java.util.Set;

import org.apache.abdera2.activities.io.gson.Properties;
import org.apache.abdera2.activities.io.gson.Property;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.common.anno.Name;
import org.apache.abdera2.common.iri.IRI;

@Name("person")
@Properties({
  @Property(name="profileUrl",to=IRI.class),
  @Property(name="thumbnailUrl",to=IRI.class),
  @Property(name="urls",to=IRI.class),
  @Property(name="name",to=NameObject.class),
  @Property(name="preferredName",to=NameObject.class),
  @Property(name="nativeName",to=NameObject.class),
  @Property(name="alternateNames",to=NameObject.class),
  @Property(name="accounts",to=AccountObject.class),
  @Property(name="addresses",to=Address.class),
  @Property(name="organizations",to=OrganizationObject.class)
})
public class PersonObject 
  extends ASObject {

  private static final long serialVersionUID = 5240611684336430061L;
  
  public PersonObject() { }
  
  public PersonObject(String displayName) {
    setDisplayName(displayName);
  }

  public String getAboutMe() {
    return getProperty("aboutMe");
  }
  
  public void setAboutMe(String val) {
    setProperty("aboutMe", val);
  }
  
  public String getContactPreference() {
    return getProperty("contactPreference");
  }
  
  public void setContactPreference(String val) {
    setProperty("contactPreference", val);
  }
  
  public String getDn() {
    return getProperty("dn");
  }
  
  public void setDn(String val) {
    setProperty("dn", val);
  }
  
  public String getPreferredUsername() {
    return getProperty("preferredUsername");
  }
  
  public void setPreferredUsername(String val) {
    setProperty("preferredUsername", val);
  }
  
  public IRI getProfileUrl() {
    return getProperty("profileUrl");
  }
  
  public void setProfileUrl(IRI iri) {
    setProperty("profileUrl", iri);
  }
  
  public void setProfileUrl(String iri) {
    setProfileUrl(new IRI(iri));
  }
  
  public String getStatus() {
    return getProperty("status");
  }
  
  public void setStatus(String status) {
    setProperty("status", status);
  }
  
  public IRI getThumbnailUrl() {
    return getProperty("thumbnailUrl");
  }
  
  public void setThumbnailUrl(IRI iri) {
    setProperty("thumbnailUrl", iri);
  }
  
  public void setThumbnailUrl(String iri) {
    setThumbnailUrl(new IRI(iri));
  }
  
  public String getUtcOffset() {
    return getProperty("utcOffset");
  }
  
  public void setUtcOffset(String val) {
    setProperty("utfOffset",val);
  }
  
  public NameObject getName() {
    return getProperty("name");
  }
  
  public void setName(NameObject name) {
    setProperty("name", name);
  }
  
  public NameObject getNativeName() {
    return getProperty("nativeName");
  }
  
  public void setNativeName(NameObject name) {
    setProperty("nativeName", name);
  }
  
  public NameObject getPreferredName() {
    return getProperty("preferredName");
  }
  
  public void setPreferredName(NameObject name) {
    setProperty("preferredName", name);
  }
  
  public Iterable<NameObject> getAlternateNames() {
    return getProperty("alternateNames");
  }
  
  public void setAlternateNames(Set<NameObject> set) {
    setProperty("alternateNames", set);
  }
  
  public void addAlternateName(NameObject name) {
    Set<NameObject> list = getProperty("alternateNames");
    if (list == null) {
      list = new HashSet<NameObject>();
      setProperty("alternateNames",list);
    }
    list.add(name);
  }
  
  public Iterable<String> getEmails() {
    return getProperty("emails");
  }
  
  public void setEmails(Set<String> set) {
    setProperty("emails", set);
  }
  
  public void addEmail(String email) {
    Set<String> list = getProperty("email");
    if (list == null) {
      list = new HashSet<String>();
      setProperty("emails",list);
    }
    list.add(email);
  }
  
  public Iterable<String> getIms() {
    return getProperty("ims");
  }
  
  public void setIms(Set<String> set) {
    setProperty("ims", set);
  }
  
  public void addIm(String im) {
    Set<String> list = getProperty("ims");
    if (list == null) {
      list = new HashSet<String>();
      setProperty("ims",list);
    }
    list.add(im);
  }
  
  public Iterable<String> getPhoneNumbers() {
    return getProperty("phoneNumbers");
  }
  
  public void setPhoneNumbers(Set<String> set) {
    setProperty("phoneNumbers", set);
  }
  
  public void addPhoneNumber(String phoneNumber) {
    Set<String> list = getProperty("phoneNumbers");
    if (list == null) {
      list = new HashSet<String>();
      setProperty("phoneNumbers",list);
    }
    list.add(phoneNumber);
  }
  
  public Iterable<IRI> getUrls() {
    return getProperty("urls");
  }
  
  public void setUrls(Set<IRI> set) {
    setProperty("urls", set);
  }
  
  public void addUrl(IRI url) {
    Set<IRI> list = getProperty("urls");
    if (list == null) {
      list = new HashSet<IRI>();
      setProperty("urls",list);
    }
    list.add(url);
  }
  
  public void addUrl(String url) {
    addUrl(new IRI(url));
  }
  
  public Iterable<Address> getAddresses() {
    return getProperty("addresses");
  }
  
  public void setAddresses(Set<Address> set) {
    setProperty("addresses", set);
  }
  
  public void addAddress(Address address) {
    Set<Address> list = getProperty("addresses");
    if (list == null) {
      list = new HashSet<Address>();
      setProperty("addresses",list);
    }
    list.add(address);
  }
  
  public Iterable<AccountObject> getAccounts() {
    return getProperty("accounts");
  }
  
  public void setAccounts(Set<AccountObject> set) {
    setProperty("accounts", set);
  }
  
  public void addAccount(AccountObject service) {
    Set<ServiceObject> list = getProperty("accounts");
    if (list == null) {
      list = new HashSet<ServiceObject>();
      setProperty("accounts",list);
    }
    list.add(service);
  }
  
  public Iterable<OrganizationObject> getOrganizations() {
    return getProperty("organizations");
  }
  
  public void setOrganizations(Set<OrganizationObject> set) {
    setProperty("organizations", set);
  }
  
  public void addOrganization(OrganizationObject org) {
    Set<OrganizationObject> list = getProperty("organizations");
    if (list == null) {
      list = new HashSet<OrganizationObject>();
      setProperty("organizations",list);
    }
    list.add(org);
  }
  
  @SuppressWarnings("rawtypes")
  public static <T extends PersonObject>PersonObjectGenerator makePerson() {
    return new PersonObjectGenerator<T>();
  }
  
  @SuppressWarnings("unchecked")
  public static class PersonObjectGenerator<T extends PersonObject> extends ASObjectGenerator<T> {
    public PersonObjectGenerator() {
      super((Class<T>)PersonObject.class);
    }
    public PersonObjectGenerator(Class<? extends T> _class) {
      super(_class);
    }
    public <X extends PersonObjectGenerator<T>>X account(AccountObject object) {
      item.addAccount(object);
      return (X)this;
    }
    public <X extends PersonObjectGenerator<T>>X address(Address object) {
      item.addAddress(object);
      return (X)this;
    }
    public <X extends PersonObjectGenerator<T>>X alternateName(NameObject name) {
      item.addAlternateName(name);
      return (X)this;
    }
    public <X extends PersonObjectGenerator<T>>X email(String email) {
      item.addEmail(email);
      return (X)this;
    }
    public <X extends PersonObjectGenerator<T>>X im(String im) {
      item.addIm(im);
      return (X)this;
    }
    public <X extends PersonObjectGenerator<T>>X organization(OrganizationObject object) {
      item.addOrganization(object);
      return (X)this;
    }
    public <X extends PersonObjectGenerator<T>>X phoneNumber(String pn) {
      item.addPhoneNumber(pn);
      return (X)this;
    }
    public <X extends PersonObjectGenerator<T>>X urls(String url) {
      item.addUrl(url);
      return (X)this;
    }
    public <X extends PersonObjectGenerator<T>>X urls(IRI url) {
      item.addUrl(url);
      return (X)this;
    }
    public <X extends PersonObjectGenerator<T>>X aboutMe(String val) {
      item.setAboutMe(val);
      return (X)this;
    }
    public <X extends PersonObjectGenerator<T>>X contactPreference(String val) {
      item.setContactPreference(val);
      return (X)this;
    }
    public <X extends PersonObjectGenerator<T>>X dn(String val) {
      item.setDn(val);
      return (X)this;
    }
    public <X extends PersonObjectGenerator<T>>X name(NameObject val) {
      item.setName(val);
      return (X)this;
    }
    public <X extends PersonObjectGenerator<T>>X nativeName(NameObject val) {
      item.setNativeName(val);
      return (X)this;
    }
    public <X extends PersonObjectGenerator<T>>X preferredName(NameObject val) {
      item.setPreferredName(val);
      return (X)this;
    }
    public <X extends PersonObjectGenerator<T>>X profileUrl(String val) {
      item.setProfileUrl(val);
      return (X)this;
    }
    public <X extends PersonObjectGenerator<T>>X profileUrl(IRI val) {
      item.setProfileUrl(val);
      return (X)this;
    }
    public <X extends PersonObjectGenerator<T>>X status(String val) {
      item.setStatus(val);
      return (X)this;
    }
    public <X extends PersonObjectGenerator<T>>X thumbnailUrl(String val) {
      item.setThumbnailUrl(val);
      return (X)this;
    }
    public <X extends PersonObjectGenerator<T>>X thumbnailUrl(IRI val) {
      item.setThumbnailUrl(val);
      return (X)this;
    }
    public <X extends PersonObjectGenerator<T>>X utcOffset(String val) {
      item.setUtcOffset(val);
      return (X)this;
    }
  }
}
