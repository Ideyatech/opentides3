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

package org.opentides.listener;

import org.apache.log4j.Logger;
import org.opentides.persistence.evolve.DBEvolveManager;
import org.opentides.util.SecurityUtil;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * This class is configured to be executed during application startup. Checks to
 * ensure admin user is created in case of new installation. Also creates
 * application variables.
 * 
 * @author allanctan
 */

public class ApplicationStartupListener implements ApplicationListener {

	private static Logger _log = Logger
			.getLogger(ApplicationStartupListener.class);

	private static boolean applicationStarted = false;

	private Boolean debug = false;

	private String propertyName;

	private DBEvolveManager evolveManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.context.ContextLoaderListener#contextInitialized
	 * (javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ApplicationEvent event) {
		_log.info("Starting up system using " + propertyName + " properties.");

		_log.info("Initializing debug mode to " + debug);
		SecurityUtil.setDebug(debug);

		_log.info("Checking for schema evolve...");
		evolveManager.evolve();

	}

	/**
	 * @param debug
	 *            the debug to set
	 */
	public void setDebug(Boolean debug) {
		this.debug = debug;
	}

	/**
	 * Triggered when context is started. For unknown reason,
	 * ContextStartedEvent is not triggered properly. So, we are using
	 * ContextRefreshedEvent with a static indicator.
	 */
	public void onApplicationEvent(ApplicationEvent event) {
		if (!applicationStarted && (event instanceof ContextRefreshedEvent)) {
			contextInitialized(event);
			applicationStarted = true;
		}
	}

	/**
	 * @param evolveManager
	 *            the evolveManager to set
	 */
	public final void setEvolveManager(DBEvolveManager evolveManager) {
		this.evolveManager = evolveManager;
	}

	/**
	 * @param propertyName
	 *            the propertyName to set
	 */
	public final void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * Returns status of application startup.
	 *
	 * @return boolean
	 */
	public static boolean isApplicationStarted() {
		return applicationStarted;
	}

}
