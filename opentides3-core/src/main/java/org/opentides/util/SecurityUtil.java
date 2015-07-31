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

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.opentides.bean.user.SessionUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * This class is an Spring Security helper that retrieves the currently logged in user.
 * 
 * @author allantan
 */
public class SecurityUtil {
	
	private static final Logger _log = Logger.getLogger(SecurityUtil.class);
	
	/**
	 * Static helper to retrieve currently logged user.
	 * @return
	 */
	public static SessionUser getSessionUser() {
		try {
			final Object userObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (userObj instanceof SessionUser) {
				return ((SessionUser) userObj);
			}
		} catch (final NullPointerException npe) {
			_log.warn("No Security Context Found!");
		} catch (final Exception e) {
			_log.error(e.getMessage());
		}
		return null;
	}
	/**
	 * Non-static accessor for JSTL. 
	 * Calls getSessionUser.
	 * @return 
	 */
	public SessionUser getUser() {
		return SecurityUtil.getSessionUser();
	}
	/**
	 * Static helper to check if currently logged-in user has access
	 * to given permission.
	 * @param permission
	 * @return
	 */
	public static boolean currentUserHasPermission(final String permission) {
		final SessionUser user = SecurityUtil.getSessionUser();
		if (user!=null && user.getAuthorities()!=null) {
			final GrantedAuthority auth = new SimpleGrantedAuthority(permission);
			return user.getAuthorities().contains(auth);
		}
		return false;
	}
	
	/**
	 * Returns adjustment of hours for the user's timezone.
	 * 
	 * @return
	 */
	public static Integer userTimeZone() {
		final Object timeZone = SecurityUtil.getSessionUser().getProfile().get("TZ_DIFF");
		if (timeZone == null) {
			return 0;
		} else {
			return new Integer(timeZone.toString());
		}
	}
	
	/**
	 * Returns Now adjusted based on user's timezone
	 * 
	 * @return
	 */
	public static Date userNow() {
<<<<<<< HEAD
		try {
			Integer tzDiff = SecurityUtil.userTimeZone();
			return DateUtils.addHours(new Date(), tzDiff);
		} catch (Exception e) {
			return null;
		}
=======
		final Integer tzDiff = SecurityUtil.userTimeZone();
		return DateUtils.addHours(new Date(), tzDiff);
>>>>>>> branch 'master' of https://github.com/Ideyatech/opentides3.git
	}

	/**
	 * Non-static accessor for JSTL
	 * @return
	 */
	public Date getNow() {
		return SecurityUtil.userNow();
	}
}
