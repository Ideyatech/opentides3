package org.opentides.social.provider.service;

import org.opentides.social.bean.SocialBaseUser;

public interface GoogleProviderService extends SocialProviderService {
	public void registerGoogleAccount(SocialBaseUser socialUser, String facebookAccessToken);
}
