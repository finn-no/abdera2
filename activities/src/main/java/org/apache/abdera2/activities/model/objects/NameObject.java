package org.apache.abdera2.activities.model.objects;

import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.common.iri.IRI;

@org.apache.abdera2.common.anno.Name("name")
public class NameObject 
  extends ASObject {

  private static final long serialVersionUID = 5503270370616022868L;

  public NameObject() {
    super();
  }

  public String getFamilyName() {
    return getProperty("familyName");
  }
  
  public void setFamilyName(String val) {
    setProperty("familyName", val);
  }
  
  public String getFormatted() {
    return getProperty("formatted");
  }
  
  public void setFormatted(String val) {
    setProperty("formatted", val);
  }
  
  public String getGivenName() {
    return getProperty("givenName");
  }
  
  public void setGivenName(String val) {
    setProperty("givenName", val);
  }
  
  public String getHonorificPrefix() {
    return getProperty("honorificPrefix");
  }
  
  public void setHonorificPrefix(String val) {
    setProperty("honorificPrefix", val);
  }
  
  public String getHonorificSuffix() {
    return getProperty("honorificSuffix");
  }
  
  public void setHonorificSuffix(String val) {
    setProperty("honorificSuffix", val);
  }
  
  public String getMiddleName() {
    return getProperty("middleName");
  }
  
  public void setMiddleName(String val) {
    setProperty("middleName", val);
  }
  
  public String getPronunciation() {
    return getProperty("pronunciation");
  }
  
  public void setPronunciation(String val) {
    setProperty("pronunciation", val);
  }
  
  public IRI getPronunciationUrl() {
    return getProperty("pronunciationUrl");
  }
  
  public void setPronunciationUrl(IRI val) {
    setProperty("pronunciationUrl", val);
  }
  
  public void setPronunciationUrl(String val) {
    setPronunciationUrl(new IRI(val));
  }
  
  public static <T extends NameObject>NameObjectGenerator<T> makeName() {
    return new NameObjectGenerator<T>();
  }
  
  @SuppressWarnings("unchecked")
  public static class NameObjectGenerator<T extends NameObject> extends ASObjectGenerator<T> {
    public NameObjectGenerator() {
      super((Class<? extends T>) NameObject.class);
    }
    public NameObjectGenerator(Class<T> _class) {
      super(_class);
    }
    public <X extends NameObjectGenerator<T>>X familyName(String fn) {
      item.setFamilyName(fn);
      return (X)this;
    }
    public <X extends NameObjectGenerator<T>>X formatted(String fn) {
      item.setFormatted(fn);
      return (X)this;
    }
    public <X extends NameObjectGenerator<T>>X givenName(String fn) {
      item.setGivenName(fn);
      return (X)this;
    }
    public <X extends NameObjectGenerator<T>>X honorificPrefix(String fn) {
      item.setHonorificPrefix(fn);
      return (X)this;
    }
    public <X extends NameObjectGenerator<T>>X honorificSuffix(String fn) {
      item.setHonorificSuffix(fn);
      return (X)this;
    }
    public <X extends NameObjectGenerator<T>>X middleName(String fn) {
      item.setMiddleName(fn);
      return (X)this;
    }
    public <X extends NameObjectGenerator<T>>X pronunciation(String fn) {
      item.setPronunciation(fn);
      return (X)this;
    }
    public <X extends NameObjectGenerator<T>>X pronunciationUrl(String fn) {
      item.setPronunciationUrl(fn);
      return (X)this;
    }
    public <X extends NameObjectGenerator<T>>X pronunciationUrl(IRI fn) {
      item.setPronunciationUrl(fn);
      return (X)this;
    }
  }
}
