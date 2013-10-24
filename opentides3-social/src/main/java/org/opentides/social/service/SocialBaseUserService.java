package org.opentides.social.service;

import org.opentides.service.UserService;
import org.opentides.social.bean.SocialBaseUser;
import org.opentides.social.enums.SocialMediaType;
import org.scribe.model.Token;

/**
 * Service for social base user operations.
 * 
 * @author rabanes
 */
public interface SocialBaseUserService extends UserService {
	public SocialBaseUser getSocialUserByToken(SocialMediaType type, Token token);
	public SocialBaseUser loadBySocialIdAndType(String socialId, SocialMediaType type);
	public Boolean hasAccount(SocialMediaType type, Token token);
}
