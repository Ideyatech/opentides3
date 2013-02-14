/**
 * 
 * This source code is property of Ideyatech, Inc.
 * All rights reserved. 
 * 
 * StringUtilTest.java
 * Created on Feb 10, 2008, 1:27:19 PM
 */
package org.opentides.util;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.opentides.bean.BaseEntity;
import org.opentides.bean.SystemCodes;
import org.opentides.bean.user.UserCredential;
import org.opentides.exception.CodeGenerationException;

import com.ideyatech.bean.UserCriteria;
import com.ideyatech.example.bean.Ninja;

/**
 * @author allanctan
 *
 */
public class CrudUtilTest {
	
	@Test
	public void testBuildCreateMessage() {
		SystemCodes sc = new SystemCodes("category","key","value");
		String expected = "Added System Codes Value:value - Key=key and Category=category";
		Assert.assertEquals(expected,
				CrudUtil.buildCreateMessage(sc));
	}
	 
	@Test
	public void testBuildUpdateMessage() {
		SystemCodes oldsc = new SystemCodes("categoryold","keyold","value");
		SystemCodes newsc = new SystemCodes("categorynew","keynew","value");
		SystemCodes samesc = new SystemCodes("categoryold","keyold","value");
		
		String expected = "Changed System Codes Value:value - Key from 'keyold' to 'keynew' and Category from 'categoryold' to 'categorynew'";
		Assert.assertEquals(expected,
				CrudUtil.buildUpdateMessage(oldsc, newsc));
		Assert.assertEquals("",
				CrudUtil.buildUpdateMessage(oldsc, samesc));
	}

	@Test
	public void testBuildUpdateArrayMessage() {
		SystemCodes oldsc = new SystemCodes("categoryold","keyold","valueold");
		SystemCodes newsc = new SystemCodes("categorynew","keynew","valuenew");
		SystemCodes samesc = new SystemCodes("categoryold","keysame","valuesame");
		List<SystemCodes> oldFaves = new ArrayList<SystemCodes>();
		oldFaves.add(samesc);
		oldFaves.add(oldsc);
		List<SystemCodes> newFaves = new ArrayList<SystemCodes>();
		newFaves.add(newsc);
		newFaves.add(samesc);

		UserCriteria oldUser = new UserCriteria();
		UserCriteria newUser = new UserCriteria();
		oldUser.setFavorites(oldFaves);
		newUser.setFavorites(newFaves);
		
		String expected = "Changed User Criteria Username: added Favorites [valuenew] and removed Favorites [valueold]";
		Assert.assertEquals(expected,
				CrudUtil.buildUpdateMessage(oldUser, newUser));
	}

	@Test
	public void testBuildUpdateSystemCodesMessage() {
		SystemCodes oldsc = new SystemCodes("categoryold","keyold","old");
		SystemCodes newsc = new SystemCodes("categorynew","keynew","new");
		Ninja oldtc = new Ninja();
//		oldtc.setStatus(oldsc);
		Ninja newtc = new Ninja();
//		newtc.setStatus(newsc);
		Ninja sametc = new Ninja();
//		sametc.setStatus(oldsc);
		
		String expected = "Changed Test Codes Key:Status from 'keyold:old' to 'keynew:new'";
		Assert.assertEquals(expected,
				CrudUtil.buildUpdateMessage(oldtc, newtc));
		Assert.assertEquals("",
				CrudUtil.buildUpdateMessage(oldtc, sametc));
	}
    
