package org.apache.abdera2.activities.extra;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.apache.abdera2.activities.model.ASBase;
import org.apache.abdera2.activities.model.IO;
import org.apache.abdera2.common.security.HashHelper;
import org.apache.commons.codec.binary.Base64;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utility class for Generating/Validating JSON Web Tokens 
 */
public class Jwt {

  public static enum Alg {
    HS256("HmacSHA256"),
    HS384("HmacSHA384"),
    HS512("HmacSHA512"),
    RS256("SHA256withRSA"),
    RS384("SHA384withRSA"),
    RS512("SHA512withRSA"),
    ES256("SHA256withECDSA"),  
    ES384("SHA384withECDSA"),  
    ES512("SHA512withECDSA");
    
    private final String internal;
    
    Alg(String internal) {
      this.internal = internal;
    }
    
    public String sig(Key key, byte[] mat) {
      switch(this) {
      case HS256:
      case HS384:
      case HS512:
        return HashHelper.hmac(key, internal, mat);
      case RS256:
      case RS384:
      case RS512:
      case ES256:
      case ES384:
      case ES512:
        return HashHelper.sig((PrivateKey)key,internal,mat);
      default:
        throw new UnsupportedOperationException();
      }
    }

    public boolean val(Key key, byte[] mat, byte[] dat) {
      try {
        switch(this) {
        case HS256:
        case HS384:
        case HS512:
          return HashHelper.hmacval(key, internal, mat, dat);
        case RS256:
        case RS384:
        case RS512:
        case ES256:
        case ES384:
        case ES512: {
          return HashHelper.sigval((PublicKey)key, internal, mat, dat);
        }
        default:
          throw new UnsupportedOperationException();
        }
      } catch (Throwable t) {
        throw new RuntimeException(t);
      }
    }
    
  }

  public static String generate(Key key, byte[] claim, ASBase header) {
    return generate(Alg.HS256, key, claim, header);
  }
  
  public static String generate(Key key, byte[] claim) {
    return generate(Alg.HS256, key, claim);
  }
  
  public static String generate(Alg alg, Key key, byte[] claim, ASBase header) {
    return generate(IO.get(), alg, key, claim, header);
  }
  
  public static String generate(Alg alg, Key key, byte[] claim) {
    return generate(IO.get(), alg, key, claim);
  }
  
  public static String generate(Key key, ASBase claim, ASBase header) {
    return generate(Alg.HS256, key, claim, header);
  }
  
  public static String generate(Key key, ASBase claim) {
    return generate(Alg.HS256, key, claim);
  }
  
  public static String generate(Alg alg, Key key, ASBase claim, ASBase header) {
    return generate(IO.get(),alg,key,claim,header);
  }
  
  public static String generate(Alg alg, Key key, ASBase claim) {
    return generate(IO.get(), alg, key, claim);
  }
  
  public static String generate(IO io, Alg alg, Key key, ASBase claim, ASBase header) {
    try {
      return generate(io,alg,key,io.write(claim).getBytes("UTF-8"),header);
    } catch (Throwable t) {
      if (t instanceof RuntimeException)
        throw (RuntimeException)t;
      else throw new RuntimeException(t);
    }   
  }
  
  public static String generate(IO io, Alg alg, Key key, ASBase claim) {
    return generate(io,alg,key,claim,ASBase.make().set("alg", alg).get());
  }
  
  public static String generate(IO io, Alg alg, Key key, byte[] claim) {
    return generate(io,alg,key,claim,ASBase.make().set("alg", alg).get());
  }
  
  public static String generate(IO io, Alg alg, Key key, byte[] claim, ASBase header) {
    try {
      checkNotNull(header);
      checkNotNull(header.getProperty("alg"));
      StringBuilder buf = new StringBuilder();
      String _header = Base64.encodeBase64URLSafeString(io.write(header).getBytes("UTF-8"));
      String _claim = Base64.encodeBase64URLSafeString(claim);
      buf.append(_header).append('.').append(_claim);
      String mat = buf.toString();
      buf.append('.').append(alg.sig(key, mat.getBytes("UTF-8")));
      return buf.toString();
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }
  
  public static boolean validate(Key key, String jwt) throws IOException {
    return validate(key, IO.get(),jwt);
  }
  
  public static boolean validate(Key key, IO io,String jwt) throws IOException {
    String[] split = jwt.split("\\.",3);
    if (split.length < 3)
      return false; // cannot validate without a signature
    ASBase header = dec(io,split[0]);
    StringBuilder buf = new StringBuilder();
    buf.append(split[0]).append('.').append(split[1]);
    byte[] dat = buf.toString().getBytes("UTF-8");
    byte[] chk = Base64.decodeBase64(split[2]);
    Object o = header.getProperty("alg");
    Alg alg = o instanceof Alg ? (Alg)o : Alg.valueOf(o.toString());
    return alg.val(key, dat, chk);
  }
  
  private static ASBase dec(IO io, String enc) throws UnsupportedEncodingException {
    byte[] data = Base64.decodeBase64(enc);
    return io.readObject(new String(data,0,data.length, "UTF-8"));
  }
  
  public static ASBase getClaimIfValid(IO io, Key key, String jwt) throws IOException {
    if (!validate(key,io,jwt)) return null;
    String[] parts = jwt.split("\\.",3);
    return dec(io, parts[1]);
  }
  
  public static ASBase getClaimIfValid(Key key, String jwt) throws IOException {
    return getClaimIfValid(IO.get(),key,jwt);
  }
  
  public static byte[] getDataIfValid(Key key, String jwt) throws IOException {
    if (!validate(key,jwt)) return null;
    String[] parts = jwt.split("\\.",3);
    return Base64.decodeBase64(parts[1].getBytes("UTF-8"));
  }
  
  public static ASBase getHeader(String jwt) throws IOException {
    return getHeader(IO.get(),jwt);
  }
  
  public static ASBase getHeader(IO io, String jwt) throws IOException {
    String[] parts = jwt.split("\\.",3);
    return dec(io, parts[0]);
  }
}
