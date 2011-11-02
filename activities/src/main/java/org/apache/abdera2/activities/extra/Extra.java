package org.apache.abdera2.activities.extra;

import java.lang.reflect.Method;
import java.util.Comparator;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.abdera2.activities.model.ASBase;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.Activity;
import org.apache.abdera2.activities.model.Activity.Audience;
import org.apache.abdera2.activities.model.Verb;
import org.apache.abdera2.common.anno.Name;
import org.apache.abdera2.common.date.DateTimes;
import org.apache.abdera2.common.selector.AbstractSelector;
import org.apache.abdera2.common.selector.PropertySelector;
import org.apache.abdera2.common.selector.Selector;
import org.apache.abdera2.common.selector.MultiSelector;
import org.joda.time.DateTime;

import com.google.common.base.CaseFormat;
import com.google.common.base.Equivalence;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;


import static org.apache.abdera2.activities.model.objects.Objects.*;
import static org.apache.abdera2.common.misc.Comparisons.*;
import static com.google.common.base.Preconditions.*;


/**
 * Miscellaneous extensions
 */
@SuppressWarnings("unchecked")
public class Extra {

  /**
   * Returns a Selector that tests whether the provided 
   * activity uses the given Verb.
   */
  public static Selector<Activity> usesVerb(Verb verb) {
    return PropertySelector.<Activity>create(
      Activity.class, 
      "getVerb", 
      Predicates.equalTo(verb));
  }
  
  /**
   * Returns a selector that tests the given Objects "published" property.
   * This can typically be used with the various Range predicates provided
   * by the (@see org.apache.abdera2.common.date.DateTimes) class
   */
  public static <A extends ASObject>Selector<A> published(
    Class<A> _class, 
    Predicate<?> predicate) {
    return PropertySelector.<A>create(
      _class,
      "getPublished",
      predicate);
  }
  
  /**
   * Returns a selector that tests the given Activities "published" property.
   */
  public static Selector<Activity> activityPublished(Predicate<DateTime> predicate) {
    return published(Activity.class,predicate);
  }
  
  /**
   * Returns a selector that tests the given Objects "published" property
   */
  public static Selector<ASObject> objectPublished(Predicate<DateTime> predicate) {
    return published(ASObject.class,predicate);
  }
  
  /**
   * Returns a Selector that tests the given Objects "updated" property.
   * This can typically be used with the various Range predicates provided
   * by the (@see org.apache.abder2.common.date.DateTimes) class
   */
  public static <A extends ASObject>Selector<A> updated(Class<A> _class, Predicate<DateTime> predicate) {
    return PropertySelector.<A>create(
      _class,
      "getUpdated",
      predicate);
  }
  
  /**
   * Returns a Selector that tests the given Activities "updated" property.
   */
  public static Selector<Activity> activityUpdated(Predicate<DateTime> predicate) {
    return updated(Activity.class,predicate);
  }
  
  /**
   * Returns a Selector that tests the given Objects "updated" property
   */
  public static Selector<ASObject> objectUpdated(Predicate<DateTime> predicate) {
    return updated(ASObject.class,predicate);
  }
       
  public static Selector<Activity> isTo(ASObject obj) {
    return Extra.audienceHas(Audience.TO, sameIdentity(obj));
  }
  
  public static Selector<Activity> isBcc(ASObject obj) {
    return Extra.audienceHas(Audience.BCC, sameIdentity(obj));
  }
  
  public static Selector<Activity> isCc(ASObject obj) {
    return Extra.audienceHas(Audience.CC, sameIdentity(obj));
  }
  
  public static Selector<Activity> isBTo(ASObject obj) {
    return Extra.audienceHas(Audience.BTO, sameIdentity(obj));
  }
  
  public static Selector<Activity> isBccMe() {
    return audienceHasMe(Audience.BCC);
  }
  
  public static Selector<Activity> isBccMeOr(ASObject obj) {
    return audienceHasMeOr(Audience.BCC, obj);
  }
  
