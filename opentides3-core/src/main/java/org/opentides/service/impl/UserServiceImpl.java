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
package org.opentides.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.opentides.bean.user.BaseUser;
import org.opentides.bean.user.SessionUser;
import org.opentides.bean.user.UserCredential;
import org.opentides.bean.user.UserGroup;
import org.opentides.dao.PasswordResetDao;
import org.opentides.dao.UserDao;
import org.opentides.dao.UserGroupDao;
import org.opentides.dao.impl.AuditLogDaoImpl;
import org.opentides.service.MailingService;
import org.opentides.service.UserGroupService;
import org.opentides.service.UserService;
import org.opentides.util.SecurityUtil;
import org.opentides.util.StringUtil;
import org.scribe.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.google.api.legacyprofile.LegacyGoogleProfile;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value="userService")
public class UserServiceImpl extends BaseCrudServiceImpl<BaseUser> implements
		UserService {

	private static Logger _log = Logger.getLogger(UserServiceImpl.class);
	
	@Autowired
	private MailingService mailingService;
	
	@Autowired
	private UserGroupService userGroupService;
	
	@Autowired
	private PasswordResetDao passwordResetDao;

	@Autowired
	private UserGroupDao userGroupDao;

	@Autowired
	private SessionRegistry sessionRegistry;
	
	@Autowired
	@Qualifier("passwordEncoder")
	private PasswordEncoder passwordEncoder;

	@Autowired
	public void setUserDao(UserDao userDao) {
		this.dao = userDao;
	}
	
	/** Static helper that encrypts the given password
	 *  into its appropriate encryption settings.
	 * @param cleartext
	 * @return
	 */
	public String encryptPassword(String cleartext) {
	    if (passwordEncoder!=null) {
	        return passwordEncoder.encodePassword(cleartext, null);
	    } else
	        return cleartext;
	}
	
	/**
	 * Ensures that admin user is created into the database. This method is
	 * called by ApplicationStartupListener to ensure admin user is available
	 */
	@Transactional
	public boolean setupAdminUser() {
		boolean exist = false;
		// let's check if there are users in the database
		UserDao userDao = (UserDao) getDao();
		if (userDao.countAll() > 0) {
			exist = true;
		} else {
			// if none, let's create admin user
			BaseUser user = new BaseUser();
			UserCredential cred = new UserCredential();
			cred.setUsername("admin");
			cred.setPassword(encryptPassword("ideyatech"));
			cred.setEnabled(true);
			cred.setUser(user);
			user.setCredential(cred);
			user.setEmailAddress("admin@opentides.com");
			user.setFirstName("Administrator");
			
			UserGroup userGroup = userGroupDao.loadUserGroupByName("Administrator");
			user.addGroup(userGroup);
			userDao.saveEntityModel(user);
			
			_log.info("New installation detected, inserted admin/ideyatech user to database.");
		}
		return !exist;
	}

	/**
	 * Updates last login of the user from a login event. Also logs the event in
	 * history log.
	 */
	@Transactional
	public void updateLogin(
			AuthenticationSuccessEvent authenticationSuccessEvent) {
		UserDao userDao = (UserDao) getDao();
		String username = authenticationSuccessEvent.getAuthentication().getName();
		
		if(username != null && !username.isEmpty()) {
			BaseUser user = userDao.loadByUsername(username);
			WebAuthenticationDetails details = (WebAuthenticationDetails) authenticationSuccessEvent.getAuthentication().getDetails();
			if(details != null) {
				String address = details.getRemoteAddress();
				if (user.getTotalLoginCount()== null)
					user.setTotalLoginCount(1l);
				else
					user.setTotalLoginCount(user.getTotalLoginCount()+1);
				user.setPrevLoginIP(user.getLastLoginIP());
				user.setLastLoginIP(address);
				user.setLastLogin(new Date());
				user.setSkipAudit(true);		
				userDao.saveEntityModel(user);
				
				// force the audit user details
				String completeName = user.getCompleteName() + " [" + username + "] ";
				user.setAuditUserId(user.getId());
				user.setAuditUsername(username);
				user.setSkipAudit(false);
				String message = completeName + " has logged-in";
				AuditLogDaoImpl.logEvent(message, user);
			}
		}
	}

	/**
	 * Records the logout event and save to history log for audit tracking
	 * purposes.
	 */
	@Override
	public void updateLogout(Authentication auth) {
		if (auth==null) return;
		Object userObj = auth.getPrincipal();
		if (userObj instanceof SessionUser) {
			SessionUser sessionUser = (SessionUser) userObj;
			String username = sessionUser.getUsername();
			String completeName = sessionUser.getCompleteName() + " ["
					+ username + "] ";
			UserDao userDao = (UserDao) getDao();
			// also add log to audit history log
			BaseUser user = userDao.loadByUsername(username);
			// force the audit user details
			user.setAuditUserId(user.getId());
			user.setAuditUsername(username);
			String message = completeName + " has logged-out.";
			AuditLogDaoImpl.logEvent(message, user);
		}
	}

	/**
	 * Returns the list of user session that is logged-in to the system.
	 * 
	 * @return
	 */
	public List<SessionInformation> getAllLoggedUsers() {
		List<SessionInformation> results = new ArrayList<SessionInformation>();
		for (Object prince : sessionRegistry.getAllPrincipals()) {
			for (SessionInformation si : sessionRegistry.getAllSessions(prince,
					false)) {
				results.add(si);
			}
		}
		return results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ideyatech.core.service.UserService#forceLogout(java.lang.String)
	 */
	public void forceLogout(String username) {
		// let's logout all sessions of this user
		for (Object prince : sessionRegistry.getAllPrincipals()) {
			if (prince instanceof SessionUser) {
				SessionUser user = (SessionUser) prince;
				if (user.getUsername().equals(username)) {
					for (SessionInformation si : sessionRegistry
							.getAllSessions(prince, false)) {
						si.expireNow();
					}
				}
			}
		}
	}

	public BaseUser getCurrentUser() {
		try {
			SessionUser sessionUser = SecurityUtil.getSessionUser();

			UserDao userDao = (UserDao) getDao();
			if(sessionUser != null)
				return userDao.loadByUsername(sessionUser.getUsername());
			else
				return null;
		} catch (Exception e) {
			return null;
		}
	}

	@Transactional
	@Override
	public void registerUser(BaseUser baseUser, boolean sendEmail) {
		
		//TODO enable user (should be disabled by default)
		baseUser.getCredential().setEnabled(true);
		
		//encrypt password
		UserCredential credential = baseUser.getCredential();
		if (!StringUtil.isEmpty(credential.getNewPassword()))
			credential.setPassword( encryptPassword(credential.getNewPassword()) );
		baseUser.setCredential(credential);

		save(baseUser);
		
		if(sendEmail) {
			//send verification email
			Map<String, Object> templateVariables = new HashMap<String, Object>();
			templateVariables.put("name", baseUser.getCompleteName());
			templateVariables.put("activationLink", "http://www.google.com");
			mailingService.sendEmail(new String[] { baseUser.getEmailAddress() },
					"Verify your Email Address", "email-verification.vm",
					templateVariables);
		}
	}

	@Override
	public BaseUser getUserByFacebookId(String facebookId) {
		UserDao userDao = (UserDao) getDao();
		return userDao.loadByFacebookId(facebookId);
	}

	@Override
	public BaseUser getUserByFacebookAccessToken(String facebookAccessToken) {
		FacebookTemplate facebookTemplate = new FacebookTemplate(facebookAccessToken);
		String facebookId = facebookTemplate.userOperations().getUserProfile().getId();
		UserDao userDao = (UserDao) getDao();
		return userDao.loadByFacebookId(facebookId);
	}

	@Override
	public BaseUser getUserByGoogleId(String googleId) {
		UserDao userDao = (UserDao) getDao();
		return userDao.loadByGoogleId(googleId);
	}

	@Override
	public BaseUser getUserByGoogleAccessToken(String googleAccessToken) {
		Google googleTemplate = new GoogleTemplate(googleAccessToken);
		String googleUserId = googleTemplate.userOperations().getUserProfile().getId();
		UserDao userDao = (UserDao) getDao();
		return userDao.loadByGoogleId(googleUserId);
	}

	@Override
	public BaseUser getUserByTwitterId(String twitterId) {
		UserDao userDao = (UserDao) getDao();
		return userDao.loadByTwitterId(twitterId);
	}

	@Override
	public BaseUser getUserByTwitterAccessToken(String appId, String clientSecret, Token token) {
		TwitterTemplate twitterTemplate = new TwitterTemplate(appId, clientSecret, token.getToken(), token.getSecret());
		long twitterUserId = twitterTemplate.userOperations().getUserProfile().getId();
		UserDao userDao = (UserDao) getDao();
		return userDao.loadByTwitterId(String.valueOf(twitterUserId));
	}
	
	@Override
	@Transactional
	public void registerFacebookAccount(BaseUser user,
			String facebookAccessToken) {
		
		FacebookTemplate facebookTemplate = new FacebookTemplate(facebookAccessToken);
		FacebookProfile profile = facebookTemplate.userOperations().getUserProfile();
		
		user.setFirstName(profile.getFirstName());
		user.setLastName(profile.getLastName());
		user.setMiddleName(profile.getMiddleName());
		user.setEmailAddress(profile.getEmail());
		
		user.setFacebookId(profile.getId());
		user.setFacebookAccessToken(facebookAccessToken);

		if(user.getId() == null) {
			user.setCredential(generateFakeCredentials());
			registerUser(user, false);
		} else
			save(user);
		
	}

	@Override
	@Transactional
	public void registerGoogleAccount(BaseUser user,
			String googleAccessToken) {
		
		Google googleTemplate = new GoogleTemplate(googleAccessToken);
		LegacyGoogleProfile profile = googleTemplate.userOperations().getUserProfile();
		
		user.setFirstName(profile.getFirstName());
		user.setLastName(profile.getLastName());
		user.setEmailAddress(profile.getEmail());
		
		user.setGoogleId(profile.getId());
		user.setGoogleAccessToken(googleAccessToken);
		
		if(user.getId() == null) {
			user.setCredential(generateFakeCredentials());
			registerUser(user, false);
		} else
			save(user);
		
	}

	@Override
	@Transactional
	public void registerTwitterAccount(BaseUser user, String appId, String clientSecret, Token token) {
		TwitterTemplate twitterTemplate = new TwitterTemplate(appId, clientSecret, token.getToken(), token.getSecret());
		TwitterProfile profile = twitterTemplate.userOperations().getUserProfile();
		
		user.setFirstName(profile.getName());
		
		user.setTwitterId(String.valueOf(profile.getId()));
		user.setTwitterSecret(token.getSecret());
		user.setGoogleAccessToken(token.getToken());
		
		if(user.getId() == null) {
			user.setCredential(generateFakeCredentials());
			registerUser(user, false);
		} else
			save(user);
		
	}
	
	public UserCredential generateFakeCredentials(){

		UserDao userDao = (UserDao) getDao();
		UserCredential credential = new UserCredential();
		
		String username;
		
		do {
			username = RandomStringUtils.randomAlphanumeric(10);
		} while (userDao.loadByUsername(username) != null);
		
		credential.setUsername(username);
		credential.setPassword(encryptPassword(RandomStringUtils.randomAlphanumeric(10)));
		
		
		return credential;
	}
}
