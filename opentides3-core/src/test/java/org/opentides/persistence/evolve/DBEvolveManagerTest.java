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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.opentides.bean.Sequence;
import org.opentides.dao.SequenceDao;
import org.opentides.exception.InvalidImplementationException;
import org.opentides.service.UserGroupService;
import org.opentides.service.UserService;

/**
 * Unit test for {@link DBEvolveManager}. This uses mocks to verify that the flow of DBEvolveManager
 * is correct. 
 * 
 * @author gino
 *
 */
public class DBEvolveManagerTest {
	
	@Mock
	private SequenceDao sequenceDao;
	
	@Mock
	private UserService userService;
	
	@Mock
	private UserGroupService userGroupService;
	
	@InjectMocks
	private DBEvolveManager manager = new DBEvolveManager();
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testEvolveFirstRunOfDbEvolve() {
		when(sequenceDao.loadSequenceByKey("DB_VERSION")).thenReturn(null);
		
		manager.setEvolveList(getDbEvolveList());
		manager.evolve();
		
		/*
		 * saveEntityModel should be called 4 times.
		 * 1 for Creation of new Sequence for DB Version
		 * 2 for the 2 DB Evolves
		 * 1 for the precaution 
		 */
		verify(sequenceDao, times(4)).saveEntityModel(any(Sequence.class));
		
		//Verify that setup of admin group and user methods were invoked
		verify(userGroupService).setupAdminGroup();
		verify(userService).setupAdminUser();
	}
	
	@Test
	public void testEvolveDbAlreadyEvolvedAtLeastOnce() {
		Sequence dbVersion = new Sequence();
		dbVersion.setKey("DB_VERSION");
		dbVersion.setValue(1l);
		
		when(sequenceDao.loadSequenceByKey("DB_VERSION")).thenReturn(dbVersion);
		
		manager.setEvolveList(getDbEvolveList());
		manager.evolve();
		
		/*
		 * saveEntityModel should be called 2 times.
		 * 1 for DBEvolve number 2
		 * 1 for the precaution 
		 */
		verify(sequenceDao, times(2)).saveEntityModel(any(Sequence.class));
		
		//Verify that setup of admin group and user methods were NOT invoked since this is not the initial run
		verify(userGroupService, never()).setupAdminGroup();
		verify(userService, never()).setupAdminUser();
	}
	
	@Test
	public void testEvolveNoEvolveList() {
		Sequence dbVersion = new Sequence();
		dbVersion.setKey("DB_VERSION");
		dbVersion.setValue(1l);
		
		when(sequenceDao.loadSequenceByKey("DB_VERSION")).thenReturn(dbVersion);
		
		manager.setEvolveList(new ArrayList<Evolver>());
		manager.evolve();
		
		/*
		 * saveEntityModel should never be called
		 */
		verify(sequenceDao, never()).saveEntityModel(any(Sequence.class));
		
		//Verify that setup of admin group and user methods were NOT invoked since this is not the initial run
		verify(userGroupService, never()).setupAdminGroup();
		verify(userService, never()).setupAdminUser();
	}
	
	@Test(expected = InvalidImplementationException.class)
	public void testEvolveDuplicateEvolve() {
		Sequence dbVersion = new Sequence();
		dbVersion.setKey("DB_VERSION");
		dbVersion.setValue(1l);
		
		manager.setEvolveList(getDuplicateDbEvolveList());
		manager.evolve();
		
	}
	
	private List<Evolver> getDbEvolveList() {
		List<Evolver> dbEvolveList = new ArrayList<>();
		dbEvolveList.add(new DBEvolve001());
		dbEvolveList.add(new DBEvolve002());
		return dbEvolveList;
	}
	
	private List<Evolver> getDuplicateDbEvolveList() {
		List<Evolver> dbEvolveList = new ArrayList<>();
		dbEvolveList.add(new DBEvolve001());
		dbEvolveList.add(new DBEvolve001());
		dbEvolveList.add(new DBEvolve002());
		return dbEvolveList;
	}
	
}
