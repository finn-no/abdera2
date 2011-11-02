package org.apache.abdera2.activities.model.objects;

import org.apache.abdera2.activities.io.gson.Properties;
import org.apache.abdera2.activities.io.gson.Property;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.MediaLink;
import org.apache.abdera2.activities.model.objects.CreativeWork.CreativeWorkGenerator;
import org.apache.abdera2.common.anno.Name;
import org.joda.time.DateTime;

/**
 * A simple "objectType":"tv-season" object that serves primarily as an 
 * example of creating new ASObject types.
 */
@Name("tv-season")
@Properties({
  @Property(name="startDate",to=DateTime.class),
  @Property(name="endDate",to=DateTime.class),
  @Property(name="preview",to=MediaLink.class)
})
@SuppressWarnings("unchecked")
public class TvSeasonObject extends CreativeWork {

  private static final long serialVersionUID = -1551754630697817614L;

  public TvSeasonObject() {}
  
  public TvSeasonObject(String displayName) {
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
  
  public <T extends ASObject>T getEpisodes() {
    return (T)getProperty("episodes");
  }
  
  public void setEpisodes(ASObject episodes) {
    setProperty("episodes", episodes);
  }
  
  public <T extends ASObject>T getSeries() {
    return (T)getProperty("series");
  }
  
  public void setSeries(ASObject series) {
    setProperty("series", series);
  }
  
  public int getSeasonNumber() {
    return (Integer)getProperty("season");
  }
  
  public void setSeasonNumber(int season) {
    setProperty("season", season);
  }
  
  
  
  public static <T extends TvSeasonObject>TvSeasonObjectGenerator<T> makeTvSeason() {
    return new TvSeasonObjectGenerator<T>();
  }
  
  public static class TvSeasonObjectGenerator<T extends TvSeasonObject> extends CreativeWorkGenerator<T> {
    public TvSeasonObjectGenerator() {
      super((Class<T>) TvSeasonObject.class);
    }
    public TvSeasonObjectGenerator(Class<T> _class) {
      super(_class);
    }
    public <X extends TvSeasonObjectGenerator<T>>X actors(ASObject obj) {
      item.setActors(obj);
      return (X)this;
    }
    public <X extends TvSeasonObjectGenerator<T>>X director(ASObject obj) {
      item.setDirector(obj);
      return (X)this;
    }
    public <X extends TvSeasonObjectGenerator<T>>X seasonNumber(int n) {
      item.setSeasonNumber(n);
      return (X)this;
    }
    public <X extends TvSeasonObjectGenerator<T>>X series(ASObject obj) {
      item.setSeries(obj);
      return (X)this;
    }
    public <X extends TvSeasonObjectGenerator<T>>X musicBy(ASObject obj) {
      item.setMusicBy(obj);
      return (X)this;
    }
    public <X extends TvSeasonObjectGenerator<T>>X preview(MediaLink obj) {
      item.setPreview(obj);
      return (X)this;
    }
    public <X extends TvSeasonObjectGenerator<T>>X producer(ASObject obj) {
      item.setProducer(obj);
      return (X)this;
    }
    public <X extends TvSeasonObjectGenerator<T>>X productionCompany(ASObject obj) {
      item.setProductionCompany(obj);
      return (X)this;
    }
    public <X extends TvSeasonObjectGenerator<T>>X startDate(DateTime dt) {
      item.setStartDate(dt);
      return (X)this;
    }
    public <X extends TvSeasonObjectGenerator<T>>X endDate(DateTime dt) {
      item.setEndDate(dt);
      return (X)this;
    }
    public <X extends TvSeasonObjectGenerator<T>>X episodes(ASObject obj) {
      item.setEpisodes(obj);
      return (X)this;
    }
  }
}
