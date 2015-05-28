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
import org.opentides.persistence.jdbc.MultitenantJdbcTemplate;
import org.opentides.util.MultitenancyUtil;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

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
				MultitenancyUtil.setSchemaName(schema);
			} else {
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
