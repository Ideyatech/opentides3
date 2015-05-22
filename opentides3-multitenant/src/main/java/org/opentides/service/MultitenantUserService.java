/*
 * This source code is property of Ideyatech,Inc.
 * All rights reserved. 
 * 
 * MultitenantUserService.java
 * May 20, 2015
 *
 */
package org.opentides.service;

import org.opentides.bean.user.MultitenantUser;
import org.opentides.bean.user.Tenant;

/**
 * @author Jeric
 *
 */
public interface MultitenantUserService extends
		BaseCrudService<MultitenantUser> {

	/**
	 * 
	 * @param user
	 */
	public void persistUserToTenantDb(final Tenant tenant,
			final MultitenantUser owner);
}
