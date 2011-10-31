package org.apache.abdera2.examples.activities;

import org.apache.abdera2.activities.extra.Extra;
import org.apache.abdera2.activities.extra.VersionObject;
import org.apache.abdera2.activities.model.Activity;
import org.apache.abdera2.activities.model.Collection;
import org.apache.abdera2.activities.model.Generator;
import org.apache.abdera2.activities.model.Verb;
import org.apache.abdera2.activities.model.objects.FileObject;

import static org.apache.abdera2.activities.model.Collection.makeCollection;
import static org.apache.abdera2.activities.model.Activity.makeActivity;
import static org.apache.abdera2.activities.model.objects.PersonObject.makePerson;
import static org.apache.abdera2.activities.model.objects.FileObject.makeFile;
import static org.apache.abdera2.activities.extra.VersionObject.makeVersion;

public class VersionControlExample {

  public static void main(String... args) throws Exception {
        
    Generator<Activity> gen = 
      makeActivity()
      .actor(
        makePerson()
          .displayName("James")
          .get())
      .get()
      .newGenerator();
    
    Collection.CollectionGenerator<Activity> builder = 
      makeCollection();
    
    // first, indicate that we created a document
    FileObject file = 
      makeFile()
        .id("http://example.org/presentation.ppt")
        .displayName("presentation.ppt")
        .get();
    
    builder.item(
      gen.startNew()
        .set("object", file)
        .set("verb", Verb.POST)
        .complete());
    
    // second, indicate that a new version was created
    VersionObject version = 
      makeVersion()
        .of(file)
        .major("2")
        .get();
    
    builder.item(
      gen.startNew() 
         .set("object", version)
         .set("verb", Verb.POST)
         .complete());
    
    // whoops, the boss rejected the new version
    builder.item(
      gen.startNew()
         .set("object", version)
         .set("verb", Extra.REJECT)
         .set("actor", makePerson().displayName("The Boss").get())
         .set("summary", "This version is missing something")
         .complete());
    
    // create a new version to deal with the bosses concerns
    VersionObject old = version;
    version = makeVersion()
      .of(file)
      .major("3")
      .previous(old)
      .get();
    
    builder.item(
        gen.startNew() 
           .set("object", version)
           .set("verb", Verb.POST)
           .complete());
    
    // the boss approves the new version
    builder.item(
      gen.startNew()
         .set("object", version)
         .set("verb", Extra.APPROVE)
         .set("actor", makePerson().displayName("The Boss").get())
         .complete());
    
    builder.get().writeTo(System.out);
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