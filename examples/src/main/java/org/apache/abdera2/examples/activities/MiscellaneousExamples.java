package org.apache.abdera2.examples.activities;

import org.apache.abdera2.activities.extra.ASContext;
import org.apache.abdera2.activities.model.Activity;
import org.apache.abdera2.activities.model.Collection;
import org.apache.abdera2.common.templates.Template;

public class MiscellaneousExamples {

  public static void main(String... args) throws Exception {
    
    // Working with URI Templates.. the Misc.ASContext 
    // provides a URI Template Context implementation
    // that wraps an Activity Streams object, making it 
    // easier to construct IRI/URI's using properties
    // contained within an activity stream. For instance,
    // the Google+ API specifies a "nextPageToken" property
    // within the root collection object that is used to
    // construct the URL for the next page in a paged 
    // collection of stream documents. Using Misc.ASContext,
    // it's a simple matter to pull that out and construct
    // the url for the next page.
    
    Collection<Activity> collection = 
      new Collection<Activity>();
    collection.setProperty("nextPageToken", "foo");
    
    Template template = 
      new Template("http://example.org/stuff{?nextPageToken}");
    
    System.out.println(
      template.expand(
        new ASContext(collection)));
    
    
  }
  
}
