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

import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.metamodel.Metamodel;

import org.eclipse.persistence.internal.jpa.EntityManagerFactoryImpl;
import org.opentides.bean.factory.support.BaseEntityRegistry;

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
    private static String persistenceUnitName = "opentidesPU";
    
    /**
     * Registry of BaseEntity available in the application
     */
    private static BaseEntityRegistry baseEntityRegistry;
    
    /**
     * Database driver class name (e.g. com.mysql.jdbc.Driver)
     */
    private static String driverClassName;

    /**
     * Database connection url (e.g. jdbc:mysql://localhost/javatest)
     */
    private static String url;

    /**
     * Database connection username
     */
    private static String username;

    /**
     * Database connection password
     */
    private static String password;

    /**
     * Database connection jndi name.
     * Optional but if this has value, url, username and password is ignored.
     */
    private static String jndiName;
    
    /**
     * Entity Manager Fcctory
     */
    private static EntityManagerFactory emf;

    /**
     * Static initializer to establish database connection.
     */
    private static void initialize() {
        try { 
        	if (emf == null || !emf.isOpen()) {
            	Properties propertiesMap = XMLPersistenceUtil.getProperties("META-INF/persistence.xml", persistenceUnitName);        	
            	if (StringUtil.isEmpty(jndiName)) {
    	            propertiesMap.put("javax.persistence.jdbc.driver", driverClassName);
            		propertiesMap.put("javax.persistence.jdbc.url", url);
            		propertiesMap.put("javax.persistence.jdbc.user", username);
            		propertiesMap.put("javax.persistence.jdbc.password", password);
            	} else {
            		propertiesMap.put("hibernate.connection.datasource", jndiName);        	
            		propertiesMap.put("eclipselink.session.customizer", 
            				"org.opentides.persistence.config.JPAEclipseLinkSessionCustomizer");
            		propertiesMap.put("javax.persistence.nonJtaDataSource", jndiName);
               	}
            	emf = Persistence.createEntityManagerFactory(persistenceUnitName, propertiesMap);		
//            	if (emf instanceof EntityManagerFactoryImpl) {
//            		EntityManagerFactoryImpl emfi = (EntityManagerFactoryImpl) emf;
//            		
//            	}
        	}
//        	List<String> entities = baseEntityRegistry.getBaseEntities();
//        	for (String entity:entities)
//        		emf.getMetamodel().entity(Class.forName(entity));
        	entityManager = emf.createEntityManager();        	
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial EntityManager creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }	
    }

    public static EntityManager getEntityManager() {
    	if (entityManager==null || !entityManager.isOpen())
    		DatabaseUtil.initialize();
        return entityManager;
    }
	
	/**
	 * @param driverClass the driverClass to set
	 */
	public final void setDriverClassName(String driverClass) {
		DatabaseUtil.driverClassName = driverClass;
	}

	/**
	 * @param url the url to set
	 */
	public final void setUrl(String url) {
		DatabaseUtil.url = url;
	}

	/**
	 * @param username the username to set
	 */
	public final void setUsername(String username) {
		DatabaseUtil.username = username;
	}

	/**
	 * @param password the password to set
	 */
	public final void setPassword(String password) {
		DatabaseUtil.password = password;
	}
	
	/**
	 * When jndiName is set, other database settings 
	 * (e.g. driverClassName, url, username and password)
	 * are ignored.
	 * 
	 * @param jndiName
	 */
	
	public final void setJndiName(String jndiName) {
		DatabaseUtil.jndiName = jndiName;
	}
	
	/**
	 * Setter method for persistenceUnitName.
	 *
	 * @param persistenceUnitName the persistenceUnitName to set
	 */
	public final void setPersistenceUnitName(String persistenceUnitName) {
		DatabaseUtil.persistenceUnitName = persistenceUnitName;
	}

	/**
	 * @param baseEntityRegistry the baseEntityRegistry to set
	 */
	public final void setBaseEntityRegistry(
			BaseEntityRegistry baseEntityRegistry) {
		DatabaseUtil.baseEntityRegistry = baseEntityRegistry;
	}	
	
}
