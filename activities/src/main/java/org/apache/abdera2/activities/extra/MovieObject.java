package org.apache.abdera2.activities.extra;

import org.apache.abdera2.activities.io.gson.Properties;
import org.apache.abdera2.activities.io.gson.Property;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.MediaLink;
import org.apache.abdera2.common.anno.Name;
import org.joda.time.Duration;

/**
 * A simple "objectType":"movie" object that serves primarily as an 
 * example of creating new ASObject types.
 */
@Name("movie")
@Properties({
  @Property(name="preview",to=MediaLink.class),
  @Property(name="duration",to=Duration.class)
})
@SuppressWarnings("unchecked")
public class MovieObject extends CreativeWork {

  private static final long serialVersionUID = -1551754630697817614L;

  public MovieObject() {}
  
  public MovieObject(String displayName) {
    setDisplayName(displayName);
  }
  
  public <T extends ASObject>T getActors() {
    return (T)getProperty("actors");
  }
  
  public void setActors(ASObject actors) {
    setProperty("actors", actors);
  }
  
  public <T extends ASObject>T getDirector() {
    return (T)getProperty("director");
  }
  
  public void setDirector(ASObject director) {
    setProperty("director", director);
  }
  
  public Duration getDuration() {
    return getProperty("duration");
  }
  
  public void setDuration(Duration duration) {
    setProperty("duration", duration);
  }
  
  public <T extends ASObject>T getMusicBy() {
    return (T)getProperty("musicBy");
  }
  
  public void setMusicBy(ASObject musicBy) {
    setProperty("musicBy", musicBy);
  }
  
  public <T extends ASObject>T getProducer() {
    return (T)getProperty("producer");
  }
  
  public void setProducer(ASObject producer) {
    setProperty("producer", producer);
  }
  
  public <T extends ASObject>T getProductionCompany() {
    return (T)getProperty("productionCompany");
  }
  
  public void setProductionCompany(ASObject org) {
    setProperty("productionCompany", org);
  }
  
  public MediaLink getPreview() {
    return getProperty("preview");
  }
  
  public void setPreview(MediaLink link) {
    setProperty("preview", link);
  }
  
  public static <T extends MovieObject>MovieObjectGenerator<T> makeMovie() {
    return new MovieObjectGenerator<T>();
  }
  
  public static class MovieObjectGenerator<T extends MovieObject> extends CreativeWorkGenerator<T> {
    public MovieObjectGenerator() {
      super((Class<T>) MovieObject.class);
    }
    public MovieObjectGenerator(Class<T> _class) {
      super(_class);
    }
    public <X extends MovieObjectGenerator<T>>X actors(ASObject obj) {
      item.setActors(obj);
      return (X)this;
    }
    public <X extends MovieObjectGenerator<T>>X director(ASObject obj) {
      item.setDirector(obj);
      return (X)this;
    }
    public <X extends MovieObjectGenerator<T>>X duration(Duration obj) {
      item.setDuration(obj);
      return (X)this;
    }
    public <X extends MovieObjectGenerator<T>>X musicBy(ASObject obj) {
      item.setMusicBy(obj);
      return (X)this;
    }
    public <X extends MovieObjectGenerator<T>>X preview(MediaLink obj) {
      item.setPreview(obj);
      return (X)this;
    }
    public <X extends MovieObjectGenerator<T>>X producer(ASObject obj) {
      item.setProducer(obj);
      return (X)this;
    }
    public <X extends MovieObjectGenerator<T>>X productionCompany(ASObject obj) {
      item.setProductionCompany(obj);
      return (X)this;
    }
  }
}
