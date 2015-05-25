/*
 * This source code is property of Ideyatech,Inc.
 * All rights reserved. 
 * 
 * MultitenantLogoutListener.java
 * May 21, 2015
 *
 */
package org.opentides.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.opentides.bean.user.SessionUser;
import org.opentides.listener.LogoutListener;
import org.opentides.util.MultitenancyUtil;
import org.opentides.util.StringUtil;
import org.springframework.security.core.Authentication;

/**
 * @author Jeric
 *
 */
public class MultitenantLogoutListener extends LogoutListener {

	private static final Logger _log = Logger
			.getLogger(MultitenantLogoutListener.class);

	private SessionUser sessionUser;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opentides.listener.LogoutListener#onLogoutSuccess(javax.servlet.http
	 * .HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * org.springframework.security.core.Authentication)
	 */
	@Override
	public void onLogoutSuccess(final HttpServletRequest req,
			final HttpServletResponse res, final Authentication auth)
			throws IOException, ServletException {
		final Object principal = auth.getPrincipal();
		_log.debug("Getting authentication principal object.");
		if (principal instanceof SessionUser) {
			_log.debug("Authentication principal object is of type session user.");
			sessionUser = (SessionUser) principal;
			// We set the schema name to the thread local object so it can be
			// used by the IdentifierResolver for the schema switching in the
			// LogoutSuccess activities
			_log.debug("Setting schema name to thread local.");
			MultitenancyUtil.setSchemaName(sessionUser.getSchemaName());
		}

		super.onLogoutSuccess(req, res, auth);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.web.authentication.
	 * AbstractAuthenticationTargetUrlRequestHandler
	 * #determineTargetUrl(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected String determineTargetUrl(final HttpServletRequest request,
			final HttpServletResponse response) {
		String url = super.determineTargetUrl(request, response);
		_log.debug("Checking session user.");
		if (sessionUser != null
				&& !StringUtil.isEmpty(sessionUser.getTenantName())) {
			url += "&a=" + sessionUser.getTenantName();
			_log.debug("Appended tenant name to logout URL " + url);
		}

		return url;
	}
}
