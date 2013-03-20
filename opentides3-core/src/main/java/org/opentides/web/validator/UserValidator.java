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

import org.opentides.bean.SystemCodes;
import org.opentides.bean.user.BaseUser;
import org.opentides.dao.UserDao;
import org.opentides.util.StringUtil;
import org.opentides.util.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
	
	@Autowired
	private UserDao userDao;

	public boolean supports(Class<?> clazz) {
		return BaseUser.class.isAssignableFrom(clazz);
	}

	public void validate(Object object, Errors e) {
		BaseUser user = (BaseUser) object;

		ValidationUtils.rejectIfEmptyOrWhitespace(e, "firstName", 
				"error.required", new Object[]{"First Name"},"First name is required.");

		ValidationUtils.rejectIfEmptyOrWhitespace(e, "lastName", 
				"error.required", new Object[]{"Last Name"},"Last name is required.");

		ValidationUtils.rejectIfEmptyOrWhitespace(e, "credential.username", 
				"error.required", new Object[]{"Username"},"Username is required.");
		
		/*if (isDuplicateUsername(user)) {
			e.reject("error.duplicate-field", new Object[]{user.getCredential().getUsername(), "username"}, "User name already exists.");
		}*/

		ValidationUtils.rejectIfEmptyOrWhitespace(e, "emailAddress", 
				"error.required", new Object[]{"Email Address"},"Email address is required.");
		
		/*if (!ValidatorUtil.isEmail(user.getEmailAddress())) {
			e.rejectValue("emailAddress", "error.invalid-email-address",new Object[]{user.getEmailAddress()},"Email Address is invalid.");
		}
		if (isDuplicateEmail(user)) {
			e.reject("error.duplicate-field", new Object[]{user.getEmailAddress(),"email"}, "Email address already exists.");
		}*/

		if (user.getIsNew()) {
			ValidationUtils.rejectIfEmptyOrWhitespace(e, "credential.newPassword", 
				"error.required", new Object[]{"Password"},"Password is required.");
			ValidationUtils.rejectIfEmptyOrWhitespace(e, "credential.confirmPassword", 
				"error.required", new Object[]{"Confirm Password"},"Confirm password is required.");
		}
		
		if (!StringUtil.isEmpty(user.getCredential().getNewPassword()) &&
		    !StringUtil.isEmpty(user.getCredential().getConfirmPassword()) &&
		    !user.getCredential().getNewPassword().equals(user.getCredential().getConfirmPassword())) {
			e.reject("error.password-confirmation-did-not-match", "Password confirmation did not match.");
		}
	}

	
	/**
	 * private helper method to find a duplicate username
	 * @param username
	 * @return boolean return true if duplicate username was found, false otherwise.
	 */
	private boolean isDuplicateUsername(BaseUser user) {
		String userName = user.getCredential().getUsername();
		if (userName != null && !StringUtil.isEmpty(userName)){
			BaseUser userCheck = userDao.loadByUsername(userName);
			if (userCheck != null && user.isNew())
				return true;		
			if (userCheck != null && !userCheck.getId().equals(user.getId())) 
				return true;
		}
		return false;
	}

	/**
	 * private helper method to find a duplicate email
	 * @param email
	 * @return boolean returns true if duplicate email was found, false otherwise.
	 */
	private boolean isDuplicateEmail(BaseUser user) {
		String email = user.getEmailAddress();
		if (email != null && !StringUtil.isEmpty(email)){
			BaseUser userCheck = userDao.loadByEmailAddress(email);
			if (userCheck != null && user.isNew())
				return true;			
			if (userCheck != null && !userCheck.getId().equals(user.getId())) 
				return true;
		}
		return false;
	}

}
