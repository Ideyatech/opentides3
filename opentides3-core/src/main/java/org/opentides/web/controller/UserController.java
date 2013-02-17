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

import javax.annotation.PostConstruct;

import org.opentides.bean.user.BaseUser;
import org.opentides.bean.user.UserCredential;
import org.opentides.service.UserService;
import org.opentides.util.SecurityUtil;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This is the controller class for User.
 * Scaffold generated by opentides3 on Jan 16, 2013 12:40:25. 
 * @author opentides
 */
@Controller 
// @Secured("MANAGE_USER_PAGE")
@RequestMapping("/organization/users")
public class UserController extends BaseCrudController<BaseUser> {

	@PostConstruct
	public void init() {
		singlePage = "/base/user-crud";
	}
	
	@Autowired
	public void setService(UserService userService) {
		this.service = userService;
	}
	
	@Override
	protected void preCreateAction(BaseUser command) {
		UserCredential credential = command.getCredential();
		if (!StringUtil.isEmpty(credential.getNewPassword()))
			credential.setPassword(SecurityUtil.encryptPassword(credential.getNewPassword()));
	}

	@Override
	protected void preUpdateAction(BaseUser command) {
		UserCredential credential = command.getCredential();
        if (!StringUtil.isEmpty(credential.getNewPassword()))
            credential.setPassword(SecurityUtil.encryptPassword(credential.getNewPassword()));
	}
}
