package org.apache.abdera2.examples.activities;

import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;

import javax.activation.DataHandler;

import org.apache.abdera2.activities.model.IO;
import org.apache.abdera2.activities.model.objects.BadgeObject;
import org.apache.abdera2.activities.model.objects.BinaryObject;

import static org.apache.abdera2.common.io.Compression.CompressionCodec.DEFLATE;
import static org.apache.abdera2.common.security.HashHelper.Md5;
import static org.apache.abdera2.activities.model.objects.BadgeObject.makeBadge;
import static org.apache.abdera2.activities.model.objects.BinaryObject.makeBinary;

/**
 * Illustrates the extension "binary" objectType... this can be useful, 
 * for instance, when attaching arbitrary base64 encoded data to an 
 * object (e.g in the "attachments" property)
 */
public class BinaryDataObjectExample {

  public static void main(String... args) throws Exception {
    
    IO io = IO.get();
    
    URL url = BinaryDataObjectExample.class.getResource("/info.png");
    DataHandler dataHandler = new DataHandler(url); 
    
    BadgeObject badge = 
      makeBadge()
        .attachment(
          makeBinary()
            .data(
              dataHandler, 
              new Md5(), 
              DEFLATE)
            .get())
        .get();
    
    // check the round trip //
    StringReader sr = new StringReader(io.write(badge));
    
    badge = io.readObject(sr);
 
    BinaryObject dataObject = (BinaryObject) badge.getAttachments().iterator().next();
    
    String md5 = dataObject.getProperty("md5");
    Md5 check = new Md5();
    
    // decompression will be applied automatically
    InputStream in = dataObject.getInputStream(); 
    byte[] buf = new byte[100];
    int r = -1;
    while((r = in.read(buf)) > -1) {
      check.update(buf, 0, r);
      System.out.write(buf,0,r);
    }
    String checks = check.get();
    
    System.out.println(checks.equalsIgnoreCase(md5));
    
  }
  
}