  public static Selector<Activity> isBccSelf() {
    return audienceHasSelf(Audience.BCC);
  }
  
  public static Selector<Activity> isBccSelfOr(ASObject obj) {
    return audienceHasSelfOr(Audience.BCC, obj);
  }
  
  public static Selector<Activity> isBccFriends() {
    return audienceHasFriends(Audience.BCC);
  }
  
  public static Selector<Activity> isBccFriendsOr( ASObject obj) {
    return audienceHasFriendsOr(Audience.BCC, obj);
  }
  
  public static Selector<Activity> isBccFriends( String id) {
    return audienceHasFriends(Audience.BCC, id);
  }
  
  public static Selector<Activity> isBccFriendsOr( String id, ASObject obj) {
    return audienceHasFriendsOr(Audience.BCC, id, obj);
  }
  
  public static Selector<Activity> isBccNetwork() {
    return audienceHasNetwork(Audience.BCC);
  }
  
  public static Selector<Activity> isBccNetworkOr( ASObject obj) {
    return audienceHasNetworkOr(Audience.BCC, obj);
  }
  
  public static Selector<Activity> isBccAll() {
    return audienceHasAll(Audience.BCC);
  }
  
  public static Selector<Activity> isBccAllOr( ASObject obj) {
    return audienceHasAllOr(Audience.BCC, obj);
  }
  
  public static Selector<Activity> isBccPublic() {
    return audienceHasPublic(Audience.BCC);
  }
  
  public static Selector<Activity> isBccPublicOr( ASObject obj) {
    return audienceHasPublicOr(Audience.BCC, obj);
  }
  
  public static Selector<Activity> isCcMe() {
    return audienceHasMe(Audience.CC);
  }
  
  public static Selector<Activity> isCcMeOr( ASObject obj) {
    return audienceHasMeOr(Audience.CC, obj);
  }
  
  public static Selector<Activity> isCcSelf() {
    return audienceHasSelf(Audience.CC);
  }
  
  public static Selector<Activity> isCcSelfOr( ASObject obj) {
    return audienceHasSelfOr(Audience.CC, obj);
  }
  
  public static Selector<Activity> isCcFriends() {
    return audienceHasFriends(Audience.CC);
  }
  
  public static Selector<Activity> isCcFriendsOr( ASObject obj) {
    return audienceHasFriendsOr(Audience.CC, obj);
  }
  
  public static Selector<Activity> isCcFriends( String id) {
    return audienceHasFriends(Audience.CC, id);
  }
  
  public static Selector<Activity> isCcFriendsOr( String id, ASObject obj) {
    return audienceHasFriendsOr(Audience.CC, id, obj);
  }
  
  public static Selector<Activity> isCcNetwork() {
    return audienceHasNetwork(Audience.CC);
  }
  
  public static Selector<Activity> isCcNetworkOr( ASObject obj) {
    return audienceHasNetworkOr(Audience.CC, obj);
  }
  
  public static Selector<Activity> isCcAll() {
    return audienceHasAll(Audience.CC);
  }
  
  public static Selector<Activity> isCcAllOr( ASObject obj) {
    return audienceHasAllOr(Audience.CC, obj);
  }
  
  public static Selector<Activity> isCcPublic() {
    return audienceHasPublic(Audience.CC);
  }
  
  public static Selector<Activity> isCcPublicOr( ASObject obj) {
    return audienceHasPublicOr(Audience.CC, obj);
  }
  
  public static Selector<Activity> isBtoMe() {
    return audienceHasMe(Audience.BTO);
  }
  
  public static Selector<Activity> isBtoMeOr( ASObject obj) {
    return audienceHasMeOr(Audience.BTO, obj);
  }
  
  public static Selector<Activity> isBtoSelf() {
    return audienceHasSelf(Audience.BTO);
  }
  
  public static Selector<Activity> isBtoSelfOr( ASObject obj) {
    return audienceHasSelfOr(Audience.BTO, obj);
  }
  
