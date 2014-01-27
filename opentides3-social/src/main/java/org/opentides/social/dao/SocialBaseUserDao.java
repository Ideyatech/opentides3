package org.opentides.social.dao;

import org.opentides.dao.BaseEntityDao;
import org.opentides.social.bean.SocialBaseUser;
import org.opentides.social.enums.SocialMediaType;

/**
 * DAO for Social BaseUser.
 * 
 * @author rabanes 
 */
public interface SocialBaseUserDao extends BaseEntityDao<SocialBaseUser, Long> {
	public SocialBaseUser loadBySocialIdAndType(String socialId, SocialMediaType type);
}
