package org.apache.abdera2.test.common.text;

import org.apache.abdera2.common.text.CharUtils;
import org.apache.abdera2.common.text.InvalidCharacterException;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CharUtilsTest {

  @Test
  public void isAlphaTest() {
    for (char c = 'A'; c <= 'Z'; c++)
      assertTrue(CharUtils.isAlpha(c));
    for (char c = 'a'; c <= 'z'; c++)
      assertTrue(CharUtils.isAlpha(c));
    assertFalse(CharUtils.isAlpha('1'));
  }
  
  @Test
  public void isDigitTest() {
    assertTrue(CharUtils.isDigit('0'));
    assertTrue(CharUtils.isDigit('1'));
    assertTrue(CharUtils.isDigit('2'));
    assertTrue(CharUtils.isDigit('3'));
    assertTrue(CharUtils.isDigit('4'));
    assertTrue(CharUtils.isDigit('5'));
    assertTrue(CharUtils.isDigit('6'));
    assertTrue(CharUtils.isDigit('7'));
    assertTrue(CharUtils.isDigit('8'));
    assertTrue(CharUtils.isDigit('9'));
    assertFalse(CharUtils.isDigit('A'));
  }
  
  @Test
  public void verifyTest() {
    CharUtils.verify(new char[] {'t','e','s','t','1'}, CharUtils.Profile.ALPHANUM);
  }
  
  @Test(expected=InvalidCharacterException.class)
  public void verifyFailureTest() {
    CharUtils.verify(new char[] {'t','e','s','t','1',','}, CharUtils.Profile.ALPHANUM);
  }
}
