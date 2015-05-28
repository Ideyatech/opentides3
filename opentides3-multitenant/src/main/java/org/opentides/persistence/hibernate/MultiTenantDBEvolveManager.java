/*
 * This source code is property of Ideyatech,Inc.
 * All rights reserved. 
 * 
 * MultiTenantDBEvolveManager.java
 * May 11, 2015
 *
 */
package org.opentides.persistence.hibernate;

import org.apache.log4j.Logger;
import org.opentides.persistence.evolve.DBEvolveManager;
import org.opentides.persistence.jdbc.MultitenantJdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Autowired
	protected MultitenantJdbcTemplate jdbcTemplate;

	/**
	 * Evolves the given schema using the same evolve list of the master schema.
	 * 
	 * @param schema
	 */
	@Transactional
	public void evolve(final String schemaName) {
		Assert.notNull(schemaName);

		final String originatingSchema = jdbcTemplate.getCurrentSchemaName();

		_log.debug("Evolving schema [" + schemaName + "]");
		jdbcTemplate.switchSchema(schemaName);
		super.evolve();
		jdbcTemplate.switchSchema(originatingSchema);
	}

}
