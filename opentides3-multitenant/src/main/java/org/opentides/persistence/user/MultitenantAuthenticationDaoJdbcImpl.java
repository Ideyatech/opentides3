/*
 * This source code is property of Ideyatech,Inc.
 * All rights reserved. 
 * 
 * MultitenantAuthenticationDaoJdbcImpl.java
 * May 19, 2015
 *
 */
package org.opentides.persistence.user;

import java.util.Map;

import org.apache.log4j.Logger;
import org.opentides.util.MultitenancyUtil;
import org.opentides.util.StringUtil;

/**
 * @author Jeric
 *
 */
public class MultitenantAuthenticationDaoJdbcImpl extends
		AuthenticationDaoJdbcImpl {

	private static final Logger _log = Logger
			.getLogger(MultitenantAuthenticationDaoJdbcImpl.class);

	private static String loadSchemaNameByTenantQuery = "select t._SCHEMA AS 'SCHEMA' from TENANT t where t.company = ?";

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
		_log.debug("Tenant set in the URL is [" + tenant + "]");
		if (!StringUtil.isEmpty(tenant)) {
			// The list of tenants is set in the "master" database for
			// multi-tenant applications and this contains the schema name
			// linked to each tenant.
			// From the tenant passed in the URL, this list will be used to
			// retrieve the schema name that we should authenticate against.
			final Map<String, Object> tenantList = getJdbcTemplate()
					.queryForMap(
							loadSchemaNameByTenantQuery.replace("?", "'"
									+ tenant + "'"));

			if (tenantList.containsKey("SCHEMA")) {
				final String schema = ((String) tenantList.get("SCHEMA"))
						.toLowerCase();
				_log.info("Altering connection to schema [" + schema + "]");
				getJdbcTemplate().execute("USE " + schema);
			} else {
				_log.warn("Tenant " + tenant
						+ " not found. Using default master schema.");
			}
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
