package org.apache.abdera2.common.security;

import java.security.Key;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Arrays;

import javax.crypto.Mac;

import org.apache.abdera2.common.misc.ExceptionHelper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class HashHelper {

  public static String sig(PrivateKey key, String alg, byte[] mat) {
    try {
      Signature sig = Signature.getInstance(alg);
      sig.initSign((PrivateKey)key);
      sig.update(mat);
      byte[] dat = sig.sign();
      return Base64.encodeBase64URLSafeString(dat);
    } catch (Throwable t) {
      throw ExceptionHelper.propogate(t);
    }
  }
  
  public static String hmac(Key key, String alg, byte[] mat) {
    try {
      Mac mac = Mac.getInstance(alg);
      mac.init(key);
      byte[] sig = mac.doFinal(mat);
      return Base64.encodeBase64URLSafeString(sig);
    } catch (Throwable t) {
      throw ExceptionHelper.propogate(t);
    }
  }
  
  public static boolean sigval(PublicKey key, String alg, byte[] mat, byte[] dat) {
    try {
      Signature sig = Signature.getInstance(alg);
      sig.initVerify(key);
      sig.update(mat);
      return sig.verify(dat);
    } catch (Throwable t) {
      throw ExceptionHelper.propogate(t);
    }
  }
  
  public static boolean hmacval(Key key, String alg, byte[] mat, byte[] dat) {
    try {
      Mac mac = Mac.getInstance(alg);
      mac.init(key);
      byte[] sig = mac.doFinal(mat);
      return Arrays.equals(sig, dat);
    } catch (Throwable t) {
      throw ExceptionHelper.propogate(t);
    }
  }
  
  public static abstract class Hasher {
    public abstract void update(byte[] buf, int s, int e);
    protected abstract byte[] digest();
    public String get() {
      return Hex.encodeHexString(digest());
    }
    public String name() {
      return getClass().getSimpleName().toLowerCase();
    }
  }
  
  public static class Md5 extends Hasher {
    private final MessageDigest md;
    public Md5() {
      this.md = init();
    }
    private MessageDigest init() {
      try {
        return MessageDigest.getInstance("MD5");
      } catch (Throwable t) {
        throw ExceptionHelper.propogate(t);
      }
    }
    public void update(byte[] buf, int s, int e) {
      if (md != null)
        md.update(buf, s, e);
    }
    public byte[] digest() {
      return md.digest();
    }
  }
  
  public abstract static class SHA extends Hasher {
    private final Mac mac;
    SHA(Key key, String alg) {
      this.mac = init(key,alg);
    }
    private Mac init(Key key, String alg) {
      try {
        Mac mac = Mac.getInstance(alg);
        mac.init(key);
        return mac;
      } catch (Throwable t) {
        throw ExceptionHelper.propogate(t);
      }
    }
    public void update(byte[] buf, int s, int e) {
      mac.update(buf, s, e);
    }
    public byte[] digest() {
      return mac.doFinal();
    }
  }
  
  public static class SHA256 extends SHA {
    public SHA256(Key key) {
      super(key, "HmacSHA256");
    }
  }
  
  public static class SHA384 extends SHA {
    public SHA384(Key key) {
      super(key, "HmacSHA384");
    }
  }
  
  public static class SHA512 extends SHA {
    public SHA512(Key key) {
      super(key, "HmacSHA512");
    }
  }
}
