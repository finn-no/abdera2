/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  For additional information regarding
 * copyright in this work, please see the NOTICE file in the top level
 * directory of this distribution.
 */
package org.apache.abdera2.common.text;

import java.util.Arrays;

import org.apache.abdera2.common.text.CodepointIterator;

import com.google.common.base.Predicate;

public abstract class CodepointMatcher 
  implements Predicate<Integer> {
    
  public static final CodepointMatcher MATCH_ALL = matchAll();
  public static final CodepointMatcher MATCH_NONE = matchNone();
  
  public void verify(CodepointIterator iterator) {
    while(iterator.hasNext()) {
      int cp = iterator.next();
      if (!apply(cp))
        throw new InvalidCharacterException(cp);
    }
  }
  
  public void verifyNot(CodepointIterator iterator) {
    while(iterator.hasNext()) {
      int cp = iterator.next();
      if (apply(cp))
        throw new InvalidCharacterException(cp);
    }
  }
  
  public boolean all(CharSequence seq) {
    return all(CodepointIterator.getInstance(seq));
  }
  
  public boolean any(CharSequence seq) {
    return any(CodepointIterator.getInstance(seq));
  }
  
  public boolean all(CodepointIterator iterator) {
    while (iterator.hasNext())
      if (!apply(iterator.next()))
        return false;
    return true;
  }
  
  public boolean any(CodepointIterator iterator) {
    while (iterator.hasNext())
      if (apply(iterator.next()))
        return true;
    return false;    
  }
  
  public boolean matches(int codepoint) {
    return apply(codepoint);
  }
  
  public boolean apply(int codepoint) {
    return this.apply(Integer.valueOf(codepoint));
  }
  
  public CodepointMatcher negate() {
    return new NegatingCodepointMatcher(this);
  }
  
  public static CodepointMatcher negate(CodepointMatcher matcher) {
    return matcher.negate();
  }

  public CodepointMatcher or(CodepointMatcher that) {
    return new OrCodepointMatcher(this,that);
  }
  
  public static CodepointMatcher or(CodepointMatcher... matchers) {
    return new OrCodepointMatcher(matchers);
  }
  
  public CodepointMatcher and(CodepointMatcher that) {
    return new AndCodepointMatcher(this,that);
  }
  
  public static CodepointMatcher and(CodepointMatcher... matchers) {
    return new AndCodepointMatcher(matchers);
  }
  
  public static CodepointMatcher inRange(int low, int high) {
    return new RangeCodepointMatcher(low,high);
  }
  
  public static CodepointMatcher notInRange(int low, int high) {
    return new NegatingCodepointMatcher(inRange(low,high));
  }
  
  public static CodepointMatcher is(int... cp) {
    return new IsCodepointMatcher(cp);
  }
  
  public static CodepointMatcher isNot(int cp) {
    return new NegatingCodepointMatcher(is(cp));
  }
  
  public static CodepointMatcher inInversionSet(int[] set) {
    return new InversionSetCodepointMatcher(set);
  }
  
  public static CodepointMatcher notInInversionSet(int[] set) {
    return new NegatingCodepointMatcher(inInversionSet(set));
  }
  
  public static CodepointMatcher matchAll() {
    return new MatchAllCodepointMatcher();
  }
  
  public static CodepointMatcher matchNone() {
    return new MatchNoneCodepointMatcher();
  }
    
  public static class MatchAllCodepointMatcher extends CodepointMatcher {
    public boolean apply(Integer codepoint) {
      return true;
    }    
  }
  
  public static class MatchNoneCodepointMatcher extends CodepointMatcher {
    public boolean apply(Integer codepoint) {
      return false;
    }
  }
  
  public static abstract class CodepointMatcherWrapper extends CodepointMatcher {
    protected final CodepointMatcher internal;
    public CodepointMatcherWrapper(CodepointMatcher internal) {
      this.internal = internal;
    }
  }
  
  public static class IsCodepointMatcher extends CodepointMatcher {
    private final int[] cp;
    public IsCodepointMatcher(int... cp) {
      this.cp = cp;
      Arrays.sort(this.cp);
    }
    public boolean apply(Integer codepoint) {
      return Arrays.binarySearch(cp, codepoint) > -1;
    }
  }
  
  public static class RangeCodepointMatcher extends CodepointMatcher {
    private final int low,high;
    public RangeCodepointMatcher(int low, int high) {
      this.low = low;
      this.high = high;
    }
    public boolean apply(Integer codepoint) {
      return low <= codepoint && high >= codepoint;
    }
  }
  
  /**
   * CodepointMatcher that matches all of the specified internal matchers
   */
  public static class AndCodepointMatcher extends CodepointMatcher {
    private final CodepointMatcher[] internal;
    public AndCodepointMatcher(CodepointMatcher... internal) {
      this.internal = internal;
    }
    public boolean apply(Integer codepoint) {
      for (CodepointMatcher matcher : internal)
        if (!matcher.apply(codepoint))
          return false;
      return true;
    }
  }
  
  public static class OrCodepointMatcher extends CodepointMatcher {
    private final CodepointMatcher[] internal;
    public OrCodepointMatcher(CodepointMatcher... internal) {
      this.internal = internal;
    }
    public boolean apply(Integer codepoint) {
      for (CodepointMatcher matcher : internal)
        if (matcher.apply(codepoint))
          return true;
      return false;
    }
  }
  
  public static class NegatingCodepointMatcher extends CodepointMatcherWrapper {
    public NegatingCodepointMatcher(CodepointMatcher internal) {
      super(internal);
    }
    public boolean apply(Integer codepoint) {
      return !internal.apply(codepoint);
    } 
  }
  
  /**
   * Matches codepoints that are contained within the specified inversion set
   */
  public static class InversionSetCodepointMatcher extends CodepointMatcher {
    private final int[] set;
    public InversionSetCodepointMatcher(int[] set) {
      this.set = set;
    }
    public boolean apply(Integer codepoint) {
      return invset_contains(set,codepoint);
    }
  }
  
  private static boolean invset_contains(int[] set, int value) {
      int s = 0, e = set.length;
      while (e - s > 8) {
          int i = (e + s) >> 1;
          s = set[i] <= value ? i : s;
          e = set[i] > value ? i : e;
      }
      while (s < e) {
          if (value < set[s])
              break;
          s++;
      }
      return ((s - 1) & 1) == 0;
  }
  
}
  
