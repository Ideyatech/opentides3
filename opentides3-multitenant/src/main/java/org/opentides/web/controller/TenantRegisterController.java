/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 *******************************************************************************/
package org.opentides.web.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Jeric
 *
 */
@Controller
@RequestMapping(value = "/register-tenant")
public class TenantRegisterController extends BaseCrudController<Tenant> {
	private static final Logger _log = Logger
			.getLogger(TenantRegisterController.class);

	@Autowired
	protected AccountTypeService accountTypeService;

	@Autowired
	@Qualifier("userService")
	protected UserService userService;

	@Value("${property.subdomain}")
	protected String subdomain;

	/**
 * 
 */
	@PostConstruct
	public void init() {
		singlePage = "registration";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opentides.web.controller.BaseCrudController#preCreate(org.opentides
	 * .bean.BaseEntity)
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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opentides.web.controller.BaseCrudController#postCreate(org.opentides
	 * .bean.BaseEntity)
	 */
	@Override
	protected void postCreate(final Tenant command) {
		((TenantService) getService()).createTenantSchema(command,
				command.getOwner());
	}

	@ModelAttribute("accountTypeList")
	public List<AccountType> getAccountTypes() {
		return accountTypeService.findAll();
	}

	@ModelAttribute("subdomain")
	public String getSubdomain() {
		return subdomain;
	}
}
