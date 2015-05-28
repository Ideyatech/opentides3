/*
 * This source code is property of Ideyatech,Inc.
 * All rights reserved. 
 * 
 * MultitenantUserServiceImpl.java
 * May 20, 2015
 *
 */
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

	@Override
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
			jdbcTemplate.switchSchema(schema);
			final UserGroup userGroup = userGroupService
					.loadUserGroupByName("Administrator");
			if (userGroup != null) {
				userCopy.addGroup(userGroup);
				_log.info("Adding Administrator user group");
			}
		}

		((MultitenantUserDao) getDao()).persistUserToTenantDb(schema, userCopy);

		// disable the copy in the master db so the owner won't be able to log
		// in there
		owner.getCredential().setEnabled(Boolean.FALSE);
	}
}
