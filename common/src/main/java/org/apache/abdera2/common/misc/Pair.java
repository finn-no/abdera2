package org.apache.abdera2.common.misc;

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
