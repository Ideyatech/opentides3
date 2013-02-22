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
import java.util.List;
import java.util.Map;

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
import org.opentides.service.UserService;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailMessage;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value="userService")
public class UserServiceImpl extends BaseCrudServiceImpl<BaseUser> implements
		UserService {

	private static Logger _log = Logger.getLogger(UserServiceImpl.class);

	private static Map<String, String> authorities;

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

	private String confirmURL;

	private MailMessage resetPasswordMailMessage;

//	private MailingService mailingService;

	
	@Autowired
	public void setUserDao(UserDao userDao) {
		this.dao = userDao;
	}
	/**
	 * @return the authorities
	 */
	public Map<String, String> getAuthorities() {
		return authorities;
	}

	/**
	 * @param authorities
	 *            the authorities to set
	 */
	public void setAuthorities(Map<String, String> authorities) {
		UserServiceImpl.authorities = authorities;
	}

	public void requestPasswordReset(String emailAddress) {
		UserDao userDao = (UserDao) getDao();
		if (!userDao.isRegisteredByEmail(emailAddress)) {
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
	 * @param emailAddress
	 * @param token
	 * @param cipher
	 */
	private void sendEmailConfirmation(String emailAddress, String token,
			String cipher) {
//		Map<String, Object> dataMap = new HashMap<String, Object>();
//		dataMap.put("address", emailAddress);
//		dataMap.put("confirmationCode", token);
//		dataMap.put("confirmURL", confirmURL);
//		dataMap.put("link", confirmURL + "?cipher=" + cipher);
//		resetPasswordMailMessage.setMsgTo(emailAddress);
//		resetPasswordMailMessage
//				.setSubject("Information regarding your password reset");
//		resetPasswordMailMessage.setTemplate("password_reset.vm");
//		resetPasswordMailMessage.setDataMap(dataMap);
//		mailingService.sendMailImmediate(resetPasswordMailMessage);
	}

	/**
	 * Resets the password by specifying email address and token.
	 */
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
		Date updated = actual.getUpdateDate().getTime();
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

	/**
	 * Validates the cipher for password reset and returns the corresponding
	 * email address and token.
	 * 
	 * @param passwd
	 * @return
	 */
	public boolean confirmPasswordResetByCipher(PasswordReset passwd) {
		String decrypted = null;
		try {
			decrypted = StringUtil.decrypt(passwd.getCipher());
		} catch (Exception e) {
			// do nothing... next step will handle the error
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

	/**
	 * Resets the password
	 * 
	 * @param passwd
	 * @return
	 */
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
		UserDao userDao = (UserDao) getDao();
		BaseUser user = userDao.loadByEmailAddress(passwd.getEmailAddress());
		user.getCredential().setPassword(passwd.getPassword());
		userDao.saveEntityModel(user);

		return true;
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
			cred.setPassword("Opentides3");
			cred.setEnabled(true);
			cred.setUser(user);
			user.setCredential(cred);
			user.setEmailAddress("admin@opentides.com");
			user.setFirstName("Default");
			user.setLastName("Admin");
			
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
		BaseUser user = userDao.loadByUsername(username);
		WebAuthenticationDetails details = (WebAuthenticationDetails) authenticationSuccessEvent.getAuthentication().getDetails();
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
		AuditLogDaoImpl.logEvent(message, message, user);
		
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
			AuditLogDaoImpl.logEvent(message, message, user);
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

	/**
	 * @param tokenLength
	 *            the tokenLength to set
	 */
	public void setTokenLength(int tokenLength) {
		this.tokenLength = tokenLength;
	}

	/**
	 * @param confirmURL
	 *            the confirmURL to set
	 */
	public void setConfirmURL(String confirmURL) {
		this.confirmURL = confirmURL;
	}

	/**
	 * 
	 * @param mailingService
	 */
//	public void setMailingService(MailingService mailingService) {
//		this.mailingService = mailingService;
//	}

	/**
	 * 
	 * @param resetPasswordMailMessage
	 */
	public void setResetPasswordMailMessage(MailMessage resetPasswordMailMessage) {
		this.resetPasswordMailMessage = resetPasswordMailMessage;
	}
}
