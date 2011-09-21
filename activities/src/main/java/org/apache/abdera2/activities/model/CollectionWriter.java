package org.apache.abdera2.activities.model;

/**
 * Interface used to stream a Collection of activity objects.
 */
public interface CollectionWriter {

  /**
   * Writes all of the properties other than the "items" property. 
   * This MUST be called before calling writeObject or writeObjects
   * and cannot be called after calling either of those.
   */
  void writeHeader(ASBase base);
  
  /**
   * Writes an object to the items array of the Collection
   */
  void writeObject(ASObject object);
  
  /**
   * Writes one or more objects to the items array of the Collection
   */
  void writeObjects(ASObject... objects);
  
  /**
   * Writes one or more objects to the items array of the Collection
   */
  void writeObjects(Iterable<ASObject> objects);
  
  /**
   * Completes the Collection
   */
  void complete();
  
}
