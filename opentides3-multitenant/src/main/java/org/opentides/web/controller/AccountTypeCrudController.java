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

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opentides.bean.user.AccountType;
import org.opentides.bean.user.AccountType.Period;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author allanctan
 *
 */
@RequestMapping("/system/account-type")
@Controller
public class AccountTypeCrudController extends BaseCrudController<AccountType> {
	
	/**
	 * Post construct that initializes the crud page to {@code "/base/account-type-crud"}.
	 */
	@PostConstruct
	public void init() {
		singlePage = "/base/account-type-crud";
	}
	
	/**
	 * Method stub that returns a list of all periods.
	 * 
	 * @return list of period
	 */
	@ModelAttribute("periodList")
	public List<String> periodList() {
		List<String> periodList = new ArrayList<String>();
		for (Period p:AccountType.Period.values()) {
			periodList.add(p.toString());
		}
		return periodList;
	}

	/* (non-Javadoc)
	 * @see org.opentides.web.controller.BaseCrudController#onLoadSearch(org.opentides.bean.BaseEntity, org.springframework.validation.BindingResult, org.springframework.ui.Model, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void onLoadSearch(AccountType command,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest request, HttpServletResponse response) {
		uiModel.addAttribute("results", search(command, request));
	}

}
