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
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.opentides.util.MultitenancyUtil;
import org.opentides.util.StringUtil;

/**
 * A servlet filter that tries to determine the account/tenant name through URL
 * parsing. It follows the parsing order described below:
 * 
 * <pre>
 * (1) subdomain.domain.tld
 * (2) subdomain.domain.tld.cc
 * (3) {path}?a=account
 * </pre>
 * 
 * @author Jeric
 *
 */
public class MultitenantSessionFilter extends SessionFilter {

	/*
	 * This tries to extract the account/tenant name in the URL either from the
	 * subdomain or from the request parameters. The extracted value is then
	 * saved in the session and in the thread local object for use in schema
	 * switching.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opentides.web.security.PreFormLoginFilter#doFilter(javax.servlet.
	 * ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(final ServletRequest req, final ServletResponse res,
			final FilterChain chain) throws IOException, ServletException {
		logger.debug("Passing through session filter.");
		if (req instanceof HttpServletRequest) {
			logger.debug("Request is of type HttpServletRequest.");
			final HttpServletRequest request = (HttpServletRequest) req;
			final String tenant = extractTenantName(request);
			if (!StringUtil.isEmpty(tenant)) {
				logger.info("Tenant name extracted is [" + tenant + "]");

				request.getSession().setAttribute("account", tenant);
				logger.debug("Tenant name [" + tenant
						+ "] added to session.");

				MultitenancyUtil.setTenantName(tenant);
				logger.debug("Tenant name [" + tenant
						+ "] set in thread local.");
			}
		}

		chain.doFilter(req, res);
	}

	/**
	 * <p>
	 * This method will extract the account from the URL. This allows the
	 * following URL path:
	 * </p>
	 * 
	 * <pre>
	 * 	(1) subdomain.domain.tld
	 * 	(2) subdomain.domain.tld.cc
	 *  (3) {path}?a=account
	 * </pre>
	 * 
	 * @param req
	 * @return
	 */
	protected String extractTenantName(final ServletRequest req) {
		// check first for the domain
		final String hostname = req.getServerName();
		final String urlParts[] = hostname.split(Pattern.quote("."));
		if (urlParts.length == 3 || urlParts.length == 4) {
			final String subdomain = urlParts[0]; //
			if (!subdomain.equalsIgnoreCase("www")) {
				return subdomain;
			}
		}

		return req.getParameter("a");
	}
}
