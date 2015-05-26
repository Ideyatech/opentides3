/*
 * This source code is property of Ideyatech,Inc.
 * All rights reserved. 
 * 
 * PreFormLoginFilter.java
 * May 26, 2015
 *
 */
package org.opentides.web.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.web.filter.GenericFilterBean;

/**
 * This filter is inserted before the {@literal FORM_LOGIN_FILTER} in the filter
 * chain. By default, it does nothing and just passes through the next filter in
 * the chain. However, this can be subclassed so that any pre authentication
 * activities can be made by the application.
 * 
 * @author Jeric
 *
 */
public class PreFormLoginFilter extends GenericFilterBean {

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(final ServletRequest paramServletRequest,
			final ServletResponse paramServletResponse, final FilterChain paramFilterChain)
			throws IOException, ServletException {
		paramFilterChain.doFilter(paramServletRequest, paramServletResponse);
	}

}
