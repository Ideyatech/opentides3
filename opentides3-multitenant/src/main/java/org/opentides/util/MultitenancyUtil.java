/*
 * This source code is property of Ideyatech,Inc.
 * All rights reserved. 
 * 
 * MultitenancyUtil.java
 * May 14, 2015
 *
 */
package org.opentides.util;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

/**
 * @author Jeric
 *
 */
public class MultitenancyUtil {

	private static final Logger _log = Logger.getLogger(MultitenancyUtil.class);

	/**
	 * This is used to store the tenant name of the account during log in
	 */
	private static ThreadLocal<String> tenantName = new InheritableThreadLocal<String>();

	/**
	 * This is used to store the schema name of the tenant. This will be
	 * populated by the user authentication service
	 */
	private static ThreadLocal<String> schemaName = new InheritableThreadLocal<String>();

	/**
	 * @return the tenantName
	 */
	public static String getTenantName() {
		return tenantName.get();
	}

	public static void setTenantName(final String tenant) {
		tenantName.set(tenant);
	}

	/**
	 * @param schemaName
	 *            the schemaName to set
	 */
	public static void setSchemaName(final String schema) {
		schemaName.set(schema);
	}

	/**
	 * @return the schemaName
	 */
	public static String getSchemaName() {
		return schemaName.get();
	}

	/**
	 * @param schema
	 * @param connection
	 */
	public static void switchSchema(final String schema,
			final Connection connection) {
		try {
			_log.debug("Altering connection to schema [" + schema + "]");
			connection.createStatement().execute("USE " + schema);
		} catch (final SQLException e) {
			throw new HibernateException(
					"Could not alter JDBC connection to specified schema ["
							+ schema + "]", e);
		}
	}
}
