package org.apache.abdera2.common.anno;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The Context Annotation is intended for use with classes 
 * with the URITemplate annotation to establish a static
 * default context. Each Context Annotation contains a 
 * list of static Param annotations
 */
@Retention(RUNTIME)
@Target( {TYPE})
public @interface Context {
  Param[] value();
}
