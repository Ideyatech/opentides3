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

package org.opentides.dao;

import org.opentides.bean.user.Tenant;

/**
 * DAO for Tenant
 * 
 * @author allantan 
 */
public interface TenantDao extends BaseEntityDao<Tenant, Long> {
	
	/**
	 * Retrieves the tenant of the given schema.
	 * @param schema
	 * @return
	 */
	public Tenant loadBySchema(String schema);
	
	/**
	 * Get schema name of tenant
	 * @param tenantName
	 * @return
	 */
	public String getTenantSchemaName(String tenantName);
	
	/**
	 * Find tenant by company name.
	 * @param company
	 * @return
	 */
	public Tenant findByName(String company);
	
}
