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
package org.opentides.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.opentides.bean.Event;
import org.opentides.bean.Notification;
import org.opentides.bean.Notification.Medium;
import org.opentides.bean.Notification.Status;
import org.opentides.bean.SystemCodes;
import org.opentides.bean.user.BaseUser;
import org.opentides.bean.user.SessionUser;
import org.opentides.service.UserService;
import org.opentides.service.impl.NotificationService;
import org.opentides.util.DateUtil;
import org.opentides.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author allanctan
 *
 */
@RequestMapping("/notification-log")
@Controller
public class NotificationController extends BaseCrudController<Notification> {
	
	@Autowired
	NotificationService notificationService;
	
	@Autowired
	UserService userService;

	/**
	 * Post construct that initializes the crud page to {@code "/base/notification-log"}.
	 */
	@PostConstruct
	public void init() {
		singlePage = "/base/notification-log";
	}
	
	@ModelAttribute("statusList")
	public List<SystemCodes> getStatusList() {
		List<SystemCodes> status = new ArrayList<SystemCodes>();		
		status.add(new SystemCodes("",Notification.Status.NEW.toString(), Notification.Status.NEW.toString()));
		status.add(new SystemCodes("",Notification.Status.PROCESSED.toString(), Notification.Status.PROCESSED.toString()));
		status.add(new SystemCodes("",Notification.Status.FAILED.toString(), Notification.Status.FAILED.toString()));
		return status;
	}
	
	@ModelAttribute("mediumList")
	public List<SystemCodes> getMediumList() {
		List<SystemCodes> mediums = new ArrayList<SystemCodes>();		
		mediums.add(new SystemCodes("",Notification.Medium.POPUP.toString(), Notification.Medium.POPUP.toString()));
		mediums.add(new SystemCodes("",Notification.Medium.EMAIL.toString(), Notification.Medium.EMAIL.toString()));
		mediums.add(new SystemCodes("",Notification.Medium.SMS.toString(), Notification.Medium.SMS.toString()));
		return mediums;
	}

	@RequestMapping(method = RequestMethod.GET, value="/add-random")
	public final String addRandom(Model model){	
		BaseUser user = userService.getCurrentUser();
		String random = DateUtil.dateToString(new Date(), "hh:mm:ss");
		Notification n1 = new Notification();
		n1.setMessage("This is a test message. " + random);
		n1.setMedium(Medium.POPUP.toString());
		n1.setRecipientUser(user);
		n1.setEntityClass(this.getClass());
		n1.setEntityId(1l);
		n1.setEventGroupId(1l);
		n1.setStatus(Status.NEW.toString());
		notificationService.save(n1);
				
		try {
			SessionUser sessionUser = SecurityUtil.getSessionUser();
			Event event = new Event();			
			event.setDescription("Hooray! " + random);
			notificationService.notify(""+sessionUser.getId());
		} catch (Exception e) {
		}		
		return "";
	}

}
