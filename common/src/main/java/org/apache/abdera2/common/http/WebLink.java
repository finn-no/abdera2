package org.apache.abdera2.common.http;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.activation.MimeType;

import org.apache.abdera2.common.iri.IRI;
import org.apache.abdera2.common.lang.Lang;
import org.apache.abdera2.common.mediatype.MimeTypeHelper;
import org.apache.abdera2.common.text.CharUtils;
import org.apache.abdera2.common.text.Codec;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;

import static org.apache.abdera2.common.text.CharUtils.scanFor;
import static org.apache.abdera2.common.text.CharUtils.quotedIfNotToken;
import static com.google.common.base.Preconditions.*;
/**
 * Implements the HTTP Link Header
 * (http://tools.ietf.org/html/rfc5988)
 */
public class WebLink implements Serializable {

  private static final long serialVersionUID = 3875558439575297581L;
  public static final String MEDIA_SCREEN = "screen";
  public static final String MEDIA_TTY = "tty";
  public static final String MEDIA_TV = "tv";
  public static final String MEDIA_PROJECTION = "projection";
  public static final String MEDIA_HANDHELD = "handheld";
  public static final String MEDIA_PRINT = "print";
  public static final String MEDIA_BRAILLE = "braille";
  public static final String MEDIA_AURAL = "aural";
  public static final String MEDIA_ALL = "all";
  
  public static Builder make() {
    return new Builder();
  }
  
  public static Builder make(WebLink from) {
    return new Builder(from);
  }
  
  public static class Builder implements Supplier<WebLink> {

    private IRI iri;
    private final Set<String> rel = 
      new LinkedHashSet<String>();
    private IRI anchor;
    private final Set<String> rev =
      new LinkedHashSet<String>();
    private Lang lang;
    private final Set<String> media = 
      new LinkedHashSet<String>();
    private String title;
    private MimeType mediaType;
    private final Map<String,String> params = 
      new HashMap<String,String>();

    public Builder () {}
    
    public Builder(WebLink from) {
      from(from);
    }
    
    public WebLink get() {
      return new WebLink(this);
    }
    
    public Builder iri(IRI iri) {
      this.iri = iri;
      return this;
    }
    
    public Builder from(WebLink link) {
      this.iri = link.iri;
      this.rel.clear();
      this.rel.addAll(link.rel);
      this.anchor = link.anchor;
      this.rev.clear();
      this.rev.addAll(link.rev);
      this.lang = link.lang;
      this.media.clear();
      this.media.addAll(link.media);
      this.title = link.title;
      this.mediaType = link.mediaType;
      this.params.clear();
      this.params.putAll(link.params);
      return this;
    }
    
    public Builder iri(String iri) {
      return iri(new IRI(iri));
    }
    
    public Builder rel(String rel) {
      this.rel.add(rel);
      return this;
    }
    
    public Builder anchor(IRI iri) {
      this.anchor = iri;
      return this;
    }
    
    public Builder anchor(String iri) {
      this.anchor = new IRI(iri);
      return this;
    }
    
    public Builder rev(String rev) {
      this.rev.add(rev);
      return this;
    }
    
    public Builder lang(String lang) {
      this.lang = new Lang(lang);
      return this;
    }
    
    public Builder lang(Lang lang) {
      this.lang = lang;
      return this;
    }
    
    public Builder lang(Locale locale) {
      this.lang = new Lang(locale);
      return this;
    }
    
    public Builder media(String media) {
      this.media.add(media);
      return this;
    }
    
    public Builder param(String name, String value) {
      checkNotNull(null);
      checkArgument(!reserved(name));
      this.params.put(name,value);
      return this;
    }
    
    public Builder title(String title) {
      this.title = title;
      return this;
    }
    
    public Builder mediaType(String type) {
      this.mediaType = MimeTypeHelper.create(type);
      return this;
    }
    
    public Builder mediaType(MimeType type) {
      this.mediaType = type;
      return this;
    }
  }
  
  private final IRI iri;
  private final Set<String> rel = 
    new LinkedHashSet<String>();
  private IRI anchor;
  private final Set<String> rev =
    new LinkedHashSet<String>();
  private Lang lang;
  private final Set<String> media = 
    new LinkedHashSet<String>();
  private String title;
  private MimeType mediaType;
  private final Map<String,String> params = 
    new HashMap<String,String>();
  
  private WebLink(Builder builder) {
    this.iri = builder.iri;
    this.rel.addAll(builder.rel);
    this.anchor = builder.anchor;
    this.rev.addAll(builder.rev);
    this.lang = builder.lang;
    this.media.addAll(builder.media);
    this.title = builder.title;
    this.mediaType = builder.mediaType;
    this.params.putAll(builder.params);
  }
  
