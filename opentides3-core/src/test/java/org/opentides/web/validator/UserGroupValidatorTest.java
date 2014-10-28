package org.opentides.web.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.opentides.bean.user.BaseUser;
import org.opentides.bean.user.UserAuthority;
import org.opentides.bean.user.UserGroup;
import org.opentides.service.UserGroupService;
import org.springframework.validation.BindException;

public class UserGroupValidatorTest {

	@InjectMocks
	private UserGroupValidator userGroupValidator = new UserGroupValidator();
	
	@Mock
	private UserGroupService usergroupService;
	
	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testSupports() {
		assertTrue(new UserGroupValidator().supports(UserGroup.class));
		assertFalse(new UserGroupValidator().supports(BaseUser.class));
	}

	@Test
	public void testValidateValid() {
		UserGroup obj = new UserGroup();
		obj.setName("Group 1");
		obj.setDescription("Description 1");
		
		UserAuthority auth1 = new UserAuthority();
		auth1.setAuthority("AUTH_1");
		UserAuthority auth2 = new UserAuthority();
		auth2.setAuthority("AUTH_2");
		obj.addAuthority(auth1);
		obj.addAuthority(auth2);
		
		BindException errors = new BindException(obj, "userGroup");
		userGroupValidator.validate(obj, errors);
		
		assertTrue(errors.getAllErrors().size() == 0);
		
	}
	
	@Test
	public void testValidateInvalid() {
		UserGroup obj = new UserGroup();
		obj.setName("");
		obj.setDescription("");
		
		BindException errors = new BindException(obj, "userGroup");
		userGroupValidator.validate(obj, errors);
		
		assertEquals(1, errors.getAllErrors().size());
		
	}

}
