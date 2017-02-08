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

import java.util.List;

import org.opentides.bean.user.UserGroup;
import org.opentides.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserGroupValidator implements Validator {

	@Autowired
	private UserGroupService userGroupService;
	
	@SuppressWarnings("rawtypes")
	public boolean supports(Class clazz) {
		return UserGroup.class.isAssignableFrom(clazz);
	}

	public void validate(Object object, Errors errors) {
		UserGroup userGroup = (UserGroup) object;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "error.required", new Object[]{"Name"});
		UserGroup lookFor = new UserGroup();
        lookFor.setName(userGroup.getName());
        List<UserGroup> groups = userGroupService.findByExample(lookFor, true);
        if(groups != null && !groups.isEmpty()) {
        	if(userGroup.isNew()) {
        		errors.reject("error.user-group.duplicate", new Object[]{userGroup.getName()},userGroup.getName());
        	} else {
        		UserGroup found = groups.get(0);
        		if(!found.getId().equals(userGroup.getId())) {
        			errors.reject("error.user-group.duplicate", new Object[]{userGroup.getName()},userGroup.getName());
        		}
        	}
        }
	}

}