  public static Selector<Activity> isBtoFriends() {
    return audienceHasFriends(Audience.BTO);
  }
  
  public static Selector<Activity> isBtoFriendsOr( ASObject obj) {
    return audienceHasFriendsOr(Audience.BTO, obj);
  }
  
  public static Selector<Activity> isBtoFriends( String id) {
    return audienceHasFriends(Audience.BTO, id);
  }
  
  public static Selector<Activity> isBtoFriendsOr( String id, ASObject obj) {
    return audienceHasFriendsOr(Audience.BTO, id, obj);
  }
  
  public static Selector<Activity> isBtoNetwork() {
    return audienceHasNetwork(Audience.BTO);
  }
  
  public static Selector<Activity> isBtoNetworkOr( ASObject obj) {
    return audienceHasNetworkOr(Audience.BTO, obj);
  }
  
  public static Selector<Activity> isBtoAll() {
    return audienceHasAll(Audience.BTO);
  }
  
  public static Selector<Activity> isBtoAllOr( ASObject obj) {
    return audienceHasAllOr(Audience.BTO, obj);
  }
  
  public static Selector<Activity> isBtoPublic() {
    return audienceHasPublic(Audience.BTO);
  }
  
  public static Selector<Activity> isBtoPublicOr( ASObject obj) {
    return audienceHasPublicOr(Audience.BTO, obj);
  }
  
  public static Selector<Activity> isToMe() {
    return audienceHasMe(Audience.TO);
  }
  
  public static Selector<Activity> isToMeOr( ASObject obj) {
    return audienceHasMeOr(Audience.TO, obj);
  }
  
  public static Selector<Activity> isToSelf() {
    return audienceHasSelf(Audience.TO);
  }
  
  public static Selector<Activity> isToSelfOr( ASObject obj) {
    return audienceHasSelfOr(Audience.TO, obj);
  }
  
  public static Selector<Activity> isToFriends() {
    return audienceHasFriends(Audience.TO);
  }
  
  public static Selector<Activity> isToFriendsOr( ASObject obj) {
    return audienceHasFriendsOr(Audience.TO, obj);
  }
  
  public static Selector<Activity> isToFriends( String id) {
    return audienceHasFriends(Audience.TO, id);
  }
  
  public static Selector<Activity> isToFriendsOr( String id, ASObject obj) {
    return audienceHasFriendsOr(Audience.TO, id, obj);
  }
  
  public static Selector<Activity> isToNetwork() {
    return audienceHasNetwork(Audience.TO);
  }
  
  public static Selector<Activity> isToNetworkOr( ASObject obj) {
    return audienceHasNetworkOr(Audience.TO, obj);
  }
  
  public static Selector<Activity> isToAll() {
    return audienceHasAll(Audience.TO);
  }
  
  public static Selector<Activity> isToAllOr( ASObject obj) {
    return audienceHasAllOr(Audience.TO, obj);
  }
  
  public static Selector<Activity> isToPublic() {
    return audienceHasPublic(Audience.TO);
  }
  
  public static Selector<Activity> isToPublicOr( ASObject obj) {
    return audienceHasPublicOr(Audience.TO, obj);
  }
  
  public static Selector<Activity> actorIsMe() {
    return actorIs(Extra.<Activity>isMe());
  }
  
  public static Selector<Activity> actorIsMeOr(ASObject object) {
    return actorIs(Extra.<Activity>isMeOr(object));
  }
  
  public static Selector<Activity> actorIsSelf() {
    return actorIs(Extra.<Activity>isSelf());
  }
  
  public static Selector<Activity> actorIsSelfOr(ASObject object) {
    return actorIs(Extra.<Activity>isSelfOr(object));
  }
  
  public static Selector<Activity> actorIsFriends() {
    return actorIs(Extra.<Activity>isFriends());
  }
  
  public static Selector<Activity> actorIsFriendsOr(ASObject object) {
    return actorIs(Extra.<Activity>isFriendsOr(object));
  }
  
