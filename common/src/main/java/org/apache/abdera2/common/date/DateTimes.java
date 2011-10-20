package org.apache.abdera2.common.date;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

import org.apache.abdera2.common.selector.Selector;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.Interval;

import com.google.common.base.Equivalence;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;

public final class DateTimes {

  private DateTimes() {}
  
  private static final DateTimeFormatter DTF =
    ISODateTimeFormat.dateTime();
  
  public static String formatNow() {
    return DateTime.now().toString(DTF);
  }
  
  public static String format(String dateTime) {
    return DTF.print(new DateTime(dateTime));
  }
  
  public static String format(DateTime dateTime) {
    return DTF.print(dateTime);
  }
  
  public static String format(Date date) {
    return DTF.print(new DateTime(date));
  }
  
  public static String format(Calendar cal) {
    return DTF.print(new DateTime(cal));
  }
  
  public static String format(long ms) {
    return DTF.print(ms);
  }
  
  public static Date parse(String t) {
    return DateTime.parse(t).toDate();
  }
  
  public static DateTime toUTC(DateTime dt) {
    return dt.toDateTime(DateTimeZone.UTC);
  }
  
  public static DateTime toTimeZone(DateTime dt, TimeZone tz) {
    return dt.toDateTime(DateTimeZone.forTimeZone(tz));
  }
  
  public static DateTime toTimeZone(DateTime dt, String id) {
    return dt.toDateTime(DateTimeZone.forID(id));
  }
  
  public DateTimeBuilder makeDateTime() {
    return new DateTimeBuilder();
  }
  
  public static class DateTimeBuilder implements Supplier<DateTime> {
    private int year, month, day, hour, minute, second, millis;
    private DateTimeZone dtz;
    public DateTimeBuilder() {}
    public DateTimeBuilder year(int year) {
      this.year = year;
      return this;
    }
    public DateTimeBuilder month(int month) {
      this.month = month;
      return this;
    }
    public DateTimeBuilder day(int day) {
      this.day = day;
      return this;
    }
    public DateTimeBuilder hour(int hour) {
      this.hour = hour;
      return this;
    }
    public DateTimeBuilder minute(int minute) {
      this.minute = minute;
      return this;
    }
    public DateTimeBuilder second(int second) {
      this.second = second;
      return this;
    }
    public DateTimeBuilder millisecond(int ms) {
      this.millis = ms;
      return this;
    }
    public DateTimeBuilder timezone(TimeZone tz) {
      this.dtz = DateTimeZone.forTimeZone(tz);
      return this;
    }
    public DateTimeBuilder timezone(String id) {
      this.dtz = DateTimeZone.forID(id);
      return this;
    }
    public DateTimeBuilder timezone(int offsetHours) {
      this.dtz = DateTimeZone.forOffsetHours(offsetHours);
      return this;
    }
    public DateTimeBuilder timezone(int offsetHours, int offsetMinutes) {
      this.dtz = DateTimeZone.forOffsetHoursMinutes(offsetHours, offsetMinutes);
      return this;
    }
    public DateTime get() {
      return new DateTime(year,month,day,hour,minute,second,millis,dtz);
    }
  }
  
  public static abstract class DateTimeComparator<X> implements Comparator<X> {
    public int innerCompare(DateTime d1, DateTime d2) {
      if (d1 != null && d2 == null) return 1;
      if (d1 == null && d2 != null) return -1;
      if (d1 == null && d2 == null) return 0;
      return d1.compareTo(d2);
    }
  }

  public static DateTime dt(String dt) {
    return new DateTime(dt);
  }
  
  public static DateTime dt(Date date) {
    return new DateTime(date);
  }
  
  public static DateTime dt(Calendar cal) {
    return new DateTime(cal);
  }
  
  public static DateTime dt(long millis) {
    return new DateTime(millis);
  }
  
  public static DateTime now() {
    return DateTime.now();
  }
  
  public static DateTime now(String tz) {
    return DateTime.now(DateTimeZone.forID(tz));
  }
  
