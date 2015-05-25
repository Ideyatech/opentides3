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

import org.apache.log4j.Logger;

/**
 * This utility allows creation of Hibernate session directly.
 * Used for logging purposes.
 *
 * @author allantan
 */
public class DatabaseUtil {
	
	private static final Logger _log = Logger.getLogger(DatabaseUtil.class);

    /**
     * Local entity manager to manage database sessions.
     */
    private static EntityManager entityManager;

    /**
     * Persistence name in hibernate.
     */
    private static String persistenceUnitName = "opentidesPU";
    
    /**
     * Persistence file
     */
    private static String persistenceFile = "META-INF/persistence.xml";
    
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
     * PropertiesMap used to initialize EntityManagerFactory.
     */
    private static Properties propertiesMap;
    
    /**
     * List of classes handled by JPA/Hibernate.
     */
    private static List<String> classes;
    
    /**
     * Entity Manager Factory
     */
    private static EntityManagerFactory emf;

	/**
	 * Hide the constructor.
	 */
	private DatabaseUtil() {		
	}
	
    /**
     * Static initializer to establish database connection.
     */
    private static void initialize() {
        try { 
        	if (emf == null || !emf.isOpen()) {
            	propertiesMap = XMLPersistenceUtil.getProperties(persistenceFile, persistenceUnitName);        	
            	classes = XMLPersistenceUtil.getClasses(persistenceFile, persistenceUnitName);
            	if (StringUtil.isEmpty(jndiName)) {
    	            propertiesMap.put("javax.persistence.jdbc.driver", driverClassName);
            		propertiesMap.put("javax.persistence.jdbc.url", url);
            		propertiesMap.put("javax.persistence.jdbc.user", username);
            		propertiesMap.put("javax.persistence.jdbc.password", password);
            	} else {
            		_log.debug("Connecting to JNDI [" + jndiName + "]");
//				For Eclipselink only
            		propertiesMap.put("eclipselink.session.customizer", 
            				"org.opentides.persistence.config.JPAEclipseLinkSessionCustomizer");
            		propertiesMap.put("javax.persistence.nonJtaDataSource",jndiName);
              }
            	emf = Persistence.createEntityManagerFactory(persistenceUnitName, propertiesMap);
        	}
			entityManager = emf.createEntityManager();
        } catch (final Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            _log.error("Initial EntityManager creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }	
    }

    public static EntityManager getEntityManager() {
    	if (entityManager==null || !entityManager.isOpen()) {
			DatabaseUtil.initialize();
		}
        return entityManager;
    }
	
	/**
	 * @param driverClass the driverClass to set
	 */
	public final void setDriverClassName(final String driverClass) {
		_log.info("Setting Driver Class Name to " + driverClass);
		DatabaseUtil.driverClassName = driverClass;
	}

	/**
	 * @param url the url to set
	 */
	public final void setUrl(final String url) {
		_log.info("Setting URL to " + url);
		DatabaseUtil.url = url;
	}

	/**
	 * @param username the username to set
	 */
	public final void setUsername(final String username) {
		_log.info("Setting Username to " + username);
		DatabaseUtil.username = username;
	}

	/**
	 * @param password the password to set
	 */
	public final void setPassword(final String password) {
		_log.info("Setting password to " + password);
		DatabaseUtil.password = password;
	}
	
	/**
	 * When jndiName is set, other database settings 
	 * (e.g. driverClassName, url, username and password)
	 * are ignored.
	 * 
	 * @param jndiName
	 */
	
	public final void setJndiName(final String jndiName) {
		_log.info("Setting JNDI Name to " + jndiName);
		DatabaseUtil.jndiName = jndiName;
	}
	
	/**
	 * Setter method for persistenceUnitName.
	 *
	 * @param persistenceUnitName the persistenceUnitName to set
	 */
	public final void setPersistenceUnitName(final String persistenceUnitName) {
		_log.info("Setting Persistence Unit Name to " + persistenceUnitName);
		DatabaseUtil.persistenceUnitName = persistenceUnitName;
	}
	
	/**
	 * 
	 * @param persistenceFile
	 */
	public final void setPersistenceFile(final String persistenceFile) {
		_log.info("Setting Persistence file to " + persistenceFile);
		DatabaseUtil.persistenceFile = persistenceFile;
	}

	/**
	 * @return the persistenceUnitName
	 */
	public static final String getPersistenceUnitName() {
		return persistenceUnitName;
	}

	/**
	 * @return the propertiesMap
	 */
	public static final Properties getPropertiesMap() {
    	if (entityManager == null || !entityManager.isOpen()) {
			DatabaseUtil.initialize();
		}		
		return propertiesMap;
	}
	
	/**
	 * @return the classes mapped in persistence.xml
	 */
	public static final List<String> getClasses() {
    	if (entityManager == null || !entityManager.isOpen()) {
			DatabaseUtil.initialize();
		}		
		return classes;
	}
	
}
