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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.opentides.bean.MessageResponse;
import org.opentides.bean.user.AccountType;
import org.opentides.bean.user.MultitenantUser;
import org.opentides.bean.user.Tenant;
import org.opentides.bean.user.UserCredential;
import org.opentides.service.AccountTypeService;
import org.opentides.service.TenantService;
import org.opentides.service.UserService;
import org.opentides.util.CrudUtil;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

	@RequestMapping(value = "/register-tenant", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Map<String, Object> register(
			@ModelAttribute("formCommand") final Tenant command,
			final HttpServletRequest request) {
		final Map<String, Object> model = new HashMap<String, Object>();
		final List<MessageResponse> messages = new ArrayList<MessageResponse>();

		final MultitenantUser owner = command.getOwner();
		if (owner != null) {
			final UserCredential credential = owner.getCredential();
			if (!StringUtil.isEmpty(credential.getNewPassword())) {
				credential.setPassword(userService.encryptPassword(credential
						.getNewPassword()));
			}

			owner.setTenant(command);
		}

		((TenantService) getService()).createTenantSchema(command,
				command.getOwner());
		((TenantService) getService()).save(command);
		messages.addAll(CrudUtil.buildSuccessMessage(command, "add",
				request.getLocale(), messageSource));
		model.put("formCommand", command);
		model.put("messages", messages);
		return model;
	}

	/**
	 * Handles all binding errors and return as json object for display to the
	 * user.
	 * 
	 * @param e
	 * @param request
	 * @return
	 */
	@Override
	@ExceptionHandler(Exception.class)
	public @ResponseBody Map<String, Object> handleBindException(
			final Exception ex, final HttpServletRequest request)
			throws Exception {
		final Map<String, Object> response = new HashMap<String, Object>();
		final List<MessageResponse> messages = new ArrayList<MessageResponse>();
		if (ex instanceof BindException) {
			final BindException e = (BindException) ex;
			messages.addAll(CrudUtil.convertErrorMessage(e.getBindingResult(),
					request.getLocale(), messageSource));
			if (_log.isDebugEnabled()) {
				_log.debug("Bind error encountered.", e);
			}
		}

		response.put("messages", messages);
		return response;
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
