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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.opentides.bean.user.UserAuthority;
import org.opentides.bean.user.UserGroup;
import org.opentides.dao.UserGroupDao;
import org.opentides.service.UserGroupService;
import org.springframework.stereotype.Service;

@Service("userGroupService")
public class UserGroupServiceImpl extends BaseCrudServiceImpl<UserGroup>
		implements UserGroupService {

	private static Logger _log = Logger.getLogger(UserGroupService.class);

	@Resource(name="authorities")
	private Map<String, String> authorities;
		
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
		this.authorities = sortedData;
	}
	
	/**
	 * @return the authorities
	 */
	public Map<String, String> getAuthorities() {
		return this.authorities;
	}

	public UserGroup loadUserGroupByName(String name){
		return ((UserGroupDao)dao).loadUserGroupByName(name);
	}
	
	/**
	 * Removes the UserAuthority from the database.
	 */
	public boolean removeUserAuthority(UserAuthority role) {
		return ((UserGroupDao)dao).removeUserAuthority(role);
	}

	@Override
	public boolean setupAdminGroup() {
		boolean exist = false;
		if (getDao().countAll() > 0) {
			exist = true;
		} else {
			UserGroup userGroup = new UserGroup();
			userGroup.setName("Administrator");
			userGroup.setDescription("System Administrators (Default)");
			userGroup.setIsDefault(Boolean.TRUE);
			List<String> names = new ArrayList<String>();
			for (String key:authorities.keySet()) {
				names.add(key);
			}
			userGroup.setAuthorityNames(names);
			getDao().saveEntityModel(userGroup);
			_log.info("New installation detected, inserted Administrator usergroup to database.");
		}		
		return !exist;
	}

	/* (non-Javadoc)
	 * @see org.opentides.service.UserGroupService#loadDefaultUserGroup()
	 */
	@Override
	public List<UserGroup> getOldDefaultUserGroups(Long ... groupIds) {
		return ((UserGroupDao)dao).getOldDefaultUserGroups(groupIds);
	}

	/* (non-Javadoc)
	 * @see org.opentides.service.UserGroupService#getDefaultUserGroup()
	 */
	@Override
	public UserGroup getDefaultUserGroup() {
		return ((UserGroupDao)dao).getDefaultUserGroup();
	}

	/* (non-Javadoc)
	 * @see org.opentides.service.UserGroupService#removeOldDefaultUserGroup()
	 */
	@Override
	public void removeOldDefaultUserGroup(Long groupId) {
		List<UserGroup> groups = ((UserGroupDao)dao).getOldDefaultUserGroups(groupId);
		for(UserGroup group : groups) {
			group.setIsDefault(Boolean.FALSE);
			save(group);
		}
	}

}