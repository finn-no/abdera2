package org.apache.abdera2.activities.model.objects;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataHandler;
import javax.activation.MimeType;

import org.apache.abdera2.activities.io.gson.Properties;
import org.apache.abdera2.activities.io.gson.Property;
import org.apache.abdera2.common.anno.Name;
import org.apache.abdera2.common.io.Compression;
import org.apache.abdera2.common.io.Compression.CompressionCodec;
import org.apache.abdera2.common.security.HashHelper.Hasher;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;

/**
 * The BinaryObject is an extension object type that allows 
 * for an Activity Streams object representing arbitrary 
 * Base64 encoded data. It is an extension of the FileObject 
 * type so it inherits the fileUrl and mimeType properties
 * to communicate the permanent url of the data and the 
 * MIME media type. The content property is used to contain
 * the Base64 encoded data. This object is particularly 
 * useful in conjunction with the "attachments" property 
 * to attach arbitrary binary resources to another ASObject.
 * For instance, attaching a VCALENDAR file to an Event Object.
 * 
 * By default, the mime type will be set to application/octet-stream.
 * Applications need to be careful about which content types they 
 * will allow. 
 */
@Name("binary")
@Properties({
  @Property(name="mimeType",to=MimeType.class)
})
public class BinaryObject extends FileObject {
  
  private static final long serialVersionUID = 5120608845229587281L;

  public BinaryObject() {
    setMimeType("application/octet-stream");
  }
  
  public BinaryObject(String displayName) {
    this();
    setDisplayName(displayName);
  }
    
  public void setData(DataHandler data, CompressionCodec... comps) throws IOException {
    setData(data,(Hasher)null,comps);
  }
  
  public void setData(byte[] data, Hasher hash, CompressionCodec... comps) throws IOException {
    setData(new ByteArrayInputStream(data),hash,comps);
  }
  
  /**
   * Set the Content and the MimeType from the DatHandler. 
   * This method defers to the setContent(InputStream) method
   * by passing it the InputStream retrieved from the DataHandler.
   * That means it currently blocks while reading, consuming, and
   * encoding the InputStream. TODO: The better approach would be
   * to simple store the DataHandler in the exts table directly
   * and use a custom TypeAdapter for the BinaryObject to read and
   * consume the DataHandler during the actual Serialization.
   */
  public void setData(DataHandler data,Hasher hash, CompressionCodec... comps) throws IOException {
    setData(data.getInputStream(),hash,comps);
    setMimeType(data.getContentType());
  }
  
  public void setData(InputStream data) throws IOException {
    setData(data,null);
  }
  
  /**
   * Set the Content as a Base64 Encoded string. Calling this 
   * method will perform a blocking read that will consume the 
   * InputStream and generate a Base64 Encoded String. 
   * 
   * If a Hasher class is provided, a hash digest for the 
   * input data prior to encoding will be generated and 
   * stored in a property value whose name is the value
   * returned by Hasher.name(); e.g., HashHelper.Md5 will 
   * add "md5":"{hex digest}" to the JSON object. 
   * 
   * The content may be optionally compressed prior to base64 
   * encoding by passing in one or more CompressionCodecs. If 
   * compression is used, a "compression" property will be added
   * to the object whose value is a comma separated list of 
   * the applied compression codecs in the order of application. 
   * The getInputStream method will automatically search for the 
   * "compression" property and attempt to automatically decompress
   * the stream when reading.
   * 
   * This will also automatically set the "length" property equal 
   * to the total number of uncompressed, unencoded octets.
   */
  public void setData(InputStream data, Hasher hash, CompressionCodec... comps) throws IOException {
    ByteArrayOutputStream out = 
      new ByteArrayOutputStream();
    OutputStream bout = 
      new Base64OutputStream(out,true,0,null);
    if (comps.length > 0) {
      bout = Compression.wrap(bout,comps);
      String comp = Compression.describe(null, comps);
      setProperty("compression",comp.substring(1,comp.length()-1));
    }
    
    byte[] d = new byte[1024];
    int r = -1, len = 0;
    while((r = data.read(d)) > -1) { 
      len += r;
      if (hash != null)
        hash.update(d, 0, r);
      bout.write(d, 0, r);
      bout.flush();
    }
    bout.close();
    setProperty("length",len);
    String c = new String(out.toByteArray(),"UTF-8");
    super.setProperty("data",c);
    if (hash != null)
      setProperty(
        hash.name(),
        hash.get());
  }
  
  /**
   * Returns an InputStream that will decode the Base64 encoded 
   * content on the fly. The data is stored encoded in memory and
   * only decoded as the InputStream is consumed.
   */
  public InputStream getInputStream() throws IOException {
    String content = super.getProperty("data");
    if (content == null) return null;
    ByteArrayInputStream in = 
      new ByteArrayInputStream(
        content.getBytes("UTF-8"));
    InputStream bin = 
      new Base64InputStream(in);
    if (has("compression")) {
      String comp = getProperty("compression");
      bin = Compression.wrap(bin, comp);
    }
    return bin;
  }
  
  public void setData(byte[] data, CompressionCodec... comps) throws IOException {
    setData(new ByteArrayInputStream(data),null,comps);
  }
  
  public void setData(byte[] data, int s, int e, CompressionCodec... comps) throws IOException {
    setData(new ByteArrayInputStream(data,s,e),null,comps);
  }
  
  public void setData(byte[] data, int s, int e, Hasher hash, CompressionCodec... comps) throws IOException {
    setData(new ByteArrayInputStream(data,s,e),hash,comps);
  }
 
  public static <T extends BinaryObject>BinaryObjectGenerator<T> makeBinary() {
    return new BinaryObjectGenerator<T>();
  }
  
  @SuppressWarnings("unchecked")
  public static class BinaryObjectGenerator<T extends BinaryObject> extends FileObjectGenerator<T> {
    public BinaryObjectGenerator() {
      super((Class<T>) BinaryObject.class);
    }
    public BinaryObjectGenerator(Class<T> _class) {
      super(_class);
    }
    public <X extends BinaryObjectGenerator<T>>X data(InputStream in) throws IOException {
      item.setData(in);
      return (X)this;
    }
    public <X extends BinaryObjectGenerator<T>>X data(InputStream in, Hasher hasher, CompressionCodec... codecs) throws IOException {
      item.setData(in,hasher,codecs);
      return (X)this;
    }
    public <X extends BinaryObjectGenerator<T>>X data(DataHandler in, CompressionCodec... codecs) throws IOException {
      item.setData(in,codecs);
      return (X)this;
    }
    public <X extends BinaryObjectGenerator<T>>X data(DataHandler in, Hasher hasher, CompressionCodec... codecs) throws IOException {
      item.setData(in,hasher,codecs);
      return (X)this;
    }
    public <X extends BinaryObjectGenerator<T>>X data(byte[] in, CompressionCodec... codecs) throws IOException {
      item.setData(in,codecs);
      return (X)this;
    }
    public <X extends BinaryObjectGenerator<T>>X data(byte[] in, Hasher hasher, CompressionCodec... codecs) throws IOException {
      item.setData(in,hasher,codecs);
      return (X)this;
    }
    
    public <X extends BinaryObjectGenerator<T>>X data(byte[] in, int s, int e, CompressionCodec... codecs) throws IOException {
      item.setData(in,s,e,codecs);
      return (X)this;
    }
    public <X extends BinaryObjectGenerator<T>>X data(byte[] in, int s, int e, Hasher hasher, CompressionCodec... codecs) throws IOException {
      item.setData(in,s,e,hasher,codecs);
      return (X)this;
    }
  }
}
