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

import org.opentides.bean.UserWidgets;

/**
 * @author gino
 *
 */
public interface UserWidgetsDao extends BaseEntityDao<UserWidgets, Long> {
	
	/**
	 * Retrieve all widgets of a user based on the given status
	 * @param userId - the user
	 * @param widgetStatus - array's of widget status
	 * @return List of UserWidgets object
	 */
	public List<UserWidgets> findUserWidgets(long userId, Integer... widgetStatus);
	
	/**
	 * Count user widget installed based on userid and column number
	 * @param column - the column to count widgets
	 * @param userId - specific user
	 */
	public long countUserWidgetsColumn(Integer column,long userId);
	
	/**
	 * Deletes the user widget given the user and widget
	 * @param widgetId
	 * @param baseUserId
	 */
	public void deleteUserWidget(long widgetId, long baseUserId);

}
