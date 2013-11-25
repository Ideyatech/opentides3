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
import java.util.Map;

import org.opentides.bean.user.UserAuthority;
import org.opentides.bean.user.UserGroup;


/**
 * Service responsible for handling UserGroup and UserRole.
 * 
 * @author allantan
 */
public interface UserGroupService extends BaseCrudService<UserGroup> {
		
	/**
	 * Returns the list of authorities registered in the application.
	 * The value returned by this method is injected by spring via rolesMap.
	 * @return
	 */
	public Map<String, String> getAuthorities();

	/**
	 * Method to used for injection of rolesMap via spring.
	 * @param authorities
	 */
	public void setAuthorities(Map<String, String> authorities);
	
	/**
	 * Returns the usergroup from the given name.
	 * @param name
	 * @return
	 */
	public UserGroup loadUserGroupByName(String name); 
	
	/**
	 * Removes the assignment of authority to a user.
	 * @param authorities
	 * @return
	 */
	public boolean removeUserAuthority(UserAuthority authorities);
	
	/**
	 * Creates the initial usergroup for system administration;
	 */
	public boolean setupAdminGroup();
	
	/**
	 * Get Default User Group 
	 * @param groupIds
	 * @return
	 */
	public List<UserGroup> getOldDefaultUserGroups(Long ... groupIds);
	
	/**
	 * Get Default User group
	 * @return
	 */
	public UserGroup getDefaultUserGroup();
	
	/**
	 * Remove old default User group.
	 * @param groupId
	 */
	public void removeOldDefaultUserGroup(Long groupId);
	
}
