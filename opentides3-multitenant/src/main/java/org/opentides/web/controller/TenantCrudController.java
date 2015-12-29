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
import org.opentides.bean.user.MultitenantUser;
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
	protected AccountTypeService accountTypeService;

	@Autowired
	@Qualifier("userService")
	protected UserService userService;

	/**
	 * Post construct that initializes the crud page to
	 * {@code "/base/tenant-crud"}.
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
	 * This will also trigger the creation of the tenant schema.
	 * 
	 * @param command
	 */
	@Override
	protected void preCreate(final Tenant command) {
		final MultitenantUser owner = command.getOwner();
		if (owner != null) {
			final UserCredential credential = owner.getCredential();
			if (!StringUtil.isEmpty(credential.getNewPassword())) {
				credential.setPassword(userService.encryptPassword(credential
						.getNewPassword()));
			}
			owner.setTenant(command);
		}

		((TenantService) getService()).createTenantSchema(command, owner);
	}

	/**
	 * Responsible for changing the password of the {@code BaseUser} if a
	 * {@code newPassword} is set.
	 * 
	 * Also responsible for ensuring that {@code BaseUser} is linked to the
	 * {@code Tenant}.
	 * 
	 * @param command
	 */
	@Override
	protected void preUpdate(final Tenant command) {
		final MultitenantUser owner = command.getOwner();
		if (owner != null) {
			final UserCredential credential = owner.getCredential();
			if (!StringUtil.isEmpty(credential.getNewPassword())) {
				credential.setPassword(userService.encryptPassword(credential
						.getNewPassword()));
			}
		}
	}

	/**
	 * Method that adds to the model parameter {@code uiModel} the results
	 * {@code results} of {@link BaseCrudController } search method.
	 */
	@Override
	protected void onLoadSearch(final Tenant command,
			final BindingResult bindingResult, final Model uiModel,
			final HttpServletRequest request, final HttpServletResponse response) {
		uiModel.addAttribute("results", search(command, request));
	}
}
