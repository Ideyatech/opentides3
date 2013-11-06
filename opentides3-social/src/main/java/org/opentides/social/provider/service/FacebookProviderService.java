package org.opentides.social.provider.service;

import org.opentides.social.bean.SocialBaseUser;

/**
 * @author rabanes
 */
public interface FacebookProviderService extends SocialProviderService {
	public void registerFacebookAccount(SocialBaseUser socialUser, String facebookAccessToken);
}