  public WebLink(String iri) {
    this(new IRI(iri));
  }
  
  public WebLink(String iri, String rel) {
    this(new IRI(iri),rel);
  }
  
  public WebLink(IRI iri, String rel) {
    checkNotNull(iri);
    this.iri = iri.normalize();
    if (rel != null) this.rel.add(rel);
    this.anchor = null;
    this.lang = null;
    this.title = null;
    this.mediaType = null;
  }
  
  public WebLink(IRI iri) {
    checkNotNull(iri);
    this.iri = iri;
    this.anchor = null;
    this.lang = null;
    this.title = null;
    this.mediaType = null;
  }
  
  public IRI getResolvedIri(IRI base) {
    checkNotNull(base);
    IRI context = getContextIri(base);
    return context != null ? context.resolve(iri) : iri;
  }
  
  public IRI getContextIri(IRI base) {
    checkNotNull(base);
    if (anchor == null) return base;
    return base != null ? base.resolve(anchor) : anchor;
  }
  
  public IRI getIri() {
    return iri;
  }
  
  public Iterable<String> getRel() {
    return Iterables.unmodifiableIterable(this.rel);
  }
  
  public IRI getAnchor() {
    return anchor;
  }
  
  public Iterable<String> getRev() {
    return Iterables.unmodifiableIterable(this.rev);
  }
  
  public Lang getHrefLang() {
    return lang;
  }
  
  public Iterable<String> getMedia() {
    return Iterables.unmodifiableIterable(this.media);
  }
  
  public String getTitle() {
    return title;
  }
  
  public MimeType getMediaType() {
    return mediaType;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((anchor == null) ? 0 : anchor.hashCode());
    result = prime * result + ((iri == null) ? 0 : iri.hashCode());
    result = prime * result + ((lang == null) ? 0 : lang.hashCode());
    result = prime * result + ((media == null) ? 0 : media.hashCode());
    result = prime * result + ((mediaType == null) ? 0 : mediaType.toString().hashCode());
    result = prime * result + ((params == null) ? 0 : params.hashCode());
    result = prime * result + ((rel == null) ? 0 : rel.hashCode());
    result = prime * result + ((rev == null) ? 0 : rev.hashCode());
    result = prime * result + ((title == null) ? 0 : title.hashCode());
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
    WebLink other = (WebLink) obj;
    if (anchor == null) {
      if (other.anchor != null)
        return false;
    } else if (!anchor.equals(other.anchor))
      return false;
    if (iri == null) {
      if (other.iri != null)
        return false;
    } else if (!iri.equals(other.iri))
      return false;
    if (lang == null) {
      if (other.lang != null)
        return false;
    } else if (!lang.equals(other.lang))
      return false;
    if (media == null) {
      if (other.media != null)
        return false;
    } else if (!media.equals(other.media))
      return false;
    if (mediaType == null) {
      if (other.mediaType != null)
        return false;
    } else if (!mediaType.equals(other.mediaType))
      return false;
    if (params == null) {
      if (other.params != null)
        return false;
    } else if (!params.equals(other.params))
      return false;
    if (rel == null) {
      if (other.rel != null)
        return false;
    } else if (!rel.equals(other.rel))
      return false;
    if (rev == null) {
      if (other.rev != null)
        return false;
    } else if (!rev.equals(other.rev))
      return false;
    if (title == null) {
      if (other.title != null)
        return false;
    } else if (!title.equals(other.title))
      return false;
    return true;
  }



  private static final Set<String> reserved = 
    new HashSet<String>();
  static {
    reserved.add("rel");
    reserved.add("anchor");
    reserved.add("rev");
    reserved.add("hreflang");
    reserved.add("media");
    reserved.add("title");
    reserved.add("type");
    reserved.add("type");
  }
  private static boolean reserved(String name) {
    return reserved.contains(name);
  }
  
  public String getParam(String name) {
    checkNotNull(name);
    checkArgument(!reserved(name));
    return params.get(name);
  }
  
