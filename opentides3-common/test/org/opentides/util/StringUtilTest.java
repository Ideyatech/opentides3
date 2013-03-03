/**
 * 
 * This source code is property of Ideyatech, Inc.
 * All rights reserved. 
 * 
 * StringUtilTest.java
 * Created on Feb 10, 2008, 1:27:19 PM
 */
package org.opentides.util;


import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.junit.Assert;
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
    public final void testReplace() {
    	Assert.assertEquals(null, StringUtil.replace(null, "*", "*"));
    	Assert.assertEquals("", StringUtil.replace("", "*", "*"));
    	Assert.assertEquals("any", StringUtil.replace("any", "*", null));
    	Assert.assertEquals("any", StringUtil.replace("any", null, "*"));
    	Assert.assertEquals("any", StringUtil.replace("any", "", "*"));
    	Assert.assertEquals("aba", StringUtil.replace("aba", "a", null));    	
    	Assert.assertEquals("b", StringUtil.replace("aba", "a", ""));
    	Assert.assertEquals("zbz", StringUtil.replace("aba", "a", "z"));    	
    	
    	Assert.assertEquals("any", StringUtil.replace("any", "*", "*", 0));
    	Assert.assertEquals("abaa", StringUtil.replace("abaa", "a", null, -1));    	
    	Assert.assertEquals("b", StringUtil.replace("abaa", "a", "", -1));    	
    	Assert.assertEquals("abaa", StringUtil.replace("abaa", "a", "z", 0));    	
    	Assert.assertEquals("zbaa", StringUtil.replace("abaa", "a", "z", 1));    	
    	Assert.assertEquals("zbza", StringUtil.replace("abaa", "a", "z", 2));    	
    	Assert.assertEquals("zbzz", StringUtil.replace("abaa", "a", "z", -1)); 	
    }
    
    @Test
    public final void testEscapeSql() {
    	Assert.assertEquals(null, StringUtil.escapeSql(null, true));
    	Assert.assertEquals("", StringUtil.escapeSql("", true));
    	Assert.assertEquals("90\\%", StringUtil.escapeSql("90%", true));
    	Assert.assertEquals("Phil''s", StringUtil.escapeSql("Phil's", true));
    	Assert.assertEquals("Phil''s\\\\\\\\Jay", StringUtil.escapeSql("Phil's\\Jay", true));
    	Assert.assertEquals("Phil''s\\\\Jay", StringUtil.escapeSql("Phil's\\Jay", false));
    	Assert.assertEquals("Phil\\_Jay", StringUtil.escapeSql("Phil_Jay", true));
    	Assert.assertEquals("Phil_Jay", StringUtil.escapeSql("Phil_Jay", false));    	
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
        Assert.assertEquals(new Double(3.21), StringUtil.convertToDouble("3.21", 0));
        Assert.assertEquals(new Double(-1.2d), StringUtil.convertToDouble("abc", -1.2));
        Assert.assertEquals(new Double (0.0d), StringUtil.convertToDouble("0", -1));
        Assert.assertEquals(new Double(32.1d), StringUtil.convertToDouble("032.1", -1));
    }
    
    @Test
    public final void testGenerateRandomString() {
    	Assert.assertEquals("", StringUtil.generateRandomString(0));
    	Assert.assertEquals("", StringUtil.generateRandomString(-1));
    	Assert.assertEquals(7, StringUtil.generateRandomString(7).length());
    	Assert.assertFalse(StringUtil.isEmpty(StringUtil.generateRandomString(3)));
    }
    
    @Test
    public final void testEncryptDecrypt() throws Exception {
		String cipher = StringUtil.encrypt("allan7@test.com");
		Assert.assertEquals("allan7@test.com",StringUtil.decrypt(cipher));
		cipher = StringUtil.encrypt("wqwer12345admin@ideyatech.com");
		Assert.assertEquals("wqwer12345admin@ideyatech.com",StringUtil.decrypt(cipher));
		Assert.assertNull(StringUtil.decrypt(""));
		Assert.assertNull(StringUtil.decrypt("abc"));
    }

    @Test
    public final void testGetEncryptedPassword() throws NoSuchAlgorithmException {
    	Assert.assertEquals("+JWAOqbF4Q2BpAxzpG9AaZd/psc7eWiEVVbpjZDyj/Y=", StringUtil.getEncryptedPassword("ideyatech"));
    	Assert.assertEquals("GYxzUrSNqswso7ddHvxj+kG8wy8YAfQ4dFi7Y4GTSwQ=", StringUtil.getEncryptedPassword("sample_password"));
    	Assert.assertEquals("", StringUtil.getEncryptedPassword(null));
    	Assert.assertEquals("", StringUtil.getEncryptedPassword(""));
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
    
    @Test
    public final void testExplode() {
    	Assert.assertNull(StringUtil.explode(',', null));
    	Assert.assertEquals("a,b,c", StringUtil.explode(',', new String[] {"a","b","c"}));    	
    }
}
