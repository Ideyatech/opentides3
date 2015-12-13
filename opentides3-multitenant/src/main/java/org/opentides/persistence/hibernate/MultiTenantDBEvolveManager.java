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
	@Transactional()
	public void evolve(final String schemaName) {
		Assert.notNull(schemaName);
		final String originatingSchema = jdbcTemplate.getCurrentSchemaName();
		_log.debug("Evolving schema [" + schemaName + "]");
		jdbcTemplate.switchSchema(schemaName);
		super.evolve();
		jdbcTemplate.switchSchema(originatingSchema);
	}

}