    @Test 
    public void testBuildJpaQueryString() {
    	SystemCodes sc = new SystemCodes();
    	Assert.assertEquals("", CrudUtil.buildJpaQueryString(sc, true));
    	sc.setValue("");
    	Assert.assertEquals("", CrudUtil.buildJpaQueryString(sc, true));
    	
    	sc.setKey("PH");
		Assert.assertEquals(" where obj.key = 'PH'", CrudUtil
				.buildJpaQueryString(sc, true));
		Assert.assertEquals(" where obj.key like '%PH%'", CrudUtil
				.buildJpaQueryString(sc, false));
 
    	sc.setValue("Philippines");
		Assert.assertEquals(
				" where obj.key = 'PH' and obj.value = 'Philippines'",
       						CrudUtil.buildJpaQueryString(sc, true));
		Assert.assertEquals(
						" where obj.key like '%PH%' and obj.value like '%Philippines%'",
       						CrudUtil.buildJpaQueryString(sc, false));

       	
//       	Category cat = new Category();
//       	cat.setId(12l);
//       	sc.setCategory(cat);
//       	Assert.assertEquals(" where key = 'PH' and value = 'Philippines' and category.id = 12",
//       						CrudUtil.buildJpaQueryString(sc, true));
//       	Assert.assertEquals(" where key like '%PH%' and value like '%Philippines%' and category.id = 12",
//       						CrudUtil.buildJpaQueryString(sc, false));
    }
    
    @Test 
    public void testBuildJpqQueryStringSpecialChars() {
    	SystemCodes sc = new SystemCodes();
		// handle (%)
    	sc.setValue("Phil%");
    	sc.setKey("");
		Assert.assertEquals(
				" where obj.value = 'Phil%'",
       						CrudUtil.buildJpaQueryString(sc, true));
		Assert.assertEquals(
				" where obj.value like '%Phil\\%%'",
       						CrudUtil.buildJpaQueryString(sc, false));

		// handle '
    	sc.setValue("Phil's");
    	sc.setKey("");
		Assert.assertEquals(
				" where obj.value = 'Phil''s'",
       						CrudUtil.buildJpaQueryString(sc, true));
		Assert.assertEquals(
				" where obj.value like '%Phil''s%'",
       						CrudUtil.buildJpaQueryString(sc, false));

		// handle (\)
    	sc.setValue("Phil's\\Jay");
    	sc.setKey("");
		Assert.assertEquals(
				" where obj.value = 'Phil''s\\\\Jay'",
       						CrudUtil.buildJpaQueryString(sc, true));
		Assert.assertEquals(
				" where obj.value like '%Phil''s\\\\\\\\Jay%'",
       						CrudUtil.buildJpaQueryString(sc, false));
		
		// handle (_)
    	sc.setValue("Phil_Jay");
    	sc.setKey("");
		Assert.assertEquals(
				" where obj.value = 'Phil_Jay'",
       						CrudUtil.buildJpaQueryString(sc, true));
		Assert.assertEquals(
				" where obj.value like '%Phil\\_Jay%'",
       						CrudUtil.buildJpaQueryString(sc, false));
    }
    
    /**
     * Test for inner class
     */
    @Test 
    public void testBuildJpaQueryString2() {
    	UserCriteria user = new UserCriteria();
    	UserCredential cred = new UserCredential();
    	user.setFirstName("Test");
      	cred.setUsername("testname");
    	cred.setEnabled(true);
    	user.setCredential(cred);

		Assert
				.assertEquals(
						" where obj.firstName like '%Test%' and obj.credential.username like '%testname%' and obj.credential.enabled = true",
					CrudUtil.buildJpaQueryString(user, false));

		Assert
				.assertEquals(
						" where obj.firstName = 'Test' and obj.credential.username = 'testname' and obj.credential.enabled = true",
					CrudUtil.buildJpaQueryString(user, true));

    }
    
    /**
     * Test for numeric types
     */
    @Test 
    public void testBuildJpaQueryString3() {
    	UserCriteria user = new UserCriteria();
    	UserCredential cred = new UserCredential();
    	user.setFirstName("Test");
    	cred.setId(123l);
    	cred.setEnabled(null);
    	user.setCredential(cred);

		Assert
				.assertEquals(
						" where obj.firstName like '%Test%' and obj.credential.id = 123",
					CrudUtil.buildJpaQueryString(user, false));

		Assert.assertEquals(
				" where obj.firstName = 'Test' and obj.credential.id = 123",
					CrudUtil.buildJpaQueryString(user, true));

    }
    
