/**
 * 
 * This source code is property of Ideyatech, Inc.
 * All rights reserved. 
 * 
 * StringUtilTest.java
 * Created on Feb 10, 2008, 1:27:19 PM
 */
package org.opentides.util;


import junit.framework.Assert;

import org.junit.Test;

/**
 * @author allanctan
 *
 */
public class NamingUtilTest {
	
	
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
    
}

