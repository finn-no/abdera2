package org.apache.abdera2.examples.activities;

import java.net.URL;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.activities.model.CollectionWriter;
import org.apache.abdera2.activities.model.IO;
import org.apache.abdera2.ext.activities.FeedToActivityConverter;
import org.apache.abdera2.model.Document;
import org.apache.abdera2.model.Feed;
import org.apache.abdera2.parser.Parser;
import org.apache.abdera2.parser.ParserOptions;

public class FeedToStreamConversionExample {

  public static void main(String... args) throws Exception {
    
    Abdera abdera = Abdera.getInstance();
    URL url = new URL("http://intertwingly.net/blog/index.atom");
    Parser parser = abdera.getParser();
    ParserOptions options = parser.getDefaultParserOptions();
    options.setCharset("UTF-8");
    Document<Feed> doc = abdera.getParser().parse(url.openStream(),url.toString(),options);
    Feed feed = doc.getRoot();

    FeedToActivityConverter c = new FeedToActivityConverter() {};

    IO io = IO.get();

    CollectionWriter cw = io.getCollectionWriter(System.out, "UTF-8");

    c.convert(feed, cw);
    
  }
  
}
