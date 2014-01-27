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

import org.opentides.bean.Widget;
import org.opentides.bean.user.BaseUser;
import org.opentides.bean.user.UserAuthority;
import org.opentides.bean.user.UserGroup;
import org.opentides.dao.WidgetDao;
import org.springframework.stereotype.Repository;

/**
 * DAO implementation for widget
 * @author opentides 1.0
 *
 */
@Repository("widgetDao")
public class WidgetDaoJpaImpl extends BaseEntityDaoJpaImpl<Widget, Long> implements
		WidgetDao {

	@Override
	public List<Widget> findDefaultWidget(BaseUser user) {
		Map<String, Object> map = new HashMap<String, Object>();
		Set<String> roles = new HashSet<String>();
		if(user.getGroups() != null){
			for (UserGroup group : user.getGroups()) {
				if(group.getAuthorities() != null){
					for(UserAuthority userRole : group.getAuthorities()){
						roles.add(userRole.getAuthority());
					}
				}
			}
		}
		if (roles==null || roles.isEmpty()) {
			return null;
		}
		map.put("roles", roles);
		List<Widget> list = findByNamedQuery("jpql.widget.findDefaultWidgets", map);
		if (list == null || list.size() == 0)
			return null;
		
		return list;
	}

	@Override
	public List<Widget> findWidgetWithAccessCode(List<String> accessCodes) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roles", accessCodes);
		
		return findByNamedQuery("jpql.widget.findWidgetsWithAccessCode", params);
	}

}
