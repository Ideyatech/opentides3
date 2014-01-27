package org.opentides.social.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.opentides.bean.user.BaseUser;
import org.opentides.dao.UserDao;
import org.opentides.service.UserService;
import org.opentides.service.impl.BaseCrudServiceImpl;
import org.opentides.social.bean.SocialBaseUser;
import org.opentides.social.dao.SocialBaseUserDao;
import org.opentides.social.enums.SocialMediaType;
import org.opentides.social.service.SocialBaseUserService;
import org.scribe.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for social base user operations.
 * 
 * @author rabanes
 */
@Service(SocialBaseUserServiceImpl.NAME)
public class SocialBaseUserServiceImpl extends BaseCrudServiceImpl<SocialBaseUser> implements
		SocialBaseUserService {

	public static final String NAME = "socialBaseUserService";

	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(SocialBaseUserServiceImpl.class);

	@Autowired
	private SocialBaseUserDao socialBaseUserDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserService userService;
	
	@Value("${twitter.appID}")
	private String twitterAppId;
	
	@Value("${twitter.clientSecret}")
	private String twitterClientSecret;
	
	@Override
	public SocialBaseUser loadBySocialIdAndType(String socialId, SocialMediaType type) {
		return socialBaseUserDao.loadBySocialIdAndType(socialId, type);
	}

	@Override
	public SocialBaseUser getSocialUserByToken(SocialMediaType type, Token token) {
		Map<String, Object> details = this.getSocialDetails(type, token);
		String socialId = details.get("socialId").toString();		
		return socialBaseUserDao.loadBySocialIdAndType(socialId, type);
	}
	
	@Override
	public Boolean hasAccount(SocialMediaType type, Token token) {
		Map<String, Object> details = this.getSocialDetails(type, token);
		
		if(details == null)
			return Boolean.FALSE;
		
		String email = details.get("email").toString();		
		BaseUser user = userDao.loadByEmailAddress(email);
		if(user != null)
			return Boolean.TRUE;
		
		return Boolean.FALSE;
	}
	
	private Map<String, Object> getSocialDetails(SocialMediaType type, Token token) {
		Map<String, Object> details = new HashMap<String, Object>();
		switch (type) {
			case FACEBOOK:
				FacebookTemplate facebookTemplate = new FacebookTemplate(token.getToken());
				details.put("socialId", facebookTemplate.userOperations().getUserProfile().getId());
				details.put("email", facebookTemplate.userOperations().getUserProfile().getEmail());
				break;
			case GOOGLE:
				Google googleTemplate = new GoogleTemplate(token.getToken());
				details.put("socialId", googleTemplate.userOperations().getUserProfile().getId());
				details.put("email", googleTemplate.userOperations().getUserProfile().getEmail());
				break;
			case TWITTER:
				TwitterTemplate twitterTemplate = new TwitterTemplate(twitterAppId, twitterClientSecret, token.getToken(), token.getSecret());
				details.put("socialId", String.valueOf(twitterTemplate.userOperations().getUserProfile().getId()));
				break;
		}
		return details;
	}
	
	@Override
	public UserService getBaseUserService() {
		return userService;
	}
	
}