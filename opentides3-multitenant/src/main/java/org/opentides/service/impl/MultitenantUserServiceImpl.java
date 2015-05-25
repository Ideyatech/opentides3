/*
 * This source code is property of Ideyatech,Inc.
 * All rights reserved. 
 * 
 * MultitenantUserServiceImpl.java
 * May 20, 2015
 *
 */
package org.opentides.service.impl;

import org.opentides.bean.user.MultitenantUser;
import org.opentides.bean.user.Tenant;
import org.opentides.dao.MultitenantUserDao;
import org.opentides.service.MultitenantUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author Jeric
 *
 */
@Service("multitenantUserService")
public class MultitenantUserServiceImpl extends
		BaseCrudServiceImpl<MultitenantUser> implements MultitenantUserService {

	@Override
	public void persistUserToTenantDb(final Tenant tenant,
			final MultitenantUser owner) {
		Assert.notNull(owner);
		Assert.notNull(tenant);

		final String schema = tenant.getSchema();
		final String tenantName = tenant.getCompany();

		owner.setTenant(null);
		owner.setSchemaName(schema);
		owner.setTenantName(tenantName);

		((MultitenantUserDao) getDao()).persistUserToTenantDb(schema, owner);
	}
}
