package org.opentides.service.impl;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.opentides.bean.Event;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = {"classpath:applicationContext-notification-test.xml"})
public class NotificationServiceImplTest {
	
	@InjectMocks
	private NotificationService notificationService = new NotificationServiceImpl();
		
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testBuildMessage() {
		Event event = new Event();
		event.setMessageCode("message.test");
		String msg = notificationService.buildMessage(event, new Object[] {"one","two","three", 4});
		Assert.assertEquals("Displaying one to two of three 4", msg);
	}
}
