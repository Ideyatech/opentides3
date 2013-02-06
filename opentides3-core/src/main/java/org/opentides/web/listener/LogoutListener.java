package org.opentides.web.listener;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opentides.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * Listener for tracking user logout.
 * @author allantan
 */
public class LogoutListener extends AbstractAuthenticationTargetUrlRequestHandler implements LogoutSuccessHandler {
	private UserService userService;
	
	public LogoutListener() {
		super.setAlwaysUseDefaultTargetUrl(true);
	}

	/**
	 * @param userService the userService to set
	 */
	public final void setUserService(UserService userService) {
		this.userService = userService;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.logout.LogoutSuccessHandler#onLogoutSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
	 */
	@Override
	public void onLogoutSuccess(HttpServletRequest req,
			HttpServletResponse res, Authentication auth) throws IOException,
			ServletException {
		userService.updateLogout(auth);		
		super.handle(req, res, auth);
	}
}
