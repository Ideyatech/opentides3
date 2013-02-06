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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.opentides.bean.user.UserAuthority;
import org.opentides.bean.user.UserGroup;
import org.opentides.dao.UserGroupDao;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author rjimenez
 * @author allanctan
 * 
 */
@Repository("userGroupDao")
public class UserGroupDaoJpaImpl extends BaseEntityDaoJpaImpl<UserGroup, Long>
		implements UserGroupDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ideyatech.ecmt.dao.UserGroupDAO#assignRoles(com.ideyatech.ecmt.bean.admin.UserGroup,
	 *      java.util.Set)
	 */
	public void assignRoles(UserGroup userGroup, Set<UserAuthority> roles)
			throws NullPointerException {
		if (userGroup == null) {
			throw new NullPointerException();
		}
		Set<UserAuthority> oldRoles = userGroup.getAuthorities();
		if (oldRoles == null) {
			oldRoles = new HashSet<UserAuthority>();
		}
		oldRoles.addAll(roles);
		updateEntityModel(userGroup);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ideyatech.ecmt.dao.UserGroupDAO#assignRoles(com.ideyatech.ecmt.bean.admin.UserGroup,
	 *      com.ideyatech.core.bean.user.UserRole)
	 */
	public void assignRoles(UserGroup userGroup, UserAuthority role)
			throws NullPointerException {
		if (userGroup == null || role == null) {
			throw new NullPointerException();
		}
		userGroup.addAuthority(role);
		updateEntityModel(userGroup);
	}
	
	public UserGroup loadUserGroupByName(String name){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		List<UserGroup> list = findByNamedQuery("jpql.usergroup.findByName", map);
		if (list == null || list.size() == 0)
			return null;
		return list.get(0);
	}
	
	/* (non-Javadoc)
	 * @see org.opentides.persistence.UserGroupDAO#removeUserRole(org.opentides.bean.user.UserRole)
	 */
	@Override
	public boolean removeUserAuthority(UserAuthority role) {
		role.setUserId();
		getEntityManager().remove(role);
		getEntityManager().flush();
		return true;
	}

	@Override
	protected String appendOrderToExample(UserGroup example) {
		return " ORDER by obj.name";
	}
}
