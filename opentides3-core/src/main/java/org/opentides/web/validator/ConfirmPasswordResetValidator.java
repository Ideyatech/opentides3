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
import org.opentides.util.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * @author allanctan
 *
 */
@Component
public class ConfirmPasswordResetValidator implements Validator {
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	public boolean supports(Class clazz) {
		return PasswordReset.class.isAssignableFrom(clazz);		
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	public void validate(Object clazz, Errors e) {
		PasswordReset obj = (PasswordReset) clazz;
		if (StringUtil.isEmpty(obj.getCipher())) {
			ValidationUtils.rejectIfEmptyOrWhitespace(e, "emailAddress", "error.required", new Object[]{"Email Address"});
			ValidationUtils.rejectIfEmptyOrWhitespace(e, "token", "error.required", new Object[]{"Confirmation Code"});
		} else {
			ValidationUtils.rejectIfEmptyOrWhitespace(e, "cipher", "error.required", new Object[]{"Link Cipher Code"});			
		}
	}
}
