package org.opentides.listener;

import org.opentides.service.UserService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

/**
 * Listener for tracking user login.
 * @author allantan
 */
public class LoginListener implements ApplicationListener<AuthenticationSuccessEvent> {
	private UserService userService;

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		userService.updateLogin(event);
	}
	/**
	 * @param userService the userService to set
	 */
	public final void setUserService(UserService userService) {
		this.userService = userService;
	}
}
