package org.apache.abdera2.test.common.date;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.Calendar;
import java.util.Date;

import org.apache.abdera2.common.date.DateTime;
import org.junit.Test;

public class DateTimeTest {

    @Test
    public void testHashCode() {
        long time = System.currentTimeMillis();
        DateTime ad1 = new DateTime(time);
        assertTrue(ad1.hashCode() == ad1.hashCode());
        DateTime ad2 = new DateTime(time + 10);
        assertFalse(ad1.hashCode() == ad2.hashCode());
    }

    @Test
    public void testDateTimeDate() {
        Date now = new Date();
        DateTime adNow = new DateTime(now);
        assertEquals(now, adNow.getDate());
        // mutate 'now', to assert DateTime cloned value
        now.setTime(now.getTime() + 10);
        assertFalse(now.getTime() == adNow.getTime());
    }

    @Test
    public void testGetDate() {
        Date now = new Date();
        DateTime adNow = new DateTime(now);
        assertEquals(now, adNow.getDate());

        // getDate, then mutate to assert it was cloned
        Date now2 = adNow.getDate();
        now2.setTime(now2.getTime() + 10);
        assertFalse(now2.equals(adNow.getDate()));
    }

    @Test
    public void testClone() {
        DateTime ad = new DateTime();
        DateTime adClone = (DateTime)ad.clone();
        assertEquals(ad, adClone);
        assertNotSame(ad, adClone);
    }

    @Test
    public void testDateTime() {
        Date now = new Date();
        DateTime atomNow = DateTime.valueOf(now);
        String rfc3339 = atomNow.getValue();
        atomNow = DateTime.valueOf(rfc3339);
        Date parsed = atomNow.getDate();
        assertEquals(now, parsed);
    }

    @Test
    public void testDateTime2() {
        String date = "2007-12-13T14:15:16.123Z";
        DateTime DateTime = new DateTime(date);
        Calendar calendar = DateTime.getCalendar();
        DateTime = new DateTime(calendar);
        assertEquals(date, DateTime.toString());
    }

    @Test
    public void testDateTime3() {
        long date = System.currentTimeMillis();
        DateTime DateTime = new DateTime(date);
        Calendar calendar = DateTime.getCalendar();
        DateTime = new DateTime(calendar);
        assertEquals(date, DateTime.getTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalDateFormat() {
        String date = "";
        new DateTime(date);
    }

}
