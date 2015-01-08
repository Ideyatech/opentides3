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
import org.opentides.bean.user.PasswordReset;
import org.opentides.bean.user.SessionUser;
import org.opentides.bean.user.UserCredential;
import org.opentides.bean.user.UserGroup;
import org.opentides.dao.PasswordResetDao;
import org.opentides.dao.UserDao;
import org.opentides.dao.UserGroupDao;
import org.opentides.dao.impl.AuditLogDaoImpl;
import org.opentides.exception.InvalidImplementationException;
import org.opentides.service.MailingService;
import org.opentides.service.UserGroupService;
import org.opentides.service.UserService;
import org.opentides.util.SecurityUtil;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

@Service(value="userService")
public class UserServiceImpl extends BaseCrudServiceImpl<BaseUser> implements
		UserService {

	private static final Logger _log = Logger.getLogger(UserServiceImpl.class);
	
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

	private int tokenLength = 10;

	@Value("#{applicationSettings['confirm.password.reset.url']}")
	private String confirmURL;

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
	        return passwordEncoder.encode(cleartext);
	    } else
	        return cleartext;
	}
	
	/**
	 * Ensures that admin user is created into the database. This method is
	 * called by ApplicationStartupListener to ensure admin user is available
	 */
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
			cred.setPassword(encryptPassword("opentides"));
			cred.setEnabled(true);
			cred.setUser(user);
			user.setCredential(cred);
			user.setEmailAddress("admin@opentides.com");
			user.setFirstName("Administrator");
			user.setLastName("Administrator");
			
			UserGroup userGroup = userGroupDao.loadUserGroupByName("Administrator");
			user.addGroup(userGroup);
			userDao.saveEntityModel(user);
			
			_log.info("New installation detected, inserted admin/opentides user to database.");
		}
		return !exist;
	}

	/**
	 * Updates last login of the user from a login event. Also logs the event in
	 * history log.
	 */
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
				String message = completeName + " has logged-in. IP Address: " + user.getLastLoginIP();
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
			UserDao userDao = (UserDao) getDao();
			// also add log to audit history log
			BaseUser user = userDao.loadByUsername(username);
			String completeName = user.getCompleteName() + " ["+ username + "] ";
			// force the audit user details
			user.setAuditUserId(user.getId());
			user.setAuditUsername(username);
			String message = completeName + " has logged-out. IP Address: " + user.getLastLoginIP();
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
	
	@Override
	public boolean isUserLockedOut(String username, long maxAttempts, long lockOutTime) {
		UserDao userDao = (UserDao) getDao();
		BaseUser user = userDao.loadByUsername(username);
		if(user != null) {
			if(user.getFailedLoginCount() != null && user.getFailedLoginCount() >= maxAttempts) {
				long elapsedTime = System.currentTimeMillis() - 
						(user.getLastFailedLoginMillis() == null ? 0 : user.getLastFailedLoginMillis());
				if(elapsedTime < 1000 * lockOutTime) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public void unlockUser(String username) {
		UserDao userDao = (UserDao) getDao();
		BaseUser user = userDao.loadByUsername(username);
		if (user!=null) {
			user.resetFailedLoginCount();
			userDao.saveEntityModel(user);			
		}
	}
	
	@Override
	public void updateFailedLogin(String username, long timestamp) {
		UserDao userDao = (UserDao) getDao();
		BaseUser user = userDao.loadByUsername(username);
		if(user != null) {
			user.incrementFailedLoginCount();
			user.setLastFailedLoginMillis(timestamp);
			userDao.saveEntityModel(user);
		}
	}
	
	@Override
	public List<BaseUser> findUsersLikeLastName(String name, int maxResults) {
		return getUserDao().findUsersLikeLastName(name, -1, maxResults);
	}
	
	@Override
	public BaseUser loadByUsername(String username) {
		return getUserDao().loadByUsername(username);
	}

	@Override
	public BaseUser loadByEmailAddress(String emailAddress) {
		return getUserDao().loadByEmailAddress(emailAddress);
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
	public List<BaseUser> findAllUsersWithAuthority(String authority) {
		return getUserDao().findAllUsersWithAuthority(authority);
	}
	
	private UserDao getUserDao() {
		return (UserDao)this.dao;
	}

	@Override
	public void requestPasswordReset(String emailAddress) {
		UserDao userDAO = (UserDao) getDao();
		if (!userDAO.isRegisteredByEmail(emailAddress)) {
			throw new InvalidImplementationException(
					"Email ["
							+ emailAddress
							+ "] was not validated prior to calling this service. Please validate first.");
		}
		PasswordReset passwd = new PasswordReset();
		String token = StringUtil.generateRandomString(tokenLength);
		String cipher = StringUtil.encrypt(token + emailAddress);
		passwd.setEmailAddress(emailAddress);
		passwd.setToken(token);
		passwd.setStatus("active");
		passwd.setCipher(cipher);
		passwordResetDao.saveEntityModel(passwd);
		// send email for confirmation
		sendEmailConfirmation(emailAddress, token, cipher);
	}

	/**
	 * Private helper to send email.
	 * 
	 * @param emailAddress
	 * @param token
	 * @param cipher
	 */
	private void sendEmailConfirmation(String emailAddress, String token,
			String cipher) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("address", emailAddress);
		dataMap.put("confirmationCode", token);
		dataMap.put("confirmURL", confirmURL);
		dataMap.put("link", confirmURL + "?cipher=" + cipher);
		mailingService.sendEmail(new String[] {emailAddress}, "Information regarding your password reset", "password_reset.vm", dataMap);
	}

	@Override
	public boolean confirmPasswordReset(String emailAddress, String token) {
		// check if email and token matched
		PasswordReset example = new PasswordReset();
		example.setEmailAddress(emailAddress);
		example.setToken(token);
		example.setStatus("active");
		List<PasswordReset> actuals = passwordResetDao.findByExample(example,
				true);
		if (actuals == null || actuals.size() == 0) {
			_log.info("Failed to confirm password reset. No records matched in password reset database for email "
					+ emailAddress);
			return false;
		}
		// check if password reset is active and not expired
		PasswordReset actual = actuals.get(0);
		Date updated = actual.getUpdateDate();
		Date expireDate = new Date(updated.getTime() + 86400000);
		Date today = new Date();
		if (expireDate.getTime() < today.getTime()) {
			// expired
			_log.info("Password reset has expired for " + emailAddress);
			actual.setStatus(PasswordReset.STATUS_EXPIRED);
			passwordResetDao.saveEntityModel(actual);
			return false;
		}
		return true;
	}

	@Override
	public boolean confirmPasswordResetByCipher(PasswordReset passwd) {
		String decrypted = "";
		try {
			decrypted = StringUtil.decrypt(passwd.getCipher());
		} catch (Exception e) {
			_log.error("Failed to decrypt password.",e);
		}
		if (StringUtil.isEmpty(decrypted)) {
			_log.info("Failed attempt to confirm password reset due to wrong cipher key.["
					+ passwd.getCipher() + "]");
			return false;
		}
		String token = decrypted.substring(0, tokenLength);
		String email = decrypted.substring(tokenLength);
		passwd.setToken(token);
		passwd.setEmailAddress(email);
		return confirmPasswordReset(email, token);
	}

	@Override
	public boolean resetPassword(PasswordReset passwd) {
		// check if password reset is active and not expired
		PasswordReset example = new PasswordReset();
		example.setEmailAddress(passwd.getEmailAddress());
		example.setToken(passwd.getToken());
		example.setStatus("active");
		List<PasswordReset> actuals = passwordResetDao.findByExample(example,
				true);
		if (actuals == null || actuals.size() == 0) {
			_log.info("Failed to reset password. No records found in password reset for email "
					+ passwd.getEmailAddress());
			return false;
		}
		PasswordReset actual = actuals.get(0);
		actual.setStatus(PasswordReset.STATUS_USED);
		passwordResetDao.saveEntityModel(actual);

		// now reset the password
		UserDao userDAO = (UserDao) getDao();
		BaseUser user = userDAO.loadByEmailAddress(passwd.getEmailAddress());
		user.getCredential().setPassword(encryptPassword(passwd.getPassword()));
		userDAO.saveEntityModel(user);

		return true;
	}
	
	@Override
	public List<SessionInformation> getAllLoggedUsersPagenation(int start, int total) {
        List<SessionInformation> results = new ArrayList<SessionInformation>();
       
        for(Object prince: sessionRegistry.getAllPrincipals()) {
            for (SessionInformation si : sessionRegistry.getAllSessions(prince,
                    false)) {
                results.add(si);
            }
        }
        if(results.size()>total) {
            return results.subList(start, total);
        }
        return results;
    }
	
}
