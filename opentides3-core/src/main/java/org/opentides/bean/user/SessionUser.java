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

import java.util.Collection;
import java.util.Date;

import org.opentides.util.StringUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

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
	
	private Long   id;
	private String firstName;
	private String lastName;
	private String position;
	private String office;
	private String company;
	private String emailAddress;
	private String pictureUrl;
	private Date   lastLogin;

	public SessionUser(String username, String password, boolean isEnabled,
			Collection<GrantedAuthority> authorities) {
		super(username, password, isEnabled, true, true, true, authorities);
	}

	/**
	 * Returns the complete name by concatenating
	 * lastName and firstName
	 * 
	 * @return
	 */
	public String getCompleteName() {
		String name = "";
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
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}
	
	/**
	 * @return the pictureUrl
	 */
	public String getPictureUrl() {
		return pictureUrl;
	}

	/**
	 * @param pictureUrl the pictureUrl to set
	 */
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * Returns the real id of user. 
	 * Override this method in case of simulating/mimicking user access rights.
	 * @return
	 */
	public Long getRealId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the lastLogin
	 */
	public Date getLastLogin() {
		return lastLogin;
	}

	/**
	 * @param lastLogin the lastLogin to set
	 */
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	/**
	 * @return the office
	 */
	public String getOffice() {
		return office;
	}

	/**
	 * @param office the office to set
	 */
	public void setOffice(String office) {
		this.office = office;
	}
	
}
