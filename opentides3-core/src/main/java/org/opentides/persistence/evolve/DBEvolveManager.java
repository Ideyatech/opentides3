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

import javax.annotation.PostConstruct;

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
	private List<DBEvolve> evolveList;

	@Autowired
	private SequenceDao sequenceDao;

	@Autowired
	private UserService userService;

	@Autowired
	private UserGroupService userGroupService;
	
	private Sequence currentVersion;
	
	private Long latestVersion;
	
	private static final Logger _log = Logger.getLogger(DBEvolveManager.class);
	
	@Transactional
	public void evolve() {
		
		if (currentVersion==null) {
			// no version available yet, lets create one
			currentVersion = new Sequence("DB_VERSION", 0l);
			sequenceDao.saveEntityModel(currentVersion);
			// initialize default admin user
			userGroupService.setupAdminGroup();
			userService.setupAdminUser();
		}

		// skip evolve if there is nothing in the evolve list
		if (evolveList.isEmpty()) {
			_log.info("No evolve scripts found.");
			return;
		}

		// get number of latest evolve script
		long currVersion   = currentVersion.getValue();
		
		if (currVersion>=latestVersion) {
			_log.info("Database is already at version " + currVersion);
			return;
		} else {
			_log.info("Updating database from version " + currVersion +" to version " + latestVersion );
		}
		
		// execute new evolve scripts
		for (DBEvolve evolve:evolveList) {
			if (evolve.getVersion() > currVersion) {
				// let's execute this evolve script
				_log.info("Executing evolve version ["+evolve.getVersion()+"] - "+evolve.getDescription());
				evolve.execute();
				// if successful, update current db version
				currentVersion.setValue(Long.valueOf(evolve.getVersion()));
				sequenceDao.saveEntityModel(currentVersion);
				_log.info("Evolve version  ["+evolve.getVersion()+"] successful.");
			}
		}
		// as precaution let's update db version again
		currentVersion.setValue(Long.valueOf(latestVersion));
		sequenceDao.saveEntityModel(currentVersion);
		_log.info("Database is now updated to version "+currentVersion.getValue());
	}

	/**
	 * This is a post construct that set ups the version numbers.
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		
		// get current db version
		try {
		    currentVersion = sequenceDao.loadSequenceByKey("DB_VERSION");
		} catch (Exception nre) {
			currentVersion = null;
		} 
		
		// get the latest db evolve version
		if (evolveList.size() > 0) {
			// sort the evolve list
			Collections.sort(evolveList, new VersionComparator());
			latestVersion = new Long (evolveList.get(evolveList.size()-1).getVersion());			
		} else {
			latestVersion = 0l;
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
	}
	
	/**
	 * @param evolveList the evolveList to set
	 */
	public void setEvolveList(List<DBEvolve> evolveList) {
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

	/**
	 * @return the currentVersion
	 */
	public final Sequence getCurrentVersion() {
		return currentVersion;
	}

	/**
	 * @return the latestVersion
	 */
	public final Long getLatestVersion() {
		return latestVersion;
	}
	
}