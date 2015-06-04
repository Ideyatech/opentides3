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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.opentides.util.StringUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 * @author Jeric
 *
 */
public class MultitenantURLAuthenticationFailureHandler extends
		SimpleUrlAuthenticationFailureHandler {
	
	private static final Logger _log = Logger
			.getLogger(MultitenantURLAuthenticationFailureHandler.class);

	protected String defaultFailureUrl;
	
	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler#onAuthenticationFailure(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.AuthenticationException)
	 */
	@Override
	public void onAuthenticationFailure(final HttpServletRequest request,
			final HttpServletResponse response, final AuthenticationException exception)
			throws IOException, ServletException {
		final StringBuffer url = new StringBuffer(defaultFailureUrl);
		final String tenant = (String) request.getSession().getAttribute(
				"account");
		_log.info("Tenant parameter is " + tenant);
		if(!StringUtil.isEmpty(tenant)) {
			final char seperator = url.lastIndexOf("?") == 0 ? '?' : '&';
			url.append(seperator);
			url.append("a=").append(tenant);

			_log.info("Setting login failure URL to " + url.toString());
		}

		super.setDefaultFailureUrl(url.toString());
		super.onAuthenticationFailure(request, response, exception);
	}
	
	/**
	 * @param defaultFailureUrl the defaultFailureUrl to set
	 */
	@Override
	public void setDefaultFailureUrl(final String defaultFailureUrl) {
		this.defaultFailureUrl = defaultFailureUrl;
	}

}
