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
package org.opentides.dao;

import java.util.Set;

import org.opentides.bean.user.UserAuthority;
import org.opentides.bean.user.UserGroup;


/**
 * UserGroup DAO, methods for accessing data related to user group such as
 * saving, editing, deleting and querying specific to usergroup
 * 
 * @author rjimenez
 * 
 */
public interface UserGroupDao extends BaseEntityDao<UserGroup, Long> {
	/**
	 * Add authoritys to a userGroup
	 * 
	 * @param userGroup
	 * @param authoritys
	 *            set of authoritys to add
	 */
	public void assignRoles(UserGroup userGroup, Set<UserAuthority> authoritys);

	/**
	 * Add a authority to a userGroup
	 * 
	 * @param userGroup
	 * @param authority
	 *            authority to add
	 */
	public void assignRoles(UserGroup userGroup, UserAuthority authority);
	
	/**
	 * get User Group by Name
	 * 
	 * @param name
	 */
	public UserGroup loadUserGroupByName(String name);
	
	/**
	 * Removes the user authority from the usergroup.
	 * @param authority
	 * @return
	 */
	public boolean removeUserAuthority(UserAuthority authority);
}
