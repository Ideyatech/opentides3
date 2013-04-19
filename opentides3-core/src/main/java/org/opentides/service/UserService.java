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
import org.scribe.model.Token;
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
	 
	 public BaseUser getCurrentUser();

	 public void registerUser(BaseUser baseUser, boolean sendEmail);
	 public void registerFacebookAccount(BaseUser baseUser, String facebookAccessToken);
	 public void registerGoogleAccount(BaseUser baseUser, String googleAccessToken);
	 public void registerTwitterAccount(BaseUser user, String appId, String clientSecret, Token accessToken);
	 
	 public BaseUser getUserByFacebookId(String facebookId);
	 public BaseUser getUserByFacebookAccessToken(String facebookAccessToken);
	 public BaseUser getUserByGoogleId(String googleId);
	 public BaseUser getUserByGoogleAccessToken(String googleAccessToken);
	 public BaseUser getUserByTwitterId(String twitterId);
	 public BaseUser getUserByTwitterAccessToken(String appId, String clientSecret, Token token);

}