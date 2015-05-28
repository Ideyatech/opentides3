/*
 * This source code is property of Ideyatech,Inc.
 * All rights reserved. 
 * 
 * TenantParameterLoginUrlAuthenticationEntryPoint.java
 * May 19, 2015
 *
 */
package org.opentides.web.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.opentides.util.MultitenancyUtil;
import org.opentides.util.StringUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/**
 * This entry point ensures that request parameters are not removed/lost
 * whenever the URL for the login page is appended to the current URL.
 * 
 * @author Jeric
 *
 */
public class TenantParameterLoginUrlAuthenticationEntryPoint extends
		LoginUrlAuthenticationEntryPoint {

	public TenantParameterLoginUrlAuthenticationEntryPoint(
			final String loginFormUrl) {
		super(loginFormUrl);
		setUseForward(true);
	}

	private static final Logger _log = Logger
			.getLogger(TenantParameterLoginUrlAuthenticationEntryPoint.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.web.authentication.
	 * LoginUrlAuthenticationEntryPoint
	 * #determineUrlToUseForThisRequest(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse,
	 * org.springframework.security.core.AuthenticationException)
	 */
	@Override
	protected String determineUrlToUseForThisRequest(
			final HttpServletRequest request,
			final HttpServletResponse response,
			final AuthenticationException exception) {
		final String login = super.determineUrlToUseForThisRequest(request,
				response, exception);
		_log.debug("Login form URL is " + login);

		StringBuffer url = new StringBuffer(request.getRequestURI().substring(
				request.getContextPath().length()));
		_log.debug("Request URI is " + url);

		if (url.lastIndexOf(login) < 0) {
			url = new StringBuffer(login);

			final String params = appendTenantName(login);
			_log.debug("Appending query string " + params);
			url.append(params);
		}

		_log.debug("Login URL will be " + url.toString());
		return url.toString();
	}

	protected String appendTenantName(final String login) {
		final StringBuilder params = new StringBuilder();
		if (!StringUtil.isEmpty(MultitenancyUtil.getTenantName())) {
			final char separator = login.lastIndexOf("?") < 1 ? '?' : '&';
			params.append(separator);
			params.append("a=");
			params.append(MultitenancyUtil.getTenantName());
		}
		
		return params.toString();
	}
}