  public static DateTime now(TimeZone tz) {
    return DateTime.now(DateTimeZone.forTimeZone(tz));
  }
  
  public static DateTime utcNow() {
    return DateTime.now(DateTimeZone.UTC);
  }
  
  public static Selector<DateTime> selectorForRange(Range<DateTime> range) {
    return Selector.Utils.forPredicate(range);
  }
  
  public static Range<DateTime> all() {
    return Ranges.<DateTime>all();
  }
  
  public static Range<DateTime> atOrAfterNow() {
    return atOrAfter(DateTime.now());
  }
  
  public static Range<DateTime> atOrAfter(DateTime dateTime) {
    return Ranges.<DateTime>atLeast(dateTime);
  }
  
  public static Range<DateTime> atOrBefore(DateTime dateTime) {
    return Ranges.<DateTime>atMost(dateTime);
  }
  
  public static Range<DateTime> atOrBetween(DateTime low, DateTime high) {
    return Ranges.<DateTime>closed(low,high);
  }
  
  public static Range<DateTime> atOrBetween(DateTime low, Duration duration) {
    return atOrBetween(low,low.plus(duration));
  }
  
  public static Range<DateTime> atOrBetween(Duration duration, DateTime high) {
    return atOrBetween(high.minus(duration),high);
  }
  
  public static Range<DateTime> atOrBetween(Interval interval) {
    return atOrBetween(interval.getStart(),interval.getEnd());
  }
  
  public static Range<DateTime> atBetweenOrBefore(DateTime low, DateTime high) {
    return Ranges.<DateTime>closedOpen(low,high);
  }
  
  public static Range<DateTime> atBetweenOrBefore(DateTime low, Duration duration) {
    return atBetweenOrBefore(low,low.plus(duration));
  }
  
  public static Range<DateTime> atBetweenOrBefore(Duration duration, DateTime high) {
    return atBetweenOrBefore(high.minus(duration),high);
  }
  
  public static Range<DateTime> atBetweenOrBefore(Interval interval) {
    return atBetweenOrBefore(interval.getStart(),interval.getEnd());
  }
  
  public static Range<DateTime> afterNow() {
    return after(DateTime.now());
  }
  
  public static Range<DateTime> after(DateTime dateTime) {
    return Ranges.<DateTime>greaterThan(dateTime);
  }
  
  public static Range<DateTime> beforeNow() {
    return before(DateTime.now());
  }
  
  public static Range<DateTime> before(DateTime dateTime) {
    return Ranges.<DateTime>lessThan(dateTime);
  }
  
  public static Range<DateTime> between(DateTime low, DateTime high) {
    return Ranges.<DateTime>open(low,high);
  }
  
  public static Range<DateTime> between(DateTime low, Duration duration) {
    return between(low,low.plus(duration));
  }
  
  public static Range<DateTime> between(Duration duration, DateTime high) {
    return between(high.minus(duration), high);
  }
  
  public static Range<DateTime> between(Interval interval) {
    return between(interval.getStart(),interval.getEnd());
  }
  
  public static Range<DateTime> afterBetweenOrAt(DateTime low, DateTime high) {
    return Ranges.<DateTime>openClosed(low,high);
  }
  
  public static Range<DateTime> afterBetweenOrAt(DateTime low, Duration duration) {
    return afterBetweenOrAt(low,low.plus(duration));
  }
  
  public static Range<DateTime> afterBetweenOrAt(Duration duration, DateTime high) {
    return afterBetweenOrAt(high.minus(duration), high);
  }
  
  public static Range<DateTime> exactlyNow() {
    return exactly(DateTime.now());
  }
  
  public static Range<DateTime> exactly(DateTime dateTime) {
    return Ranges.<DateTime>singleton(dateTime);
  }
  
  public static Range<DateTime> exactlyBefore(DateTime date, Duration duration) {
    return exactly(date.minus(duration));
  }
  
