package org.apache.abdera2.activities.extra;

import org.apache.abdera2.activities.io.gson.Properties;
import org.apache.abdera2.activities.io.gson.Property;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.MediaLink;
import org.apache.abdera2.common.anno.Name;

/**
 * A simple "objectType":"tv-episode" object that serves primarily as an 
 * example of creating new ASObject types.
 */
@Name("tv-episode")
@Properties({
  @Property(name="preview",to=MediaLink.class)
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
}
