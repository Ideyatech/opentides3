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

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.opentides.bean.AuditableField;
import org.opentides.bean.SystemCodes;
import org.opentides.bean.user.BaseUser;

import com.ideyatech.bean.Ninja;

/**
 * Unit test for {@link CacheUtil}
 * @author gino
 *
 */
public class CacheUtilTest {

	/**
	 * Test method for {@link org.opentides.util.CacheUtil#getReadableName(org.opentides.bean.BaseEntity)}.
	 */
	@Test
	public void testGetReadableName() {
		Ninja ninja = new Ninja();
		assertEquals("Ninja", CacheUtil.getReadableName(ninja));
		
		BaseUser baseUser = new BaseUser();
		assertEquals("Base User", CacheUtil.getReadableName(baseUser));
	}

	/**
	 * Test method for {@link org.opentides.util.CacheUtil#getAuditable(org.opentides.bean.BaseEntity)}.
	 */
	@Test
	public void testGetAuditable() {
		Ninja ninja = new Ninja();
		assertEquals(getNinjaExpectedAuditableFields(), CacheUtil.getAuditable(ninja));
		
		SystemCodes systemCodes = new SystemCodes();
		assertEquals(getSystemCodesExpectedAuditableFields(), CacheUtil.getAuditable(systemCodes));
	}
	
	private List<AuditableField> getNinjaExpectedAuditableFields() {
		List<AuditableField> auditableFields = new ArrayList<>();
		auditableFields.add(new AuditableField("id"));
		auditableFields.add(new AuditableField("firstName"));
		auditableFields.add(new AuditableField("lastName"));
		auditableFields.add(new AuditableField("email"));
		auditableFields.add(new AuditableField("description"));
		auditableFields.add(new AuditableField("age"));
		auditableFields.add(new AuditableField("score"));
		auditableFields.add(new AuditableField("joinDate"));
		auditableFields.add(new AuditableField("active"));
		auditableFields.add(new AuditableField("status"));
		auditableFields.add(new AuditableField("secretCode"));
		auditableFields.add(new AuditableField("partner"));
		auditableFields.add(new AuditableField("skillSet"));
		auditableFields.add(new AuditableField("gender"));
		auditableFields.add(new AuditableField("nextFight"));
		auditableFields.add(new AuditableField("sellingPrice"));
		auditableFields.add(new AuditableField("avatar"));
		auditableFields.add(new AuditableField("attachment"));
		auditableFields.add(new AuditableField("images"));
		auditableFields.add(new AuditableField("comments"));
		return auditableFields;
	}
	
	private List<AuditableField> getSystemCodesExpectedAuditableFields() {
		List<AuditableField> auditableFields = new ArrayList<>();
		auditableFields.add(new AuditableField("id"));
		auditableFields.add(new AuditableField("key"));
		auditableFields.add(new AuditableField("value"));
		auditableFields.add(new AuditableField("category"));
		auditableFields.add(new AuditableField("numberValue"));
		auditableFields.add(new AuditableField("sortOrder"));
		return auditableFields;
	}

	/**
	 * Test method for {@link org.opentides.util.CacheUtil#getPrimaryField(org.opentides.bean.BaseEntity)}.
	 */
	@Test
	public void testGetPrimaryField() {
		AuditableField ninjaPrimaryField = new AuditableField("getCompleteName", "Name");
		Ninja ninja = new Ninja();
		assertEquals(ninjaPrimaryField, CacheUtil.getPrimaryField(ninja));
		
		AuditableField systemCodesPrimaryField = new AuditableField("value");
		SystemCodes systemCodes = new SystemCodes();
		assertEquals(systemCodesPrimaryField, CacheUtil.getPrimaryField(systemCodes));
	}

	/**
	 * Test method for {@link org.opentides.util.CacheUtil#getPersistentFields(org.opentides.bean.BaseEntity)}.
	 */
	@Test
	public void testGetPersistentFields() {
		Ninja ninja = new Ninja();
		assertEquals(getNinjaExpectedPersistentFields(), CacheUtil.getPersistentFields(ninja));
	}
	
	private List<String> getNinjaExpectedPersistentFields() {
		List<String> persistentFields = new ArrayList<>();
		persistentFields.add("id");
		persistentFields.add("createDate");
		persistentFields.add("updateDate");
		persistentFields.add("createdBy");
		persistentFields.add("version");
		persistentFields.add("firstName");
		persistentFields.add("lastName");
		persistentFields.add("email");
		persistentFields.add("description");
		persistentFields.add("age");
		persistentFields.add("score");
		persistentFields.add("joinDate");
		persistentFields.add("active");
		persistentFields.add("status");
		persistentFields.add("secretCode");
		persistentFields.add("partner");
		persistentFields.add("skillSet");
		persistentFields.add("gender");
		persistentFields.add("nextFight");
		persistentFields.add("sellingPrice");
		persistentFields.add("avatar");
		persistentFields.add("attachment");
		persistentFields.add("images");
		persistentFields.add("comments");
		return persistentFields;
	}

	/**
	 * Test method for {@link org.opentides.util.CacheUtil#getSearchableFields(org.opentides.bean.BaseEntity)}.
	 */
	@Test
	public void testGetSearchableFields() {
		Ninja ninja = new Ninja();
		assertEquals(getNinjaExpectedPersistentFields(), CacheUtil.getSearchableFields(ninja));
		
		BaseUser baseUser = new BaseUser();
		List<String> expected = new ArrayList<String>();
		expected.add("firstName");
		expected.add("lastName");
		expected.add("emailAddress");
		assertEquals(expected, CacheUtil.getSearchableFields(baseUser));
	}

	/**
	 * Test method for {@link org.opentides.util.CacheUtil#getNewFormBindMethod(java.lang.Class)}.
	 */
	@Test
	@Ignore
	public void testGetNewFormBindMethod() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.opentides.util.CacheUtil#getUpdateFormBindMethod(java.lang.Class)}.
	 */
	@Test
	@Ignore
	public void testGetUpdateFormBindMethod() {
		fail("Not yet implemented");
	}

}
