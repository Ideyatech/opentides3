/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 
 * NamingUtilTest.java
 * Created on Feb 10, 2008, 1:27:19 PM
 */
package org.opentides.util;



import org.junit.Assert;
import org.junit.Test;

/**
 * @author allanctan
 *
 */
public class NamingUtilTest {
	

	@Test 
	public void testToElementName() {
		Assert.assertEquals("test", NamingUtil.toElementName("Test"));
		Assert.assertEquals("test", NamingUtil.toElementName("test"));
		Assert.assertEquals("test-codes", NamingUtil.toElementName("TestCodes"));
		Assert.assertEquals("test-codes", NamingUtil.toElementName("testCodes"));
		Assert.assertEquals("test-codes-form5", NamingUtil.toElementName("TestCodesForm5"));
		Assert.assertEquals("", NamingUtil.toElementName(""));
	}

	@Test 
	public void testToSQLName() {
		Assert.assertEquals("test", NamingUtil.toSQLName("Test"));
		Assert.assertEquals("test", NamingUtil.toSQLName("test"));
		Assert.assertEquals("test_codes", NamingUtil.toSQLName("TestCodes"));
		Assert.assertEquals("test_codes", NamingUtil.toSQLName("testCodes"));
		Assert.assertEquals("test_codes_form5", NamingUtil.toSQLName("TestCodesForm5"));
		Assert.assertEquals("", NamingUtil.toSQLName(""));
	}
	
	@Test
	public void testToLabel() {
		Assert.assertEquals("Test", NamingUtil.toLabel("Test"));
		Assert.assertEquals("Test", NamingUtil.toLabel("test"));
		Assert.assertEquals("Test Codes", NamingUtil.toLabel("TestCodes"));
		Assert.assertEquals("Test Codes", NamingUtil.toLabel("testCodes"));
		Assert.assertEquals("Test Codes Form Z", NamingUtil.toLabel("TestCodesFormZ"));
		Assert.assertEquals("", NamingUtil.toLabel(""));
	}
	
	@Test
	public void testToGetterName(){
		Assert.assertEquals("getFirstName", NamingUtil.toGetterName("firstName"));
		Assert.assertEquals("", NamingUtil.toGetterName(""));
	}
	
	@Test
	public void testToSettername(){
		Assert.assertEquals("setFirstName", NamingUtil.toSetterName("firstName"));
		Assert.assertEquals("", NamingUtil.toSetterName(""));
	}
	
	@Test
	public void testToAttributeName(){
		Assert.assertEquals("", NamingUtil.toAttributeName(""));
		Assert.assertEquals("a", NamingUtil.toAttributeName("A"));		
		Assert.assertEquals("systemCodes", NamingUtil.toAttributeName("SystemCodes"));		
	}
	
	@Test
    public void testGetPropertyName() {
    	Assert.assertEquals("name", NamingUtil.getPropertyName("getName"));
    	Assert.assertEquals("name", NamingUtil.getPropertyName("setName"));
    	Assert.assertNull(NamingUtil.getPropertyName("garbage"));
    	Assert.assertNull(NamingUtil.getPropertyName(""));
    	Assert.assertNull(NamingUtil.getPropertyName("get"));
       	Assert.assertNull(NamingUtil.getPropertyName("set"));
    	Assert.assertEquals("sampleUnit",NamingUtil.getPropertyName("getsampleUnit"));
    	Assert.assertEquals("sampleUnit",NamingUtil.getPropertyName("getSampleUnit"));    	
    }
    
	@Test
	public void testGetSimpleName() {
		Assert.assertEquals("", NamingUtil.getSimpleName(""));
		Assert.assertEquals("SystemCodes", NamingUtil.getSimpleName("SystemCodes"));
		Assert.assertEquals("SystemCodes", NamingUtil.getSimpleName("org.opentides.bean.SystemCodes"));
	}

	@Test
	public void testPackageName() {
		Assert.assertEquals("", NamingUtil.getPackageName(""));
		Assert.assertEquals("", NamingUtil.getPackageName("SystemCodes"));
		Assert.assertEquals("org.opentides.bean", NamingUtil.getPackageName("org.opentides.bean.SystemCodes"));
	}
}

