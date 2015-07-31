/*
   Licensed to the Apache Software Foundation (ASF) under one
   or more contributor license agreements.  See the NOTICE file
   distributed with this work for additional information
   regarding copyright ownership.  The ASF licenses this file
   to you under the Apache License, Version 2.0 (the
   "License"); you may not use this file except in compliance
   with the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing,
   software distributed under the License is distributed on an
   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
   KIND, either express or implied.  See the License for the
   specific language governing permissions and limitations
   under the License.    
 */

package org.opentides.persistence.hibernate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.opentides.util.MultitenancyUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

/**
 * For supporting multi-tenancy applications.
 * 
 * Simple implementation using a single connection pool used to serve multiple
 * schemas using "connection altering". This approach assumes one application
 * server connects to only one database server and connection pool is shared
 * across multiple tenants.
 * 
 * @author allantan
 *
 */
public class MultiTenantConnectionProviderImpl implements
		MultiTenantConnectionProvider, InitializingBean {

	private static final long serialVersionUID = 5727736453474895433L;

	private static final Logger _log = Logger
			.getLogger(MultiTenantConnectionProviderImpl.class);

	private ConnectionProvider connectionProvider = null;

	@Autowired
	private DataSource dataSource;

	@Value("${database.default_schema}")
	private String defaultSchema;

	@Override
	public Connection getAnyConnection() throws SQLException {
		return connectionProvider.getConnection();
	}

	@Override
	public void releaseAnyConnection(final Connection connection) throws SQLException {
		connectionProvider.closeConnection(connection);
	}

	@Override
	public Connection getConnection(final String tenantId) throws SQLException {
		final Connection connection = getAnyConnection();
		MultitenancyUtil.switchSchema(tenantId, connection);
		return connection;
	}

	@Override
	public void releaseConnection(final String tenantIdentifier, final Connection connection)
			throws SQLException {
		try {
			connection.createStatement().execute("USE " + defaultSchema);
		} catch (final SQLException e) {
			// on error, throw an exception to make sure the connection is not
			// returned to the pool.
			throw new HibernateException(
					"Could not alter JDBC connection to specified schema ["
							+ tenantIdentifier + "]", e);
		}

		connectionProvider.closeConnection(connection);
	}

	/**
	 * @param dataSource
	 *            the dataSource to set
	 */
	public void setDataSource(final DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean isUnwrappableAs(final Class unwrapType) {
		return connectionProvider.isUnwrappableAs(unwrapType);
	}

	@Override
	public <T> T unwrap(final Class<T> unwrapType) {
		return connectionProvider.unwrap(unwrapType);
	}

	@Override
	public boolean supportsAggressiveRelease() {
		return connectionProvider.supportsAggressiveRelease();
	}

	/**
	 * This is a post construct that set ups the connection provider.
	 * 
	 * @throws Exception
	 */
	@Override
	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		if (dataSource != null) {
			final DatasourceConnectionProviderImpl ds = new DatasourceConnectionProviderImpl();
			final Map<String, String> config = new HashMap<String, String>();
			ds.setDataSource(dataSource);
			ds.configure(config);
			connectionProvider = ds;
		}

		Assert.notNull(connectionProvider, this.getClass().getSimpleName()
				+ " does not have a datasource for the database connection."
				+ " Please check your configuration.");
	}
}