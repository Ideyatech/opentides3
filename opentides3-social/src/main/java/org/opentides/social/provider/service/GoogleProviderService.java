package org.opentides.social.provider.service;

import org.opentides.social.bean.SocialBaseUser;

/**
 * @author rabanes
 */
public interface GoogleProviderService extends SocialProviderService {
	public void registerGoogleAccount(SocialBaseUser socialUser, String facebookAccessToken);
}
