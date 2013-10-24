package org.opentides.dao.impl;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.opentides.bean.user.UserAuthority;
import org.opentides.bean.user.UserGroup;
import org.opentides.dao.UserGroupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

@Transactional
public class UserGroupDAOIntegrationTest extends BaseDaoTest {

	@Autowired
	private UserGroupDao userGroupDAO;

	@Test
	public void testAdd() {
		final UserGroup group = new UserGroup();
		group.setName("test");
		group.setDescription("test");
		UserAuthority role1 = new UserAuthority(group, "role1");
		group.addAuthority(role1);

		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				userGroupDAO.saveEntityModel(group);
			}
		});

		assertNotNull(group.getId());
		final UserGroup testGroup = userGroupDAO.loadEntityModel(group.getId());
		Set<UserAuthority> roles = testGroup.getAuthorities();
		assertNotNull(roles);
		assertTrue(roles.size() == 1);
		assertTrue(roles.iterator().next().getAuthority().equals(role1.getAuthority()));

		testGroup.removeAuthority(role1);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				userGroupDAO.saveEntityModel(testGroup);
				assertEquals(testGroup.getId(), group.getId());
				UserGroup testGroup2 = userGroupDAO.loadEntityModel(testGroup.getId());
				assertTrue(testGroup2.getAuthorities().size() == 0);
			}
		});

	}
	
	@Test
	@Rollback(true)
	public void testAssignRoles() {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				UserGroup userGroup = userGroupDAO.loadEntityModel(1l);
				UserAuthority newAuth = new UserAuthority(userGroup, "CAN_ADD_SOMETHING");
				Set<UserAuthority> newUserAuths = new HashSet<>();
				newUserAuths.add(newAuth);
				userGroupDAO.assignRoles(userGroup, newUserAuths);
			}
		});
		UserGroup userGroupCheck = userGroupDAO.loadEntityModel(1l);
		assertEquals(4, userGroupCheck.getAuthorities().size());
	}
	
	@Test
	@Rollback(true)
	public void testAssignRolesEmptyInitialRoles() {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				UserGroup userGroup = userGroupDAO.loadEntityModel(2l);
				UserAuthority newAuth = new UserAuthority(userGroup, "CAN_ADD_SOMETHING");
				Set<UserAuthority> newUserAuths = new HashSet<>();
				newUserAuths.add(newAuth);
				userGroupDAO.assignRoles(userGroup, newUserAuths);
			}
		});
		UserGroup userGroupCheck = userGroupDAO.loadEntityModel(2l);
		assertEquals(1, userGroupCheck.getAuthorities().size());
	}
	
	@Test(expected = NullPointerException.class)
	public void testAssignRolesNullUserGroup() {
		UserAuthority newAuth = new UserAuthority(null, "CAN_ADD_SOMETHING");
		Set<UserAuthority> newUserAuths = new HashSet<>();
		newUserAuths.add(newAuth);
		userGroupDAO.assignRoles(null, newUserAuths);
	}
	
	@Test
	@Rollback(true)
	public void testAssignRolesSingleRole() {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				UserGroup userGroup = userGroupDAO.loadEntityModel(1l);
				UserAuthority newAuth = new UserAuthority(userGroup, "CAN_ADD_SOMETHING");
				userGroupDAO.assignRoles(userGroup, newAuth);
			}
		});
		UserGroup userGroupCheck = userGroupDAO.loadEntityModel(1l);
		assertEquals(4, userGroupCheck.getAuthorities().size());
	}
	
	@Test
	public void testAssignRolesSingleRoleNullGroupOrRole() {
		UserAuthority newAuth = new UserAuthority(null, "CAN_ADD_SOMETHING");
		Set<UserAuthority> newUserAuths = new HashSet<>();
		newUserAuths.add(newAuth);
		try {
			userGroupDAO.assignRoles(null, newUserAuths);
			fail("Should throw NullPointerException");
		} catch(NullPointerException npe) {
			
		}
		
		try {
			newAuth = null;
			userGroupDAO.assignRoles(new UserGroup(), newAuth);
			fail("Should throw NullPointerException");
		} catch(NullPointerException npe) {
			
		}
	}
	
	@Test
	public void testLoadUserGroupByName() {
		UserGroup userGroup = userGroupDAO.loadUserGroupByName("Group 1");
		assertNotNull(userGroup);
		assertEquals("Group 1", userGroup.getName());
		assertEquals("This is group 1", userGroup.getDescription());
		
		assertNull(userGroupDAO.loadUserGroupByName("Group 5"));
	}
	
	@Test
	public void testRemoveUserAuthority() {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				UserGroup userGroup = userGroupDAO.loadEntityModel(1l);
				UserAuthority newAuth = new UserAuthority(userGroup, "CAN_ADD_SOMETHING");
				userGroupDAO.assignRoles(userGroup, newAuth);
				userGroupDAO.removeUserAuthority(newAuth);
			}
		});
	}
	
	@Test
	public void testAppendOrderToExample() {
		assertEquals(" ORDER by obj.name", ((UserGroupDaoJpaImpl)userGroupDAO).appendOrderToExample(new UserGroup()));
	}

}
