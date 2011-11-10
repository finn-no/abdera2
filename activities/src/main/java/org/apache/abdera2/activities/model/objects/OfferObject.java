package org.apache.abdera2.activities.model.objects;

import java.util.Map;

import org.apache.abdera2.activities.io.gson.Properties;
import org.apache.abdera2.activities.io.gson.Property;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.common.anno.Name;
import org.joda.time.DateTime;

/**
 * A simple "objectType":"offer" object that serves primarily as an 
 * example of creating new ASObject types.
 */
@SuppressWarnings("unchecked")
public class OfferObject extends ASObject {


  public OfferObject(Map<String,Object> map) {
    super(map,OfferBuilder.class,OfferObject.class);
  }
  
  public <X extends OfferObject, M extends Builder<X,M>>OfferObject(Map<String,Object> map,Class<M>_class,Class<X>_obj) {
    super(map,_class,_obj);
  }
  
  public <T extends ASObject>T getAvailability() {
    return (T)getProperty("availability");
  }
  
  public <T extends ASObject>T getCondition() {
    return (T)getProperty("condition");
  }
  
  public <T extends ASObject>T getItem() {
    return (T)getProperty("item");
  }
  
  public String getPrice() {
    return getProperty("price");
  }
  
  public String getCurrency() {
    return getProperty("currency");
  }
  
  public DateTime getValidUntil() {
    return getProperty("validUntil");
  }
  
  public DateTime getValidFrom() {
    return getProperty("validFrom");
  }
  
  public <T extends ASObject>T getRestriction() {
    return (T)getProperty("restriction");
  }
  
  public static OfferBuilder makeOffer() {
    return new OfferBuilder("offer");
  }
  
  @Name("offer")
  @Properties({
    @Property(name="validFrom",to=DateTime.class),
    @Property(name="validUntil",to=DateTime.class)
  })
  public static final class OfferBuilder extends Builder<OfferObject,OfferBuilder> {

    public OfferBuilder() {
      super(OfferObject.class,OfferBuilder.class);
    }

    public OfferBuilder(Map<String, Object> map) {
      super(map, OfferObject.class,OfferBuilder.class);
    }

    public OfferBuilder(String objectType) {
      super(objectType, OfferObject.class,OfferBuilder.class);
    }
    
  }
  
  public static abstract class Builder<X extends OfferObject,M extends Builder<X,M>> 
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
    public M availability(ASObject obj) {
      set("availability",obj);
      return (M)this;
    }
    public M condition(ASObject obj) {
      set("condition",obj);
      return (M)this;
    }
    public M currency(String obj) {
      set("currency",obj);
      return (M)this;
    }
    public M item(ASObject obj) {
      set("item",obj);
      return (M)this;
    }
    public M price(String obj) {
      set("price",obj);
      return (M)this;
    }
    public M restriction(ASObject obj) {
      set("restriction",obj);
      return (M)this;
    }
    public M validFrom(DateTime obj) {
      set("validFrom",obj);
      return (M)this;
    }
    public M validUntil(DateTime obj) {
      set("validUntil",obj);
      return (M)this;
    }
  }
}
