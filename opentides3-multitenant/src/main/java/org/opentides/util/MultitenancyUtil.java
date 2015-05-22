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
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

/**
 * @author Jeric
 *
 */
public class MultitenancyUtil {

	private static final Logger _log = Logger.getLogger(MultitenancyUtil.class);

	private static ThreadLocal<String> tenantName = new ThreadLocal<String>();

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
	 * @param tenantId
	 * @param connection
	 */
	public static void switchSchema(final String tenantId,
			final Connection connection) {
		try {
			_log.debug("Altering connection to schema [" + tenantId + "]");
			connection.createStatement().execute("USE " + tenantId);
		} catch (final SQLException e) {
			throw new HibernateException(
					"Could not alter JDBC connection to specified schema ["
							+ tenantId + "]", e);
		}
	}

	/**
	 * 
	 * @param tenantId
	 * @param connection
	 */
	public static void switchSchema(final String tenantId, final Session session) {
		session.doWork(new MultitenancyUtil.MultitenancySchemaSwitchWork(
				tenantId));
	}

	/**
	 * 
	 * @author Jeric
	 *
	 */
	protected static class MultitenancySchemaSwitchWork implements Work {
		private String schema;

		/**
		 * 
		 */
		public MultitenancySchemaSwitchWork(final String schema) {
			this.schema = schema;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.hibernate.jdbc.Work#execute(java.sql.Connection)
		 */
		@Override
		public void execute(final Connection connection) throws SQLException {
			switchSchema(schema, connection);
		}

	}
}
