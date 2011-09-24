package org.apache.abdera2.common.security;
import java.security.SecureRandom;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public abstract class KeyBase {

  public static final String DEFAULT_ALG="HmacSHA256";
  public static final int DEFAULT_SIZE=256;
  
  protected final SecretKeySpec key;
  protected final String alg;
  protected final int size;
  
  public abstract String generateNext();
  
  public KeyBase(SecretKeySpec key) {
    this(key,DEFAULT_ALG,DEFAULT_SIZE);
  }
  
  public KeyBase(SecretKeySpec key, int size) {
    this(key,DEFAULT_ALG,size);
  }
  
  public KeyBase(SecretKeySpec key, String alg, int size) { 
    this.key = key;
    this.alg = alg;
    this.size = size;
  }
  
  public KeyBase(String key) {
    this(key,DEFAULT_ALG,DEFAULT_SIZE);
  }
  
  public KeyBase(String key, int size) {
    this(key,DEFAULT_ALG,size);
  }
  
  public KeyBase(byte[] key, String alg, int size) {
    this.key = secret(key);
    this.alg = alg;
    this.size = size;
  }
  
  public KeyBase(byte[] key, int size) {
    this(key,DEFAULT_ALG,size);
  }
  
  public KeyBase(String key, String alg, int size) {
    this(dec(key),alg,size);
  }
  
  protected SecretKeySpec secret(byte[] key) {
    return new SecretKeySpec(key, "RAW");
  }
  
  protected static byte[] dec(String val) {
    try {
      return Hex.decodeHex(val.toCharArray());
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }
  
  protected byte[] hmac(byte[] mat){
    try {
      Mac hmac = Mac.getInstance(alg);
      hmac.init(key);
      return hmac.doFinal(mat);
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
}
  
  protected byte[] randomBytes(int count) {
    SecureRandom random = new SecureRandom();
    byte[] buf = new byte[count];
    random.nextBytes(buf);
    return buf;
  }
}
