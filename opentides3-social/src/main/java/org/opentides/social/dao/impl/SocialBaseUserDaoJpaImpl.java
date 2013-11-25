package org.opentides.social.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opentides.dao.UserDao;
import org.opentides.dao.impl.BaseEntityDaoJpaImpl;
import org.opentides.social.bean.SocialBaseUser;
import org.opentides.social.dao.SocialBaseUserDao;
import org.opentides.social.enums.SocialMediaType;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author rabanes
 */
@Repository(SocialBaseUserDaoJpaImpl.NAME)
public class SocialBaseUserDaoJpaImpl extends BaseEntityDaoJpaImpl<SocialBaseUser, Long> implements SocialBaseUserDao {

	public static final String NAME = "socialBaseUserDao";

	@Autowired
	protected UserDao userDao;

	@Override
	public SocialBaseUser loadBySocialIdAndType(String socialId, SocialMediaType type) {
		if (StringUtil.isEmpty(socialId) || type == null)
			return null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("socialId", socialId);
		params.put("type", type);
		
		List<SocialBaseUser> results = this.findByNamedQuery("jpql.socialBaseUser.findBySocialIdAndType", params);
		if(results != null && !results.isEmpty())
			return results.get(0);
		else 
			return null;
	}
	
	protected UserDao getBaseUserDao() {
		return userDao;
	}
	
}
