package org.apache.abdera2.activities.extra;

import org.apache.abdera2.activities.io.gson.Properties;
import org.apache.abdera2.activities.io.gson.Property;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.MediaLink;
import org.apache.abdera2.common.anno.Name;
import org.joda.time.DateTime;

/**
 * A simple "objectType":"tv-episode" object that serves primarily as an 
 * example of creating new ASObject types.
 */
@Name("tv-episode")
@Properties({
  @Property(name="preview",to=MediaLink.class),
  @Property(name="aired",to=DateTime.class)
})
@SuppressWarnings("unchecked")
public class TvEpisodeObject extends CreativeWork {

  private static final long serialVersionUID = -1551754630697817614L;

  public TvEpisodeObject() {}
  
  public TvEpisodeObject(String displayName) {
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
  
  public DateTime getAired() {
    return getProperty("aired");
  }
  
  public void setAired(DateTime dt) {
    setProperty("aired",dt);
  }
  
  public MediaLink getPreview() {
    return getProperty("preview");
  }
  
  public void setPreview(MediaLink link) {
    setProperty("preview", link);
  }
  
  public <T extends ASObject>T getSeries() {
    return (T)getProperty("series");
  }
  
  public void setSeries(ASObject series) {
    setProperty("series", series);
  }
  
  public <T extends ASObject>T getSeason() {
    return (T)getProperty("season");
  }
  
  public void setSeason(ASObject season) {
    setProperty("season", season);
  }
  
  public int getEpisodeNumber() {
    return (Integer)getProperty("episode");
  }
  
  public void setEpisodeNumber(int episode) {
    setProperty("episode", episode);
  }
  
  
  public static <T extends TvEpisodeObject>TvEpisodeObjectGenerator<T> makeTvEpisode() {
    return new TvEpisodeObjectGenerator<T>();
  }
  
  public static class TvEpisodeObjectGenerator<T extends TvEpisodeObject> extends CreativeWorkGenerator<T> {
    public TvEpisodeObjectGenerator() {
      super((Class<T>) TvEpisodeObject.class);
    }
    public TvEpisodeObjectGenerator(Class<T> _class) {
      super(_class);
    }
    public <X extends TvEpisodeObjectGenerator<T>>X actors(ASObject obj) {
      item.setActors(obj);
      return (X)this;
    }
    public <X extends TvEpisodeObjectGenerator<T>>X director(ASObject obj) {
      item.setDirector(obj);
      return (X)this;
    }
    public <X extends TvEpisodeObjectGenerator<T>>X episodeNumber(int n) {
      item.setEpisodeNumber(n);
      return (X)this;
    }
    public <X extends TvEpisodeObjectGenerator<T>>X season(ASObject obj) {
      item.setSeason(obj);
      return (X)this;
    }
    public <X extends TvEpisodeObjectGenerator<T>>X series(ASObject obj) {
      item.setSeries(obj);
      return (X)this;
    }
    public <X extends TvEpisodeObjectGenerator<T>>X musicBy(ASObject obj) {
      item.setMusicBy(obj);
      return (X)this;
    }
    public <X extends TvEpisodeObjectGenerator<T>>X preview(MediaLink obj) {
      item.setPreview(obj);
      return (X)this;
    }
    public <X extends TvEpisodeObjectGenerator<T>>X producer(ASObject obj) {
      item.setProducer(obj);
      return (X)this;
    }
    public <X extends TvEpisodeObjectGenerator<T>>X productionCompany(ASObject obj) {
      item.setProductionCompany(obj);
      return (X)this;
    }
    public <X extends TvEpisodeObjectGenerator<T>>X aired(DateTime dateTime) {
      item.setAired(dateTime);
      return (X)this;
    }
  }
}
