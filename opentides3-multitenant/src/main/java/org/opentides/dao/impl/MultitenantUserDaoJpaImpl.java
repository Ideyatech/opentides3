/*
 * This source code is property of Ideyatech,Inc.
 * All rights reserved. 
 * 
 * MultitenantDaoJpaImpl.java
 * May 20, 2015
 *
 */
package org.opentides.dao.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.opentides.bean.user.MultitenantUser;
import org.opentides.bean.user.UserGroup;
import org.opentides.dao.MultitenantUserDao;
import org.opentides.service.TenantService;
import org.opentides.service.UserGroupService;
import org.opentides.util.MultitenancyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Jeric
 *
 */
@Repository("multitenantUserDao")
public class MultitenantUserDaoJpaImpl extends
		BaseEntityDaoJpaImpl<MultitenantUser, Long> implements
		MultitenantUserDao {

	private static final Logger _log = Logger
			.getLogger(MultitenantUserDaoJpaImpl.class);

	@Autowired
	private UserGroupService userGroupService;

	@Autowired
	private TenantService tenantService;
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opentides.dao.MultitenantDao#persistUserToTenantDb(org.opentides.
	 * bean.user.MultitenantUser)
	 */
	@Override
	public void persistUserToTenantDb(final String schema,
			final MultitenantUser owner) {

		final Session session = getEntityManager().unwrap(Session.class);
		final String originatingSchema = session.getTenantIdentifier();

		_log.debug("Switching to tenant schema " + schema);
		// switch to the schema of the tenant and save the owner
		MultitenancyUtil.switchSchema(schema, session);

		_log.info("Creating owner on tenant schema " + schema);
		saveEntityModel(owner);

		if (owner.getGroups() == null || owner.getGroups().isEmpty()) {
			final UserGroup userGroup = userGroupService
					.loadUserGroupByName("Administrator");
			owner.addGroup(userGroup);
			_log.info("Adding Administrator user group");
			saveEntityModel(owner);
		}

		_log.debug("Switching back to originating schema " + originatingSchema);
		// switch the connection back to the original schema and continue with
		// the operation
		MultitenancyUtil.switchSchema(originatingSchema, session);
	}

}
