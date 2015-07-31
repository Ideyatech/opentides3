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

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.opentides.util.DatabaseUtil;
import org.opentides.util.DateUtil;
import org.opentides.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Utilities for performing multi-tenancy operations.
 * 
 * @author allantan
 *
 */
@Service("multiTenantSchemaUpdate")
public class MultiTenantSchemaUpdate {

	private static final Logger _log = Logger
			.getLogger(MultiTenantSchemaUpdate.class);

	private ConnectionProvider connectionProvider;

	@Autowired
	private PersistenceScanner persistenceScanner;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private MultiTenantDBEvolveManager multiTenantDBEvolveManager;

	@Value("${jpa.log_ddl.directory}")
	private String ddlLogs = "/var/log/ss_ddl/";

	@Value("${jpa.script_ddl.latest}")
	private Resource ddlScript;

	@Value("${jpa.script_evolve.latest}")
	private Resource evolveScript;

	@Value("${jpa.log_ddl}")
	private Boolean logDdl = false;

	/**
	 * Persistence name in hibernate.
	 */
	@Value("${persistence.name}")
	private final String persistenceUnitName = "opentidesPU";

	/**
	 * Creates or updates the schema for the given tenantId. Invoke this method
	 * only when a separate schema is needed for the tenant.
	 * 
	 * @param tenantId
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws HibernateException
	 */
	@Transactional
	public boolean schemaEvolve(final String schema) {
		Assert.notNull(schema);
		_log.info("Performing schema update for schema = " + schema);
		try {
			final Connection connection = connectionProvider.getConnection();
			connection.createStatement().execute(
					"CREATE SCHEMA IF NOT EXISTS " + schema + ";");
			connection.createStatement().execute("USE " + schema + ";");

			// Code below is specific to hibernate
			final Configuration cfg = new Configuration();
			for (final String clazz : DatabaseUtil.getClasses()) {
				try {
					cfg.addAnnotatedClass(Class.forName(clazz));
				} catch (final ClassNotFoundException e) {
					_log.error("Class not found for schema upate [" + clazz
							+ "]", e);
				}
			}
			// add classes from packagesToScan
			for (final String clazz : persistenceScanner.scanPackages()) {
				try {
					cfg.addAnnotatedClass(Class.forName(clazz));
				} catch (final ClassNotFoundException e) {
					_log.error("Class not found for schema upate [" + clazz
							+ "]", e);
				}
			}
			cfg.configure();

			// is this a new schema?
			if (connection.createStatement()
					.executeQuery("SHOW TABLES LIKE 'SYSTEM_CODES'").next() == false) {
				// new schema, let's build it
				initializeSchema(cfg, connection, schema);
				// evolve it
				evolveSchema(connection, schema);
			}
			return true;
		} catch (final HibernateException e) {
			_log.error("Failed to update schema for [" + schema + "].", e);
			return false;
		} catch (final SQLException e) {
			_log.error("Failed to update schema for [" + schema + "].", e);
			return false;
		}
	}

	/**
	 * This is the helper function that initializes the schema and tables.
	 * Initialization is as follows: (1) Get the latest initialization sql
	 * script. Execute the sql script. (2) If there is no initialization script,
	 * use the hibernate SchemaExport.
	 * 
	 * @param tenantId
	 */
	@Transactional
	private void initializeSchema(final Configuration cfg,
			final Connection connection, final String schema) {
		// check if there SQL file under the sslScript folder
		boolean initialized = false;

		if (ddlScript != null && ddlScript.exists()) {
			_log.info("Initializing schema [" + schema + "] using DDL script ["
					+ ddlScript.getFilename() + "].");
			initialized = executeResource(connection, ddlScript);
		}

		if (!initialized) {
			_log.info("Initializing schema [" + schema
					+ "] using SchemaExport. ");
			final SchemaExport export = new SchemaExport(cfg, connection);
			if (logDdl) {
				final String dir = ddlLogs + "/"
						+ DateUtil.convertShortDate(new Date());
				_log.info("DDL logs can be found in " + dir + "/schema-"
						+ schema + ".sql");
				FileUtil.createDirectory(dir);
				export.setOutputFile(dir + "/schema-" + schema + ".sql");
				export.setDelimiter(";");
			}

			export.execute(logDdl, true, false, true);
		}
	}

	/**
	 * 
	 * @param connection
	 * @param schema
	 */
	private void evolveSchema(final Connection connection, final String schema) {
		boolean evolved = false;
		if (evolveScript != null && evolveScript.exists()) {
			_log.info("Evolving schema [" + schema + "] using evolve script ["
					+ evolveScript.getFilename() + "].");
			evolved = executeResource(connection, evolveScript);
		}

		if (!evolved) {
			_log.info("Evolving schema [" + schema + "] using DB evolve. ");
			multiTenantDBEvolveManager.evolve(schema);
		}
	}

	/**
	 * 
	 * @param connection
	 * @param resource
	 * @return
	 */
	private boolean executeResource(final Connection connection,
			final Resource resource) {
		InputStream inputStream = null;
		try {
			inputStream = resource.getInputStream();
			final Scanner f = new Scanner(inputStream);
			final StringBuilder stmt = new StringBuilder();
			while (f.hasNext()) {
				final String line = f.nextLine();
				// ignore comment
				if (line.startsWith("--")) {
					continue;
				}
				stmt.append(" ").append(line);
				if (line.endsWith(";")) {
					// end of statement, execute then clear
					connection.createStatement().execute(stmt.toString());
					_log.info(stmt.toString());
					stmt.setLength(0);
				}
			}
			f.close();
			return true;
		} catch (final SQLException e) {
			_log.error("Failed to execute sql script for initialization", e);
		} catch (final IOException e) {
			_log.error("Failed to read sql script for initialization", e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (final IOException e) {
				}
			}
		}

		return false;
	}

	/**
	 * This is a post construct that set ups the connection provider.
	 * 
	 * @throws Exception
	 */
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
