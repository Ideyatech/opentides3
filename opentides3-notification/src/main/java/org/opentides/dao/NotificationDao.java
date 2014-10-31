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

import java.util.List;

import org.opentides.bean.Notification;

/**
 * DAO for Notification.
 * 
 * @author allantan 
 */
public interface NotificationDao extends BaseEntityDao<Notification, Long> {

	/**
	 * Retrieves all new notifications to be executed. 
	 * Results will be sorted with oldest first but limited 
	 * based on limit parameter.
	 * 
	 * Limit is used to avoid long running batch process.
	 * 
	 * @param limit
	 * @return
	 */
	public List<Notification> findNewNotifications(int limit);
	
	/**
	 * Returns the total number of popup notification that is new and 
	 * not yet seen by user.
	 * @return
	 */
	public long countNewPopup(long userId);
	
	/**
	 * Retrieves all the new notification to be displayed.
	 * 
	 * @param userId
	 * @return
	 */
	public List<Notification> findNewPopup(long userId);
	

}
