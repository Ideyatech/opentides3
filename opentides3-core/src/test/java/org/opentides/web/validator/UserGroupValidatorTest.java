package org.opentides.web.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.opentides.bean.user.BaseUser;
import org.opentides.bean.user.UserAuthority;
import org.opentides.bean.user.UserGroup;
import org.springframework.validation.BindException;

public class UserGroupValidatorTest {

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
		new UserGroupValidator().validate(obj, errors);
		
		assertTrue(errors.getAllErrors().size() == 0);
		
	}
	
	@Test
	public void testValidateInvalid() {
		UserGroup obj = new UserGroup();
		obj.setName("");
		obj.setDescription("");
		
		BindException errors = new BindException(obj, "userGroup");
		new UserGroupValidator().validate(obj, errors);
		
		assertEquals(3, errors.getAllErrors().size());
		
	}

}
