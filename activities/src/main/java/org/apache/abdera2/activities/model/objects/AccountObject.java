package org.apache.abdera2.activities.model.objects;

import java.util.Map;

import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.common.anno.Name;

public class AccountObject 
  extends ServiceObject {


  public AccountObject(Map<String,Object> map) {
    super(map,AccountBuilder.class,AccountObject.class);
  }
  
  public <X extends AccountObject, M extends Builder<X,M>>AccountObject(Map<String,Object> map, Class<M> _class, Class<X> _obj) {
    super(map,_class,_obj);
  }

  public String getDomain() {
    return getProperty("domain");
  }
  
  public String getUsername() {
    return getProperty("username");
  }
  
  public String getUserId() {
    return getProperty("userId");
  }
  
  public static AccountBuilder makeAccount() {
    return new AccountBuilder("account");
  }
  
  public static AccountObject makeAccount(String domain, String username, String userid) {
    return makeAccount()
      .domain(domain)
      .username(username)
      .userId(userid)
      .get();
  }
  
  @Name("account")
  public static class AccountBuilder extends Builder<AccountObject,AccountBuilder> {
    public AccountBuilder() {
      super(AccountObject.class,AccountBuilder.class);
    }
    public AccountBuilder(Map<String, Object> map) {
      super(map,AccountObject.class,AccountBuilder.class);
    }
    public AccountBuilder(String objectType) {
      super(objectType, AccountObject.class,AccountBuilder.class);
    }
  }
  
  @SuppressWarnings("unchecked")
  public static abstract class Builder<X extends AccountObject,M extends Builder<X,M>> 
  extends ASObject.Builder<X,M> {
    public Builder(Class<X>_class,Class<M>_builder) {
      super(_class,_builder);
    }
    public Builder(String objectType,Class<X>_class,Class<M>_builder) {
      super(objectType,_class,_builder);
    }
    public Builder(Map<String,Object> map,Class<X>_class,Class<M>_builder) {
      super(map,_class,_builder);
    }
    public M domain(String domain) {
      set("domain",domain);
      return (M)this;
    }
    public M username(String username) {
      set("username",username);
      return (M)this;
    }
    public M userId(String userid) {
      set("userId",userid);
      return (M)this;
    }    
  }
}
