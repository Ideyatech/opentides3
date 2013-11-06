package org.opentides.social.service;

import org.opentides.service.BaseCrudService;
import org.opentides.social.bean.SocialBaseUser;
import org.opentides.social.bean.SocialCredential;
import org.opentides.social.enums.SocialMediaType;

/**
 * Service for social credentials operations.
 * 
 * @author rabanes
 */
public interface SocialCredentialService extends BaseCrudService<SocialCredential>{
	public void createSocialCredential(SocialMediaType type, String socialId, String email, SocialBaseUser socialUser);
	public void removeSocialCredential(SocialBaseUser socialBaseUser, SocialMediaType type);
}