  public static Selector<Activity> actorIsFriends(String id) {
    return actorIs(Extra.<Activity>isFriends(id));
  }
  
  public static Selector<Activity> actorIsFriendsOr(String id, ASObject object) {
    return actorIs(Extra.<Activity>isFriendsOr(id, object));
  }
  
  public static Selector<Activity> actorIsNetwork() {
    return actorIs(Extra.<Activity>isNetwork());
  }
  
  public static Selector<Activity> actorIsNetworkOr(ASObject object) {
    return actorIs(Extra.<Activity>isNetworkOr(object));
  }
  
  public static Selector<Activity> actorIsAll() {
    return actorIs(Extra.<Activity>isAll());
  }
  
  public static Selector<Activity> actorIsAllOr(ASObject object) {
    return actorIs(Extra.<Activity>isAllOr(object));
  }
  
  public static Selector<Activity> actorIsPublic() {
    return actorIs(Extra.<Activity>isPublic());
  }
  
  public static Selector<Activity> actorIsPublicOr(ASObject object) {
    return actorIs(Extra.<Activity>isPublicOr(object));
  }
  
  private static Selector<Activity> actorIs(Predicate<Activity> pred) {
    return 
        PropertySelector
          .<Activity>create(
            Activity.class, 
            "getActor", 
            pred);
  }
  
  public static Selector<Activity> audienceHasMe(Audience audience) {
    return audienceHas(audience,isMe());
  }
  
  public static Selector<Activity> audienceHasMeOr(Audience audience, ASObject obj) {
    return audienceHas(audience,isMeOr(obj));
  }
  
  public static Selector<Activity> audienceHasSelf(Audience audience) {
    return audienceHas(audience,isSelf());
  }
  
  public static Selector<Activity> audienceHasSelfOr(Audience audience, ASObject obj) {
    return audienceHas(audience,isSelfOr(obj));
  }
  
  public static Selector<Activity> audienceHasFriends(Audience audience) {
    return audienceHas(audience,isFriends());
  }
  
  public static Selector<Activity> audienceHasFriendsOr(Audience audience, ASObject obj) {
    return audienceHas(audience,isFriendsOr(obj));
  }
  
  public static Selector<Activity> audienceHasFriends(Audience audience, String id) {
    return audienceHas(audience,isFriends(id));
  }
  
  public static Selector<Activity> audienceHasFriendsOr(Audience audience, String id, ASObject obj) {
    return audienceHas(audience,isFriendsOr(id,obj));
  }
  
  public static Selector<Activity> audienceHasNetwork(Audience audience) {
    return audienceHas(audience,isNetwork());
  }
  
  public static Selector<Activity> audienceHasNetworkOr(Audience audience, ASObject obj) {
    return audienceHas(audience,isNetworkOr(obj));
  }
  
  public static Selector<Activity> audienceHasAll(Audience audience) {
    return audienceHas(audience,isMe());
  }
  
  public static Selector<Activity> audienceHasAllOr(Audience audience, ASObject obj) {
    return audienceHas(audience,isAllOr(obj));
  }
  
  public static Selector<Activity> audienceHasPublic(Audience audience) {
    return audienceHas(audience,isPublic());
  }
  
  public static Selector<Activity> audienceHasPublicOr(Audience audience, ASObject obj) {
    return audienceHas(audience,isPublicOr(obj));
  }
  
  private static Selector<Activity> audienceHas(
    final Audience audience, 
    final Selector<ASObject> pred) {
      return new AbstractSelector<Activity>() {
        public boolean select(Object item) {
          checkArgument(item instanceof Activity);
          Activity activity = (Activity) item;
          Iterable<ASObject> aud = 
            activity.getAudience(audience, pred);
          return !Iterables.isEmpty(aud);
        }
      };
  }
  
  
  public static <X extends ASObject>Selector<X> isMe() {
    return (Selector<X>)sameIdentity(ME());
  }
  
