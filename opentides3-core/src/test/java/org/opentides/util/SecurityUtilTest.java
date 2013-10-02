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

import org.junit.Before;
import org.junit.Test;
import org.opentides.bean.user.SessionUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Unit test for {@link SecurityUtil}
 * @author gino
 *
 */
public class SecurityUtilTest {
	
	@Before
	public void init() {
		SecurityContextHolder.clearContext();
	}

	@Test
	public void testGetSessionUser() {
		List<GrantedAuthority> auths = new ArrayList<>();
		auths.add(new SimpleGrantedAuthority("ROLE1"));
		auths.add(new SimpleGrantedAuthority("ROLE2"));
		
		UserDetails userDetails = new User("admin", "password", auths);
		SessionUser sessionUser = new SessionUser(userDetails);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				sessionUser, null, auths);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		SessionUser actual = SecurityUtil.getSessionUser();
		assertNotNull(actual);
		assertEquals("admin", actual.getUsername());
	}
	
	@Test
	public void testGetSessionUserNullContext() {
		SecurityContextHolder.clearContext();
		assertNull(SecurityUtil.getSessionUser());
	}
	
	@Test
	public void testGetSessionUserNotInstanceSessionUser() {
		List<GrantedAuthority> auths = new ArrayList<>();
		auths.add(new SimpleGrantedAuthority("ROLE1"));
		auths.add(new SimpleGrantedAuthority("ROLE2"));
		
		UserDetails userDetails = new User("admin", "password", auths);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				userDetails, null, auths);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		SessionUser actual = SecurityUtil.getSessionUser();
		assertNull(actual);
	}

	@Test
	public void testGetUser() {
		List<GrantedAuthority> auths = new ArrayList<>();
		auths.add(new SimpleGrantedAuthority("ROLE1"));
		auths.add(new SimpleGrantedAuthority("ROLE2"));
		
		UserDetails userDetails = new User("admin", "password", auths);
		SessionUser sessionUser = new SessionUser(userDetails);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				sessionUser, null, auths);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		SecurityUtil securityUtil = new SecurityUtil();
		SessionUser actual = securityUtil.getUser();
		assertNotNull(actual);
		assertEquals("admin", actual.getUsername());
	}

	@Test
	public void testCurrentUserHasPermission() {
		List<GrantedAuthority> auths = new ArrayList<>();
		auths.add(new SimpleGrantedAuthority("ROLE1"));
		auths.add(new SimpleGrantedAuthority("ROLE2"));
		
		UserDetails userDetails = new User("admin", "password", auths);
		SessionUser sessionUser = new SessionUser(userDetails);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				sessionUser, null, auths);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		assertTrue(SecurityUtil.currentUserHasPermission("ROLE1"));
		assertFalse(SecurityUtil.currentUserHasPermission("ROLE3"));
	}

}
