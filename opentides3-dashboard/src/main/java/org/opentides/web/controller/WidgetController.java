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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hsqldb.lib.StringUtil;
import org.opentides.annotation.FormBind;
import org.opentides.annotation.FormBind.Load;
import org.opentides.bean.Widget;
import org.opentides.bean.user.BaseUser;
import org.opentides.service.UserService;
import org.opentides.service.UserWidgetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author gino
 *
 */
@Controller
@RequestMapping(value = "/widget")
public class WidgetController extends BaseCrudController<Widget> {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserWidgetsService userWidgetsService;
	
	@PostConstruct
	public void init() {
		singlePage = "/base/widget-crud";
	}
	
	@FormBind(mode = Load.NEW)
	public Widget getInitialWidget(HttpServletRequest request) {
		Widget object = new Widget();
		object.setCacheDuration(3600);
		object.setIsUserDefined(true);
		return object;
	}
	
	@Override
	protected void onLoadSearch(Widget command, BindingResult bindingResult,
			Model uiModel, HttpServletRequest request,
			HttpServletResponse response) {
		
		uiModel.addAttribute("results", search(command, request));
	}
	
	@InitBinder
	protected void registerEditor(WebDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, "lastCacheUpdate", new CustomDateEditor(new SimpleDateFormat("MMM dd, yyyy hh:mm:ss"), true));
	}
	
	@Override
	protected void postCreate(Widget command, BindingResult bindingResult,
			Model uiModel, HttpServletRequest request,
			HttpServletResponse response) {
		if(command.getIsShown()) {
			List<BaseUser> users = new ArrayList<>(1);
			if(StringUtil.isEmpty(command.getAccessCode())) {
				users = userService.findAll();
			} else {
				users = userService.findAllUsersWithAuthority(command.getAccessCode());
			}
			userWidgetsService.addUserWidgets(users, command);
		}
	}
	
}
