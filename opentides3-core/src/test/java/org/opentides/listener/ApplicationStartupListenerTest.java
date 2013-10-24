package org.opentides.listener;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.opentides.persistence.evolve.DBEvolveManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

public class ApplicationStartupListenerTest {
	
	@InjectMocks
	private ApplicationStartupListener listener = new ApplicationStartupListener();
	
	@Mock
	private DBEvolveManager evolveManager;
	
	@Mock
	private ApplicationContext applicationContext;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testContextInitialized() {
		listener.contextInitialized(new ContextRefreshedEvent(applicationContext));
		verify(evolveManager).evolve();
	}
	
	@Test
	public void testOnApplicationEvent() {
		listener.onApplicationEvent(new ContextRefreshedEvent(applicationContext));
		verify(evolveManager, times(1)).evolve();
		listener.onApplicationEvent(new ContextRefreshedEvent(applicationContext));
		verify(evolveManager, times(1)).evolve();
		assertTrue(ApplicationStartupListener.isApplicationStarted());
	}
	
	
}
