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

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.hibernate.engine.jdbc.internal.Formatter;
import org.hibernate.tool.hbm2ddl.DatabaseMetadata;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdateScript;
import org.opentides.util.DatabaseUtil;
import org.opentides.util.DateUtil;
import org.opentides.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
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

	@Autowired
	private PersistenceScanner persistenceScanner;

	@Autowired
	private MultiTenantDBEvolveManager multiTenantDBEvolveManager;

	@Value("${jpa.log_ddl.directory}")
	private String ddlLogs = "/var/log/ss_ddl/";

	@Value("${jpa.log_ddl}")
	private Boolean logDdl = false;
	
	private Connection defaultConnection;
	
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
		Connection connection = null;
		DatasourceConnectionProviderImpl connectionProvider = null;
		try {
			
			if (defaultConnection==null) {
				final DataSource dataSource =  new DriverManagerDataSource(DatabaseUtil.getUrl(), 
						DatabaseUtil.getUsername(), DatabaseUtil.getPassword());
				final Map<String, String> config = new HashMap<String, String>();
				DatasourceConnectionProviderImpl dcp = new DatasourceConnectionProviderImpl();
				dcp.setDataSource(dataSource);
				dcp.configure(config);
				defaultConnection = dcp.getConnection();				
			}
			
			defaultConnection.createStatement().execute(
					"CREATE SCHEMA IF NOT EXISTS " + schema + ";");

			final DataSource dataSource =  new DriverManagerDataSource("jdbc:mysql://localhost/"+schema, 
					DatabaseUtil.getUsername(), DatabaseUtil.getPassword());
			final Map<String, String> config = new HashMap<String, String>();
			connectionProvider = new DatasourceConnectionProviderImpl();
			connectionProvider.setDataSource(dataSource);
			connectionProvider.configure(config);
			connection = connectionProvider.getConnection();
			
			// Code below is specific to hibernate
			final Configuration cfg = new Configuration();
			for (final String clazz : DatabaseUtil.getClasses()) {
				try {
					cfg.addAnnotatedClass(Class.forName(clazz));
				} catch (final ClassNotFoundException e) {
					_log.error("Class not found for schema update [" + clazz
							+ "]", e);
				}
			}
			// add classes from packagesToScan
			for (final String clazz : persistenceScanner.scanPackages()) {
				try {
					cfg.addAnnotatedClass(Class.forName(clazz));
				} catch (final ClassNotFoundException e) {
					_log.error("Class not found for schema update [" + clazz
							+ "]", e);
				}
			}
			cfg.configure();

			// is this a new schema?
			if (connection.createStatement()
					.executeQuery("SHOW TABLES LIKE 'SYSTEM_CODES'").next() == false) {
				// new schema, let's build it
				initializeSchema(cfg, connection, schema);
			} else {
				// old schema, let's evolve it
				updateSchema(cfg, connection, schema);
			}
			return true;
		} catch (final HibernateException e) {
			_log.error("Failed to update schema for [" + schema + "].", e);
			return false;
		} catch (final SQLException e) {
			_log.error("Failed to update schema for [" + schema + "].", e);
			return false;
		} finally {
			try {
				if( connection != null ) {
					connection.close();
				}
			} catch(Exception e) {
				
			}	
		}
	}

	/**
	 * This is the helper function that initializes the schema and tables
	 * using hibernate SchemaExport.
	 * 
	 * @param tenantId
	 */
	@Transactional
	private void initializeSchema(final Configuration cfg,
			final Connection connection, final String schema) {
		_log.info("Initializing schema [" + schema
				+ "] using SchemaExport. ");
		final SchemaExport export = new SchemaExport(cfg, connection);
		if (logDdl) {
			final String dir = ddlLogs + schema;
			final String file= dir + "/create-" 
					+ DateUtil.convertShortDate(new Date()) + ".sql";
			_log.info("DDL logs can be found in " + file);
			FileUtil.createDirectory(dir);
			export.setOutputFile(file);
			export.setDelimiter(";");
		}
		export.execute(logDdl, true, false, true);
	}
	
	/**
	 * This is the helper function that updates the schema and tables
	 * using Hibernate SchemaUpdate.
	 * 
	 * @param tenantId
	 */
	@Transactional
	private void updateSchema(final Configuration cfg, final Connection connection, final String schema) {
		_log.info("Updating schema [" + schema + "]. ");
		final Dialect dialect = Dialect.getDialect( cfg.getProperties() );
		final Formatter formatter = FormatStyle.DDL.getFormatter();
		
		Statement stmt = null;
		Writer outputFileWriter = null;
		
		if (logDdl) {
			final String dir = ddlLogs + schema;
			final String file= dir + "/evolve-" 
					+ DateUtil.convertShortDate(new Date()) + ".sql";
			_log.info("DDL logs can be found in " + file);
			FileUtil.createDirectory(dir);
			try {
				outputFileWriter = new FileWriter(file);
			} catch (IOException e) {
				_log.error(e,e);
			}
		}
		
		try {			
			DatabaseMetadata meta = new DatabaseMetadata(connection, dialect, cfg );
			stmt = connection.createStatement();

			List<SchemaUpdateScript> scripts = cfg.generateSchemaUpdateScriptList( dialect, meta );
			for ( SchemaUpdateScript script : scripts ) {
				String formatted = formatter.format( script.getScript() );
				try {
					formatted += ";";
					stmt.executeUpdate( formatted );						
					if ( logDdl != null ) {
						outputFileWriter.write( formatted + "\n" );
					}
				} catch ( SQLException e ) {
					_log.error("Error in schema update using statement:" + formatted, e);
				}
			}
		} catch ( Exception e ) {
			_log.error("Error in schema update.", e);
		} finally {
			try {
				if( outputFileWriter != null ) {
					outputFileWriter.close();
				}
			} catch(Exception e) {
			}
		}
	}
}