  public static Range<DateTime> exactlyAfter(DateTime date, Duration duration) {
    return exactly(date.plus(duration));
  }
  
  public static Range<DateTime> exactlyBeforeNow(Duration duration) {
    return exactlyBefore(DateTime.now(),duration);
  }
  
  public static Range<DateTime> exactlyAfterNow(Duration duration) {
    return exactlyAfter(DateTime.now(),duration);
  }
  
  public static final boolean equivalent(DateTime d1, DateTime d2) {
    return Equivalence.equivalent(d1, d2);
  }
  
  public static final boolean equivalent(DateTime d1, Date date) {
    return equivalent(d1, new DateTime(date));
  }
  
  public static final boolean equivalent(DateTime d1, Calendar cal) {
    return equivalent(d1, new DateTime(cal));
  }
  
  public static final boolean equivalent(DateTime d1, long time) {
    return equivalent(d1, new DateTime(time));
  }
  
  public static final boolean equivalent(DateTime d1, String obj) {
    return equivalent(d1, new DateTime(obj));
  }
  
  public static final boolean equivalent(Date d1, Date date) {
    return equivalent(new DateTime(d1), new DateTime(date));
  }
  
  public static final boolean equivalent(Date d1, Calendar cal) {
    return equivalent(new DateTime(d1), new DateTime(cal));
  }
  
  public static final boolean equivalent(Date d1, long time) {
    return equivalent(new DateTime(d1), new DateTime(time));
  }
  
  public static final boolean equivalent(Date d1, String obj) {
    return equivalent(new DateTime(d1), new DateTime(obj));
  }
  
  public static final boolean equivalent(Calendar c1, Calendar c2) {
    return equivalent(new DateTime(c1),new DateTime(c2));
  }
  
  public static final boolean equivalent(Calendar c1, long time) {
    return equivalent(new DateTime(c1), new DateTime(time));
  }
  
  public static final boolean equivalent(Calendar c1, String c2) {
    return equivalent(new DateTime(c1), new DateTime(c2));
  }
  
  public static final boolean equivalent(long c1, long c2) {
    return equivalent(new DateTime(c1), new DateTime(c2));
  }
  
  public static final boolean equivalent(long c1, String c2) {
    return equivalent(new DateTime(c1), new DateTime(c2));
  }
  
  public static final boolean equivalent(String c1, String c2) {
    return equivalent(new DateTime(c1), new DateTime(c2));
  }
  
  public static Predicate<DateTime> equivalentTo(Date date) {
    return equivalentTo(new DateTime(date));
  }
  
  public static Predicate<DateTime> equivalentTo(Calendar cal) {
    return equivalentTo(new DateTime(cal));
  }
  
  public static Predicate<DateTime> equivalentTo(long time) {
    return equivalentTo(new DateTime(time));
  }
  
  public static Predicate<DateTime> equivalentTo(String dt) {
    return equivalentTo(new DateTime(dt));
  }
  
  public static Predicate<DateTime> equivalentTo(DateTime dateTime) {
    return Equivalence.equivalentTo(dateTime);
  }
  
  public static final Equivalence<DateTime> Equivalence = equivalence();
  
  private static Equivalence<DateTime> equivalence() {
    return new Equivalence<DateTime>() {
      protected boolean doEquivalent(DateTime a, DateTime b) {
        return a.compareTo(b) == 0;
      }
      protected int doHash(DateTime t) {
        return toUTC(t).hashCode();
      }
    };
  }
  
  public static Selector<DateTime> selectorForAll() {
    return selectorForRange(all());
  }
  
  public static Selector<DateTime> selectorForAtOrAfterNow() {
    return selectorForRange(atOrAfterNow());
  }
  
  public static Selector<DateTime> selectorForAtOrAfter(DateTime dateTime) {
    return selectorForRange(atOrAfter(dateTime));
  }
  
  public static Selector<DateTime> selectorForAtOrBefore(DateTime dateTime) {
    return selectorForRange(atOrBefore(dateTime));
  }
  
