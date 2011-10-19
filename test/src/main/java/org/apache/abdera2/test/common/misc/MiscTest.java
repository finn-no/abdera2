package org.apache.abdera2.test.common.misc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.abdera2.common.misc.PoolManager;
import org.junit.Test;

import com.google.common.collect.Iterators;

public class MiscTest {

  @Test
  public void multiIteratorTest() {
    Set<String> a1 = new LinkedHashSet<String>();
    a1.add("a");
    a1.add("b");
    a1.add("c");
    Set<String> a2 = new LinkedHashSet<String>();
    a2.add("d");
    a2.add("e");
    a2.add("f");
    Iterator<String> mi = 
      Iterators.concat(a1.iterator(),a2.iterator());
    assertEquals("a",mi.next());
    assertEquals("b",mi.next());
    assertEquals("c",mi.next());
    assertTrue(mi.hasNext());
    assertEquals("d",mi.next());
    assertEquals("e",mi.next());
    assertEquals("f",mi.next());
    assertFalse(mi.hasNext());
  }
  
  @Test
  public void poolManagerTest() {
    PoolManager<String,String> pm = 
      new PoolManager<String,String>(2) {
        private int c = 0;
        protected String internalNewInstance() {
          return "A"+(c++);
        }
    };
    assertEquals("A0",pm.get(""));
    assertEquals("A1",pm.get(""));
    pm.release("A0");
    assertEquals("A0",pm.get(""));
    pm.release("A0");
    pm.release("A1");
    pm.release("A2");
    assertEquals("A1",pm.get(""));
    assertEquals("A2",pm.get(""));
  }
}
