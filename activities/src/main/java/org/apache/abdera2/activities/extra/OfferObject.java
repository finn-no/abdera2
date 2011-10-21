package org.apache.abdera2.activities.extra;

import org.apache.abdera2.activities.io.gson.Properties;
import org.apache.abdera2.activities.io.gson.Property;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.common.anno.Name;
import org.joda.time.DateTime;

/**
 * A simple "objectType":"offer" object that serves primarily as an 
 * example of creating new ASObject types.
 */
@Name("offer")
@Properties({
  @Property(name="validFrom",to=DateTime.class),
  @Property(name="validUntil",to=DateTime.class)
})
@SuppressWarnings("unchecked")
public class OfferObject extends ASObject {

  private static final long serialVersionUID = 8693274483912587801L;

  public OfferObject() {}
  
  public OfferObject(String displayName) {
    setDisplayName(displayName);
  }
  
  public <T extends ASObject>T getAvailability() {
    return (T)getProperty("availability");
  }
  
  public void setAvailability(ASObject availability) {
    setProperty("availability", availability);
  }
  
  public <T extends ASObject>T getCondition() {
    return (T)getProperty("condition");
  }
  
  public void setCondition(ASObject condition) {
    setProperty("condition", condition);
  }
  
  public <T extends ASObject>T getItem() {
    return (T)getProperty("item");
  }
  
  public void setItem(ASObject item) {
    setProperty("item", item);
  }
  
  public String getPrice() {
    return getProperty("price");
  }
  
  public void setPrice(String price) {
    setProperty("price", price);
  }
  
  public String getCurrency() {
    return getProperty("currency");
  }
  
  public void setCurrency(String currency) {
    setProperty("currency",currency);
  }
  
  public DateTime getValidUntil() {
    return getProperty("validUntil");
  }
  
  public void setValidUntil(DateTime date) {
    setProperty("validUntil", date);
  }
  
  public DateTime getValidFrom() {
    return getProperty("validFrom");
  }
  
  public void setValidFrom(DateTime date) {
    setProperty("validFrom", date);
  }

  public <T extends ASObject>T getRestriction() {
    return (T)getProperty("restriction");
  }
  
  public void setRestriction(ASObject restriction) {
    setProperty("restriction", restriction);
  }
  
  public static <T extends OfferObject>OfferObjectGenerator<T> makeOffer() {
    return new OfferObjectGenerator<T>();
  }
  
  public static class OfferObjectGenerator<T extends OfferObject> extends ASObjectGenerator<T> {
    public OfferObjectGenerator() {
      super((Class<T>)OfferObject.class);
    }
    public OfferObjectGenerator(Class<T> _class) {
      super(_class);
    }
    public <X extends OfferObjectGenerator<T>>X availability(ASObject obj) {
      item.setAvailability(obj);
      return (X)this;
    }
    public <X extends OfferObjectGenerator<T>>X condition(ASObject obj) {
      item.setCondition(obj);
      return (X)this;
    }
    public <X extends OfferObjectGenerator<T>>X currency(String obj) {
      item.setCurrency(obj);
      return (X)this;
    }
    public <X extends OfferObjectGenerator<T>>X item(ASObject obj) {
      item.setItem(obj);
      return (X)this;
    }
    public <X extends OfferObjectGenerator<T>>X price(String obj) {
      item.setPrice(obj);
      return (X)this;
    }
    public <X extends OfferObjectGenerator<T>>X restriction(ASObject obj) {
      item.setRestriction(obj);
      return (X)this;
    }
    public <X extends OfferObjectGenerator<T>>X validFrom(DateTime obj) {
      item.setValidFrom(obj);
      return (X)this;
    }
    public <X extends OfferObjectGenerator<T>>X validUntil(DateTime obj) {
      item.setValidUntil(obj);
      return (X)this;
    }
  }
}
