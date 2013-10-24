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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.opentides.bean.UserWidgets;
import org.opentides.dao.UserWidgetsDao;
import org.springframework.stereotype.Repository;

/**
 * @author gino
 *
 */
@Repository("userWidgetsDao")
public class UserWidgetsDaoJpaImpl extends BaseEntityDaoJpaImpl<UserWidgets, Long>
		implements UserWidgetsDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<UserWidgets> findUserWidgets(long userId, Integer... widgetStatus) {
		List<UserWidgets> defaultList = new ArrayList<UserWidgets>();
		Query query = getEntityManager().createQuery("SELECT i from UserWidgets i where i.user.id = :userId" + 
				((widgetStatus != null && widgetStatus.length > 0) ? " AND i.status in (:widgetStatus) " : " ") + 
				" ORDER BY i.column asc, i.row asc");
		if((widgetStatus != null && widgetStatus.length > 0)) {
			query.setParameter("widgetStatus", Arrays.asList(widgetStatus));
		}
		query.setParameter("userId", userId);
		
		List<UserWidgets> list = (List<UserWidgets>) query.getResultList();
		if (!list.isEmpty())
			return list;
		else
			return defaultList;
	}

	@Override
	public long countUserWidgetsColumn(Integer column, long userId) {
		Query query = getEntityManager().createQuery(getJpqlQuery("jpql.userwidgets.countUserWidgetsColumn"));
		query.setParameter("userId", userId);
		query.setParameter("column", column);
		return ((Long) query.getSingleResult()).intValue();
	}

	@Override
	public void deleteUserWidget(long widgetId, long baseUserId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", baseUserId);
		params.put("widgetId", widgetId);
		
		executeByNamedQuery("jpql.widget.deleteUserWidgetsByWidgetAndUser", params);
	}

}
