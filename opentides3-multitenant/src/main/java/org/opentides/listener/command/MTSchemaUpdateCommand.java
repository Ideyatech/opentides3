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

package org.opentides.listener.command;

import org.apache.log4j.Logger;
import org.opentides.persistence.hibernate.MultiTenantSchemaUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class is configured to be executed during application startup. 
 * Performs schema evolve for the master database.
 * 
 * @author allanctan
 */
@Component
public class MTSchemaUpdateCommand implements Command {

	private static final Logger _log = Logger
			.getLogger(MTSchemaUpdateCommand.class);

	@Autowired
	private MultiTenantSchemaUpdate multiTenantSchemaUpdate;
	
	@Override
	public void execute() {
		_log.info("Performance schema update for multitenant.");

		// update the master database
		multiTenantSchemaUpdate.schemaEvolve(null);
	}
}
