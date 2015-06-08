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
