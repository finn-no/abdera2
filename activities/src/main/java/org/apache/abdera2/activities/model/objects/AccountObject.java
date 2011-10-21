package org.apache.abdera2.activities.model.objects;

import org.apache.abdera2.common.anno.Name;

@Name("account")
public class AccountObject 
  extends ServiceObject {

  private static final long serialVersionUID = 1058258637558799759L;

  public AccountObject() {
    super();
  }

  public AccountObject(String displayName) {
    super(displayName);
  }

  public String getDomain() {
    return getProperty("domain");
  }
  
  public void setDomain(String domain) {
    setProperty("domain", domain);
  }
  
  public String getUsername() {
    return getProperty("username");
  }
  
  public void setUsername(String username) {
    setProperty("username", username);
  }
  
  public String getUserId() {
    return getProperty("userId");
  }
  
  public void setUserId(String userid) {
    setProperty("userId", userid);
  }
  
  public static <T extends AccountObject>AccountGenerator<T> makeAccount() {
    return new AccountGenerator<T>();
  }
  
  @SuppressWarnings("unchecked")
  public static class AccountGenerator<T extends AccountObject> extends ServiceObjectGenerator<T> {
    public AccountGenerator() {
      super((Class<? extends T>) AccountObject.class);
    }
    public AccountGenerator(Class<T> _class) {
      super(_class);
    }
    public <X extends AccountGenerator<T>>X domain(String domain) {
      item.setDomain(domain);
      return (X)this;
    }
    public <X extends AccountGenerator<T>>X username(String username) {
      item.setUsername(username);
      return (X)this;
    }
    public <X extends AccountGenerator<T>>X userId(String userid) {
      item.setUserId(userid);
      return (X)this;
    }
  }
}
