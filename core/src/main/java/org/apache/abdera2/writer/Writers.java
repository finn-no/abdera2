package org.apache.abdera2.writer;

import java.io.Closeable;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.common.misc.ExceptionHelper;
import org.apache.abdera2.model.Base;

import com.google.common.base.Function;

public final class Writers {

  private Writers() {}
  
  public static abstract class WriterFunction 
    implements Function<Base,Void> { 
    protected final Closeable closeable;
    protected WriterFunction(Closeable closeable) {
      this.closeable = closeable;
    }
    public WriterFunction closing() {
      return new ClosingWriterFunction(this);
    }
  }
  private static class ClosingWriterFunction
    extends WriterFunction
    implements Function<Base,Void> {
      private final WriterFunction function;
      public ClosingWriterFunction(
          WriterFunction function) {
        super(function.closeable);
        this.function = function;
      }
      public Void apply(Base input) {
        try {
          function.apply(input);
          return null;
        } catch (Throwable t) {
          throw ExceptionHelper.propogate(t);
        } finally {
          try {
            closeable.close();
          } catch (Throwable t) {
            throw ExceptionHelper.propogate(t);
          }
        }
      }
  }

  public static WriterFunction forWriter(
    final java.io.Writer w) {
    return new WriterFunction(w) {
      public Void apply(Base input) {
        try {
          Abdera.getInstance().getWriter().writeTo(input, w);
          return null;
        } catch (Throwable t) {
          throw ExceptionHelper.propogate(t);
        }
      }
    };
  }
  
  public static WriterFunction forWriter(
    final Writer writer, 
    final java.io.Writer w) {
    return new WriterFunction(w) {
      public Void apply(Base input) {
        try {
          writer.writeTo(input, w);
          return null;
        } catch (Throwable t) {
          throw ExceptionHelper.propogate(t);
        }
      }
    };
  }
  
  public static WriterFunction forWriter(
    final Writer writer, 
    final java.io.Writer w, 
    final WriterOptions options) {
    return new WriterFunction(w) {
      public Void apply(Base input) {
        try {
          writer.writeTo(input, w, options);
          return null;
        } catch (Throwable t) {
          throw ExceptionHelper.propogate(t);
        }
      }
    };
  }
  
  public static WriterFunction forWriter(
    final String writer, 
    final java.io.Writer w) {
    return new WriterFunction(w) {
      public Void apply(Base input) {
        try {
          Abdera.getInstance().getWriterFactory().getWriter(writer).writeTo(input, w);
          return null;
        } catch (Throwable t) {
          throw ExceptionHelper.propogate(t);
        }
      }
    };
  }
  
  public static WriterFunction forWriter(
    final String writer, 
    final java.io.Writer w, 
    final WriterOptions options) {
    return new WriterFunction(w) {
      public Void apply(Base input) {
        try {
          Abdera.getInstance().getWriterFactory().getWriter(writer).writeTo(input, w, options);
          return null;
        } catch (Throwable t) {
          throw ExceptionHelper.propogate(t);
        }
      }
    };
  }
  
  
  public static WriterFunction forOutputStream(
    final java.io.OutputStream w) {
    return new WriterFunction(w) {
      public Void apply(Base input) {
        try {
          Abdera.getInstance().getWriter().writeTo(input, w);
          return null;
        } catch (Throwable t) {
          throw ExceptionHelper.propogate(t);
        }
      }
    };
  }
  
  public static WriterFunction forOutputStream(
    final Writer writer, 
    final java.io.OutputStream w) {
    return new WriterFunction(w) {
      public Void apply(Base input) {
        try {
          writer.writeTo(input, w);
          return null;
        } catch (Throwable t) {
          throw ExceptionHelper.propogate(t);
        }
      }
    };
  }
  
  public static WriterFunction forOutputStream(
    final Writer writer, 
    final java.io.OutputStream w, 
    final WriterOptions options) {
    return new WriterFunction(w) {
      public Void apply(Base input) {
        try {
          writer.writeTo(input, w, options);
          return null;
        } catch (Throwable t) {
          throw ExceptionHelper.propogate(t);
        }
      }
    };
  }
  
  public static WriterFunction forOutputStream(
    final String writer, 
    final java.io.OutputStream w) {
    return new WriterFunction(w) {
      public Void apply(Base input) {
        try {
          Abdera.getInstance().getWriterFactory().getWriter(writer).writeTo(input, w);
          return null;
        } catch (Throwable t) {
          throw ExceptionHelper.propogate(t);
        }
      }
    };
  }
  
  public static WriterFunction forOutputStream(
    final String writer, 
    final java.io.OutputStream w, 
    final WriterOptions options) {
    return new WriterFunction(w) {
      public Void apply(Base input) {
        try {
          Abdera.getInstance().getWriterFactory().getWriter(writer).writeTo(input, w, options);
          return null;
        } catch (Throwable t) {
          throw ExceptionHelper.propogate(t);
        }
      }
    };
  }
}
