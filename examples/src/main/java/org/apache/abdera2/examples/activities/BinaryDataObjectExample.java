package org.apache.abdera2.examples.activities;

import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;

import javax.activation.DataHandler;

import org.apache.abdera2.activities.extra.BinaryObject;
import org.apache.abdera2.activities.extra.Extra;
import org.apache.abdera2.activities.model.IO;
import org.apache.abdera2.activities.model.objects.BadgeObject;

/**
 * Illustrates the extension "binary" objectType... this can be useful, 
 * for instance, when attaching arbitrary base64 encoded data to an 
 * object (e.g in the "attachments" property)
 */
public class BinaryDataObjectExample {

  public static void main(String... args) throws Exception {
    
    IO io = IO.get();
    Extra.initExtras(io);
    
    URL url = BinaryDataObjectExample.class.getResource("/info.png");
    DataHandler dataHandler = new DataHandler(url); 
    
    BinaryObject dataObject = new BinaryObject();
    dataObject.setContent(dataHandler);
    
    BadgeObject badge = new BadgeObject();
    badge.addAttachment(dataObject);
    
    // check the round trip //
    StringReader sr = new StringReader(io.write(badge));
    
    badge = io.readObject(sr);
    
    dataObject = (BinaryObject) badge.getAttachments().iterator().next();
    
    InputStream in = dataObject.getInputStream(); 
    byte[] buf = new byte[100];
    int r = -1;
    while((r = in.read(buf)) > -1)
      System.out.write(buf,0,r);
    
  }
  
}
