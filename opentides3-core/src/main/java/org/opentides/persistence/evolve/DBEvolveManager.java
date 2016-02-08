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

import java.util.Collections;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.log4j.Logger;
import org.opentides.bean.Sequence;
import org.opentides.dao.SequenceDao;
import org.opentides.exception.InvalidImplementationException;
import org.opentides.service.UserGroupService;
import org.opentides.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author allantan
 *
 */
@Service("evolveManager")
public class DBEvolveManager {

	@Autowired(required=false)
	private List<Evolver> evolveList;

	@Autowired
	private Evolver adminEvolve;
	
	@Autowired
	private SequenceDao sequenceDao;

	@Autowired
	private UserService userService;

	@Autowired
	private UserGroupService userGroupService;
	
	private static final Logger _log = Logger.getLogger(DBEvolveManager.class);

	public void evolve() {
		this.evolve(null);
	}
	
	@Transactional
	public void evolve(String schemaName) {

		// get current db version
		Sequence version;
		try {
		    version = sequenceDao.loadSequenceByKey("DB_VERSION");
		} catch (NoResultException nre) {
		    version = null;
		}
		
		if (version==null) {
			// initialize default admin user
			adminEvolve.doExecute(schemaName);
		}

		// skip evolve if there is nothing in the evolve list
		if (evolveList.isEmpty()) {
			_log.info("No evolve scripts found.");
			return;
		}
		
		// sort the evolve list
		Collections.sort(evolveList, new VersionComparator());
		// check for duplicate version numbers
		for (int i=0; i<(evolveList.size()-1); i++) {
			if (evolveList.get(i).getVersion() == evolveList.get(i+1).getVersion()) {
				// we have a duplicate version... exit
				throw new InvalidImplementationException(
						"Duplicate version number ["+evolveList.get(i).getVersion() +
						"] detected on evolve script for "+evolveList.get(i).getClass().getName() + 
						" and " + evolveList.get(i+1).getClass().getName());
			}
		}
		
		long currVersion = 0;
		// get number of latest evolve script
		if (version != null)
			currVersion = version.getValue();

		long latestVersion = evolveList.get(evolveList.size()-1).getVersion();
		
		if (currVersion>=latestVersion) {
			_log.info("Database is already at version " + currVersion);
			return;
		} else {
			_log.info("Updating database from version " + currVersion +" to version " + latestVersion );
		}
		
		// execute new evolve scripts
		for (Evolver evolve:evolveList) {
			if (evolve.getVersion() > currVersion) {
				// let's execute this evolve script
				evolve.doExecute(schemaName);
			}
		}
		_log.info("Database is now updated to version "+latestVersion);
	}

	/**
	 * @param evolveList the evolveList to set
	 */
	public void setEvolveList(List<Evolver> evolveList) {
		this.evolveList = evolveList;
	}

	/**
	 * 
	 * @param sequenceDao
	 */
	public void setSequenceDao(SequenceDao sequenceDao) {
		this.sequenceDao = sequenceDao;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
}