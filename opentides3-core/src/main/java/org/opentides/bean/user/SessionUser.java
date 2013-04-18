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

package org.opentides.bean.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opentides.util.StringUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * This class is used by ACEGI to represent the currently logged
 * user for the session. To retrieve the SessionUser object
 * use SecurityUtil.getSessionUser();
 * 
 * @author allantan
 *
 */
public class SessionUser extends User {

	private static final long serialVersionUID = 8493532913557193485L;
	
	private final Map<String, Object> profile = new HashMap<String, Object>();

	private Long id;
	
	private String office;

	public SessionUser(UserDetails user) {
		super(user.getUsername(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(), 
				user.isCredentialsNonExpired(), user.isAccountNonLocked(), user.getAuthorities());
	}
	
	public SessionUser(BaseUser user, List<GrantedAuthority> authorities) {
		super(user.getCredential().getUsername(), user.getCredential().getPassword(), user.getCredential().getEnabled(), true, true, true, authorities);
	}

	/**
	 * Returns the complete name by concatenating
	 * lastName and firstName
	 * 
	 * @return
	 */
	public String getCompleteName() {
		String name = "";
		String lastName = "" + profile.get("lastName");
		String firstName = "" + profile.get("firstName");
		if (!StringUtil.isEmpty(lastName))
			name += lastName + ", ";
		name += firstName;
		return name;		
	}

	/**
	 * Checks if user has permission to the specified 
	 * permission string
	 * 
	 * @param permission
	 * @return
	 */
	public boolean hasPermission(String permission) {
	    for (GrantedAuthority auth: this.getAuthorities()) {
	        if (permission.equals(auth.getAuthority()))
	            return true;
	    }
		return false;
	}

	/**
	 * @return the profile
	 */
	public final Map<String, Object> getProfile() {
		return profile;
	}
	
	/**
	 * Adds profile settings to the session user.
	 */
	public void addProfile(String key, Object value) {
		profile.put(key, value);		
	}

	/**
	 * @return the id
	 */
	public final Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public final void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the office
	 */
	public final String getOffice() {
		return office;
	}

	/**
	 * @param office the office to set
	 */
	public final void setOffice(String office) {
		this.office = office;
	}
	
}
