package org.apache.abdera2.examples.activities;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.abdera2.activities.io.gson.AdaptedType;
import org.apache.abdera2.activities.io.gson.Properties;
import org.apache.abdera2.activities.io.gson.Property;
import org.apache.abdera2.activities.io.gson.SimpleAdapter;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.IO;
import org.apache.abdera2.common.anno.Name;
import org.apache.abdera2.common.http.EntityTag;

public class ExtensionExample {

  @SuppressWarnings("unchecked")
  public static void main(String... args) throws Exception {
    
    // create the io with our custom type adapter
    IO io = IO.get(new BarAdapter());
    
    // tell the serializer about our new object type
    io.addObjectMapping(FooObject.class);
    
    FooObject foo = new FooObject();
    foo.setETag(new EntityTag("test",true));
    foo.setBar(new Bar("foobarbaz"));
        
    Map<Bar,String> map = new HashMap<Bar,String>();
    map.put(new Bar("z"),"a");
    map.put(new Bar("y"), "b");
    foo.setProperty("map",map);
    
    // outputs: foo
    System.out.println(foo.getObjectType());
    
    // outputs: {"etag":"W/\"test\"","map":{"y":"b","z":"a"},"bar":"foobarbaz","objectType":"foo"}
    foo.writeTo(io,System.out);
    
    // now try reading it
    StringReader sr = new StringReader(io.write(foo));
    foo = io.readObject(sr);

    System.out.println();
    
    // check to make sure the etag was deserialized properly
    System.out.println(foo.getETag().getClass());
    
    System.out.println(foo.getBar().getClass());
    
    // map will deserialize as an asobject
    System.out.println(foo.getProperty("map").getClass());
    
  }
  
  @Name("foo")  // the value of the objectType property
  @Properties({
    // tell the deserializer to map the etag property to the EntityTag class
    @Property(name="etag",to=EntityTag.class),
    @Property(name="bar",to=Bar.class)
  })
  public static class FooObject extends ASObject {
    private static final long serialVersionUID = 3601006822295281310L;
    public EntityTag getETag() {
      return getProperty("etag");
    }
    public void setETag(EntityTag etag) {
      setProperty("etag", etag);
    }
    public Bar getBar() {
      return getProperty("bar");
    }
    public void setBar(Bar bar) {
      setProperty("bar",bar);
    }
  }
  
  // Some new class that we want to use as a value.. need to tell 
  // the serializer how to handle it!
  public static class Bar {
    private final String s;
    public Bar(String s) {
      this.s = s;
    }
    public String toString() {
      return s;
    }
  }
  
  // The (de)serializer for the Bar class.. SimpleAdapter uses 
  // toString to serialize.. other TypeAdapter implementations
  // can use any strategy for serialization
  @AdaptedType(Bar.class)
  public static class BarAdapter 
    extends SimpleAdapter<Bar> {
    protected Bar deserialize(String v) {
      return new Bar(v);
    }
  }
}
