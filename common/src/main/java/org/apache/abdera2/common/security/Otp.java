package org.apache.abdera2.common.security;
import java.security.Key;

import com.google.common.base.Supplier;

/**
 * Utility class for generating One-Time-Passwords using the HOTP algorithm
 */
public abstract class Otp extends KeyBase {

  protected Otp(byte[] key, int size) {
    super(key, size);
  }

  protected Otp(byte[] key) {
    super(key,8);
  }
  
  protected Otp(byte[] key, String alg, int size) {
    super(key, alg, size);
  }

  protected Otp(Key key, int size) {
    super(key, size);
  }

  protected Otp(Key key, String alg, int size) {
    super(key, alg, size);
  }

  protected Otp(Key key) {
    super(key,8);
  }

  protected Otp(String key, int size) {
    super(key, size);
  }

  protected Otp(String key, String alg, int size) {
    super(key, alg, size);
  }

  protected Otp(String key) {
    super(key,8);
  }

  /**
   * Return the moving factor for this one-time-password. The
   * moving factor is the "thing that changes" each time the 
   * password is generated, resulting in a new password each
   * time generateNext is called. For most applications, this 
   * should be a 
   */
  protected abstract byte[] getMovingFactor();
  
  /**
   * Generates the next One-time-password based on the key and
   * a moving factor retrieved by calling getMovingFactor(). 
   * The Otp subclass instance is responsible for maintaining 
   * the state necessary for retrieving the appropriate moving
   * factor
   */
  public String generateNext(){
    int len = Math.max(1, Math.min(9, size));
    byte[] h = hmac(getMovingFactor());
    int o = h[h.length - 1] & 0xf;
    return pad(
      Integer.toString(
        (((h[o] & 0x7f) << 24) |
        ((h[o + 1] & 0xff) << 16) |
        ((h[o + 2] & 0xff) << 8) |
        (h[o + 3] & 0xff))
          % (int)Math.pow(10, len)),
      len,'0');
  } 
  
  private static class OtpSupplier implements Supplier<String> {
    private final Otp otp;
    OtpSupplier(Otp otp) {
      this.otp = otp;
    }
    public String get() {
      return otp.generateNext();
    }
    
  }
  
  public static Supplier<String> supplier(Otp otp) {
    return new OtpSupplier(otp);
  }
  
  public static Supplier<String> totpSupplier(byte[] key, int step, int size) {
    return new OtpSupplier(new Totp(step,key,size));
  }
  
  public static Supplier<String> totpSupplier(byte[] key, int step) {
    return new OtpSupplier(new Totp(step,key));
  }
  
  public static Supplier<String> totpSupplier(byte[] key, int step, int size, String alg) {
    return new OtpSupplier(new Totp(step,key,alg,size));
  }
  
  public static Supplier<String> totpSupplier(Key key, int step, int size) {
    return new OtpSupplier(new Totp(step,key,size));
  }
  
  public static Supplier<String> totpSupplier(Key key, int step) {
    return new OtpSupplier(new Totp(step,key));
  }
  
  public static Supplier<String> totpSupplier(Key key, int step, int size, String alg) {
    return new OtpSupplier(new Totp(step,key,alg,size));
  }
  
  public static Supplier<String> totpSupplier(String key, int step, int size) {
    return new OtpSupplier(new Totp(step,key,size));
  }
  
  public static Supplier<String> totpSupplier(String key, int step) {
    return new OtpSupplier(new Totp(step,key));
  }
  
  public static Supplier<String> totpSupplier(String key, int step, int size, String alg) {
    return new OtpSupplier(new Totp(step,key,alg,size));
  }
  
  /**
   * Utility implementation of the Time-based One Time Password (TOTP) 
   * algorithm. 
   */
  public static class Totp extends Otp {

    private final int step;
    
    public Totp(int step, byte[] key, int size) {
      super(key, size);
      this.step = step;
    }

    public Totp(int step, byte[] key) {
      super(key,8);
      this.step = step;
    }
    
    public Totp(int step, byte[] key, String alg, int size) {
      super(key, alg, size);
      this.step = step;
    }

    public Totp(int step, Key key, int size) {
      super(key, size);
      this.step = step;
    }

    public Totp(int step, Key key, String alg, int size) {
      super(key, alg, size);
      this.step = step;
    }

    public Totp(int step, Key key) {
      super(key);
      this.step = step;
    }

    public Totp(int step, String key, int size) {
      super(key, size);
      this.step = step;
    }

    public Totp(int step, String key, String alg, int size) {
      super(key, alg, size);
      this.step = step;
    }

    public Totp(int step, String key) {
      super(key);
      this.step = step;
    }

    @Override
    protected byte[] getMovingFactor() {
      long t = (System.currentTimeMillis() / 1000l) / step;
      String r = Long.toHexString(t);
      return dec(pad(r,16,'0'));
    }
  }
}