  public static <X extends ASObject>Selector<X> isSelf() {
    return (Selector<X>)sameIdentity(SELF());
  }
  
  public static <X extends ASObject>Selector<X> isFriends() {
    return (Selector<X>)sameIdentity(FRIENDS());
  }
  
  public static <X extends ASObject>Selector<X> isFriends(String id) {
    return (Selector<X>)sameIdentity(FRIENDS(id));
  }
  
  public static <X extends ASObject>Selector<X> isNetwork() {
    return (Selector<X>)sameIdentity(NETWORK());
  }
  
  public static <X extends ASObject>Selector<X> isAll() {
    return (Selector<X>)sameIdentity(ALL());
  }
  
  public static <X extends ASObject>Selector<X> isPublic() {
    return (Selector<X>)sameIdentity(PUBLIC());
  }
  
  public static <X extends ASObject>Selector<X> isMeOr(ASObject object) {
    Selector<X> s1 = sameIdentity(object);
    Selector<X> s2 = sameIdentity(ME());
    return MultiSelector.<X>or(s1,s2);
  }
  
  public static <X extends ASObject>Selector<X> isSelfOr(ASObject object) {
    Selector<X> s1 = sameIdentity(object);
    Selector<X> s2 = sameIdentity(SELF());
    return MultiSelector.<X>or(s1,s2);
  }
  
  public static <X extends ASObject>Selector<X> isFriendsOr(ASObject object) {
    Selector<X> s1 = sameIdentity(object);
    Selector<X> s2 = sameIdentity(FRIENDS());
    return MultiSelector.<X>or(s1,s2);
  }
  
  public static <X extends ASObject>Selector<X> isFriendsOr(String id, ASObject object) {
    Selector<X> s1 = sameIdentity(object);
    Selector<X> s2 = sameIdentity(FRIENDS(id));
    return MultiSelector.<X>or(s1,s2);
  }
  
  public static <X extends ASObject>Selector<X> isNetworkOr(ASObject object) {
    Selector<X> s1 = sameIdentity(object);
    Selector<X> s2 = sameIdentity(NETWORK());
    return MultiSelector.<X>or(s1,s2);
  }
  
  public static <X extends ASObject>Selector<X> isAllOr(ASObject object) {
    Selector<X> s1 = sameIdentity(object);
    Selector<X> s2 = sameIdentity(ALL());
    return MultiSelector.<X>or(s1,s2);
  }
  
  public static <X extends ASObject>Selector<X> isPublicOr(ASObject object) {
    Selector<X> s1 = sameIdentity(object);
    Selector<X> s2 = sameIdentity(PUBLIC());
    return MultiSelector.<X>or(s1,s2);
  }
  
  /**
   * Returns a Selector that tests if two objects are identity equivalent.
   * ASObjets are identity equivalent if they have the same objectType
   * and id property values.
   */
  public static <X extends ASObject>Selector<X> sameIdentity(final ASObject obj) {
    return new AbstractSelector<X>() {
      public boolean select(Object item) {
        checkArgument(item instanceof ASObject);
        ASObject other = (ASObject) item;
        return IDENTITY_EQUIVALENCE.equivalent(obj, other);
      }     
    };
  }
  
  /**
   * Equivalence instance that can be used to check the equivalence of two
   * ASObjects
   */
  public static final Equivalence<ASObject> IDENTITY_EQUIVALENCE = identity();
  
