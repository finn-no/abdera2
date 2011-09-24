package org.apache.abdera2.common.security;
import javax.crypto.spec.SecretKeySpec;


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

  protected Otp(SecretKeySpec key, int size) {
    super(key, size);
  }

  protected Otp(SecretKeySpec key, String alg, int size) {
    super(key, alg, size);
  }

  protected Otp(SecretKeySpec key) {
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

  protected abstract String getMaterial();
  
  public String generateNext(){
    String mat = getMaterial();
    int len = Math.max(1, Math.min(9, size));
    while (mat.length() < 16 )
      mat = "0" + mat;
    byte[] h = hmac(dec(mat));
    int o = h[h.length - 1] & 0xf;
    int binary =
        ((h[o] & 0x7f) << 24) |
        ((h[o + 1] & 0xff) << 16) |
        ((h[o + 2] & 0xff) << 8) |
        (h[o + 3] & 0xff);
    int otp = binary % (int)Math.pow(10, len);
    String r = Integer.toString(otp);
    while (r.length() < len)
        r = "0" + r;
    return r;
  }
  
  /**
   * Utility implementation of the Time-based One Time Password (TOTP) 
   * algorithm. 
   */
  public static final class Totp extends Otp {

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

    public Totp(int step, SecretKeySpec key, int size) {
      super(key, size);
      this.step = step;
    }

    public Totp(int step, SecretKeySpec key, String alg, int size) {
      super(key, alg, size);
      this.step = step;
    }

    public Totp(int step, SecretKeySpec key) {
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
    protected String getMaterial() {
      long t = (System.currentTimeMillis() / 1000l) / step;
      String r = Long.toHexString(t);
      while(r.length()<16) r = "0"+r;
      return r;
    }
  }
}
