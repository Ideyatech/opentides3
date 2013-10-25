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

import org.opentides.bean.user.BaseUser;
import org.opentides.bean.user.UserCredential;
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
	 * Encrypts the password if passwordEncoder is available.
	 * @param password
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
	  */
	 public List<SessionInformation> getAllLoggedUsers();
	 
	 /**
	  * Performs a force logout to specified username
	  */
	 public void forceLogout(String username);
	 
	 /**
	  * Check if the user with the specified username is locked out.
	  *  
	  * @param username username of the user
	  * @param maxAttempts maximum number of attempts
	  * @param lockOutTime lockout time in seconds
	  * 
	  * @return
	  */
	 public boolean isUserLockedOut(String username, long maxAttempts, long lockOutTime);
	 
	 /**
	  * Update the details of the user when login failed. <p>This should increment the failed
	  * login count and update the last failed login timestamp </p>
	  * 
	  * @param username username of the user
	  * @param timestamp the last failed login timestamp
	  */
	 public void updateFailedLogin(String username, long timestamp);
	 
	 /**
	  * Unlock a locked-out user
	  * @param username
	  */
	 public void unlockUser(String username);
	 
	 /**
	  * 
	  * @param name
	  * @return
	  */
	 public List<BaseUser> findUsersLikeLastName(String name, int maxResults);
	 
	 /**
	  * Find all users with authority
	  * @param authority
	  * @return
	  */
	 public List<BaseUser> findAllUsersWithAuthority(String authority);
	 
	 public BaseUser getCurrentUser();

	 public void registerUser(BaseUser baseUser, boolean sendEmail);
	 
	 public UserCredential generateFakeCredentials();
}
