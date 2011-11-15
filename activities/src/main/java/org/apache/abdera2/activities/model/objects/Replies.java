package org.apache.abdera2.activities.model.objects;

import org.apache.abdera2.activities.extra.Extra.ExtensionObject;
import org.apache.abdera2.activities.extra.Extra.ExtensionBuilder;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.Collection;
import org.apache.abdera2.common.anno.Name;

import com.google.common.base.Supplier;

/**
 * Extension interface intended to be used with ASObject.extend() to 
 * provide a type-safe way of working with various extension properties
 * defined by the Activity Streams Replies and Response Draft
 * (http://activitystrea.ms/specs/json/replies/1.0/)
 */
public interface Replies extends ExtensionObject {

  public <X extends ASObject>Collection<X> getAttending();
  public <X extends ASObject>Collection<X> getFollowers();
  public <X extends ASObject>Collection<X> getFollowing();
  public <X extends ASObject>Collection<X> getFriends();
  @Name("friend-requests")
  public <X extends ASObject>Collection<X> getFriendRequests();
  public <X extends ASObject>Collection<X> getLikes();
  public <X extends ASObject>Collection<X> getNotAttending();
  public <X extends ASObject>Collection<X> getMaybeAttending();
  public <X extends ASObject>Collection<X> getMembers();
  public <X extends ASObject>Collection<X> getReplies();
  public <X extends ASObject>Collection<X> getReviews();
  public <X extends ASObject>Collection<X> getSaves();
  public <X extends ASObject>Collection<X> getShares();
  
  
  public static interface Builder extends ExtensionBuilder {
    
    <X extends ASObject>Builder attending(Collection<X> collection);
    <X extends ASObject>Builder followers(Collection<X> collection);
    <X extends ASObject>Builder following(Collection<X> collection);
    <X extends ASObject>Builder friends(Collection<X> collection);
    @Name("friend-requests") 
    <X extends ASObject>Builder friendRequests(Collection<X> collection);
    <X extends ASObject>Builder likes(Collection<X> collection);
    <X extends ASObject>Builder notAttending(Collection<X> collection);
    <X extends ASObject>Builder maybeAttending(Collection<X> collection);
    <X extends ASObject>Builder members(Collection<X> collection);
    <X extends ASObject>Builder replies(Collection<X> collection);
    <X extends ASObject>Builder reviews(Collection<X> collection);
    <X extends ASObject>Builder saves(Collection<X> collection);
    <X extends ASObject>Builder shares(Collection<X> collection);
    
    <X extends ASObject>Builder attending(Supplier<Collection<X>> collection);
    <X extends ASObject>Builder followers(Supplier<Collection<X>> collection);
    <X extends ASObject>Builder following(Supplier<Collection<X>> collection);
    <X extends ASObject>Builder friends(Supplier<Collection<X>> collection);
    @Name("friend-requests") 
    <X extends ASObject>Builder friendRequests(Supplier<Collection<X>> collection);
    <X extends ASObject>Builder likes(Supplier<Collection<X>> collection);
    <X extends ASObject>Builder notAttending(Supplier<Collection<X>> collection);
    <X extends ASObject>Builder maybeAttending(Supplier<Collection<X>> collection);
    <X extends ASObject>Builder members(Supplier<Collection<X>> collection);
    <X extends ASObject>Builder replies(Supplier<Collection<X>> collection);
    <X extends ASObject>Builder reviews(Supplier<Collection<X>> collection);
    <X extends ASObject>Builder saves(Supplier<Collection<X>> collection);
    <X extends ASObject>Builder shares(Supplier<Collection<X>> collection);
    
  }
  
}
