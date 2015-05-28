/*
 * This source code is property of Ideyatech,Inc.
 * All rights reserved. 
 * 
 * MultitenantJdbcTemplate.java
 * May 27, 2015
 *
 */
package org.opentides.persistence.jdbc;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

/**
 * @author Jeric
 *
 */
public class MultitenantJdbcTemplate extends JdbcTemplate {
	private static final Logger _log = Logger
			.getLogger(MultitenantJdbcTemplate.class);

	/**
	 * Switch the schema to the specified schema name
	 * 
	 * @param schema
	 */
	public void switchSchema(final String schema) {
		Assert.notNull(schema);

		execute("USE " + schema);
	}

	/**
	 * Returns the schema name
	 * 
	 * @return
	 */
	public String getCurrentSchemaName() {
		try {
			final DatabaseMetaData metaData = getDataSource().getConnection()
					.getMetaData();

			// TODO make this db agnostic; see
			// http://alvinalexander.com/java/jdbc-connection-string-mysql-postgresql-sqlserver
			// for possible URLs
			final String url = metaData.getURL();
			final String schema = extractSchema(url);

			_log.debug("Current schema name is " + schema);
			return schema;

		} catch (final SQLException e) {
			_log.error("Cannot get schema name.", e);
			throw new HibernateException("Cannot get current schema name.", e);
		}
	}

	/**
	 * Extracts the schema name from the database URL. 
	 * @param url
	 * @return
	 */
	protected String extractSchema(final String url) {
		// see
		// http://alvinalexander.com/java/jdbc-connection-string-mysql-postgresql-sqlserver
		// for a list of supported URLs

		// try for SQL server
		int idx = url.lastIndexOf("DatabaseName=");
		if (idx < 0) {
			// not found, try for other dbs
			idx = url.lastIndexOf("/");
		}

		// move the index to start in the schema name
		idx += 1;

		final Matcher matcher = Pattern.compile("(\\w+)\\W*").matcher(
				url.substring(idx));
		if (matcher.find()) {
			return matcher.group(1);
		}

		return null;
	}
}
