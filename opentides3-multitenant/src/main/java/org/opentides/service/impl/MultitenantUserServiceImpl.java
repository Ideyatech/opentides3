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
import org.opentides.util.CrudUtil;
import org.springframework.stereotype.Service;

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
		// create a copy of the user and persist to the tenant db
		final MultitenantUser copy = (MultitenantUser) CrudUtil.clone(owner);
		((MultitenantUserDao) getDao()).persistUserToTenantDb(tenant, copy);
	}
}
