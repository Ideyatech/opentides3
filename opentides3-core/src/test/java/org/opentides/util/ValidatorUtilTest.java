/*
   Licensed to the Apache Software Foundation (ASF) under one
   or more contributor license agreements.  See the NOTICE file
   distributed with this work for additional information
   regarding copyright ownership.  The ASF licenses this file
   to you under the Apache License, Version 2.0 (the
   "License"); you may not use this file except in compliance
   with the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing,
   software distributed under the License is distributed on an
   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
   KIND, either express or implied.  See the License for the
   specific language governing permissions and limitations
   under the License.    
 */
package org.opentides.util;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class ValidatorUtilTest {

	@Test
	public void testIsEmail() {
		assertTrue(ValidatorUtil.isEmail("email_1@email.com"));
		assertTrue(ValidatorUtil.isEmail("email_email@email.com.ph"));
		assertTrue(ValidatorUtil.isEmail("email.email@email.com.ph"));
		assertFalse(ValidatorUtil.isEmail("email.email@email@com.ph"));
		assertFalse(ValidatorUtil.isEmail("email.emailemail.com.ph"));
		assertFalse(ValidatorUtil.isEmail("email.emailemail.com.ph@"));
		assertFalse(ValidatorUtil.isEmail("a.com"));
		assertFalse(ValidatorUtil.isEmail(""));
	}

	@Test
	public void testIsPhoneNumber() {
		assertTrue(ValidatorUtil.isPhoneNumber("123-456"));
		assertTrue(ValidatorUtil.isPhoneNumber("123 456 1111"));
		assertTrue(ValidatorUtil.isPhoneNumber("1234561111"));
		assertTrue(ValidatorUtil.isPhoneNumber("123-456 1111"));
		assertFalse(ValidatorUtil.isPhoneNumber("123+456+1111"));
		assertFalse(ValidatorUtil.isPhoneNumber("+456+1111"));
		assertFalse(ValidatorUtil.isPhoneNumber("AAA-456-1111"));
	}

	@Test
	public void testIsMatchRegex() {
		
	}

	@Test
	public void testIsNumeric() {
		assertTrue(ValidatorUtil.isNumeric("100"));
		assertTrue(ValidatorUtil.isNumeric("0.001234"));
		assertTrue(ValidatorUtil.isNumeric("123.25"));
		assertTrue(ValidatorUtil.isNumeric("100,000"));
		assertFalse(ValidatorUtil.isNumeric("100.0.1"));
	}

	@Test
	public void testIsValidDateRange() {
		Calendar cal = Calendar.getInstance();
		cal.set(2013, 8, 10);
		Date date1 = cal.getTime();
		cal.set(2013, 9, 10);
		Date date2 = cal.getTime();
		
		assertTrue(ValidatorUtil.isValidDateRange(date1, date2, "yyyy-MM-dd"));
		assertFalse(ValidatorUtil.isValidDateRange(date2, date1, "yyyy-MM-dd"));
		
		cal.set(2013, 9, 25);
		date1 = cal.getTime();
		date2 = cal.getTime();
		
		assertTrue(ValidatorUtil.isValidDateRange(date1, date2, "yyyy-MM-dd"));
	}

}
