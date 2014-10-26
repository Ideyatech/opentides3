package org.opentides.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

 import org.opentides.util.StringUtil;
import org.opentides.util.ValidatorUtil;


import org.opentides.bean.Ninja;

public class NinjaValidator implements Validator {

	@SuppressWarnings("rawtypes")
	public boolean supports(Class clazz) {
		return Ninja.class.isAssignableFrom(clazz);
	}

	public void validate(Object clazz, Errors errors) {
		Ninja ninja = (Ninja) clazz;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "error.required", new Object[]{"Email"});
		if (!StringUtil.isEmpty(ninja.getEmail()) && !ValidatorUtil.isEmail(ninja.getEmail())) {
			errors.reject("error.invalid-email-address", new Object[]{"Email Address"}, "email");
		}
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "error.required", new Object[]{"First Name"});
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gender", "error.required", new Object[]{"Gender"});
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "error.required", new Object[]{"Last Name"});
	}

}
