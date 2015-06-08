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
package org.opentides.listener;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.opentides.persistence.jdbc.MultitenantJdbcTemplate;
import org.opentides.util.StringUtil;
import org.opentides.util.TenantContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

/**
 * @author Jeric
 *
 */
public class MultitenantLogoutListener extends LogoutListener {

	private static final Logger _log = Logger
			.getLogger(MultitenantLogoutListener.class);

	@Autowired
	private MultitenantJdbcTemplate jdbcTemplate;

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
		super.onLogoutSuccess(req, res, auth);

		// make sure that the tenant context is cleared
		_log.debug("Clearing tenant context.");
		TenantContextHolder.clearContext();
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
		if (!StringUtil.isEmpty(TenantContextHolder.getTenantName())) {
			url += "&a=" + TenantContextHolder.getTenantName();
			_log.debug("Appended tenant name to logout URL " + url);
		}

		return url;
	}
}
