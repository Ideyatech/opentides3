package org.opentides.context.support;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.NoSuchMessageException;

public class PatternResourceBundleMessageSourceTest {
	
	private PatternResourceBundleMessageSource ms;
	
	@Before
	public void init() {
		ms = new PatternResourceBundleMessageSource();
	}
	
	@Test
	public void testSetBaseNames() {
		ms.setBasenames("classpath:languages/*messages.properties");
		assertEquals("Ninja Message1", ms.getMessage("ninja.message1", null, Locale.getDefault()));
	}
	
	@Test
	public void testSetBaseNamesAll() {
		ms.setBasenames("classpath*:languages/*messages.properties");
		assertEquals("Ninja Message1", ms.getMessage("ninja.message1", null, Locale.getDefault()));
	}
	
	@Test(expected = NoSuchMessageException.class)
	public void testSetBaseNamesNoMessageFound() {
		ms.setBasenames("classpath:languages/*messages.properties");
		ms.getMessage("dummy.message1", null, Locale.getDefault());
	}

}
