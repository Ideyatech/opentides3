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

import org.opentides.bean.user.Tenant;
import org.opentides.dao.TenantDao;
import org.opentides.persistence.hibernate.MultiTenantSchemaUpdate;
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
	public boolean createTenantSchema(final Tenant tenant) {
		// create the schema
		final String schema = (tenant == null) ? "" : tenant.getSchema();
		multiTenantSchemaUpdate.schemaEvolve(schema);
		return true;
	}

	@Override
	public boolean deleteTenantSchema(final Tenant tenant, final boolean createBackup) {
		// TODO Auto-generated method stub
		return false;
	}
}
