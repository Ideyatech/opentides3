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
import org.opentides.service.SystemCodesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * @author allanctan
 * 
 */
@Component
public class SystemCodesValidator implements Validator {

	@Autowired
	private SystemCodesService systemCodesService;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class<?> clazz) {
		return SystemCodes.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		SystemCodes systemCodes = (SystemCodes) target;
		
		ValidationUtils.rejectIfEmpty(errors, "category", "error.required",
				new Object[] { "Category" });
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "key", "error.required",
				new Object[] { "Key" });
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "value", "error.required",
				new Object[] { "Value" });

		if(systemCodesService.isDuplicateKey(systemCodes)){
			errors.reject("error.duplicate-key", new Object[]{"\""+systemCodes.getKey()+"\"","key"}, "\""+systemCodes.getKey() +"\" already exists. Please try a different key.");
		}					

	}

}
