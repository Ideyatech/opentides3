/*
 * This source code is property of Ideyatech,Inc.
 * All rights reserved. 
 * 
 * MultitenantURLAuthenticationFailureHandler.java
 * May 25, 2015
 *
 */
package org.opentides.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.opentides.util.MultitenancyUtil;
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
		final String tenant = MultitenancyUtil.getTenantName();
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