  /**
   * Two ASObject's are considered equivalent in identity if 
   * they share the same objectType and id property
   * values. 
   */
  private static Equivalence<ASObject> identity() {
    return new Equivalence<ASObject>() {
      protected boolean doEquivalent(ASObject a, ASObject b) {
        if (bothAreNull(a,b)) return true;
        if (onlyOneIsNull(a,b)) return false;
        String aot = a.getObjectType();
        String bot = b.getObjectType();
        if (bothAreNull(aot,bot)) return true;
        if (onlyOneIsNull(aot,bot)) return false;
        if (!aot.equalsIgnoreCase(bot)) return false;
        String aid = a.getId();
        String bid = b.getId();
        if (bothAreNull(aid,bid)) return true;
        if (onlyOneIsNull(aid,bid)) return false;
        if (!aid.equals(bid)) return false;
        return true;
      }
      protected int doHash(ASObject t) {
        String id = t.getId();
        String objectType = t.getObjectType();
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
            + ((objectType == null) ? 0 : objectType.hashCode());
        return result;
      }
    };
  }
  
  public static final Comparator<ASObject> UPDATED_COMPARATOR = 
    new UpdatedComparator();
  public static final Comparator<ASObject> PUBLISHED_COMPARATOR = 
    new PublishedComparator();
  
  private static class UpdatedComparator 
    extends DateTimes.DateTimeComparator<ASObject> {
      public int compare(ASObject a1, ASObject a2) {
        DateTime d1 = a1.getUpdated();
        DateTime d2 = a2.getUpdated();
        return innerCompare(d1,d2);
      }
  }
  
  private static class PublishedComparator 
    extends DateTimes.DateTimeComparator<ASObject> {
      public int compare(ASObject a1, ASObject a2) {
        return innerCompare(
          a1.getPublished(), 
          a2.getPublished());
      }
  }
  
  
  private static Class<?>[] addin(Class<?>[] types, Class<?> type) {
    Class<?>[] ntypes = new Class<?>[types.length+1];
    System.arraycopy(types, 0, ntypes, 0, types.length);
    ntypes[ntypes.length-1] = type;
    return ntypes;
  }
  
  /**
   * Uses cglib to create an extension of the base ASObject type
   * that implements the given interface. All setter/getter methods
   * on the supplied interface will be mapped to properties on the
   * underlying ASObject.. for instance, getFoo() and setFoo(..) will
   * be mapped to a "foo" property
   */
  public static <X extends ASBase,M>M extend(
    X object,
    Class<?> type) {
    checkNotNull(type);
    checkNotNull(object);
    Enhancer e = new Enhancer();
    if (type.isInterface()) {
      e.setSuperclass(type);
      e.setInterfaces(addin(object.getClass().getInterfaces(),type));
    } else if (ASObject.class.isAssignableFrom(type)) {
      e.setSuperclass(type);
    }
    e.setCallback(new ExtensionWrapper(type,object));
    return (M)e.create();
  }
  
  
  private static class ExtensionWrapper 
    implements MethodInterceptor {
    private final Class<?> type;
    private final ASBase base;
    ExtensionWrapper(
      Class<?> type, 
      ASBase base) {
      this.type = type;
      this.base = base;
    }
    public Object intercept(
      Object obj, 
      Method method, 
      Object[] args,
      MethodProxy proxy) 
        throws Throwable {
      if (method.getDeclaringClass().equals(type)) {
        boolean setter = 
          method.getName().matches("[Ss]et.+") || 
          (void.class.isAssignableFrom(method.getReturnType()) && 
          method.getParameterTypes().length == 1);
        String name = get_name(method);
        if (setter) {
          if (args.length != 1)
            throw new UnsupportedOperationException();
          base.setProperty(name,args[0]);
          return null; 
        } else if (method.getParameterTypes().length == 0) {
          return method.getReturnType().cast(base.getProperty(name));
        } else {
          throw new UnsupportedOperationException();
        }
      } else return proxy.invokeSuper(base, args);
    }    
  }
  
  private static String get_name(Method obj) {   
    String name = null;
    if (obj.isAnnotationPresent(Name.class))
      name = obj.getAnnotation(Name.class).value();
    else {
      name = obj.getName();
      if (name.startsWith("get") || 
        name.startsWith("set"))
      name = name.substring(3);
      name = CaseFormat.UPPER_CAMEL.to(
          CaseFormat.LOWER_CAMEL, name);
    }
    return name;
  }
}
