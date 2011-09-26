package org.apache.abdera2.activities.extra;

import org.apache.abdera2.activities.io.gson.Properties;
import org.apache.abdera2.activities.io.gson.Property;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.common.anno.Name;

/**
 * Activity Stream object that represents a description of a Version 
 * of a referenced object. "objectType":"version"... The basic use 
 * case for this would be for Version control systems.
 */
@Name("version")
@Properties({
  @Property(name="previousVersion", to=VersionObject.class),
  @Property(name="nextVersion",to=VersionObject.class)
})
public class VersionObject 
  extends ASObject {

  private static final long serialVersionUID = -7473463819890471971L;

  public VersionObject() {}
  
  public VersionObject(String displayName) {
    super();
    setDisplayName(displayName);
  }
  
  /** The object this object describes a version of **/
  @SuppressWarnings("unchecked")
  public <T extends ASObject>T getOf() {
    return (T)getProperty("of");
  }
  
  /** The object this object describes a version of **/
  public void setOf(ASObject object) {
    setProperty("of", object);
  }
  
  public VersionObject getPreviousVersion() {
    return getProperty("previousVersion");
  }
  
  public void setPreviousVersion(VersionObject version) {
    setProperty("previousVersion", version);
  }
  
  public VersionObject getNextVersion() {
    return getProperty("nextVersion");
  }
  
  public void setNextVersion(VersionObject version) {
    setProperty("nextVersion", version);
  }
  
  public VersionObject getStableVersion() {
    return getProperty("stableVersion");
  }
  
  public void setStableVersion(VersionObject version) {
    setProperty("stableVersion", version);
  }
  
  public VersionObject getActiveVersion() {
    return getProperty("activeVersion");
  }
  
  public void setActiveVersion(VersionObject version) {
    setProperty("activeVersion", version);
  }
  
  public String getMajor() {
    return getProperty("major");
  }
  
  public void setMajor(String val) {
    setProperty("major", val);
  }
  
  public String getMinor() {
    return getProperty("minor");
  }
  
  public void setMinor(String minor) {
    setProperty("minor", minor);
  }
  
  public String getRevision() {
    return getProperty("revision");
  }
  
  public void setRevision(String val) {
    setProperty("revision", val);
  }
  
}
