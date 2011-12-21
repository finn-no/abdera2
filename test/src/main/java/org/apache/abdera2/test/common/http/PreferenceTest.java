package org.apache.abdera2.test.common.http;

import org.apache.abdera2.common.http.Preference;
import static org.apache.abdera2.common.http.Preference.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Iterables;

public class PreferenceTest {

  @Test
  public void testPreference() {
    
    String prefs = 
      Preference.toString(
        PREF_LENIENT, 
        WAIT(10), 
        PREF_RETURN_ASYNCH,
        make("A","B").param("B", "c").param("C","fo\u00F6 bar baz").get());

    assertEquals("lenient,wait=10,return-asynch,a=B;b=c;c*=UTF-8''fo%C3%B6%20bar%20baz",prefs);
    
    Iterable<Preference> list = parse(prefs);
    assertTrue(contains(list, WAIT(10)));
    assertTrue(contains(list, PREF_LENIENT));
    assertTrue(contains(list, PREF_RETURN_ASYNCH));
    assertTrue(contains(list, make("A").get()));
    
    Preference wait = get(list,"wait");
    assertEquals(10,wait.getIntValue());
    
    Preference a = get(list,"a");
    assertEquals("B",a.getValue());
    assertTrue(a.hasParam("b"));
    assertEquals("c",a.getParam("B"));
    assertEquals("fo\u00F6 bar baz", a.getParam("C"));    
    list = parse("lenient , wait=;foo=");
    assertEquals(2,Iterables.size(list));
    
  }
  
}
