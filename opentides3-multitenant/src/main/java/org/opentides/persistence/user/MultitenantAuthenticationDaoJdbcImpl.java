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
package org.opentides.persistence.user;

import java.util.Map;

import org.apache.log4j.Logger;
import org.opentides.persistence.jdbc.MultitenantJdbcTemplate;
import org.opentides.util.MultitenancyUtil;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 * @author Jeric
 *
 */
public class MultitenantAuthenticationDaoJdbcImpl extends
		AuthenticationDaoJdbcImpl {

	private static final Logger _log = Logger
			.getLogger(MultitenantAuthenticationDaoJdbcImpl.class);

	protected static String loadSchemaNameByTenantQuery = "select t._SCHEMA AS 'SCHEMA' from TENANT t where t.company = ?";

	@Autowired
	protected MultitenantJdbcTemplate jdbcTemplate;

	/**
	 * 
	 */
	public MultitenantAuthenticationDaoJdbcImpl() {
		loadUserByUsernameQuery = "select U.USERID ID, FIRSTNAME, LASTNAME, EMAIL, P.LASTLOGIN LASTLOGIN, P.OFFICE OFFICE, P.SCHEMA_NAME SCHEMA_NAME, P.TENANT_NAME TENANT_NAME "
				+ "from USER_PROFILE P inner join USERS U on P.ID=U.USERID where U.USERNAME=?";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opentides.persistence.user.AuthenticationDaoJdbcImpl#preAuthentication
	 * ()
	 */
	@Override
	protected void preAuthentication() {
		final String tenant = MultitenancyUtil.getTenantName();
		_log.info("Tenant set in the URL is [" + tenant + "]");
		if (!StringUtil.isEmpty(tenant)) {
			try {
				// The list of tenants is set in the "master" database for
				// multi-tenant applications and this contains the schema name
				// linked to each tenant.
				// From the tenant passed in the URL, this list will be used to
				// retrieve the schema name that we should authenticate against.
				final Map<String, Object> tenantList = getJdbcTemplate()
						.queryForMap(
								loadSchemaNameByTenantQuery.replace("?", "'"
										+ tenant + "'"));

				final String schema = ((String) tenantList.get("SCHEMA"))
						.toLowerCase();
				_log.info("Altering connection to schema [" + schema + "]");
				getJdbcTemplate().execute("USE " + schema);
				MultitenancyUtil.setSchemaName(schema);
			} catch (final EmptyResultDataAccessException e) {
				_log.warn("Tenant " + tenant
						+ " not found. Using default master schema.");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opentides.persistence.user.AuthenticationDaoJdbcImpl#preApplicationEvent
	 * ()
	 */
	@Override
	protected void preAuthenticationEvent() {
		// We will need to change the schema so that the authentication event
		// activities will use the tenant schema
		final String schema = MultitenancyUtil.getSchemaName();
		_log.debug("Schema taken from thread local is " + schema);
		if (!StringUtil.isEmpty(schema)) {
			jdbcTemplate.switchSchema(schema);
		}
	}

	/**
	 * @param loadSchemaNameByTenantQuery
	 *            the loadSchemaNameByTenantQuery to set
	 */
	public static void setLoadSchemaNameByTenantQuery(
			final String loadSchemaNameByTenantQuery) {
		MultitenantAuthenticationDaoJdbcImpl.loadSchemaNameByTenantQuery = loadSchemaNameByTenantQuery;
	}

}
