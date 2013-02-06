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

package org.opentides.util;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * This utility allows creation of Hibernate session directly.
 * Used for logging purposes.
 *
 * @author allantan
 */
public class DatabaseUtil {

    /**
     * Local entity manager to manage database sessions.
     */
    private static EntityManager entityManager;

    /**
     * Persistence name in hibernate.
     */
    private static String persistenceUnitName = "opentides";

    /**
     * Static initializer to establish database connection.
     */
    private static void initialize() {
        try { 
        	Properties propertiesMap = XMLPersistenceUtil.getProperties("META-INF/persistence.xml", persistenceUnitName);
        	EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName, propertiesMap);
        	entityManager = emf.createEntityManager();        	
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial EntityManager creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }	
    }

    public static EntityManager getEntityManager() {
    	if (entityManager==null)
    		DatabaseUtil.initialize();
        return entityManager;
    }
	
	/**
	 * Setter method for persistenceUnitName.
	 *
	 * @param persistenceUnitName the persistenceUnitName to set
	 */
	public static final void setPersistenceUnitName(String persistenceUnitName) {
		DatabaseUtil.persistenceUnitName = persistenceUnitName;
	}
}
