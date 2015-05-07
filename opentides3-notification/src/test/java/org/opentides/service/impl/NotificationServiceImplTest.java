package org.opentides.service.impl;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.opentides.bean.Event;
import org.springframework.context.MessageSource;

public class NotificationServiceImplTest {
	
	@InjectMocks
	private final NotificationServiceImpl notificationService = new NotificationServiceImpl();

	@Mock
	private MessageSource messageSource;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testBuildMessage() {
		String messageCode = "messages.test";
		Object[] params = new Object[] { "one", "two", "three" };

		Mockito.when(
				messageSource.getMessage(messageCode, params,
						Locale.getDefault())).thenReturn(
				"This is one for testing two. three.");

		Event event = new Event();
		event.setMessageCode(messageCode);
		String msg = notificationService.buildMessage(event, params);

		Assert.assertEquals("This is one for testing two. three.", msg);

		Mockito.verify(messageSource).getMessage(messageCode, params,
				Locale.getDefault());
	}
}
