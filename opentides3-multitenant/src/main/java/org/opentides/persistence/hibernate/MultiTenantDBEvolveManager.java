/*
 * This source code is property of Ideyatech,Inc.
 * All rights reserved. 
 * 
 * MultiTenantDBEvolveManager.java
 * May 11, 2015
 *
 */
package org.opentides.persistence.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.opentides.persistence.evolve.DBEvolveManager;
import org.opentides.util.MultitenancyUtil;
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
		final String originatingSchema = session.getTenantIdentifier();
		MultitenancyUtil.switchSchema(schemaName, session);
		super.evolve();
		MultitenancyUtil.switchSchema(originatingSchema, session);
	}

}
