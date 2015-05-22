/*
 * This source code is property of Ideyatech,Inc.
 * All rights reserved. 
 * 
 * MultitenantDao.java
 * May 20, 2015
 *
 */
package org.opentides.dao;

import org.opentides.bean.user.MultitenantUser;
import org.opentides.bean.user.Tenant;

/**
 * @author Jeric
 *
 */
public interface MultitenantUserDao extends BaseEntityDao<MultitenantUser, Long> {
	/**
	 * 
	 * @param user
	 */
	public void persistUserToTenantDb(final Tenant tenant,
			final MultitenantUser owner);
}
