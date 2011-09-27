package org.apache.abdera2.activities.extra;

import java.util.Date;

import org.apache.abdera2.activities.io.gson.Properties;
import org.apache.abdera2.activities.io.gson.Property;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.MediaLink;
import org.apache.abdera2.common.anno.Name;

/**
 * A simple "objectType":"tv-season" object that serves primarily as an 
 * example of creating new ASObject types.
 */
@Name("tv-season")
@Properties({
  @Property(name="startDate",to=Date.class),
  @Property(name="endDate",to=Date.class),
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
  
  public void setActor(ASObject actors) {
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
  
  public Date getStartDate() {
    return getProperty("startDate");
  }
  
  public void setStartDate(Date date) {
    setProperty("startDate", date);
  }
  
  public Date getEndDate() {
    return getProperty("endDate");
  }
  
  public void setEndDate(Date date) {
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
}