    @Test 
    public void testRetrieveObjectValue() {
    	UserCriteria user = new UserCriteria();
    	UserCredential cred = new UserCredential();
    	user.setFirstName("Test");
       	user.setEmailAddress("admin@ideyatech.com");
      	cred.setUsername("testname");
    	cred.setPassword("password");
    	cred.setId(123l);
    	cred.setEnabled(true);
    	user.setCredential(cred);
    	Assert.assertEquals("Test", CrudUtil.retrieveObjectValue(user, "firstName"));
    	Assert.assertEquals("admin@ideyatech.com", CrudUtil.retrieveObjectValue(user, "emailAddress"));
    	Assert.assertEquals("testname", CrudUtil.retrieveObjectValue(user, "credential.username"));
    	// Assert.assertEquals("password", CrudUtil.retrieveObjectValue(user, "credential.password"));
    	Assert.assertEquals(123l,CrudUtil.retrieveObjectValue(user, "credential.id"));
    	try {
    		Assert.assertEquals(null,CrudUtil.retrieveObjectValue(user, "garbage"));
    		Assert.fail("No exception thrown on invalid property [garbage]");
    	} catch (CodeGenerationException iie) {
    		
    	}
    	try {
    		Assert.assertEquals(null,CrudUtil.retrieveObjectValue(user, "credential.garbage"));
    		Assert.fail("No exception thrown on invalid property [credential.garbage]");
    	} catch (CodeGenerationException iie) {
    		
    	}
    }

    @SuppressWarnings("rawtypes")
	@Test 
    public void testRetrieveObjectValueArray1() {
    	UserCriteria user = new UserCriteria();
    	user.setFirstName("Test");
       	user.setEmailAddress("admin@ideyatech.com");
       	List<SystemCodes> codes = new ArrayList<SystemCodes>();
       	codes.add(new SystemCodes("CATEGORY", "STRING_1", "VALUE_1"));
       	codes.add(new SystemCodes("CATEGORY", "STRING_2", "VALUE_2"));
       	codes.add(new SystemCodes("CATEGORY", "STRING_3", "VALUE_3"));
       	user.setFavorites(codes);

       	List keysResult = (List) CrudUtil.retrieveObjectValue(user, "favorites.key");
       	Assert.assertEquals(3, keysResult.size());
       	Assert.assertEquals("STRING_1", keysResult.get(0));
       	Assert.assertEquals("STRING_2", keysResult.get(1));
       	Assert.assertEquals("STRING_3", keysResult.get(2));
       	
       	List valuesResult = (List) CrudUtil.retrieveObjectValue(user, "favorites.value");
       	Assert.assertEquals(3, keysResult.size());
       	Assert.assertEquals("VALUE_1", valuesResult.get(0));
       	Assert.assertEquals("VALUE_2", valuesResult.get(1));
       	Assert.assertEquals("VALUE_3", valuesResult.get(2));
}

    @Test 
    public void testRetrieveObjectMap() {
    	UserCriteria user = new UserCriteria();
    	UserCredential cred = new UserCredential();
    	user.setFirstName("Test");
       	user.setEmailAddress("admin@ideyatech.com");
      	cred.setUsername("testname");
    	cred.setPassword("password");
    	cred.setId(123l);
    	cred.setEnabled(true);
    	user.setCredential(cred);
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("user", user);
    	map.put("cred", cred);
    	Assert.assertEquals("Test", CrudUtil.retrieveObjectValue(map, "user.firstName"));
    	Assert.assertEquals("admin@ideyatech.com", CrudUtil.retrieveObjectValue(map, "user.emailAddress"));
    	Assert.assertEquals("testname", CrudUtil.retrieveObjectValue(map, "cred.username"));
    	Assert.assertEquals("testname", CrudUtil.retrieveObjectValue(map, "user.credential.username"));
		Assert.assertEquals(null,CrudUtil.retrieveObjectValue(map, "credential.garbage"));
		Assert.assertEquals(null,CrudUtil.retrieveObjectValue(map, "garbage"));
    }
    
