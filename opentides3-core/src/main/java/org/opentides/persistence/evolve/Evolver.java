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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.opentides.bean.Sequence;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base class for all evolve script to execute native SQL or JPQL queries.
 * 
 * @author allantan
 */
public abstract class Evolver {
	// the entity manager
	@PersistenceContext
    protected EntityManager em;
	
	private static final Logger _log = Logger.getLogger(Evolver.class);

	
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
	 * Wrapper method to handle updating of version within the transaction.
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void doExecute() {
		_log.info("Executing evolve version ["+this.getVersion()+"] - "+this.getDescription());
		this.execute();
		List<Sequence> result = em.createQuery("from Sequence where key='DB_VERSION'").getResultList();
		Sequence dbVersion = null;		
		if (result.size() == 0) {
			// no version available yet, lets create one
			dbVersion = new Sequence(); 
			dbVersion.setKey("DB_VERSION"); 
		} else {
			// use existing record
			dbVersion = result.get(0);
		}
		dbVersion.setValue(new Long(this.getVersion())); 
		em.persist(dbVersion); 
		_log.info("Evolve version  ["+dbVersion.getValue()+"] successful.");		
		em.flush();
	}
	
	/**
	 * Actual database evolve script operations.
	 */
	
	public abstract void execute();
	
	/**
	 * Returns the description of evolve script.
	 * @return
	 */
	public abstract String getDescription();
	
	/**
	 * Returns the version of this evolve script.
	 * Ensures that execution of evolve script is in proper
	 * sequence.
	 * @return
	 */
	public abstract int getVersion();
	
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
