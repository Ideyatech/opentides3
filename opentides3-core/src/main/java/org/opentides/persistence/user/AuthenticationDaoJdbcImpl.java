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
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

/**
 * This class is responsible in retrieving the user information.
 * TODO: Rewrite this to use JPA EntityManager... if at all, possible.
 * @author allantan
 *
 */
public class AuthenticationDaoJdbcImpl extends JdbcDaoImpl {

	private static Log _log = LogFactory.getLog(AuthenticationDaoJdbcImpl.class);	
	private static String loadUserByUsernameQuery = 
		"select U.USERID ID, FIRSTNAME, LASTNAME, EMAIL, P.LASTLOGIN LASTLOGIN, P.OFFICE OFFICE " +
		"from USER_PROFILE P inner join USERS U on P.ID=U.USERID where U.USERNAME=?";
	
	@Override 
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		try {
            UserDetails user = super.loadUserByUsername(username);
            SessionUser sessUser = new SessionUser(user);
            Map<String,Object> result = getJdbcTemplate().queryForMap(loadUserByUsernameQuery.replace("?", "'"+username+"'"));
            for (String key:result.keySet())
            	sessUser.addProfile(key, result.get(key));
            return sessUser;
		} catch (UsernameNotFoundException ex1) {
			_log.error(ex1);
		    throw ex1;
		} catch (DataAccessException ex2) {
			_log.error(ex2);
		    throw ex2;
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
	
}
