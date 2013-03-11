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

package org.opentides.service;

import java.util.List;
import java.util.Map;

import org.opentides.bean.user.BaseUser;
import org.opentides.bean.user.PasswordReset;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;

/**
 * Service for user management operations.
 * 
 * @author allanctan
 */
public interface UserService extends BaseCrudService<BaseUser> {
	/**
	 * Returns the list of authorities
	 * @return
	 */
	public Map<String,String> getAuthorities();
	
	/**
	 * Inject roles allowed for the application here.
	 * @param roles
	 */
	public void setAuthorities(Map<String,String>  authorities);
	
	/**
	 * Requests for password reset 
	 * @param emailAddress
	 */
	public void requestPasswordReset(String emailAddress);
	
	/**
	 * Confirms the password by validating the email address and token
	 * @param emailAddress
	 * @param token
	 * @return
	 */
	public boolean confirmPasswordReset(String emailAddress, String token);
	
	/**
	 * Confirms the password by validating the cipher
	 * @param passwd
	 * @return
	 */
	public boolean confirmPasswordResetByCipher(PasswordReset passwd);
	
	/**
	 * Resets the password
	 * @param passwd
	 * @return
	 */
	public boolean resetPassword(PasswordReset passwd);
	
	/**
	 * Encrypts the password if passwordEncoder is available.
	 * @param password
	 * @return
	 */
	public String encryptPassword(String password);

	/**
	 * Ensures that admin user is created into the database.
	 * This method is called by ApplicationStartupListener
	 * to ensure admin user is available
	 */
	 public boolean setupAdminUser();
	 
	 /**
	  * Updates last login of the user from a login event
	  */
	 public void updateLogin(AuthenticationSuccessEvent authenticationSuccessEvent);
	 
	 /**
	  * Logs the event of logout
	  */
	 public void updateLogout(Authentication auth);
	 
	 /**
	  * Returns the list of user session that is logged-in to the system.
	  * @return
	  */
	 public List<SessionInformation> getAllLoggedUsers();
	 
	 /**
	  * Performs a force logout to specified username
	  * @return
	  */
	 public void forceLogout(String username);
	 
	 public BaseUser getCurrentUser();
}