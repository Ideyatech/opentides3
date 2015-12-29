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

package org.opentides.service;

import java.sql.SQLException;

import org.opentides.bean.user.MultitenantUser;
import org.opentides.bean.user.Tenant;

/**
 * Interface for Tenant Service
 * 
 * @author allantan
 *
 */
public interface TenantService extends BaseCrudService<Tenant> {

	/**
	 * Looks for a unique schema name
	 */
	public String findUniqueSchemaName(String company);
	
	/**
	 * Finds the tenant for the given name.
	 * @param company
	 * @return
	 */
	public Tenant findByName(String company);
	
	/**
	 * Finds the tenant for the given schema.
	 * @param company
	 * @return
	 */
	public Tenant findBySchema(String schema);

	/**
	 * Creates the schema of the tenant using JPA database create.
	 */
	public void createTenantSchema(Tenant tenant, MultitenantUser owner);
	
	/**
	 * Clones the template to create new tenant. 
	 * This uses mysql command line statement, so it is required that mysql client
	 * is installed on the server.
	 * 
	 * @param template
	 * @param tenant
	 */
	public void cloneTenantSchema(Tenant template, Tenant tenant);

	/**
	 * Creates the schema of the tenant using JPA database create.
	 */
	public void createTemplateSchema(Tenant tenant);	

	/**
	 * Backups and drop the schema of the tenant.
	 */
	public boolean deleteTenantSchema(Tenant tenant, 
									  boolean createBackup);
	
	/**
	 * Get schema name of tenant
	 * @param tenantName
	 * @return
	 */
	public String getTenantSchemaName(String tenantName);
	
	/**
	 * Change schema
	 * @param schemaName
	 * @return
	 */
	public void changeSchema(String schemaName) throws SQLException;
}
