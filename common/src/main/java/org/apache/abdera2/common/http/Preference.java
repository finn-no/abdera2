package org.apache.abdera2.common.http;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.abdera2.common.text.CharUtils;
import static org.apache.abdera2.common.text.CharUtils.quotedIfNotToken;
import org.apache.abdera2.common.text.Codec;
import org.apache.abdera2.common.text.CharUtils.Profile;

import com.google.common.base.Supplier;

/**
 * Implementation of the Prefer HTTP Header, e.g.
 * 
 * Prefer: return-no-content, my-preference=abc;xyz=123
 */
public class Preference implements Serializable {
  
  public static final String RETURN_NO_CONTENT = "return-no-content";
  public static final String RETURN_ACCEPTED = "return-accepted";
  public static final String RETURN_CONTENT = "return-content";
  public static final String RETURN_STATUS = "return-status";
  public static final String WAIT = "wait";
  
  /** 
   * The "return-no-content" token indicates that the client prefers that
   * the server not include an entity in the response to a successful
   * request.  Typically, such responses would use the 204 No Content
   * status code as defined in Section 10.2.5 of [RFC2616], but other
   * status codes can be used as appropriate.
   */
  public static final Preference PREF_RETURN_NO_CONTENT = 
    new Preference(RETURN_NO_CONTENT);
  
  /**
   * The "return-accepted" token indicates that the client prefers that
   * the server respond with a 202 Accepted response indicating that the
   * request has been accepted for processing.
   */
  public static final Preference PREF_RETURN_ACCEPTED =
    new Preference(RETURN_ACCEPTED);
  
  /**
   * The "return-content" token indicates that the client prefers that the
   * server include an entity representing the current state of the
   * resource in the response to a successful request.
   */
  public static final Preference PREF_RETURN_CONTENT =
    new Preference(RETURN_CONTENT);
  
  public static Preference WAIT(long millis) {
    return 
      make()
        .token(WAIT)
        .value(millis)
     .get();
  }
  
  public static Builder make() {
    return new Builder();
  }
  
  public static class Builder implements Supplier<Preference> {

    private String token;
    private String value;
    private final Map<String,String> params = 
      new HashMap<String,String>();
    
    public Preference get() {
      return new Preference(this);
    }
    
    public Builder token(String token) {
      this.token = token;
      return this;
    }
    
    public Builder value(String value) {
      this.value = value;
      return this;
    }
    
    public Builder value(long value) {
      this.value = Long.toString(value);
      return this;
    }
    
    public Builder value(int value) {
      this.value = Integer.toString(value);
      return this;
    }
    
    public Builder value(short value) {
      this.value = Short.toString(value);
      return this;
    }
    
    public Builder value(boolean value) {
      this.value = Boolean.toString(value);
      return this;
    }
    
    public Builder param(String key, String val) {
      if (key == null || reserved(key)) 
        throw new IllegalArgumentException();
      this.params.put(key,val);
      return this;
    }
    
    public Builder params(Map<String,String> params) {
      this.params.putAll(params);
      return this;
    }
    
  }
  
  /**
   * The "return-status" token indicates that the client prefers that the
   * server include an entity describing the status of the request in the
   * response to a successful request.
   */
  public static final Preference PREF_RETURN_STATUS = 
    new Preference(RETURN_STATUS);
  
  private static final long serialVersionUID = -6238673046322517740L;
  private final String token;
  private final String value;
  private final Map<String,String> params = 
    new HashMap<String,String>();
  
  private Preference(Builder builder) {
    this.token = builder.token;
    this.value = builder.value;
    this.params.putAll(builder.params);
  }
  
  public Preference(String token) {
    this(token,null);
  }
  
  public Preference(String token, String value) {
    Profile.TOKEN.verify(token);
    this.token = token.toLowerCase();
    this.value = value;
  }
  
  public String getToken() {
    return token;
  }
  
  public String getValue() {
    return value;
  }
  
  public long getLongValue() {
    return Long.parseLong(value);
  }
  
  public int getIntValue() {
    return Integer.parseInt(value);
  }
  
  public short getShortValue() {
    return Short.parseShort(value);
  }
  
  public boolean getBooleanValue() {
    return Boolean.parseBoolean(value);
  }
  
  private static final Set<String> reserved = 
    new HashSet<String>();
  static {
    // no reserved yet
  }
  private static boolean reserved(String name) {
    return reserved.contains(name);
  }
  
