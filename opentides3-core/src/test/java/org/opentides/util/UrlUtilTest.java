package org.opentides.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class UrlUtilTest {

	@Test
	public void testGetHostname() {
		assertEquals("www.ideyatech.com", UrlUtil.getHostname("http://www.ideyatech.com"));
		assertEquals("www.ideyatech.com", UrlUtil.getHostname("http://www.ideyatech.com/"));
		assertEquals("www.ideyatech.com", UrlUtil.getHostname("http://www.ideyatech.com/test/original"));
		assertEquals("www.ideyatech.com", UrlUtil.getHostname("www.ideyatech.com"));
		assertEquals("www.ideyatech.com", UrlUtil.getHostname("www.ideyatech.com/"));
		assertEquals("www.ideyatech.com", UrlUtil.getHostname("www.ideyatech.com/test/original"));
	}
	
	@Test 
	public void testGetAbsoluteUrl() {
		assertEquals("http://www.google.com/images/one.gif", UrlUtil.getAbsoluteUrl("http://www.google.com", "/images/one.gif"));
		assertEquals("http://www.google.com/images/one.gif", UrlUtil.getAbsoluteUrl("http://www.google.com/images/test.html", "/images/one.gif"));
		assertEquals("http://www.google.com/images/one.gif", UrlUtil.getAbsoluteUrl("http://www.google.com/images/test.html", "one.gif"));
		assertEquals("http://www.google.com/images/one.gif", UrlUtil.getAbsoluteUrl("http://www.google.com", "images/one.gif"));
		assertEquals("http://www.google.com:8080/images/one.gif", UrlUtil.getAbsoluteUrl("http://www.google.com:8080/images/test.html", "/images/one.gif"));
		assertEquals("http://www.google.com:8080/images/one.gif", UrlUtil.getAbsoluteUrl("http://www.google.com:8080/images/test.html", "one.gif"));
		assertEquals("http://www.google.com/images/one.gif", UrlUtil.getAbsoluteUrl("http://www.google.com/", "/images/one.gif"));
		assertEquals("http://www.google.com/images/one.gif", UrlUtil.getAbsoluteUrl("http://www.google.com/", "images/one.gif"));
		assertEquals("http://www.google.com/images/one.gif", UrlUtil.getAbsoluteUrl("http://www.google.com/test.html", "/images/one.gif"));
		assertEquals("http://www.google.com/images/one.gif", UrlUtil.getAbsoluteUrl("http://www.google.com/test.html", "images/one.gif"));
	}

	@Test
	public void testEnsureProtocol() {
		assertEquals("http://www.yahoo.com",UrlUtil.ensureProtocol("http://www.yahoo.com"));
		assertEquals("ftp://www.yahoo.com",UrlUtil.ensureProtocol("www.yahoo.com","ftp"));
		assertEquals("ftp://www.yahoo.com",UrlUtil.ensureProtocol("ftp://www.yahoo.com"));
		assertEquals("http://www.yahoo.com",UrlUtil.ensureProtocol("www.yahoo.com"));
		assertEquals("http://ftp:www.yahoo.com",UrlUtil.ensureProtocol("ftp:www.yahoo.com"));
	}

	@Test
	public void testGetUrlParam() {
		assertEquals("value",
				UrlUtil.getURLParam("http://www.test.com?key=value", "key"));
		assertEquals("value",
				UrlUtil.getURLParam("http://www.test.com?dum=dum&key=value&key2=sample", "key"));
		assertEquals("value",
				UrlUtil.getURLParam("http://www.test.com.ph?key2=sample&key=value", "key"));
	}
}
