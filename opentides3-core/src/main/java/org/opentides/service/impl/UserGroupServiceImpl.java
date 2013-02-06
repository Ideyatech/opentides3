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

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.opentides.bean.user.UserAuthority;
import org.opentides.bean.user.UserGroup;
import org.opentides.dao.UserGroupDao;
import org.opentides.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


public class UserGroupServiceImpl extends BaseCrudServiceImpl<UserGroup>
		implements UserGroupService {

	private static Map<String, String> authorities;
	
	private UserGroupDao userGroupDao;
	
	/** inner class to do sorting of the map **/
	private static class ValueComparer implements Comparator<String> {
		private Map<String, String>  _data = null;
		public ValueComparer (Map<String, String> data){
			super();
			_data = data;
		}

         public int compare(String o1, String o2) {
        	 String e1 = _data.get(o1);
             String e2 = _data.get(o2);
             return e1.compareTo(e2);
         }
	}
	
	/**
	 * @param authorities
	 *            the authorities to set
	 */
	public void setAuthorities(Map<String, String> authorities) {
		// added process of sorting the authoritiesMap by the values in ascending order
		SortedMap<String, String> sortedData = 
			new TreeMap<String, String>(new UserGroupServiceImpl.ValueComparer(authorities));		
		sortedData.putAll(authorities);
		UserGroupServiceImpl.authorities = sortedData;
	}
	
	/**
	 * @return the authorities
	 */
	public Map<String, String> getAuthorities() {
		return UserGroupServiceImpl.authorities;
	}

	public UserGroup loadUserGroupByName(String name){
		return userGroupDao.loadUserGroupByName(name);
	}
	
	/**
	 * Removes the UserAuthority from the database.
	 */
	@Transactional
	public boolean removeUserAuthority(UserAuthority role) {
		return userGroupDao.removeUserAuthority(role);
	}

	/**
	 * @param userGroupDao the userGroupDao to set
	 */
	@Autowired
	public void setUserGroupDao(UserGroupDao userGroupDao) {
		this.userGroupDao = userGroupDao;
	}	
}
