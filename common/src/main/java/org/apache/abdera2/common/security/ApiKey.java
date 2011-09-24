package org.apache.abdera2.common.security;

import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

/**
 * Utility Class used for Generating API Keys
 */
public class ApiKey extends KeyBase {

  public ApiKey(byte[] key, int size) {
    super(key, size);
  }

  public ApiKey(byte[] key, String alg, int size) {
    super(key, alg, size);
  }

  public ApiKey(SecretKeySpec key, int size) {
    super(key, size);
  }

  public ApiKey(SecretKeySpec key, String alg, int size) {
    super(key, alg, size);
  }

  public ApiKey(SecretKeySpec key) {
    super(key);
  }

  public ApiKey(String key, int size) {
    super(key, size);
  }

  public ApiKey(String key, String alg, int size) {
    super(key, alg, size);
  }

  public ApiKey(String key) {
    super(key);
  }

  public String generateNext() {
    int len = Math.min(20, size);
    byte[] buf = hmac(randomBytes(len));
    buf = Base64.encodeBase64(buf, false, true);
    StringBuilder sb = new StringBuilder();
    for (byte b : buf)
      sb.append(Character.isLetterOrDigit(b)?(char)b:'.');
    return sb.toString();
  }
 
  public String generateNextHex() {
    int len = Math.min(20, size);
    byte[] buf = hmac(randomBytes(len));
    return Hex.encodeHexString(buf);
  }
  
  public static ApiKey WEAK(byte[] key) {
    return new ApiKey(key,"HmacSHA1",20);
  }
  
  public static ApiKey WEAK(SecretKeySpec key) {
    return new ApiKey(key,"HmacSHA1",20);
  }
  
  public static ApiKey WEAK(String key) {
    return new ApiKey(key,"HmacSHA1",20);
  }
  
  public static ApiKey MEDIUM(byte[] key) {
    return new ApiKey(key,"HmacSHA256",256);
  }
  
  public static ApiKey MEDIUM(SecretKeySpec key) {
    return new ApiKey(key,"HmacSHA256",256);
  }
  
  public static ApiKey MEDIUM(String key) {
    return new ApiKey(key,"HmacSHA256",256);
  }
  
  public static ApiKey STRONG(byte[] key) {
    return new ApiKey(key,"HmacSHA512",512);
  }
  
  public static ApiKey STRONG(SecretKeySpec key) {
    return new ApiKey(key,"HmacSHA512",512);
  }
  
  public static ApiKey STRONG(String key) {
    return new ApiKey(key,"HmacSHA512",512);
  }
}
