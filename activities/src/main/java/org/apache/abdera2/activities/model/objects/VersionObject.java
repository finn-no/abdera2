package org.apache.abdera2.activities.model.objects;

import java.util.Map;

import org.apache.abdera2.activities.io.gson.Properties;
import org.apache.abdera2.activities.io.gson.Property;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.common.anno.Name;
import org.apache.abdera2.common.anno.Version;

/**
 * Activity Stream object that represents a description of a Version 
 * of a referenced object. "objectType":"version"... The basic use 
 * case for this would be for Version control systems.
 */
public class VersionObject 
  extends ASObject {

  public static VersionBuilder makeVersion() {
    return new VersionBuilder("version");
  }
  
  public static VersionObject makeVersion(
    ASObject of, 
    int major, 
    int minor, 
    int revision,
    Version.Status status) {
    return makeVersion()
      .of(of)
      .major(major)
      .minor(minor)
      .revision(revision)
      .status(status)
      .get();
  }
  
  public static VersionObject makeVersion(Version version) {
    return makeVersion()
      .of(
        ASObject.makeObject()
          .displayName(version.name())
          .url(version.uri())
          .get())
      .major(version.major())
      .minor(version.minor())
      .revision(version.revision())
      .status(version.status())
      .get();
  }

  @Name("version")
  @Properties({
    @Property(name="previousVersion", to=VersionObject.class),
    @Property(name="nextVersion",to=VersionObject.class),
    @Property(name="stableVersion",to=VersionObject.class),
    @Property(name="activeVersion",to=VersionObject.class)
  })
  public static final class VersionBuilder extends Builder<VersionObject,VersionBuilder> {

    public VersionBuilder() {
      super(VersionObject.class,VersionBuilder.class);
    }

    public VersionBuilder(Map<String, Object> map) {
      super(map, VersionObject.class,VersionBuilder.class);
    }

    public VersionBuilder(String objectType) {
      super(objectType, VersionObject.class,VersionBuilder.class);
    }
    
  }
  
  @SuppressWarnings("unchecked")
  public static abstract class Builder<X extends VersionObject, M extends Builder<X,M>> 
    extends ASObject.Builder<X,M> {
    protected Builder(Class<X>_class,Class<M>_builder) {
      super(_class,_builder);
    }
    protected Builder(String objectType,Class<X>_class,Class<M>_builder) {
      super(objectType,_class,_builder);
    }
    protected Builder(Map<String,Object> map,Class<X>_class,Class<M>_builder) {
      super(map,_class,_builder);
    }
    public M active(VersionObject object) {
      set("activeVersion",object);
      return (M)this;
    }
    public M major(int val) {
      set("major",Math.max(0, val));
      return (M)this;
    }
    public M minor(int val) {
      set("minor",Math.max(0, val));
      return (M)this;
    }
    public M revision(int val) {
      set("revision",Math.max(0,val));
      return (M)this;
    }
    public M status(Version.Status status) {
      set("status",status.name().toLowerCase());
      return (M)this;
    }
    public M next(VersionObject val) {
      set("nextVersion",val);
      return (M)this;
    }
    public M of(ASObject val) {
      set("of",val);
      return (M)this;
    }
    public M previous(VersionObject val) {
      set("previousVersion",val);
      return (M)this;
    }
    public M stable(VersionObject val) {
      set("stableVersion",val);
      return (M)this;
    }
  }
  

  public VersionObject(Map<String,Object> map) {
    super(map,VersionBuilder.class,VersionObject.class);
  }
  
  public <X extends VersionObject, M extends Builder<X,M>>VersionObject(Map<String,Object> map,Class<M>_class,Class<X>_obj) {
    super(map,_class,_obj);
  }
  
  /** The object this object describes a version of **/
  @SuppressWarnings("unchecked")
  public <T extends ASObject>T getOf() {
    return (T)getProperty("of");
  }
    
  public VersionObject getPreviousVersion() {
    return getProperty("previousVersion");
  }
  
  public VersionObject getNextVersion() {
    return getProperty("nextVersion");
  }
  
  public VersionObject getStableVersion() {
    return getProperty("stableVersion");
  }
  
  public VersionObject getActiveVersion() {
    return getProperty("activeVersion");
  }
  
  public int getMajor() {
    return getPropertyInt("major");
  }
  
  public int getMinor() {
    return getPropertyInt("minor");
  }
  
  public int getRevision() {
    return getPropertyInt("revision");
  }
  
  public Version.Status getStatus() {
    String status = getProperty("status");
    return status != null ?
      Version.Status.valueOf(status.toUpperCase()) : null;
  }
}
