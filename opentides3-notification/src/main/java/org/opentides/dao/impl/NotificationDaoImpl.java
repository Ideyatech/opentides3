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
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.lang.time.DateUtils;
import org.opentides.bean.Notification;
import org.opentides.dao.NotificationDao;
import org.opentides.util.DateUtil;
import org.opentides.util.StringUtil;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Logging DAO for notification.
 * 
 * @author allantan
 *
 */
@Repository("notificationDao")
public class NotificationDaoImpl extends BaseEntityDaoJpaImpl<Notification, Long> 
implements NotificationDao {	

    /* (non-Javadoc)
	 * @see com.ideyatech.core.persistence.impl.BaseEntityDAOJpaImpl#appendClauseToExample(com.ideyatech.core.bean.BaseEntity, boolean)
	 */
	@Override
	protected String appendOrderToExample(Notification example) {
		return "order by createDate desc";
	}
	
	@Override
	protected String appendClauseToExample(Notification example, boolean exactMatch) {
		StringBuilder append = new StringBuilder("");
		if (!StringUtil.isEmpty(append.toString())){
			append.append(" and ");
		}
		
		if (example.getStartDate() != null){
			if (!StringUtil.isEmpty(append.toString())){
				append.append(" and ");
			}
			String startDate = DateUtil.dateToString(example.getStartDate(), "yyyy-MM-dd");
			append.append(" date(obj.createDate) >= '");
			append.append(startDate + "'");
		}
		
		if (example.getEndDate() != null){
			if (!StringUtil.isEmpty(append.toString())){
				append.append(" and ");
			}			
			String endDate = DateUtil.dateToString(
								DateUtils.addDays(example.getEndDate(), 1), "yyyy-MM-dd");
			append.append(" date(obj.createDate) <= '");
			append.append(endDate + "'");
		}
		
		return append.toString();
	}

	@Override
	@Transactional(readOnly=true)
	public List<Notification> findNewNotifications(int limit) {
        Map<String, Object> params = new HashMap<String, Object>();
        List<Notification> result = findByNamedQuery("jpql.notification.findNewNotification", params, 0, limit);
        return result;
	}

	@Override
	@Transactional(readOnly=true)
	public long countNewPopup(long userId) {
		String queryString = getJpqlQuery("jpql.notification.countNewPopup");
		Query queryObject = getEntityManager().createQuery(queryString);
		queryObject.setParameter("userId", userId);
		return (Long) queryObject.getSingleResult();
	}

	@Override
	@Transactional(readOnly=true)
	public List<Notification> findMostRecentPopup(long userId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        List<Notification> result = findByNamedQuery("jpql.notification.findMostRecentPopup", params, 0, 10);
        return result;
	}

	/* (non-Javadoc)
	 * @see org.gsis.tms.dao.NotificationDao#clearPopup(long)
	 */
	@Override
	@Transactional
	public void clearPopup(long userId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
		executeByNamedQuery("jpql.notification.clearPopup", params);
		return;		
	}

	@Override
	public void clearNotification(long id) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
		executeByNamedQuery("jpql.notification.clearNotification", params);
		return;		
	}
	
}
