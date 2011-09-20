package org.apache.abdera2.test.common.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.abdera2.common.http.CacheControl;
import org.junit.Test;

public class CacheControlTest {

  public static final String CC = "private=\"A\", public, no-cache=\"A\", no-store, no-transform, only-if-cached, must-revalidate, proxy-revalidate, max-age=10, max-stale=10, min-fresh=10, stale-if-error=10, stale-while-revalidate=10, a=\"b\"";
  
  @Test
  public void testCacheControl() {
    CacheControl cc = new CacheControl();
    cc.setExtension("a", "b");
    assertEquals("b",cc.getExtension("a"));
    cc.setMaxAge(10);
    assertEquals(10,cc.getMaxAge());
    cc.setMaxStale(10);
    assertEquals(10,cc.getMaxStale());
    cc.setMinFresh(10);
    assertEquals(10,cc.getMinFresh());
    cc.setMustRevalidate(true);
    assertTrue(cc.isMustRevalidate());
    cc.setNoCache(true);
    assertTrue(cc.isNoCache());
    cc.setNoCacheHeaders("A");
    String[] hs = cc.getNoCacheHeaders();
    assertEquals(1,hs.length);
    assertEquals("A",hs[0]);
    cc.setNoStore(true);
    assertTrue(cc.isNoStore());
    cc.setNoTransform(true);
    assertTrue(cc.isNoTransform());
    cc.setOnlyIfCached(true);
    assertTrue(cc.isOnlyIfCached());
    cc.setPrivate(true);
    assertTrue(cc.isPrivate());
    cc.setPrivateHeaders("A");
    hs = cc.getNoCacheHeaders();
    assertEquals(1,hs.length);
    assertEquals("A",hs[0]);
    cc.setProxyRevalidate(true);
    assertTrue(cc.isProxyRevalidate());
    cc.setPublic(true);
    assertTrue(cc.isPublic());
    cc.setStaleIfError(10);
    assertEquals(10,cc.getStaleIfError());
    cc.setStaleWhileRevalidate(10);
    assertEquals(10,cc.getStaleWhileRevalidate());
    assertEquals(CC,cc.toString());
  }
  
  @Test
  public void testCacheControl2() {
    CacheControl cc = new CacheControl(CC);
    assertEquals("b",cc.getExtension("a"));
    assertEquals(10,cc.getMaxAge());
    assertEquals(10,cc.getMaxStale());
    assertEquals(10,cc.getMinFresh());
    assertTrue(cc.isMustRevalidate());
    assertTrue(cc.isNoCache());
    String[] hs = cc.getNoCacheHeaders();
    assertEquals(1,hs.length);
    assertEquals("A",hs[0]);
    assertTrue(cc.isNoStore());
    assertTrue(cc.isNoTransform());
    assertTrue(cc.isOnlyIfCached());
    assertTrue(cc.isPrivate());
    hs = cc.getNoCacheHeaders();
    assertEquals(1,hs.length);
    assertEquals("A",hs[0]);
    assertTrue(cc.isProxyRevalidate());
    assertTrue(cc.isPublic());
    assertEquals(10,cc.getStaleIfError());
    assertEquals(10,cc.getStaleWhileRevalidate());
    assertEquals(CC,cc.toString());
  }
}
