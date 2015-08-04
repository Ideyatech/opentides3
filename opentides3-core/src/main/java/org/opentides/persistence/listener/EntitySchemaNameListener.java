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
package org.opentides.persistence.listener;

import javax.persistence.PrePersist;
import org.opentides.bean.BaseEntity;

import org.apache.log4j.Logger;
import org.opentides.util.TenantContextHolder;

/**
 * This listener is set the schema name of the object important for multitenancy.
 * 
 * @author jpereira
 * 
 */
public class EntitySchemaNameListener {

	private static final Logger _log = 
			Logger.getLogger(EntitySchemaNameListener.class);
	
	@PrePersist
	public void setDbName(final BaseEntity entity) {
		try {
			// set schema name
			if (entity.getDbName() == null) {
				entity.setDbName(TenantContextHolder.getSchemaName());
				
				_log.debug("Schema name is " + entity.getDbName());
			}
		} catch (Throwable e) {
			// Suppress error
			_log.error(e, e);
		}
	}

}