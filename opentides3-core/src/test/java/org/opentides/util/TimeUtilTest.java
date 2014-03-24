/**
 * 
 */
package org.opentides.util;

import java.text.ParseException;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author allantan
 *
 */
public class TimeUtilTest {

	/**
	 * Test method for {@link org.opentides.util.TimeUtil#prettyTime(java.util.Date)}.
	 * 
	 * Rules are as follows:
	 * less than 1 minute, returns "just now"
	 * less than 5 minutes, returns "moments ago"
	 * 5-50 minutes ago, returns "x minutes ago"
	 * 50 mins to 1:50 hours ago, return "about an hour ago" 
	 * less than 24 hours, returns "x hours ago"
	 * 1-2 days ago, return "yesterday"
	 * if less than 1 week, returns "x days ago"
	 * otherwise, return "date"
	 * 
	 */
	@Test
	public void testPrettyTimeJustNow() {
		long justNow = System.currentTimeMillis();
		Assert.assertEquals("just now", TimeUtil.prettyTime(new Date(justNow)));
		Assert.assertEquals("just now", TimeUtil.prettyTime(new Date(justNow-50000)));
	}

	@Test
	public void testPrettyTimeMomentsAgo() {
		long moments = System.currentTimeMillis()-61000;
		Assert.assertEquals("moments ago", TimeUtil.prettyTime(new Date(moments)));
		Assert.assertEquals("moments ago", TimeUtil.prettyTime(new Date(moments-200000)));
	}

	@Test
	public void testPrettyTimeMinutesAgo() {
		long now = System.currentTimeMillis();
		Assert.assertEquals("5 minutes ago", TimeUtil.prettyTime(new Date(now-310000)));
		Assert.assertEquals("31 minutes ago", TimeUtil.prettyTime(new Date(now-1866000)));
		Assert.assertEquals("50 minutes ago", TimeUtil.prettyTime(new Date(now-3000000)));
	}

	@Test
	public void testPrettyTimeAnHourAgo() {
		long now = System.currentTimeMillis();
		Assert.assertEquals("about an hour ago", TimeUtil.prettyTime(new Date(now-3600000)));
		Assert.assertEquals("about an hour ago", TimeUtil.prettyTime(new Date(now-6000000)));
	}

	@Test
	public void testPrettyTimeHoursAgo() {
		long now = System.currentTimeMillis();
		Assert.assertEquals("2 hours ago", TimeUtil.prettyTime( new Date(now- 7200000)));
		Assert.assertEquals("7 hours ago", TimeUtil.prettyTime( new Date(now-25200000)));
		Assert.assertEquals("23 hours ago", TimeUtil.prettyTime(new Date(now-82800000)));
	}

	@Test
	public void testPrettyTimeYesterday() {
		long now = System.currentTimeMillis();
		Assert.assertEquals("yesterday", TimeUtil.prettyTime(new Date(now-86400000)));
		Assert.assertEquals("yesterday", TimeUtil.prettyTime(new Date(now-165600000)));
	}
	
	@Test
	public void testPrettyTimeDaysAgo() {
		long now = System.currentTimeMillis();
		Assert.assertEquals("2 days ago", TimeUtil.prettyTime(new Date(now-180000000)));
		Assert.assertEquals("6 days ago", TimeUtil.prettyTime(new Date(now-518400000)));
	}

	@Test
	public void testPrettyTimeLastWeek() {
		long now = System.currentTimeMillis();
		Assert.assertEquals("last week", TimeUtil.prettyTime(new Date(now-691200000)));
		Assert.assertEquals("last week", TimeUtil.prettyTime(new Date(now-1123200000)));
	}

	@Test
	public void testPrettyTimeDate() throws ParseException {		
		Date future = DateUtil.convertFlexibleDate("12/1/2030");
		Date past = DateUtil.convertFlexibleDate("1/12/1977");
		Assert.assertEquals("on December 1, 2030", TimeUtil.prettyTime(future));
		Assert.assertEquals("last January 12, 1977", TimeUtil.prettyTime(past));
	}

}
