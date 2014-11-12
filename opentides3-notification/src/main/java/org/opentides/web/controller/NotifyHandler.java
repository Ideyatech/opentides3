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
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.opentides.bean.Notification;
import org.opentides.service.impl.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * A Spring MVC controller that enables the server to push notifications to clients
 * using Atmosphere.
 */
@Controller
public class NotifyHandler {

	@Autowired
	private NotificationService notificationService;
	
    /**
     * When a comet request is received allow Spring to auto-magically resolve the meteor argument then:
     * Set the meteor's broadcaster to '/notify', suspend, then broadcast to this request only.
     * @param m A meteor that represents the current comet session.
     */
    @RequestMapping(value = "/notify/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<String> watch(@PathVariable("userId") String userId,
                      HttpServletRequest request) throws Exception {
        //Atmosphere framework puts filter/servlet that adds ATMOSPHERE_RESOURCE to all requests
        AtmosphereResource resource = (AtmosphereResource) request.getAttribute(ApplicationConfig.ATMOSPHERE_RESOURCE);

        //suspending resource to keep connection
        resource.suspend();

        //find broadcaster, second parameter says to create broadcaster if it doesn't exist
        Broadcaster broadcaster = BroadcasterFactory.getDefault().lookup(userId,true);

        //saving resource for notifications
        broadcaster.addAtmosphereResource(resource);
        
        long nCount = notificationService.countNewPopup(new Long(userId));
        List<Notification> notifications = notificationService.findMostRecentPopup(new Long(userId));
        List<String> response = new ArrayList<String>();
        response.add(""+nCount);
        for (Notification n:notifications) {
        	response.add(n.getMessage());
        }
        return response;
    }
   
}