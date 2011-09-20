package org.apache.abdera2.activities.model;

public interface CollectionWriter {

  void writeHeader(ASBase base);
  
  void writeObject(ASObject object);
  
  void writeObjects(ASObject... objects);
  
  void writeObjects(Iterable<ASObject> objects);
  
  void complete();
  
}
