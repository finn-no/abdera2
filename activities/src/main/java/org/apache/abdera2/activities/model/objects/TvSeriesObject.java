package org.apache.abdera2.activities.model.objects;

import org.apache.abdera2.activities.io.gson.Properties;
import org.apache.abdera2.activities.io.gson.Property;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.MediaLink;
import org.apache.abdera2.activities.model.objects.CreativeWork.CreativeWorkGenerator;
import org.apache.abdera2.common.anno.Name;
import org.joda.time.DateTime;

/**
 * A simple "objectType":"tv-series" object that serves primarily as an 
 * example of creating new ASObject types.
 */
@Name("tv-series")
@Properties({
  @Property(name="startDate",to=DateTime.class),
  @Property(name="endDate",to=DateTime.class),
  @Property(name="preview",to=MediaLink.class)
})
@SuppressWarnings("unchecked")
public class TvSeriesObject extends CreativeWork {

  private static final long serialVersionUID = -1551754630697817614L;

  public TvSeriesObject() {}
  
  public TvSeriesObject(String displayName) {
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
  
  public void setDirector(ASObject object) {
    setProperty("director", object);
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
  
  public DateTime getStartDate() {
    return getProperty("startDate");
  }
  
  public void setStartDate(DateTime date) {
    setProperty("startDate", date);
  }
  
  public DateTime getEndDate() {
    return getProperty("endDate");
  }
  
  public void setEndDate(DateTime date) {
    setProperty("endDate", date);
  }
  
  public <T extends ASObject>T getSeasons() {
    return (T)getProperty("seasons");
  }
  
  public void setSeasons(ASObject seasons) {
    setProperty("seasons", seasons);
  }
  
  public <T extends ASObject>T getEpisodes() {
    return (T)getProperty("episodes");
  }
  
  public void setEpisodes(ASObject episodes) {
    setProperty("episodes", episodes);
  }
  
  
  
  public static <T extends TvSeriesObject>TvSeriesObjectGenerator<T> makeTvSeries() {
    return new TvSeriesObjectGenerator<T>();
  }
  
  public static class TvSeriesObjectGenerator<T extends TvSeriesObject> extends CreativeWorkGenerator<T> {
    public TvSeriesObjectGenerator() {
      super((Class<T>) TvSeriesObject.class);
    }
    public TvSeriesObjectGenerator(Class<T> _class) {
      super(_class);
    }
    public <X extends TvSeriesObjectGenerator<T>>X actors(ASObject obj) {
      item.setActors(obj);
      return (X)this;
    }
    public <X extends TvSeriesObjectGenerator<T>>X director(ASObject obj) {
      item.setDirector(obj);
      return (X)this;
    }
    public <X extends TvSeriesObjectGenerator<T>>X musicBy(ASObject obj) {
      item.setMusicBy(obj);
      return (X)this;
    }
    public <X extends TvSeriesObjectGenerator<T>>X preview(MediaLink obj) {
      item.setPreview(obj);
      return (X)this;
    }
    public <X extends TvSeriesObjectGenerator<T>>X producer(ASObject obj) {
      item.setProducer(obj);
      return (X)this;
    }
    public <X extends TvSeriesObjectGenerator<T>>X productionCompany(ASObject obj) {
      item.setProductionCompany(obj);
      return (X)this;
    }
    public <X extends TvSeriesObjectGenerator<T>>X episodes(ASObject obj) {
      item.setEpisodes(obj);
      return (X)this;
    }
    public <X extends TvSeriesObjectGenerator<T>>X seasons(ASObject obj) {
      item.setSeasons(obj);
      return (X)this;
    }
    public <X extends TvSeriesObjectGenerator<T>>X startDate(DateTime dt) {
      item.setStartDate(dt);
      return (X)this;
    }
    public <X extends TvSeriesObjectGenerator<T>>X endDate(DateTime dt) {
      item.setEndDate(dt);
      return (X)this;
    }
  }
}
