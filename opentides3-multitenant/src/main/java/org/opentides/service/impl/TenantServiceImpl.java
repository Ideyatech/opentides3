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
package org.opentides.service.impl;

import java.sql.SQLException;

import org.opentides.bean.user.MultitenantUser;
import org.opentides.bean.user.Tenant;
import org.opentides.dao.TenantDao;
import org.opentides.persistence.hibernate.MultiTenantSchemaUpdate;
import org.opentides.persistence.jdbc.MultitenantJdbcTemplate;
import org.opentides.service.MultitenantUserService;
import org.opentides.service.TenantService;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author allantan
 *
 */
@Service("tenantService")
public class TenantServiceImpl extends BaseCrudServiceImpl<Tenant> implements
		TenantService {

	@Value("${database.default_schema}")
	private String defaultSchema = "master";

	@Autowired
	private MultiTenantSchemaUpdate multiTenantSchemaUpdate;

	@Autowired
	private MultitenantUserService multitenantUserService;
	
	@Autowired
	private MultitenantJdbcTemplate jdbcTemplate;

	@Override
	public String findUniqueSchemaName(final String company) {
		final String schema = company.replaceAll("[^a-zA-Z]", "");
		final StringBuffer uniqueSchema = new StringBuffer(defaultSchema + "_"
				+ schema);
		Tenant t = ((TenantDao) getDao()).loadBySchema(uniqueSchema.toString());

		while (t != null) {
			uniqueSchema.append(StringUtil.generateRandomString(3));
			t = ((TenantDao) getDao()).loadBySchema(uniqueSchema.toString());
		}

		return uniqueSchema.toString();
	}
	

	@Override
	public Tenant findByName(String company) {
		return ((TenantDao) getDao()).findByName(company);	
	}

	@Override
	public void createTenantSchema(final Tenant tenant,
			final MultitenantUser owner) {

		final String company = tenant.getCompany();
		final String schema = findUniqueSchemaName(company);

		tenant.setSchema(schema);
		tenant.setDbVersion(1l);

		multiTenantSchemaUpdate.schemaEvolve(schema);
		multitenantUserService.persistUserToTenantDb(tenant, owner);
		
		// disable the copy in the master db so the owner won't be able to log
		// in there
		owner.getCredential().setEnabled(Boolean.FALSE);
	}

	@Override
	public boolean deleteTenantSchema(final Tenant tenant, final boolean createBackup) {
		throw new UnsupportedOperationException(
				"Deleting of tenant schema is not yet supported.");
	}

	@Override
	public String getTenantSchemaName(String tenantName) {
		return ((TenantDao) getDao()).getTenantSchemaName(tenantName);
	}

	@Override
	public void changeSchema(String schemaName) throws SQLException {
		if (!jdbcTemplate.getCurrentSchemaName().equalsIgnoreCase(schemaName)) {
			jdbcTemplate.switchSchema(schemaName);
		}

	}

}
