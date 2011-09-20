package org.apache.abdera2.examples.activities;

import org.apache.abdera2.activities.extra.Extra;
import org.apache.abdera2.activities.extra.VersionObject;
import org.apache.abdera2.activities.model.Activity;
import org.apache.abdera2.activities.model.Collection;
import org.apache.abdera2.activities.model.Generator;
import org.apache.abdera2.activities.model.Verb;
import org.apache.abdera2.activities.model.objects.FileObject;
import org.apache.abdera2.activities.model.objects.PersonObject;

public class VersionControlExample {

  public static void main(String... args) throws Exception {
    
    Collection<Activity> stream = 
      new Collection<Activity>();
    
    Activity template = 
      new Activity();
    PersonObject actor = 
      new PersonObject();
    actor.setDisplayName("James");
    template.setActor(actor);
    
    Generator<Activity> gen = 
      template.newGenerator();
    
    
    // first, indicate that we created a document
    
    FileObject file = new FileObject();
    file.setDisplayName("presentation.ppt");
    file.setId("http://example.org/presentation.ppt");
    
    stream.addItem(
      gen.startNew()
         .set("object", file)
         .set("verb", Verb.POST)
         .complete());
    
    // second, indicate that a new version was created
    VersionObject version = new VersionObject();
    version.setOf(file);
    version.setMajor("2");
    
    stream.addItem(
      gen.startNew() 
         .set("object", version)
         .set("verb", Verb.POST)
         .complete());
    
    // whoops, the boss rejected the new version
    stream.addItem(
      gen.startNew()
         .set("object", version)
         .set("verb", Extra.REJECT)
         .set("actor", new PersonObject("The Boss"))
         .set("summary", "This version is missing something")
         .complete());
    
    // create a new version to deal with the bosses concerns
    VersionObject old = version;
    version = new VersionObject();
    version.setOf(file);
    version.setMajor("3");
    version.setPreviousVersion(old);
    
    stream.addItem(
        gen.startNew() 
           .set("object", version)
           .set("verb", Verb.POST)
           .complete());
    
    // the boss approves the new version
    stream.addItem(
      gen.startNew()
         .set("object", version)
         .set("verb", Extra.APPROVE)
         .set("actor", new PersonObject("The Boss"))
         .complete());
    
    
    stream.writeTo(System.out);
  }
  
}


/** Produces:
{
  "items":[
    {
      "verb":"post",
      "object": {

        "id":"http://example.org/presentation.ppt",
        "displayName":"presentation.ppt",
        "objectType":"file"},
      "actor":{

        "displayName":"James",
        "objectType":"person" },
      "objectType":"activity"
    },
    {
      "verb":"post",
      "object":{
        "of":{
          "id":"http://example.org/presentation.ppt",
          "displayName":"presentation.ppt",
          "objectType":"file"},
        "objectType":"version",
        "major":"2"},
      "actor":{
        "displayName":"James",
        "objectType":"person"},
      "objectType":"activity"},
    {
      "summary":"This version is missing something",
      "verb":"reject",
      "object":{
        "of":{
          "id":"http://example.org/presentation.ppt",
          "displayName":"presentation.ppt",
          "objectType":"file"},
        "objectType":"version",
        "major":"2"},
      "actor":{
        "displayName":"The Boss",
        "objectType":"person"},
      "objectType":"activity"},
    {
      "verb":"post",
      "object":{
        "of":{
          "id":"http://example.org/presentation.ppt",
          "displayName":"presentation.ppt",
          "objectType":"file"},
        "previousVersion":{
          "of":{
            "id":"http://example.org/presentation.ppt",
            "displayName":"presentation.ppt",
            "objectType":"file"},
          "objectType":"version",
          "major":"2"},
        "objectType":"version",
        "major":"3"},
      "actor":{
        "displayName":"James",
        "objectType":"person"},
      "objectType":"activity"},
    {
      "verb":"approve",
      "object":{
        "of":{
          "id":"http://example.org/presentation.ppt",
          "displayName":"presentation.ppt",
          "objectType":"file"},
        "previousVersion":{
          "of":{
            "id":"http://example.org/presentation.ppt", 
            "displayName":"presentation.ppt",
            "objectType":"file"},
          "objectType":"version",
          "major":"2"},
        "objectType":"version",
        "major":"3"},
      "actor":{
        "displayName":"The Boss",
        "objectType":"person"},
      "objectType":"activity"}
  ],
  "totalItems":5,
  "objectType":"collection"
}

*/