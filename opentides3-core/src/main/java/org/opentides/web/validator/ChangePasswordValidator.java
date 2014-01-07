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

package org.opentides.web.validator;

import org.opentides.bean.user.PasswordReset;
import org.opentides.dao.UserDao;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * @author allanctan
 */
@Component
public class ChangePasswordValidator implements Validator {
	@Autowired
	private UserDao userDao;

	public boolean supports(Class<?> clazz) {
		return PasswordReset.class.isAssignableFrom(clazz);		
	}

	public void validate(Object clazz, Errors e) {
		PasswordReset obj = (PasswordReset) clazz;
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "emailAddress", "error.required", new Object[]{"Email Address"});
		if (!StringUtil.isEmpty(obj.getEmailAddress()) && !userDao.isRegisteredByEmail(obj.getEmailAddress())) {
			e.rejectValue("emailAddress","msg.email-address-is-not-registered",
					"Sorry, but your email address is not registered.");
		}
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "password", "error.required", new Object[]{"Password"});
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "confirmPassword", "error.required", new Object[]{"Confirm Password"});
		if (!StringUtil.isEmpty(obj.getPassword()) && (obj.getPassword().length()<6)) {
			e.reject("err.your-password-should-be-at-least-6-characters-long","Your password should be at least 6 characters long.");
		}
		if (!StringUtil.isEmpty(obj.getPassword()) && !StringUtil.isEmpty(obj.getConfirmPassword()) &&
				!obj.getPassword().equals(obj.getConfirmPassword()) ) {				
			e.reject("err.your-password-confirmation-did-not-match-with-password",
					"Your password confirmation did not match with password.");
		}
	}
}
