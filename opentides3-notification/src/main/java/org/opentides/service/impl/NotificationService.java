/*
 * Copyright 2007-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opentides.service.impl;

import java.util.List;
import java.util.Map;

import org.opentides.bean.BaseEntity;
import org.opentides.bean.Event;
import org.opentides.bean.Notification;
import org.opentides.service.BaseCrudService;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author allantan
 *
 */
public interface NotificationService extends BaseCrudService<Notification>{

	/**
	 * Invoke this method to trigger an event notification.
	 * 
	 * @param event
	 * @param command
	 */
	public void triggerEvent(Event event, BaseEntity command);
	
	/**
	 * Executes the notification messages by sending either via email, SMS, 
	 * or in-app.
	 */
	@Scheduled
	public void executeNotification();

	/**
	 * This is helper method that builds the message from messages.properties.
	 * 
	 * @param event
	 * @param command
	 * @return
	 */
	public String buildMessage(Event event, Object[] params);
	
	/**
	 * Invoke this method to notify the client of an event via atmosphere.
	 * 
	 * @param userId
	 */
	public void notify(String userId);
	
	/**
	 * Returns the total number of popup notification that is new and 
	 * not yet seen by user.
	 * @return
	 */
	public long countNewPopup(long userId);
	
	/**
	 * Clears all notifications as read.
	 * @param userId
	 */
	public void clearPopup(long userId);
	
	/**
	 * Returns the message for the notification ajax call.
	 * 
	 * @param userId
	 * @return
	 */
	public Map<String, Object> getPopupNotification(long userId);
	
	/**
	 * Retrieves all the new notification to be displayed.
	 * 
	 * @param userId
	 * @return
	 */
	public List<Notification> findMostRecentPopup(long userId);
}