  public boolean matches(String token) {
    if (token == null) return false;
    return this.token.equalsIgnoreCase(token.toLowerCase());
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((params == null) ? 0 : params.hashCode());
    result = prime * result + ((token == null) ? 0 : token.hashCode());
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Preference other = (Preference) obj;
    if (params == null) {
      if (other.params != null)
        return false;
    } else if (!params.equals(other.params))
      return false;
    if (token == null) {
      if (other.token != null)
        return false;
    } else if (!token.equals(other.token))
      return false;
    if (value == null) {
      if (other.value != null)
        return false;
    } else if (!value.equals(other.value))
      return false;
    return true;
  }

  public String getParam(String name) {
    if (name == null || reserved(name))
      throw new IllegalArgumentException();
    return params.get(name);
  }
  
  public String toString() {
    StringBuilder buf = new StringBuilder();
    buf.append(token);
    
    if (value != null) {
      String encval = Codec.encode(value, Codec.STAR);
      if (value.equals(encval)) {
        buf.append('=')
           .append(quotedIfNotToken(value));
      } else {
        buf.append('*')
           .append('=')
           .append(encval);
      }
    }

   for (Map.Entry<String, String> entry : params.entrySet()) {
     String val = entry.getValue();
     String encval = Codec.encode(val,Codec.STAR);
     buf.append(';')
        .append(entry.getKey());
     if (!val.equals(encval)) {
       buf.append('*')
          .append('=')
          .append(encval);
     } else {
       buf.append('=')
          .append(quotedIfNotToken(val));
     }
   }
    
    return buf.toString();
  }
  
  private final static String TOKEN = "[\\!\\#\\$\\%\\&\\'\\*\\+\\-\\.\\^\\_\\`\\|\\~a-zA-Z0-9]+";
  private final static String PARAM = TOKEN+"\\s*={1}\\s*(?:(?:\"[^\"]+\")|(?:"+TOKEN+"))";
  private final static String PREF = TOKEN+"(?:\\s*={1}\\s*(?:(?:\"[^\"]+\")|(?:"+TOKEN+"))){0,1}";
  private final static String PARAMS = "(?:\\s*;\\s*(" + PARAM + "(?:\\s*;\\s*"+PARAM+")))*";
  private final static String PATTERN = "("+PREF+")" + PARAMS;

  private final static Pattern pattern = 
    Pattern.compile(PATTERN);
  private final static Pattern param = 
    Pattern.compile("("+PARAM+")");
  
  public static Iterable<Preference> parse(String text) {
    List<Preference> prefs = new ArrayList<Preference>();
    Matcher matcher = pattern.matcher(text);
    while (matcher.find()) {
      String pref = matcher.group(1);
      String params = matcher.group(2);
      String token = null, tokenval = null;
      
      if (pref != null) {
        String[] ps = pref.split("\\s*=\\s*", 2);
        token = ps[0].trim();
        if (ps.length == 2)
          tokenval = Codec.decode(CharUtils.unquote(ps[1]));
      }
      
      Preference.Builder maker = 
        Preference.make().token(token).value(tokenval);   
      if (params != null) {
        Matcher mparams = param.matcher(params);
        while(mparams.find()) {
          String p = mparams.group(1);
          String[] ps = p.split("\\s*=\\s*", 2);
          maker.param(ps[0], Codec.decode(CharUtils.unquote(ps[1])));
        }
      }
      prefs.add(maker.get());
    }
    return prefs;
  }
  
  public static String toString(
    Preference preference, 
    Preference... preferences) {
    if (preference == null)
      return null;
    StringBuilder buf = new StringBuilder();
    buf.append(preference.toString());
    for (Preference pref : preferences) {
      buf.append(',').append(pref.toString());
    }
    return buf.toString();
  }
  
  public static String toString(Iterable<Preference> preferences) {
    StringBuilder buf = new StringBuilder();
    boolean first = true;
    for (Preference pref : preferences) {
      if (!first) buf.append(',');
      else first = !first;
      buf.append(pref.toString());
    }
    return buf.toString();
  }
  
  /**
   * Utility method that checks to see if the given token is included
   * in the collection of preferences.
   */
  public static boolean contains(
    Iterable<Preference> preferences, 
    String token) {
    for (Preference pref : preferences)
      if (pref.matches(token))
        return true;
    return false;
  }
  
  /**
   * Utility method that checks to see if the given token is included
   * in the collection of preference
   */
  public static boolean contains(
    Iterable<Preference> preferences, 
    Preference preference) {
      return preferences instanceof Collection ?
        ((Collection<Preference>)preferences).contains(preference) :
        contains(preferences,preference.getToken());
  }
  
  public static Preference get(Iterable<Preference> preferences, String token) {
    for (Preference pref : preferences)
      if (pref.matches(token))
        return pref;
    return null;
  }
}
