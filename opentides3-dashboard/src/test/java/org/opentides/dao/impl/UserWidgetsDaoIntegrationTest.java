package org.opentides.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.opentides.bean.UserWidgets;
import org.opentides.dao.UserWidgetsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

public class UserWidgetsDaoIntegrationTest extends BaseDaoTest {
	
	@Autowired
	private UserWidgetsDao userWidgetsDao;

	@Test
	public void testFindUserWidgets() {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				List<UserWidgets> actual = userWidgetsDao.findUserWidgets(1, 1, 2);
				assertNotNull(actual);
				assertFalse(actual.isEmpty());
				UserWidgets uw1 = userWidgetsDao.loadEntityModel(1l);
				UserWidgets uw2 = userWidgetsDao.loadEntityModel(2l);
				List<UserWidgets> expected = Arrays.asList(uw1, uw2);
				assertEquals(expected, actual);
			}
		});
	}
	
	@Test
	public void testFindUserWidgetsWithoutStatus() {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				List<UserWidgets> actual = userWidgetsDao.findUserWidgets(1);
				assertNotNull(actual);
				assertFalse(actual.isEmpty());
				UserWidgets uw1 = userWidgetsDao.loadEntityModel(1l);
				UserWidgets uw2 = userWidgetsDao.loadEntityModel(2l);
				UserWidgets uw3 = userWidgetsDao.loadEntityModel(3l);
				List<UserWidgets> expected = Arrays.asList(uw1, uw3, uw2);
				assertEquals(expected, actual);
			}
		});
	}

	@Test
	public void testCountUserWidgetsColumn() {
		assertEquals(2l, userWidgetsDao.countUserWidgetsColumn(1, 1));
		assertEquals(1l, userWidgetsDao.countUserWidgetsColumn(2, 1));
		assertEquals(0l, userWidgetsDao.countUserWidgetsColumn(3, 1));
	}

	@Test
	public void testDeleteUserWidget() {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				userWidgetsDao.deleteUserWidget(1l, 1l);
				assertNull(userWidgetsDao.loadEntityModel(1l));
			}
		});
	}

}
