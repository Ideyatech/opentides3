/*
 * This source code is property of Ideyatech,Inc.
 * All rights reserved. 
 * 
 * RequestParametersLoginUrlAuthenticationEntryPoint.java
 * May 19, 2015
 *
 */
package org.opentides.web.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
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
public class RequestParametersLoginUrlAuthenticationEntryPoint extends
		LoginUrlAuthenticationEntryPoint {

	public RequestParametersLoginUrlAuthenticationEntryPoint(
			final String loginFormUrl) {
		super(loginFormUrl);
		setUseForward(true);
	}

	private static final Logger _log = Logger
			.getLogger(RequestParametersLoginUrlAuthenticationEntryPoint.class);

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

		StringBuffer url = new StringBuffer(request.getRequestURI()
				.substring(request.getContextPath().length()));
		_log.debug("Request URI is " + url);
		
		if (url.lastIndexOf(login) < 0 ) {

			final int lastIndex = url.lastIndexOf("/");
			// this is to avoid double slashes when appending the login url
			// e.g. opentides3//login
			if ((lastIndex == url.length() - 1)
					&& (login.startsWith("/"))) {
				url.deleteCharAt(lastIndex);
			}

			url = new StringBuffer(login);
			if (!StringUtil.isEmpty(request.getQueryString())) {
				_log.debug("Appending query string "
						+ request.getQueryString());
				url.append(";").append(request.getQueryString());
			}
		}
		
		return url.toString();
	}

}
