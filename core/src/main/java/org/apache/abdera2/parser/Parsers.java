package org.apache.abdera2.parser;

import java.io.InputStream;
import java.io.Reader;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.model.Document;
import org.apache.abdera2.model.Element;

import com.google.common.base.Function;

public final class Parsers {

  private Parsers() {}
 
  public static interface InputStreamFunction<E extends Element> 
    extends Function<InputStream,Document<E>> {}
  
  public static interface ReaderFunction<E extends Element> 
    extends Function<Reader,Document<E>> {}
  
  public static <E extends Element>InputStreamFunction<E> forInputStream(
    final Parser parser,
    final String base,
    final ParserOptions options) {
    return new InputStreamFunction<E>() {
      public Document<E> apply(InputStream in) {
        return parser.parse(in, base, options);
      }
    };
  }
  
  public static <E extends Element>InputStreamFunction<E> forInputStream(
    final Parser parser, 
    final ParserOptions options) {
    return new InputStreamFunction<E>() {
      public Document<E> apply(InputStream in) {
        return parser.parse(in, options);
      }
    };
  }
  
  public static <E extends Element>InputStreamFunction<E> forInputStream(
    final Parser parser) {
    return new InputStreamFunction<E>() {
      public Document<E> apply(InputStream in) {
        return parser.parse(in);
      }
    };
  }
  
  public static <E extends Element>InputStreamFunction<E> forInputStream() {
    return new InputStreamFunction<E>() {
      public Document<E> apply(InputStream in) {
        return Abdera.getInstance().getParser().parse(in);
      }
    };
  }
  
  public static <E extends Element>InputStreamFunction<E> forInputStream(
    final String base,
    final ParserOptions options) {
    return new InputStreamFunction<E>() {
      public Document<E> apply(InputStream in) {
        return Abdera.getInstance().getParser().parse(in, base, options);
      }
    };
  }
  
  public static <E extends Element>InputStreamFunction<E> forInputStream(
    final ParserOptions options) {
    return new InputStreamFunction<E>() {
      public Document<E> apply(InputStream in) {
        return Abdera.getInstance().getParser().parse(in, options);
      }
    };
  }
  
  public static <E extends Element>ReaderFunction<E> forReader(
    final Parser parser,
    final String base,
    final ParserOptions options) {
    return new ReaderFunction<E>() {
      public Document<E> apply(Reader in) {
        return parser.parse(in, base, options);
      }
    };
  }
  
  public static <E extends Element>ReaderFunction<E> forReader(
    final Parser parser, 
    final ParserOptions options) {
    return new ReaderFunction<E>() {
      public Document<E> apply(Reader in) {
        return parser.parse(in, options);
      }
    };
  }
  
  public static <E extends Element>ReaderFunction<E> forReader(
    final Parser parser) {
    return new ReaderFunction<E>() {
      public Document<E> apply(Reader in) {
        return parser.parse(in);
      }
    };
  }
  
  public static <E extends Element>ReaderFunction<E> forReader() {
    return new ReaderFunction<E>() {
      public Document<E> apply(Reader in) {
        return Abdera.getInstance().getParser().parse(in);
      }
    };
  }
  
  public static <E extends Element>ReaderFunction<E> forReader(
    final String base,
    final ParserOptions options) {
    return new ReaderFunction<E>() {
      public Document<E> apply(Reader in) {
        return Abdera.getInstance().getParser().parse(in, base, options);
      }
    };
  }
  
  public static <E extends Element>ReaderFunction<E> forReader(
    final ParserOptions options) {
    return new ReaderFunction<E>() {
      public Document<E> apply(Reader in) {
        return Abdera.getInstance().getParser().parse(in, options);
      }
    };
  }
}
