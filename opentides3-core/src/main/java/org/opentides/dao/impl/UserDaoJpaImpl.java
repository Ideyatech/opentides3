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
package org.opentides.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opentides.bean.user.BaseUser;
import org.opentides.dao.UserDao;
import org.opentides.util.DateUtil;
import org.opentides.util.StringUtil;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public class UserDaoJpaImpl extends BaseEntityDaoJpaImpl<BaseUser, Long> implements
		UserDao {

	@SuppressWarnings("unused")
	private static Log _log = LogFactory.getLog(UserDaoJpaImpl.class);
	private static final String DEFAULT_TIME_ZONE = "Asia/Manila";

	public final boolean isRegisteredByEmail(String emailAddress) {
		if (StringUtil.isEmpty(emailAddress))
			return false;
		String queryString = getJpqlQuery("jpql.user.countByEmailAddress");
		Query queryObject = getEntityManager().createQuery(queryString);
		queryObject.setParameter("emailAddress", emailAddress);
		long count = (Long) queryObject.getSingleResult();
		return count != 0;
	}

	public final BaseUser loadByUsername(String username) {
		if (StringUtil.isEmpty(username))
			return null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		List<BaseUser> result = findByNamedQuery("jpql.user.findByUsername",
				params);
		if (result == null || result.size() == 0) {
			return null;
		} else {
			return result.get(0);
		}
	}

	public final BaseUser loadByEmailAddress(String emailAddress) {
		if (StringUtil.isEmpty(emailAddress))
			return null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("emailAddress", emailAddress);
		List<BaseUser> result = findByNamedQuery("jpql.user.findByEmailAddress",
				params);
		if (result == null || result.size() == 0) {
			return null;
		} else {
			return result.get(0);
		}
	}
	
	public List<BaseUser> findByUsergroupName(String userGroupName) {
		if (StringUtil.isEmpty(userGroupName))
			return null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", userGroupName);
		List<BaseUser> result = findByNamedQuery(
				"jpql.user.findByUsergroupName", params);
		if (result == null || result.size() == 0) {
			return null;
		} else {
			return result;
		}
	}

	public void updateLastLogin(String username) {
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone(DEFAULT_TIME_ZONE));
		Date currentDate = DateUtil.getClientCurrentDate(now, now.getTimeZone());
		Query update = getEntityManager().createNativeQuery(
				"update USER_PROFILE up, USERS u set LASTLOGIN='"
						+ DateUtil.dateToString(currentDate,
								"yyyy-MM-dd HH:mm:ss")
						+ "' where u.id=up.id and u.username='" + username
						+ "'");
		update.executeUpdate();
	}

	@Override
	public BaseUser loadByFacebookId(String facebookId) {
		if (StringUtil.isEmpty(facebookId))
			return null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("facebookId", facebookId);
		List<BaseUser> result = findByNamedQuery("jpql.user.findByFacebookId",
				params);
		if (result == null || result.size() == 0) {
			return null;
		} else {
			return result.get(0);
		}
	}

}
