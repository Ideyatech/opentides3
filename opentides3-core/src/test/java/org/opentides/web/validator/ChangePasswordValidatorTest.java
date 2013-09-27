package org.opentides.web.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.opentides.bean.user.PasswordReset;
import org.opentides.dao.UserDao;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * JUnit test class for testing ChangePasswordValidator. This will verify the method
 * validate. Since ChangePasswordValidator needs a {@link UserDao} object a mock object
 * was used.
 * 
 * @author gino
 *
 */
public class ChangePasswordValidatorTest {
	
	private ChangePasswordValidator changePasswordValidator;
	
	@Mock 
	private UserDao userDao;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		changePasswordValidator = new ChangePasswordValidator();
		changePasswordValidator.setCoreUserDao(userDao);
	}
	@Test
	public void testValidateObjectPassedTheValidation() {
		PasswordReset obj = new PasswordReset();
		//Obj has an email address
		obj.setEmailAddress("test@test.com");
		//Obj has password and confirm password
		obj.setPassword("qwerty");
		obj.setConfirmPassword("qwerty");
		//mock coreUserDao.isRegisteredByEmail to return true
		Mockito.when(userDao.isRegisteredByEmail("test@test.com")).thenReturn(true);
		
		BindException error = new BindException(obj, "passwordReset");
		
		changePasswordValidator.validate(obj, error);
		//Validate that coreUserDao.isRegisteredByEmail was invoked
		Mockito.verify(userDao).isRegisteredByEmail("test@test.com");
		
		//There should be no errors
		assertFalse(error.hasErrors());
	}
	
	@Test
	public void testValidateMissingEmailAndPassword() {
		PasswordReset obj = new PasswordReset();
		//Obj has an email address
		obj.setEmailAddress("");
		//Obj has password and confirm password
		obj.setPassword("");
		obj.setConfirmPassword("");
		
		BindException error = new BindException(obj, "passwordReset");
		
		changePasswordValidator.validate(obj, error);
		//Validate that coreUserDao.isRegisteredByEmail was NOT invoked since email is empty
		Mockito.verify(userDao, Mockito.never()).isRegisteredByEmail("test@test.com");
		
		//There should be errors
		assertTrue(error.hasErrors());
		assertEquals(3, error.getFieldErrors().size());
		
		FieldError emailAddressError = error.getFieldError("emailAddress");
		assertNotNull(emailAddressError);
		
		FieldError passwordError = error.getFieldError("password");
		assertNotNull(passwordError);
		
		FieldError confirmPasswordError = error.getFieldError("confirmPassword");
		assertNotNull(confirmPasswordError);
		
	}
	
	@Test
	public void testValidatePasswordInvalidLength() {
		PasswordReset obj = new PasswordReset();
		//Obj has an email address
		obj.setEmailAddress("test@test.com");
		//Obj has password and confirm password
		obj.setPassword("12345");
		obj.setConfirmPassword("12345");
		
		Mockito.when(userDao.isRegisteredByEmail("test@test.com")).thenReturn(true);
		
		BindException error = new BindException(obj, "passwordReset");
		
		changePasswordValidator.validate(obj, error);
		//Validate that coreUserDao.isRegisteredByEmail was invoked since email is empty
		Mockito.verify(userDao).isRegisteredByEmail("test@test.com");
		
		//There should be errors
		assertTrue(error.hasErrors());
		
		//1 global errors for password length
		assertEquals(1, error.getGlobalErrorCount());
		ObjectError objError = error.getGlobalError();
		assertNotNull(objError);
		assertEquals("err.your-password-should-be-at-least-6-characters-long", objError.getCode());
		
	}
	
	@Test
	public void testValidatePasswordsNotTheSame() {
		PasswordReset obj = new PasswordReset();
		//Obj has an email address
		obj.setEmailAddress("test@test.com");
		//Obj has password and confirm password, but not the same
		obj.setPassword("123456");
		obj.setConfirmPassword("1234567");
		
		Mockito.when(userDao.isRegisteredByEmail("test@test.com")).thenReturn(true);
		
		BindException error = new BindException(obj, "passwordReset");
		
		changePasswordValidator.validate(obj, error);
		//Validate that coreUserDao.isRegisteredByEmail was invoked since email is empty
		Mockito.verify(userDao).isRegisteredByEmail("test@test.com");
		
		//There should be errors
		assertTrue(error.hasErrors());
		
		//1 global errors for password length
		assertEquals(1, error.getGlobalErrorCount());
		ObjectError objError = error.getGlobalError();
		assertNotNull(objError);
		assertEquals("err.your-password-confirmation-did-not-match-with-password", objError.getCode());
		
	}
	
	@Test
	public void testValidateEmailNotYetRegistered() {
		PasswordReset obj = new PasswordReset();
		//Obj has an email address
		obj.setEmailAddress("test@test.com");
		//Obj has password and confirm password
		obj.setPassword("qwerty");
		obj.setConfirmPassword("qwerty");
		//mock coreUserDao.isRegisteredByEmail to return true
		Mockito.when(userDao.isRegisteredByEmail("test@test.com")).thenReturn(false);
		
		BindException error = new BindException(obj, "passwordReset");
		
		changePasswordValidator.validate(obj, error);
		//Validate that coreUserDao.isRegisteredByEmail was invoked
		Mockito.verify(userDao).isRegisteredByEmail("test@test.com");
		
		//There should be no errors
		assertTrue(error.hasErrors());
		assertEquals(1, error.getFieldErrors().size());
		FieldError emailError = error.getFieldError("emailAddress");
		
		assertNotNull(emailError);
		assertEquals("msg.email-address-is-not-registered", emailError.getCode());

	}

}
