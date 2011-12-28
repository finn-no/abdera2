package org.apache.abdera2.common.selector;

// Marker interface used to identify selectors that maintain an 
// internal state (and therefore are not threadsafe
public interface StatefulSelector<T> extends Selector<T> {

}
