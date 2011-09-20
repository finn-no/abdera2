package org.apache.abdera2.test.common.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.apache.abdera2.common.http.Authentication;
import org.junit.Test;

public class AuthenticationTest {

  @Test
  public void basicTest() {
    Authentication auth = Authentication.basic("a", "b");
    assertEquals("basic",auth.getScheme());
    assertEquals("YTpi",auth.getBase64Token());
  }
  
  @Test
  public void parseTest() {
    Iterable<Authentication> ia = Authentication.parse("Custom a=b, Custom2 xyz, Custom3 c=\"foo\"");
    Iterator<Authentication> i = ia.iterator();
    Authentication auth = i.next();
    assertEquals("custom",auth.getScheme());
    assertTrue(auth.hasParam("a"));
    assertEquals("b",auth.getParam("a"));
    
    auth = i.next();
    assertEquals("custom2",auth.getScheme());
    assertEquals("xyz",auth.getBase64Token());
    
    auth = i.next();
    assertEquals("custom3",auth.getScheme());
    assertTrue(auth.hasParam("c"));
    assertEquals("foo",auth.getParam("c"));
  }
  
}
