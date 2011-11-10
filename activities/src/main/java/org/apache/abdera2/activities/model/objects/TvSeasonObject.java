package org.apache.abdera2.activities.model.objects;

import java.util.Map;

import org.apache.abdera2.activities.io.gson.Properties;
import org.apache.abdera2.activities.io.gson.Property;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.MediaLink;
import org.apache.abdera2.common.anno.Name;
import org.joda.time.DateTime;

/**
 * A simple "objectType":"tv-season" object that serves primarily as an 
 * example of creating new ASObject types.
 */
@SuppressWarnings("unchecked")
public class TvSeasonObject extends CreativeWork {

  public TvSeasonObject(Map<String,Object> map) {
    super(map,TvSeasonBuilder.class,TvSeasonObject.class);
  }
  
  public <X extends TvSeasonObject, M extends Builder<X,M>>TvSeasonObject(Map<String,Object> map,Class<M>_class,Class<X>_obj) {
    super(map,_class,_obj);
  }
  
  public <T extends ASObject>T getActors() {
    return (T)getProperty("actors");
  }
  
  public <T extends ASObject>T getDirector() {
    return (T)getProperty("director");
  }
  
  public <T extends ASObject>T getMusicBy() {
    return (T)getProperty("musicBy");
  }
  
  public <T extends ASObject>T getProducer() {
    return (T)getProperty("producer");
  }
  
  public <T extends ASObject>T getProductionCompany() {
    return (T)getProperty("productionCompany");
  }
  
  public MediaLink getPreview() {
    return getProperty("preview");
  }
  
  public DateTime getStartDate() {
    return getProperty("startDate");
  }
  
  public DateTime getEndDate() {
    return getProperty("endDate");
  }
  
  public <T extends ASObject>T getEpisodes() {
    return (T)getProperty("episodes");
  }
  
  public <T extends ASObject>T getSeries() {
    return (T)getProperty("series");
  }
  
  public int getSeasonNumber() {
    return getPropertyInt("season");
  }
  
  public static TvSeasonBuilder makeTvSeason() {
    return new TvSeasonBuilder("tv-season");
  }
  
  @Name("tv-season")
  @Properties({
    @Property(name="startDate",to=DateTime.class),
    @Property(name="endDate",to=DateTime.class),
    @Property(name="preview",to=MediaLink.class)
  })
    public static final class TvSeasonBuilder extends Builder<TvSeasonObject,TvSeasonBuilder> {

    public TvSeasonBuilder() {
      super(TvSeasonObject.class,TvSeasonBuilder.class);
    }

    public TvSeasonBuilder(Map<String, Object> map) {
      super(map, TvSeasonObject.class,TvSeasonBuilder.class);
    }

    public TvSeasonBuilder(String objectType) {
      super(objectType, TvSeasonObject.class,TvSeasonBuilder.class);
    }
    
  }
  
  public static abstract class Builder<X extends TvSeasonObject, M extends Builder<X,M>>
    extends CreativeWork.Builder<X,M> {
    protected Builder(Class<X>_class,Class<M>_builder) {
      super(_class,_builder);
    }
    protected Builder(String objectType,Class<X>_class,Class<M>_builder) {
      super(objectType,_class,_builder);
    }
    protected Builder(Map<String,Object> map,Class<X>_class,Class<M>_builder) {
      super(map,_class,_builder);
    }
    public M actors(ASObject obj) {
      set("actors",obj);
      return (M)this;
    }
    public M director(ASObject obj) {
      set("director",obj);
      return (M)this;
    }
    public M seasonNumber(int n) {
      set("seasonNumber",n);
      return (M)this;
    }
    public M series(ASObject obj) {
      set("series",obj);
      return (M)this;
    }
    public M musicBy(ASObject obj) {
      set("musicBy",obj);
      return (M)this;
    }
    public M preview(MediaLink obj) {
      set("preview",obj);
      return (M)this;
    }
    public M producer(ASObject obj) {
      set("producer",obj);
      return (M)this;
    }
    public M productionCompany(ASObject obj) {
      set("productionCompany",obj);
      return (M)this;
    }
    public M startDate(DateTime dt) {
      set("startDate",dt);
      return (M)this;
    }
    public M endDate(DateTime dt) {
      set("endDate",dt);
      return (M)this;
    }
    public M episodes(ASObject obj) {
      set("episodes",obj);
      return (M)this;
    }
  }
}
