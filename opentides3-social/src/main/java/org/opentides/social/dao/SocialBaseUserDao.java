package org.opentides.social.dao;

import org.opentides.dao.UserDao;
import org.opentides.social.bean.SocialBaseUser;
import org.opentides.social.enums.SocialMediaType;

/**
 * DAO for Social BaseUser.
 * 
 * @author rabanes 
 */
public interface SocialBaseUserDao extends UserDao {
	public SocialBaseUser loadBySocialIdAndType(String socialId, SocialMediaType type);
}