  public static Selector<DateTime> selectorForAtOrBetween(DateTime low, DateTime high) {
    return selectorForRange(atOrBetween(low,high));
  }
  
  public static Selector<DateTime> selectorForAtOrBetween(DateTime low, Duration duration) {
    return selectorForRange(atOrBetween(low,low.plus(duration)));
  }
  
  public static Selector<DateTime> selectorForAtOrBetween(Duration duration, DateTime high) {
    return selectorForRange(atOrBetween(high.minus(duration),high));
  }
  
  public static Selector<DateTime> selectorForAtOrBetween(Interval interval) {
    return selectorForRange(atOrBetween(interval.getStart(),interval.getEnd()));
  }
  
  public static Selector<DateTime> selectorForAtBetweenOrBefore(DateTime low, DateTime high) {
    return selectorForRange(atBetweenOrBefore(low,high));
  }
  
  public static Selector<DateTime> selectorForAtBetweenOrBefore(DateTime low, Duration duration) {
    return selectorForRange(atBetweenOrBefore(low,duration));
  }
  
  public static Selector<DateTime> selectorForAtBetweenOrBefore(Duration duration, DateTime high) {
    return selectorForRange(atBetweenOrBefore(duration,high));
  }
  
  public static Selector<DateTime> selectorForAtBetweenOrBefore(Interval interval) {
    return selectorForRange(atBetweenOrBefore(interval));
  }
  
  public static Selector<DateTime> selectorForAfterNow() {
    return selectorForRange(afterNow());
  }
  
  public static Selector<DateTime> selectorForAfter(DateTime dateTime) {
    return selectorForRange(after(dateTime));
  }
  
  public static Selector<DateTime> selectorForBeforeNow() {
    return selectorForRange(beforeNow());
  }
  
  public static Selector<DateTime> selectorForBefore(DateTime dateTime) {
    return selectorForRange(before(dateTime));
  }
  
  public static Selector<DateTime> selectorForBetween(DateTime low, DateTime high) {
    return selectorForRange(between(low,high));
  }
  
  public static Selector<DateTime> selectorForBetween(DateTime low, Duration duration) {
    return selectorForRange(between(low,duration));
  }
  
  public static Selector<DateTime> selectorForBetween(Duration duration, DateTime high) {
    return selectorForRange(between(duration,high));
  }
  
  public static Selector<DateTime> selectorForBetween(Interval interval) {
    return selectorForRange(between(interval));
  }
  
  public static Selector<DateTime> selectorForAfterBetweenOrAt(DateTime low, DateTime high) {
    return selectorForRange(afterBetweenOrAt(low,high));
  }
  
  public static Selector<DateTime> selectorForAfterBetweenOrAt(DateTime low, Duration duration) {
    return selectorForRange(afterBetweenOrAt(low,duration));
  }
  
  public static Selector<DateTime> selectorForAfterBetweenOrAt(Duration duration, DateTime high) {
    return selectorForRange(afterBetweenOrAt(duration,high));
  }
  
  public static Selector<DateTime> selectorForExactlyNow() {
    return selectorForRange(exactlyNow());
  }
  
  public static Selector<DateTime> selectorForExactly(DateTime dateTime) {
    return selectorForRange(exactly(dateTime));
  }
  
  public static Selector<DateTime> selectorForExactlyBefore(DateTime date, Duration duration) {
    return selectorForRange(exactlyBefore(date,duration));
  }
  
  public static Selector<DateTime> selectorForExactlyAfter(DateTime date, Duration duration) {
    return selectorForRange(exactlyAfter(date,duration));
  }
  
  public static Selector<DateTime> selectorForExactlyBeforeNow(Duration duration) {
    return selectorForRange(exactlyBeforeNow(duration));
  }
  
  public static Selector<DateTime> selectorForExactlyAfterNow(Duration duration) {
    return selectorForRange(exactlyAfterNow(duration));
  }
}
