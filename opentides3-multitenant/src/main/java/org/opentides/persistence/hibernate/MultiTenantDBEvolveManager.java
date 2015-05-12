/*
 * This source code is property of Ideyatech,Inc.
 * All rights reserved. 
 * 
 * MultiTenantDBEvolveManager.java
 * May 11, 2015
 *
 */
package org.opentides.persistence.hibernate;

import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.opentides.persistence.evolve.DBEvolveManager;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

/**
 * Utility responsible for managing the database evolves of different schemas
 * for multi-tenancy.
 * 
 * @author Jeric
 *
 */
public class MultiTenantDBEvolveManager extends DBEvolveManager {
	private static final Logger _log = Logger.getLogger(DBEvolveManager.class);
	
	@Value("${database.default_schema}")
	private String defaultSchema = "master";

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Evolves the given schema using the same evolve list of the master schema.
	 * 
	 * @param schema
	 */
	@Transactional
	public void evolve(String schema) {
		if (StringUtil.isEmpty(schema)) {
			schema = defaultSchema;
		} else if (!schema.startsWith(defaultSchema)) {
			schema = defaultSchema + "_" + schema;
		}

		final String schemaName = schema;
		_log.debug("Evolving schema [" + schemaName + "]");

		// Hibernate specific code
		final Session session = entityManager.unwrap(Session.class);
		session.doWork(new Work() {
			@Override
			public void execute(final Connection connection) throws SQLException {
				_log.debug("Using [" + schemaName + "] for database evolve.");
				// we need to switch the schema before running the evolve
				connection.createStatement().execute("USE " + schemaName + ";");
			}
		});

		super.evolve();
	}

}
