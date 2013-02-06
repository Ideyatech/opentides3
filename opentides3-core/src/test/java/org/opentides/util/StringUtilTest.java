/**
 * 
 * This source code is property of Ideyatech, Inc.
 * All rights reserved. 
 * 
 * StringUtilTest.java
 * Created on Feb 10, 2008, 1:27:19 PM
 */
package org.opentides.util;


import java.util.List;

import junit.framework.Assert;

import org.junit.Test;


/**
 * @author allanctan
 *
 */
public class StringUtilTest {    

    @Test
    public final void testIsEmpty() {
        Assert.assertTrue("space only", StringUtil.isEmpty(" "));
        Assert.assertTrue("empty", StringUtil.isEmpty(""));
        Assert.assertTrue(null, StringUtil.isEmpty(null));
        Assert.assertFalse("a", StringUtil.isEmpty("a"));
        Assert.assertFalse("a with spaces", StringUtil.isEmpty(" a "));
    }
    
    @Test
    public final void testToFixedString() {
        Assert.assertEquals("123", StringUtil.toFixedString(123, 2));
        Assert.assertEquals("123", StringUtil.toFixedString(123, 3));
        Assert.assertEquals("000123", StringUtil.toFixedString(123, 6));
        Assert.assertEquals("00004560", StringUtil.toFixedString(4560, 8));
    }
    
    @Test
    public final void testRemoveHTMLTags() {
        String expected = " This is non html . ";
        String html = "<html> This is <b>non html</b>.</html>";
        Assert.assertEquals(expected, StringUtil.removeHTMLTags(html));
    }
    
    @Test
    public final void testConvertToInt() {
        Assert.assertEquals(321, StringUtil.convertToInt("321", 0));
        Assert.assertEquals(-1, StringUtil.convertToInt("abc", -1));
        Assert.assertEquals(0, StringUtil.convertToInt("0", -1));
        Assert.assertEquals(321, StringUtil.convertToInt("0321", -1));
    }
    
    @Test
    public final void testConvertToLong() {
        Assert.assertEquals(321l, StringUtil.convertToLong("321", 0));
        Assert.assertEquals(-1l, StringUtil.convertToLong("abc", -1));
        Assert.assertEquals(0l, StringUtil.convertToLong("0", -1));
        Assert.assertEquals(321l, StringUtil.convertToLong("0321", -1));
    }
    
    @Test
    public final void testConvertToDouble() {
        Assert.assertEquals(3.21, StringUtil.convertToDouble("3.21", 0));
        Assert.assertEquals(-1.2, StringUtil.convertToDouble("abc", -1.2));
        Assert.assertEquals(0.0, StringUtil.convertToDouble("0", -1));
        Assert.assertEquals(32.1, StringUtil.convertToDouble("032.1", -1));
    }

    @Test
    public final void testParseCsvLine() {
    	String line1 = "1,\"Lion's Lair\",September,";
    	String line2 = "2,Test Word,\"September 15, 1990\",End";
    	String line3 = "";
    	List<String> str1 = StringUtil.parseCsvLine(line1);
    	List<String> str2 = StringUtil.parseCsvLine(line2);
    	List<String> str3 = StringUtil.parseCsvLine(line3);
    	Assert.assertEquals("1", str1.get(0));
    	Assert.assertEquals("Lion's Lair", str1.get(1));
    	Assert.assertEquals("September", str1.get(2));
    	Assert.assertEquals("", str1.get(3));
        	
    	Assert.assertEquals("2", str2.get(0));
    	Assert.assertEquals("Test Word", str2.get(1));
    	Assert.assertEquals("September 15, 1990", str2.get(2));
    	Assert.assertEquals("End", str2.get(3));
    	
       	Assert.assertEquals("", str3.get(0));
    }
}
