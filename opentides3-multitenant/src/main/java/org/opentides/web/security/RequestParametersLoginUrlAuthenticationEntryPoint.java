/*
 * This source code is property of Ideyatech,Inc.
 * All rights reserved. 
 * 
 * RequestParametersLoginUrlAuthenticationEntryPoint.java
 * May 19, 2015
 *
 */
package org.opentides.web.security;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
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

		StringBuffer url = new StringBuffer(request.getRequestURI().substring(
				request.getContextPath().length()));
		_log.debug("Request URI is " + url);

		if (url.lastIndexOf(login) < 0) {
			url = new StringBuffer(login);

			final String params = appendRequestParameters(request);
			_log.debug("Appending query string " + params);
			url.append(params);
		}

		_log.debug("Login URL will be " + url.toString());
		return url.toString();
	}

	protected String appendRequestParameters(final HttpServletRequest request) {
		final StringBuilder params = new StringBuilder();
		final Enumeration<String> parameterNames = request.getParameterNames();
		if(parameterNames.hasMoreElements()) {
			params.append(";");
		}
		
		while (parameterNames.hasMoreElements()) {
			final String paramName = parameterNames.nextElement();
			params.append(paramName);
			params.append("=");
			final String paramValue = request.getParameter(paramName);
			params.append(paramValue);

			if (parameterNames.hasMoreElements()) {
				params.append("&");
			}
		}
		return params.toString();
	}
}