  public String toString() {
    StringBuilder buf = new StringBuilder();
    buf.append('<')
       .append(iri.toASCIIString())
       .append('>');
    
    if (rel.size() > 0) {
      buf.append(';')
         .append("rel=");
      boolean first = true;
      if (rel.size() > 1)
        buf.append('"');
      for (String r : rel) {
        if (!first) buf.append(' ');
        else first = false;
        buf.append(quotedIfNotToken(r));
      }
      if (rel.size() > 1)
        buf.append('"');
    }
    
    if (anchor != null) {
      buf.append(';')
         .append("anchor=<")
         .append(anchor.toASCIIString())
         .append('>');
    }
    
    if (rev.size() > 0) {
      buf.append(';')
         .append("rev=");
      boolean first = true;
      if (rev.size() > 1)
        buf.append('"');
      for (String r : rev) {
        if (!first) buf.append(' ');
        else first = false;
        buf.append(quotedIfNotToken(r));
      }
      if (rev.size() > 1)
        buf.append('"');
    }
    
    if (lang != null) {
      buf.append(';')
         .append("hreflang=")
         .append(lang.toString());
    }
    
    if (media.size() > 0) {
      buf.append(';')
         .append("media=");
      boolean first = true;
      if (media.size() > 1)
        buf.append('"');
      for (String r : media) {
        if (!first) buf.append(' ');
        else first = false;
        buf.append(quotedIfNotToken(r));
      }
      if (media.size() > 1)
        buf.append('"');
    }
    
    if (title != null) {
      String enctitle = Codec.encode(title,Codec.STAR);
      buf.append(';')
         .append("title");
      if (!title.equals(enctitle))
        buf.append('*')
           .append('=')
           .append(enctitle);
      else
        buf.append('=')
           .append(quotedIfNotToken(title));
    }
    
   if (mediaType != null) {
     buf.append(';')
        .append("type=")
        .append(quotedIfNotToken(mediaType.toString()));
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
          .append(quotedIfNotToken(entry.getValue()));
     }
   }
    
    return buf.toString();
  }

  public static final Function<String,Iterable<WebLink>> parser = 
    new Function<String,Iterable<WebLink>>() {
      public Iterable<WebLink> apply(String input) {
        return
          input != null ?
          parse(input) :
          Collections.<WebLink>emptySet();
      }
  };
  
  public static Iterable<WebLink> parse(String text) {
    List<WebLink> links = new ArrayList<WebLink>();
    if (text == null) return Collections.emptyList();
    
    int z = scanFor('<', text, 0, true);

    while(z != -1) {
      int s = z;
      int e = scanFor('>', text, s, false);
      if (e == -1)
        throw new IllegalArgumentException();
      
      String uri = text.substring(s+1,e).trim();
      WebLink.Builder maker = WebLink.make().iri(uri);
      
      s = scanFor(';', text,e+1,false);
      while(s != -1 && text.charAt(s) != ',') {
        e = scanFor('=', text,s+1,false);
        String name = text.substring(s+1,text.charAt(e-1)=='*'?e-1:e).trim();
        s = scanFor(';', text,e+1,false);
        String val = s!=-1?text.substring(e+1,s).trim():text.substring(e+1).trim();
        val = Codec.decode(val);
        if (name.equalsIgnoreCase("rel")) {
          String[] vals = CharUtils.unquote(val).split("\\s+");
          for (String v : vals)
            maker.rel(v);
        } else if (name.equalsIgnoreCase("anchor")) {
          maker.anchor(CharUtils.unwrap(val, '<', '>'));
        } else if (name.equalsIgnoreCase("rev")) {
          String[] vals = CharUtils.unquote(val).split("\\s+");
          for (String v : vals)
            maker.rev(v);
        } else if (name.equalsIgnoreCase("hreflang")) {
          maker.lang(CharUtils.unquote(val));
        } else if (name.equalsIgnoreCase("media")) {
          String[] vals = CharUtils.unquote(val).split("\\s+");
          for (String v : vals)
            maker.media(v);
        } else if (name.equalsIgnoreCase("title")) {
          maker.title(CharUtils.unquote(val));
        } else if (name.equalsIgnoreCase("type")) {
          maker.mediaType(CharUtils.unquote(val));
        } else {
          maker.param(name,CharUtils.unquote(val));
        }
      }
      links.add(maker.get());
      if (s == -1) break;
      z = scanFor('<', text, s+1, false);
    }
    return links;
  }
    
  public static String toString(WebLink link, WebLink... links) {
    if (link == null) return null;
    StringBuilder buf = new StringBuilder();
    buf.append(link.toString());
    for (WebLink l : links)
      buf.append(", ").append(l.toString());
    return buf.toString();
  }
  
  public static String toString(Iterable<WebLink> links ) {
    StringBuilder buf = new StringBuilder();
    boolean first = true;
    for (WebLink link : links) {
      if (!first) buf.append(", ");
      else first = !first;
      buf.append(link.toString());
    }
    return buf.toString();
  }
}
