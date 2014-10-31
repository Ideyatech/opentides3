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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.opentides.bean.BaseEntity;
import org.opentides.bean.Event;
import org.opentides.bean.Notification;
import org.opentides.bean.Notification.Medium;
import org.opentides.bean.Notification.Status;
import org.opentides.dao.NotificationDao;
import org.opentides.eventhandler.EmailHandler;
import org.opentides.service.MailingService;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author allantan
 *
 */

@Service(value = "notificationService")
public class NotificationServiceImpl extends BaseCrudServiceImpl<Notification>
implements NotificationService {

	private static Logger _log = Logger.getLogger(NotificationServiceImpl.class);

	@Value("#{notificationSettings['mail.default-template']}")
	private String mailVmTemplate;
	
	@Value("#{notificationSettings['notification.execute-limit']}")
	private String limit;

	@Autowired
	protected MessageSource messageSource;
	
	@Autowired
	private VelocityEngine velocityEngine;

	@Autowired
	private NotificationDao notificationDao;
	
	@Autowired
	private EmailHandler emailHandler;
	
	@Autowired 
	private MailingService mailingService;
	
//	public String buildEmailMessage() {
//		Map<String, Object> model = new HashMap<String, Object>();
//		
//		String text = VelocityEngineUtils.mergeTemplateIntoString(
//                velocityEngine, "com/dns/registration-confirmation.vm", "UTF-8", model);
//	}
	
	@Scheduled(fixedDelayString = "${notification.delay}")	
	public void executeNotification() {
		List<Notification> notifications = notificationDao.findNewNotifications(StringUtil.convertToInt(limit,20));
		for (Notification n:notifications) {			
			Status status = null;
			boolean processed = false;
			StringBuffer remarks = new StringBuffer();
			try {
				if (n.getMedium().equals(Medium.EMAIL)) {
					// send email
					if (n.getSubject()==null) n.setSubject("");
					emailHandler.sendEmail(new String [] {n.getRecipientReference()}, new String [] {}, new String [] {}, 
						n.getSubject(), n.getMessage());
					status=Status.PROCESSED;
					processed = true;
					remarks.append("Email successfully sent to "+n.getRecipientReference()+".\n");
				}
				if (n.getMedium().equals(Medium.SMS)) {
					// send SMS
					processed = true;
					remarks.append("SMS notification not supported.\n");
				}
				if (processed) {
					n.setStatus(status);
					n.setRemarks(remarks.toString());				
					notificationDao.saveEntityModel(n);
				}
			} catch (Exception e) {
				remarks.append(e.getMessage());
				if (remarks.length()>3999)
					n.setRemarks(remarks.substring(0, 3999));
				else
					n.setRemarks(remarks.toString()+"\n");				
				n.setStatus(Status.FAILED);
				notificationDao.saveEntityModel(n);	
			}
		}
	}
	
	public void notify(String userId, Event event) {
	    Broadcaster b = BroadcasterFactory.getDefault().lookup(userId);
	    if (b!=null) {
	        long nCount = notificationDao.countNewPopup(new Long(userId));
	        List<Notification> notifications = notificationDao.findNewPopup(new Long(userId));
	        StringBuffer response = new StringBuffer("[");
	        response.append(nCount);
	        for (Notification n:notifications) {
	        	response.append(",\"")
	        			.append(n.getMessage())
	        			.append("\"");
	        }
	        response.append("]");
	        b.broadcast(response.toString());
	    }
	}
	
	@Override
	public String buildMessage(Event event, Object[] params) {				
		return messageSource.getMessage(event.getMessageCode(), params, 
				Locale.getDefault());
	}
	
	@Override
	public void triggerEvent(Event event, BaseEntity command) {
		// build the message
		
		
		// get the recipients
		
		// save as new notification
	}

	@Override
	public long countNewPopup(long userId) {
		return notificationDao.countNewPopup(userId);
	}

	@Override
	public List<Notification> findNewPopup(long userId) {
		return notificationDao.findNewPopup(userId);
	}

}
