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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Utility responsible for managing the database evolves of different schemas
 * for multi-tenancy.
 * 
 * @author Jeric
 *
 */
public class MultiTenantDBEvolveManager extends DBEvolveManager {
	private static final Logger _log = Logger.getLogger(DBEvolveManager.class);
	
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Evolves the given schema using the same evolve list of the master schema.
	 * 
	 * @param schema
	 */
	@Transactional
	public void evolve(final String schemaName) {
		Assert.notNull(schemaName);
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
