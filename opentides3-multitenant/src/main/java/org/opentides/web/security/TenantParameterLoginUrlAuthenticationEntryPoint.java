/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 *******************************************************************************/
package org.opentides.web.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.opentides.util.StringUtil;
import org.opentides.util.TenantContextHolder;
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
		if (!StringUtil.isEmpty(TenantContextHolder.getTenantName())) {
			final char separator = login.lastIndexOf("?") < 1 ? '?' : '&';
			params.append(separator);
			params.append("a=");
			params.append(TenantContextHolder.getTenantName());
		}
		
		return params.toString();
	}
}
