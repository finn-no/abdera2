package org.apache.abdera2.activities.model.objects;

import java.util.Map;

import org.apache.abdera2.activities.model.ASBase;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.Generator;
import org.apache.abdera2.common.iri.IRI;

/**
 * Represents an Embedded Experience data structure. Embedded Experiences
 * were introduced to Activity Streams through the OpenSocial 2.0 specification.
 * While EE structures can be used outside the scope of OpenSocial, they are
 * primarily intended to be used as a means of associating an OpenSocial
 * Gadget specification with an Activity Stream object that can be rendered 
 * in-line when the activity data is displayed within an OpenSocial container.
 */
public class EmbeddedExperience 
  extends ASBase {

  private static final long serialVersionUID = -4311572934016006379L;

  public EmbeddedExperience() {}
  
  public IRI getUrl() {
    return getProperty("url");
  }
  
  public void setUrl(IRI iri) {
    setProperty("url", iri);
  }
  
  public void setUrl(String iri) {
    setUrl(new IRI(iri));
  }
  
  public IRI getGadget() {
    return getProperty("gadget");
  }
  
  public void setGadget(IRI iri) {
    setProperty("gadget", iri);
  }
  
  public void setGadget(String iri) {
    setGadget(new IRI(iri));
  }
  
  public ASBase getContext() {
    return getProperty("context");
  }
  
  public void setContext(ASBase context) {
    setProperty("context", context);
  }
  
  public void setContext(Map<String,Object> context) {
    setProperty("context", context);
  }
  
  public IRI getPreviewImage() {
    return getProperty("previewImage");
  }
  
  public void setPreviewImage(IRI iri) {
    setProperty("previewImage", iri);
  }
  
  public void setPreviewImage(String iri) {
    setGadget(new IRI(iri));
  }
  
  public static EmbeddedExperienceGenerator makeEmbeddedExperience() {
    return new EmbeddedExperienceGenerator();
  }
  
  public static class EmbeddedExperienceGenerator 
    extends Generator<EmbeddedExperience> {

    public EmbeddedExperienceGenerator() {
      super(EmbeddedExperience.class);
    }
    
    public EmbeddedExperienceGenerator context(ASObject object) {
      item.setContext(object);
      return this;
    }
    
    public EmbeddedExperienceGenerator context(Map<String,Object> map) {
      item.setContext(map);
      return this;
    }
    
    public EmbeddedExperienceGenerator gadget(IRI iri) {
      item.setGadget(iri);
      return this;
    }
    
    public EmbeddedExperienceGenerator gadget(String iri) {
      item.setGadget(iri);
      return this;
    }
    
    public EmbeddedExperienceGenerator previewImage(IRI iri) {
      item.setPreviewImage(iri);
      return this;
    }
    
    public EmbeddedExperienceGenerator previewImage(String iri) {
      item.setPreviewImage(iri);
      return this;
    }
    
    public EmbeddedExperienceGenerator url(IRI iri) {
      item.setUrl(iri);
      return this;
    }
    
    public EmbeddedExperienceGenerator url(String uri) {
      item.setUrl(uri);
      return this;
    }
  }
}
