package org.opentides.dao.impl;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;
import org.opentides.bean.user.UserAuthority;
import org.opentides.bean.user.UserGroup;
import org.opentides.dao.UserGroupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;


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

}
