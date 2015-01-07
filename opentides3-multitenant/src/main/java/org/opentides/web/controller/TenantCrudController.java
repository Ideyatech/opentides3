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

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opentides.bean.user.AccountType;
import org.opentides.bean.user.Tenant;
import org.opentides.bean.user.UserCredential;
import org.opentides.service.AccountTypeService;
import org.opentides.service.TenantService;
import org.opentides.service.UserService;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author allanctan
 *
 */
@RequestMapping("/system/tenant")
@Controller
public class TenantCrudController extends BaseCrudController<Tenant> {
	
	@Autowired
	private AccountTypeService accountTypeService;
	
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	/**
	 * Post construct that initializes the crud page to {@code "/base/tenant-crud"}.
	 */
	@PostConstruct
	public void init() {
		singlePage = "/base/tenant-crud";
	}

	@ModelAttribute("accountTypeList")
	public List<AccountType> accountTypeList() {
		return accountTypeService.findAll();
	}
	
	/**
	 * Responsible for changing the password of the {@code BaseUser} if a 
	 * {@code newPassword} is set.
	 * 
	 * @param command
	 */
	@Override
	protected void preCreate(Tenant command) {
		if (command.getOwner() != null) {
			UserCredential credential = command.getOwner().getCredential();
			if (!StringUtil.isEmpty(credential.getNewPassword()))
				credential.setPassword(userService.encryptPassword(credential.getNewPassword()));			
		}
		command.setSchema(((TenantService)service).findUniqueSchemaName(command.getCompany()));
		command.setDbVersion(1l);
	}
	

	/* (non-Javadoc)
	 * @see org.opentides.web.controller.BaseCrudController#postCreate(org.opentides.bean.BaseEntity)
	 */
	@Override
	protected void postCreate(Tenant tenant) {
		((TenantService)getService()).createTenantSchema(tenant);
	}

	/**
	 * Responsible for changing the password of the {@code BaseUser} if a 
	 * {@code newPassword} is set.
	 * 
	 * @param command
	 */
	@Override
	protected void preUpdate(Tenant command) {
		if (command.getOwner() != null) {
			UserCredential credential = command.getOwner().getCredential();
			if (!StringUtil.isEmpty(credential.getNewPassword()))
				credential.setPassword(userService.encryptPassword(credential.getNewPassword()));			
		}
	}
	
	/**
	 * Method that adds to the model parameter {@code uiModel} the results {@code results} 
	 * of {@link BaseCrudController } search method.
	 */
	@Override
	protected void onLoadSearch(Tenant command, BindingResult bindingResult, 
			Model uiModel, HttpServletRequest request,
			HttpServletResponse response) {
		uiModel.addAttribute("results", search(command, request));
	}
}
