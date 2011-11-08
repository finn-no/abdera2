package org.apache.abdera2.common.misc;

import java.util.List;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class Pair<K,V> {

  private final K k;
  private final V v;
  
  public Pair(K k, V v) {
    this.k = k;
    this.v = v;
  }
  
  public K first() {
    return k;
  }
  
  public V second() {
    return v;
  }

  public static <K,V>Pair<K,V> of(K k, V v) {
    return new Pair<K,V>(k,v);
  }
  
  public static Pair<String,String> from(String pair) {
    String[] split = pair.split("\\s*=\\s*",2);
    return new Pair<String,String>(split[0],split.length>1?split[1]:null);
  }
  
  public static Iterable<Pair<String,String>> from(String[] pairs) {
    List<Pair<String,String>> list = Lists.newArrayList();
    for (String pair : pairs)
      list.add(from(pair));
    return Iterables.unmodifiableIterable(list);
  }
  
  public static Iterable<Pair<String,String>> from(String pairs, String delim) {
    return from(pairs.split(String.format("\\s*%s\\s*",delim)));
  }
  
  @Override
  public int hashCode() {
    return MoreFunctions.genHashCode(1, k,v);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Pair<K,V> other = (Pair) obj;
    if (k == null) {
      if (other.k != null)
        return false;
    } else if (!k.equals(other.k))
      return false;
    if (v == null) {
      if (other.v != null)
        return false;
    } else if (!v.equals(other.v))
      return false;
    return true;
  }
  
}
