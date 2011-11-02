package org.apache.abdera2.activities.model.objects;

import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.common.anno.Name;

/**
 * A simple "objectType":"book" object that serves primarily as an 
 * example of creating new ASObject types.
 */
@Name("book")
public class BookObject extends CreativeWork {

  private static final long serialVersionUID = -178336535850006357L;

  public BookObject() {}
  
  public BookObject(String displayName) {
    setDisplayName(displayName);
  }
  
  @SuppressWarnings("unchecked")
  public <T extends ASObject>T getFormat() {
    return (T)getProperty("format");
  }
  
  public void setFormat(ASObject format) {
    setProperty("format", format);
  }
  
  public String getEdition() {
    return getProperty("edition");
  }
  
  public void setEdition(String edition) {
    setProperty("edition", edition);
  }
  
  public String getIsbn10() {
    return getProperty("isbn10");
  }
  
  public void setIsbn10(String isbn) {
    setProperty("isbn10", isbn);
  }
  
  public String getIsbn13() {
    return getProperty("isbn13");
  }
  
  public void setIsbn13(String isbn) {
    setProperty("isbn13", isbn);
  }
  
  public int getPageCount() {
    return (Integer)getProperty("pageCount");
  }
  
  public void setPageCount(int pageCount) {
    setProperty("pageCount", pageCount);
  }

  @SuppressWarnings("unchecked")
  public <T extends ASObject>T getIllustrator() {
    return (T)getProperty("illustrator");
  }
  
  public void setIllustrator(ASObject illustrator) {
    setProperty("illustrator", illustrator);
  }
 
  public static <T extends BookObject>BookObjectGenerator<T> makeBook() {
    return new BookObjectGenerator<T>();
  }
  
  @SuppressWarnings("unchecked")
  public static class BookObjectGenerator<T extends BookObject> extends CreativeWorkGenerator<T> {
    public BookObjectGenerator() {
      super((Class<T>) BookObject.class);
    }
    public BookObjectGenerator(Class<T> _class) {
      super(_class);
    }
    public <X extends BookObjectGenerator<T>>X edition(String val) {
      item.setEdition(val);
      return (X)this;
    }
    public <X extends BookObjectGenerator<T>>X format(ASObject obj) {
      item.setFormat(obj);
      return (X)this;
    }
    public <X extends BookObjectGenerator<T>>X illustrator(ASObject obj) {
      item.setIllustrator(obj);
      return (X)this;
    }
    public <X extends BookObjectGenerator<T>>X isbn10(String val) {
      item.setIsbn10(val);
      return (X)this;
    }
    public <X extends BookObjectGenerator<T>>X pageCount(int count) {
      item.setPageCount(count);
      return (X)this;
    }
  }
}
