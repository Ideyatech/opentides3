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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.opentides.persistence.jdbc.MultitenantJdbcTemplate;
import org.opentides.util.StringUtil;
import org.opentides.util.TenantContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 * A servlet filter that tries to determine the account/tenant name through URL
 * parsing. It follows the parsing order described below:
 * 
 * <pre>
 * (1) subdomain.domain.tld
 * (2) subdomain.domain.tld.cc
 * </pre>
 * 
 * @author Jeric
 *
 */
public class MultitenantContextFilter extends TenantContextFilter {

	protected static final Map<String, String> schemas = new HashMap<String, String>();

	protected static String loadSchemaNameByTenantQuery = "select t._SCHEMA AS 'SCHEMA' from TENANT t where t.company = ?";

	@Autowired
	protected MultitenantJdbcTemplate jdbcTemplate;

	@Value("${database.default_schema}")
	private String defaultSchema;

	/*
	 * This tries to extract the account/tenant name in the URL either from the
	 * subdomain or from the request parameters. The extracted value is then
	 * saved in the session and in the thread local object. Using this tenant
	 * name, the schema name is then searched in the master database for use in
	 * schema switching.
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

		logger.debug("Clearing tenant context.");
		TenantContextHolder.clearContext();

		final String tenant = extractTenantName(req);
		logger.debug("Tenant name extracted is [" + tenant + "]");

		if (StringUtil.isEmpty(tenant)) {
			logger.debug("Tenant name is empty. Using defaults.");
			chain.doFilter(req, res);

			return;
		}

		if (req instanceof HttpServletRequest) {
			logger.debug("Request is of type HttpServletRequest.");
			final HttpServletRequest request = (HttpServletRequest) req;
			final HttpSession session = request.getSession();

			final Object sessionObj = session.getAttribute("account");
			if (sessionObj == null
					|| !tenant.equalsIgnoreCase((String) sessionObj)) {
				storeContextInSession(tenant, session);
			}

			final String sessionTenant = (String) session
					.getAttribute("account");
			final String sessionSchema = (String) session
					.getAttribute("schema");

			TenantContextHolder.setTenantName(sessionTenant);
			TenantContextHolder.setSchemaName(sessionSchema);
		}

		chain.doFilter(req, res);
	}

	/**
	 * This method will store in session the schema and tenant name by looking
	 * either in cache or querying the database.
	 * 
	 * @param tenant
	 * @param session
	 */
	protected void storeContextInSession(String tenant, final HttpSession session) {
		// let us clear first the session attributes to be sure
		session.removeAttribute("account");
		session.removeAttribute("schema");

		logger.debug("Looking for schema name in cache for tenant [" + tenant
				+ "]");
		String schema = schemas.get(tenant);
		if (StringUtil.isEmpty(schema)) {
			logger.debug("Schema name for tenant [" + tenant
					+ "] not found in cache.");
			try {
				logger.debug("Quering for schema name for tenant [" + tenant
						+ "]");
				// The list of tenants is set in the "master" database
				// for multi-tenant applications and this contains the
				// schema name linked to each tenant.
				// From the tenant passed in the URL, this list will be
				// used to retrieve the schema name that we should query
				// against.
				final Map<String, Object> tenantList = jdbcTemplate
						.queryForMap(loadSchemaNameByTenantQuery
								.replace("?", "'" + tenant + "'"));

				schema = ((String) tenantList.get("SCHEMA")).toLowerCase();

				// cache the schema name
				synchronized (schema) {
					schemas.put(tenant, schema);
					logger.debug("Tenant name [" + tenant + "] added to cache.");
				}

			} catch (final EmptyResultDataAccessException e) {
				logger.warn("Tenant " + tenant
						+ " not found. Using default master schema.");
				// revert to defaults
				schema = defaultSchema;
				tenant = "";
			}
		}

		session.setAttribute("account", tenant);
		logger.debug("Tenant name [" + tenant + "] added to session.");
		session.setAttribute("schema", schema);
		logger.debug("Schema name [" + schema + "] added to session.");
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

	/**
	 * @param loadSchemaNameByTenantQuery
	 *            the loadSchemaNameByTenantQuery to set
	 */
	public static void setLoadSchemaNameByTenantQuery(
			final String loadSchemaNameByTenantQuery) {
		MultitenantContextFilter.loadSchemaNameByTenantQuery = loadSchemaNameByTenantQuery;
	}
}
