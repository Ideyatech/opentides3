/*
 * Copyright 2007-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opentides.persistence.user;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opentides.bean.user.SessionUser;
import org.opentides.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * This class is responsible in retrieving the user information.
 * TODO: Rewrite this to use JPA EntityManager... if at all, possible.
 * @author allantan
 *
 */
public class AuthenticationDaoJdbcImpl extends JdbcDaoImpl implements ApplicationListener<ApplicationEvent> {
	
	@Autowired
	private UserService userService;
	
	/**
	 * Flag to determine if user locking will be enabled. By default set to true
	 */
	private boolean enableUserLockCheck = true;
	
	/**
	 * The lockout seconds for locked users
	 */
	private long lockoutSeconds = 60;
	
	/**
	 * The max attempts
	 */
	private long maxAttempts = 5;

	private static Log _log = LogFactory.getLog(AuthenticationDaoJdbcImpl.class);
	
	private static String loadUserByUsernameQuery = 
		"select U.USERID ID, FIRSTNAME, LASTNAME, EMAIL, P.LASTLOGIN LASTLOGIN, P.OFFICE OFFICE " +
		"from USER_PROFILE P inner join USERS U on P.ID=U.USERID where U.USERNAME=?";
	
	@Override 
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		try {
            UserDetails user = super.loadUserByUsername(username);
            SessionUser sessUser = null;
        	sessUser = new SessionUser(user);
            Map<String,Object> result = getJdbcTemplate().queryForMap(loadUserByUsernameQuery.replace("?", "'"+username+"'"));
            for (String key:result.keySet())
            	sessUser.addProfile(key, result.get(key));
            if(enableUserLockCheck) {
            	if(userService.isUserLockedOut(username, maxAttempts, lockoutSeconds)) {
            		user = new User(sessUser.getUsername(), sessUser.getPassword(), sessUser.isEnabled(), sessUser.isAccountNonExpired(),
            				sessUser.isCredentialsNonExpired(), false, sessUser.getAuthorities());
            		return user;
            	}
            }
            return sessUser;
		} catch (UsernameNotFoundException ex1) {
			_log.error(ex1);
		    throw ex1;
		} catch (DataAccessException ex2) {
			_log.error(ex2);
		    throw ex2;
		}
	}
	
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if(enableUserLockCheck) {
			if(event instanceof AuthenticationSuccessEvent) {
				userService.unlockUser(((AbstractAuthenticationEvent)event).getAuthentication().getName());
			} else if(event instanceof AbstractAuthenticationFailureEvent) {
				String username = ((AbstractAuthenticationEvent)event).getAuthentication().getName();
				String origin = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
	                    .getRemoteAddr();
	            String cause =  ((AbstractAuthenticationFailureEvent) event).getException().toString();
	            logger.info("Failed authentication for user '" + username + "' from ip " + origin + " caused by " + cause);
	            if (event instanceof AuthenticationFailureBadCredentialsEvent) {
	            	if(!userService.isUserLockedOut(username, maxAttempts, lockoutSeconds))
	            		userService.updateFailedLogin(username, event.getTimestamp());
	            }
			}
		}
	}

	/**
	 * Setter method for loadUserByUsernameQuery.
	 *
	 * @param loadUserByUsernameQuery the loadUserByUsernameQuery to set
	 */
	public void setLoadUserByUsernameQuery(String loadUserByUsernameQuery) {
		AuthenticationDaoJdbcImpl.loadUserByUsernameQuery = loadUserByUsernameQuery;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * @param enableUserLockCheck the enableUserLockCheck to set
	 */
	public void setEnableUserLockCheck(boolean enableUserLockCheck) {
		this.enableUserLockCheck = enableUserLockCheck;
	}

	/**
	 * @param lockoutSeconds the lockoutSeconds to set
	 */
	public void setLockoutSeconds(long lockoutSeconds) {
		this.lockoutSeconds = lockoutSeconds;
	}

	/**
	 * @param maxAttempts the maxAttempts to set
	 */
	public void setMaxAttempts(long maxAttempts) {
		this.maxAttempts = maxAttempts;
	}
	
}
