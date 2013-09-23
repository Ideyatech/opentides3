package org.opentides.web.validator;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.opentides.bean.user.BaseUser;
import org.opentides.bean.user.UserCredential;
import org.opentides.bean.user.UserGroup;
import org.opentides.dao.UserDao;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * JUnit test for {@link UserValidator}.
 * 
 * @author gino
 *
 */
public class UserValidatorTest {
	
	@InjectMocks
	private UserValidator userValidator = new UserValidator();
	
	@Mock
	private UserDao userDao;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testValidateCreatingNewUser() {
		BaseUser obj = new BaseUser();
		UserCredential credential = new UserCredential();
		obj.setCredential(credential);
		
		BindException errors = new BindException(obj, "baseUser");
		
		obj.setFirstName("First");
		obj.setLastName("Last");
		credential.setUsername("flast");
		credential.setNewPassword("123456");
		credential.setPassword("123456");
		credential.setConfirmPassword("123456");
		obj.setEmailAddress("test@test.com");
		
		//There should be no duplicate user so just return null
		Mockito.when(userDao.loadByUsername("flast")).thenReturn(null);
		//There should be no user with the same email so just return null
		Mockito.when(userDao.loadByEmailAddress("test@test.com")).thenReturn(null);
		
		//Just put a non-empty set of UserGroup
		Set<UserGroup> groups = new HashSet<UserGroup>();
		groups.add(new UserGroup());
		obj.setGroups(groups);
		
		userValidator.validate(obj, errors);
		assertFalse(errors.hasErrors());
		
	}
	
	@Test
	public void testValidateExistingUser() {
		BaseUser obj = new BaseUser();
		UserCredential credential = new UserCredential();
		obj.setCredential(credential);
		
		BindException errors = new BindException(obj, "baseUser");
		
		obj.setFirstName("First");
		obj.setLastName("Last");
		//Set id > 0 since it is existing
		obj.setId(1l);
		credential.setUsername("flast");
		//New password and confirm password should not matter
		credential.setNewPassword("");
		credential.setConfirmPassword("");
		obj.setEmailAddress("test@test.com");
		
		//There should be no duplicate user so just return null
		Mockito.when(userDao.loadByUsername("flast")).thenReturn(null);
		//There should be no user with the same email so just return null
		Mockito.when(userDao.loadByEmailAddress("test@test.com")).thenReturn(null);
		
		//Just put a non-empty set of UserGroup
		Set<UserGroup> groups = new HashSet<UserGroup>();
		groups.add(new UserGroup());
		obj.setGroups(groups);
		
		userValidator.validate(obj, errors);
		assertFalse(errors.hasErrors());
		
	}
	
	@Test
	public void testValidateEmptyRequiredFields() {
		BaseUser obj = new BaseUser();
		UserCredential credential = new UserCredential();
		obj.setCredential(credential);
		
		BindException errors = new BindException(obj, "baseUser");
		
		obj.setFirstName("");
		obj.setLastName("");
		credential.setUsername("");
		credential.setNewPassword("");
		credential.setConfirmPassword("");
		obj.setEmailAddress("");
		
		obj.setGroups(null);
		
		userValidator.validate(obj, errors);
		
		Mockito.verify(userDao, Mockito.never()).loadByUsername("flast");
		Mockito.verify(userDao, Mockito.never()).loadByEmailAddress("test@test.com");
		
		assertTrue(errors.hasErrors());
		
		assertEquals(7, errors.getFieldErrorCount());
		
		FieldError firstNameError = errors.getFieldError("firstName");
		assertNotNull(firstNameError);
		assertEquals("error.required", firstNameError.getCode());
		
		FieldError lastNameError = errors.getFieldError("lastName");
		assertNotNull(lastNameError);
		assertEquals("error.required", lastNameError.getCode());
		
		FieldError usernameError = errors.getFieldError("credential.username");
		assertNotNull(usernameError);
		assertEquals("error.required", usernameError.getCode());
		
		FieldError groupsError = errors.getFieldError("groups");
		assertNotNull(groupsError);
		assertEquals("error.required.at-least-one", groupsError.getCode());
		
		FieldError emailAddressError = errors.getFieldError("emailAddress");
		assertNotNull(emailAddressError);
		assertEquals("error.required", emailAddressError.getCode());
		
		FieldError newPasswordError = errors.getFieldError("credential.newPassword");
		assertNotNull(newPasswordError);
		assertEquals("error.required", newPasswordError.getCode());
		
		FieldError confirmPasswordError = errors.getFieldError("credential.confirmPassword");
		assertNotNull(confirmPasswordError);
		assertEquals("error.required", confirmPasswordError.getCode());
		
	}
	
	@Test
	public void testValidateDuplicateEmailAndUser() {
		BaseUser obj = new BaseUser();
		UserCredential credential = new UserCredential();
		obj.setCredential(credential);
		
		BindException errors = new BindException(obj, "baseUser");
		
		obj.setFirstName("First");
		obj.setLastName("Last");
		credential.setUsername("flast");
		credential.setNewPassword("123456");
		credential.setPassword("123456");
		credential.setConfirmPassword("123456");
		obj.setEmailAddress("test@test.com");
		
		//There should be duplicate user so return something
		BaseUser userCheck = new BaseUser();
		userCheck.setId(1l);
		userCheck.setEmailAddress("test@test.com");
		userCheck.setCredential(credential);
		Mockito.when(userDao.loadByUsername("flast")).thenReturn(userCheck);
		//There should be user with the same email so return something
		Mockito.when(userDao.loadByEmailAddress("test@test.com")).thenReturn(userCheck);
		
		//Just put a non-empty set of UserGroup
		Set<UserGroup> groups = new HashSet<UserGroup>();
		groups.add(new UserGroup());
		obj.setGroups(groups);
		
		userValidator.validate(obj, errors);
		
		Mockito.verify(userDao).loadByUsername("flast");
		Mockito.verify(userDao).loadByEmailAddress("test@test.com");
		
		assertTrue(errors.hasErrors());
		assertEquals(2, errors.getGlobalErrorCount());
		int errorCheck = 0;
		for(ObjectError error : errors.getGlobalErrors()) {
			if(error.getCode().equals("error.duplicate-field")) {
				errorCheck ++;
			}
		}
		//The code for both errors should be error.duplicate-field
		assertEquals(2, errorCheck);
		
	}
	
	@Test
	public void testValidateInvalidEmailFormat() {
		BaseUser obj = new BaseUser();
		UserCredential credential = new UserCredential();
		obj.setCredential(credential);
		
		BindException errors = new BindException(obj, "baseUser");
		
		obj.setFirstName("First");
		obj.setLastName("Last");
		credential.setUsername("flast");
		credential.setNewPassword("123456");
		credential.setPassword("123456");
		credential.setConfirmPassword("123456");
		obj.setEmailAddress("testtest.com");
		
		//There should be no duplicate user so just return null
		Mockito.when(userDao.loadByUsername("flast")).thenReturn(null);
		//There should be no user with the same email so just return null
		Mockito.when(userDao.loadByEmailAddress("test@test.com")).thenReturn(null);
		
		//Just put a non-empty set of UserGroup
		Set<UserGroup> groups = new HashSet<UserGroup>();
		groups.add(new UserGroup());
		obj.setGroups(groups);
		
		userValidator.validate(obj, errors);
		assertTrue(errors.hasErrors());
		
		FieldError emailError =  errors.getFieldError("emailAddress");
		assertNotNull(emailError);
		assertEquals("error.invalid-email-address", emailError.getCode());
		
	}

}
