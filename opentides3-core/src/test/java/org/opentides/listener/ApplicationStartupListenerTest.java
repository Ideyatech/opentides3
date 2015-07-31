package org.opentides.listener;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.opentides.listener.command.DBEvolveCommand;
import org.opentides.listener.command.StartupCommand;
import org.opentides.persistence.evolve.DBEvolveManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

public class ApplicationStartupListenerTest {
	
	@InjectMocks
	private final ApplicationStartupListener listener = new ApplicationStartupListener();
	
	@Mock
	private DBEvolveManager evolveManager;
	
	@Mock
	private ApplicationContext applicationContext;
	
	@InjectMocks
	private final DBEvolveCommand dbEvolveCommand = new DBEvolveCommand();

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		List<StartupCommand> startupCommand = new ArrayList<StartupCommand>();
		startupCommand.add(dbEvolveCommand);
		listener.setStartupCommand(startupCommand);
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
