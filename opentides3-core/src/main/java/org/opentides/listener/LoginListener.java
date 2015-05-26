package org.opentides.listener;

import org.apache.log4j.Logger;
import org.opentides.persistence.user.AuthenticationDaoJdbcImpl;
import org.opentides.service.UserService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

/**
 * Listener for tracking user login.
 * 
 * @author allantan
 * 
 * @deprecated The authentication activities done in this listener was
 *             incorporated to {@link AuthenticationDaoJdbcImpl}
 *             {@code onApplicationEvent()} stub instead. This should not be
 *             used anymore to avoid any problems for multi-tenant applications.
 */
@Deprecated
public class LoginListener implements ApplicationListener<AuthenticationSuccessEvent> {
	private static final Logger _log = Logger.getLogger(LoginListener.class);

	protected UserService userService;

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(final AuthenticationSuccessEvent event) {
		_log.debug("Updating login for " + event.getAuthentication().getName());
		userService.updateLogin(event);
	}
	/**
	 * @param userService the userService to set
	 */
	public final void setUserService(final UserService userService) {
		this.userService = userService;
	}
}
