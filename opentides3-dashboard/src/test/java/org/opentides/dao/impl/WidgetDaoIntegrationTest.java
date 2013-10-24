package org.opentides.dao.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.opentides.bean.Widget;
import org.opentides.bean.user.BaseUser;
import org.opentides.dao.UserDao;
import org.opentides.dao.WidgetDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

public class WidgetDaoIntegrationTest extends BaseDaoTest {
	
	@Autowired
	private WidgetDao widgetDao;
	
	@Autowired
	private UserDao userDao;

	@Test
	@Transactional
	public void testFindDefaultWidget() {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				BaseUser user = userDao.loadEntityModel(1l);
				List<Widget> actual = widgetDao.findDefaultWidget(user);
				assertNotNull(actual);
				assertEquals(2, actual.size());
				Widget widget1 = widgetDao.loadEntityModel(1l);
				Widget widget2 = widgetDao.loadEntityModel(3l);
				List<Widget> expected = Arrays.asList(widget1, widget2);
				assertEquals(expected, actual);
		
			}
		});
	}

	@Test
	public void testFindWidgetWithAccessCode() {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				List<Widget> actual = widgetDao.findWidgetWithAccessCode(Arrays.asList("ACCESS_CODE_1", "ACCESS_CODE_2"));
				assertNotNull(actual);
				assertEquals(2, actual.size());
				Widget widget1 = widgetDao.loadEntityModel(1l);
				Widget widget2 = widgetDao.loadEntityModel(2l);
				List<Widget> expected = Arrays.asList(widget1, widget2);
				assertEquals(expected, actual);
		
			}
		});
	}

}
