/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 *******************************************************************************/
package org.opentides.service.impl;

import org.apache.log4j.Logger;
import org.opentides.bean.user.MultitenantUser;
import org.opentides.bean.user.Tenant;
import org.opentides.bean.user.UserGroup;
import org.opentides.dao.MultitenantUserDao;
import org.opentides.persistence.jdbc.MultitenantJdbcTemplate;
import org.opentides.service.MultitenantUserService;
import org.opentides.service.UserGroupService;
import org.opentides.util.CrudUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * @author Jeric
 *
 */
@Service("multitenantUserService")
public class MultitenantUserServiceImpl extends
		BaseCrudServiceImpl<MultitenantUser> implements MultitenantUserService {

	private static final Logger _log = Logger
			.getLogger(MultitenantUserServiceImpl.class);

	@Autowired
	protected UserGroupService userGroupService;

	@Autowired
	protected MultitenantJdbcTemplate jdbcTemplate;

	/**
	 * This should be done in a new transaction since the older transaction may
	 * not be aware yet of the new schema that will be created, and thus may
	 * cause errors because of older fast index
	 * 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void persistUserToTenantDb(final Tenant tenant,
			final MultitenantUser owner) {
		Assert.notNull(owner);
		Assert.notNull(tenant);

		final String schema = tenant.getSchema();
		final String tenantName = tenant.getCompany();

		// create a copy of the user since we are persisting this to the tenant
		// db
		final MultitenantUser userCopy = (MultitenantUser) CrudUtil
				.clone(owner);
		userCopy.setTenant(null);
		userCopy.setSchemaName(schema);
		userCopy.setTenantName(tenantName);

		if (userCopy.getGroups() == null || userCopy.getGroups().isEmpty()) {
			_log.debug("Switching to tenant schema " + schema);
			jdbcTemplate.switchSchema(schema);

			final UserGroup userGroup = userGroupService
					.loadUserGroupByName("Administrator");
			if (userGroup != null) {
				userCopy.addGroup(userGroup);
				_log.info("Adding Administrator user group");
			}
		}

		((MultitenantUserDao) getDao()).persistUserToTenantDb(schema, userCopy);
	}
}
