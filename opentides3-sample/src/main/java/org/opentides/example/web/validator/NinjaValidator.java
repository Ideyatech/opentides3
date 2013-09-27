package org.opentides.example.web.validator;

import org.opentides.example.bean.Ninja;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * Ninja Validator
 * 
 * Tests if you are a true Ninja
 * 
 * @author AJ
 *
 */
@Component
public class NinjaValidator implements Validator {

	@SuppressWarnings("rawtypes")
	public boolean supports(Class clazz) {
		return Ninja.class.isAssignableFrom(clazz);
	}

	public void validate(Object object, Errors e) {

		ValidationUtils.rejectIfEmptyOrWhitespace(e, "firstName", 
				"error.required", new Object[]{"First Name"},"First Name is required.");
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "lastName", 
				"error.required", new Object[]{"Last Name"},"Last Name is required.");
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "gender", 
				"error.required", new Object[]{"Gender"},"Gender is required.");

	}

}
