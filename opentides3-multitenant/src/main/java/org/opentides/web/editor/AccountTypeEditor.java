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
package org.opentides.web.editor;

import java.beans.PropertyEditorSupport;

import org.apache.log4j.Logger;
import org.opentides.bean.user.AccountType;
import org.opentides.service.AccountTypeService;
import org.opentides.util.StringUtil;

/**
 * PropertyEditor for Account Type using name.
 * 
 * @author allantan
 *
 */
public class AccountTypeEditor extends PropertyEditorSupport {
	
	private static final Logger _log = Logger.getLogger(AccountTypeEditor.class);
	
	private AccountTypeService accountTypeService;
	
	public AccountTypeEditor(AccountTypeService accountTypeService) {
		this.accountTypeService = accountTypeService;
	}
	
	@Override
	public String getAsText() {
		if (getValue() != null) {
			AccountType acct = (AccountType) getValue();
			return acct.getName();
		}
		return "";
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		try {
			if (!StringUtil.isEmpty(text))
				setValue(accountTypeService.getByName(text));
			else
				setValue(null);
		} catch (IllegalArgumentException iae) {
			_log.error("Failed to convert AccountType.",iae);
			throw iae;
		}
	}
}
