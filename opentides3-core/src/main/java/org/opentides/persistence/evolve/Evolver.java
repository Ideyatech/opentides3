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
package org.opentides.persistence.evolve;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

/**
 * Base class for all evolve script to execute native SQL or JPQL queries.
 * 
 * @author allantan
 */
public class Evolver {
	// the entity manager
	@PersistenceContext
    protected EntityManager em;
	
	@Transactional
	public int executeJpqlUpdate(String query) {
		Query q = em.createQuery(query);
		return q.executeUpdate();
	}
	
	@Transactional
	public int executeSqlUpdate(String query) {
		Query q = em.createNativeQuery(query);
		return q.executeUpdate();
	}

	@Transactional(noRollbackFor=Exception.class)
	public int executeSqlUpdate(String query, boolean ignoreError) {
		try {
			Query q = em.createNativeQuery(query);
			return q.executeUpdate();
		} catch (Exception e) {
			if (!ignoreError)
				throw e;
			// ignore SQL errors
		}
		return 0;
	}

	/**
	 * Sets the EntityManager
	 * @param em
	 */
	public final void setEntityManager(EntityManager em) {
        this.em = em;
    }

	/**
	 * Returns the EntityManager for this evolve script.
	 */
	public final EntityManager getEntityManager() {
		return em;
	}
}
