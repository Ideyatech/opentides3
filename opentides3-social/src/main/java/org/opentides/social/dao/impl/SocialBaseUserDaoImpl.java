package org.opentides.social.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opentides.dao.impl.UserDaoJpaImpl;
import org.opentides.social.bean.SocialBaseUser;
import org.opentides.social.dao.SocialBaseUserDao;
import org.opentides.social.enums.SocialMediaType;
import org.opentides.util.StringUtil;
import org.springframework.stereotype.Repository;

/**
 * @author rabanes
 */
@Repository(SocialBaseUserDaoImpl.NAME)
public class SocialBaseUserDaoImpl extends UserDaoJpaImpl implements SocialBaseUserDao {

	public static final String NAME = "socialBaseUserDao";

	@SuppressWarnings("unused")
	private static Log _log = LogFactory.getLog(SocialBaseUserDaoImpl.class);

	@Override
	public SocialBaseUser loadBySocialIdAndType(String socialId, SocialMediaType type) {
		if (StringUtil.isEmpty(socialId) || type == null)
			return null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("socialId", socialId);
		params.put("type", type);
		
		List<SocialBaseUser> results = this.findSocialUsersByNamedQuery("jpql.socialBaseUser.findBySocialIdAndType", params);
		if(results != null && !results.isEmpty())
			return results.get(0);
		else 
			return null;
	}
	
	/**
	 * Overloaded function for retrieving SocialBaseUser.
	 */
	private List<SocialBaseUser> findSocialUsersByNamedQuery(final String name, final Map<String,Object> params) {
		return this.findSocialUsersByNamedQuery(name, params, -1, -1);		
	}
	
	/**
	 * Private method to perform named queries of SocialBaseUser.
	 * 
	 * @param name
	 * @param params
	 * @param start
	 * @param params 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<SocialBaseUser> findSocialUsersByNamedQuery(final String name, final Map<String,Object> params, final int start, final int total) {
		String queryString = getJpqlQuery(name);
		Query queryObject = getEntityManager().createQuery(queryString);
		if (params != null) {
			for (Map.Entry<String, Object> entry:params.entrySet())
				queryObject.setParameter(entry.getKey(), entry.getValue());
		}
		if (start > -1) 
			queryObject.setFirstResult(start);
		if (total > -1)
			queryObject.setMaxResults(total);		
		return queryObject.getResultList();
	}
	
}
