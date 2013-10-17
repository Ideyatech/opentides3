package org.opentides.web.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.opentides.bean.SystemCodes;
import org.opentides.bean.user.PasswordReset;
import org.springframework.validation.BindException;

public class ConfirmPasswordResetValidatorTest {

	@Test
	public void testSupports() {
		assertTrue(new ConfirmPasswordResetValidator().supports(PasswordReset.class));
		assertFalse(new ConfirmPasswordResetValidator().supports(SystemCodes.class));
	}

	@Test
	public void testValidateEmptyCipher() {
		PasswordReset obj = new PasswordReset();
		obj.setCipher("");
		obj.setEmailAddress("email@email.com");
		obj.setToken("token");
		BindException errors = new BindException(obj, "passwordReset");
		new ConfirmPasswordResetValidator().validate(obj, errors);
		assertTrue(errors.getAllErrors().size() == 0);
	}
	
	@Test
	public void testValidateEmptyCipherWithError() {
		PasswordReset obj = new PasswordReset();
		obj.setCipher("");
		obj.setEmailAddress("");
		obj.setToken("");
		BindException errors = new BindException(obj, "passwordReset");
		new ConfirmPasswordResetValidator().validate(obj, errors);
		assertTrue(errors.getAllErrors().size() == 2);
	}
	
	@Test
	public void testValidateNonEmptyCipher() {
		PasswordReset obj = new PasswordReset();
		obj.setCipher("12345");
		BindException errors = new BindException(obj, "passwordReset");
		new ConfirmPasswordResetValidator().validate(obj, errors);
		assertTrue(errors.getAllErrors().size() == 0);
	}

}
