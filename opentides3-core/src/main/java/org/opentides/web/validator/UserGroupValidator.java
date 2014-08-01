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

import org.opentides.bean.user.UserGroup;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UserGroupValidator implements Validator {

	@SuppressWarnings("rawtypes")
	public boolean supports(Class clazz) {
		return UserGroup.class.isAssignableFrom(clazz);
	}

	public void validate(Object object, Errors errors) {
		UserGroup userGroup = (UserGroup) object;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "error.required", new Object[]{"Name"});
		ValidationUtils.rejectIfEmpty(errors, "description", "error.required", new Object[]{"Description"});
		if (userGroup.getAuthorities() == null || userGroup.getAuthorities().size() < 1){
			errors.reject("error.role-required");
		}
	}

}