    @Test 
    public void testRetrieveObjectType() {
    	UserCriteria user = new UserCriteria();
    	UserCredential cred = new UserCredential();
    	user.setFirstName("Test");
       	user.setEmailAddress("admin@ideyatech.com");
      	cred.setUsername("testname");
    	cred.setPassword("password");
    	cred.setId(123l);
    	cred.setEnabled(true);
    	user.setCredential(cred);
    	Assert.assertEquals(String.class, CrudUtil.retrieveObjectType(user, "firstName"));
    	Assert.assertEquals(String.class, CrudUtil.retrieveObjectType(user, "credential.username"));
    	Assert.assertEquals(Long.class, CrudUtil.retrieveObjectType(user, "credential.id"));
    	try {
    		Assert.assertEquals(null,CrudUtil.retrieveObjectType(user, "garbage"));
    		Assert.fail("No exception thrown on invalid property [garbage]");
    	} catch (CodeGenerationException iie) {
    		
    	}
    	try {
    		Assert.assertEquals(null,CrudUtil.retrieveObjectType(user, "credential.garbage"));
    		Assert.fail("No exception thrown on invalid property [credential.garbage]");
    	} catch (CodeGenerationException iie) {
    		
    	}   	
    }

    @Test 
    public void testReplaceSQLParameters() {
    	UserCriteria user = new UserCriteria();
    	UserCredential cred = new UserCredential();
    	user.setFirstName("Test");
       	user.setEmailAddress("admin@ideyatech.com");
      	cred.setUsername("testname");
    	cred.setPassword("password");
    	cred.setId(123l);
    	cred.setEnabled(true);
    	user.setCredential(cred);
    	Assert.assertEquals("firstName='Test' and credential.id=123", 
    			CrudUtil.replaceSQLParameters("firstName=:firstName and credential.id=:credential.id", user));
    }
    
    @Test 
    public void testReplaceSQLParametersList() {
    	UserCriteria user = new UserCriteria();
    	UserCredential cred = new UserCredential();
       	List<SystemCodes> favorites = new ArrayList<SystemCodes>();
       	List<String> alias = new ArrayList<String>();
       	favorites.add(new SystemCodes("FAVORITES","BANANA","Banana"));
       	favorites.add(new SystemCodes("FAVORITES","MANGO","Mango"));
       	alias.add("name1");
       	alias.add("name2");
    	user.setFirstName("Test");
       	user.setEmailAddress("admin@ideyatech.com");
       	user.setFavorites(favorites);
       	user.setAlias(alias);
      	cred.setUsername("testname");
    	cred.setPassword("password");
    	cred.setId(123l);
    	cred.setEnabled(true);
    	user.setCredential(cred);
    	Assert.assertEquals("firstName='Test' and credential.id=123 and favorites in ( 'BANANA', 'MANGO' ) and alias in ( 'name1', 'name2' )", 
    			CrudUtil.replaceSQLParameters("firstName=:firstName and credential.id=:credential.id " +
    					"and favorites in ( :favorites ) and alias in ( :alias )", user));
    }
     
	@Test
	public void testGetAllFields() throws SecurityException, NoSuchFieldException {
		List<Field> fields = CrudUtil.getAllFields(Ninja.class);
		Assert.assertEquals(25, fields.size());
		Field keyField = Ninja.class.getDeclaredField("key");
		Field statusField = Ninja.class.getDeclaredField("status");
		Field createDateField = BaseEntity.class.getDeclaredField("createDate");
		Assert.assertTrue(fields.contains(keyField));
		Assert.assertTrue(fields.contains(statusField));
		Assert.assertTrue(fields.contains(createDateField));
	}

}

