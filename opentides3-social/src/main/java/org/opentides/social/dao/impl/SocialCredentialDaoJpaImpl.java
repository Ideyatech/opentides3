package org.opentides.social.dao.impl;

import org.opentides.dao.impl.BaseEntityDaoJpaImpl;
import org.opentides.social.bean.SocialCredential;
import org.opentides.social.dao.SocialCredentialDao;
import org.springframework.stereotype.Repository;

/**
 * @author rabanes
 */
@Repository(SocialCredentialDaoJpaImpl.NAME)
public class SocialCredentialDaoJpaImpl extends BaseEntityDaoJpaImpl<SocialCredential, Long> implements SocialCredentialDao {
	
	public static final String NAME = "socialCredentialDao";
	
}
