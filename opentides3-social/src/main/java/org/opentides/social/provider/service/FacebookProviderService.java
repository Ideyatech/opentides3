package org.opentides.social.provider.service;

import org.opentides.social.bean.SocialBaseUser;

public interface FacebookProviderService extends SocialProviderService {
	public void registerFacebookAccount(SocialBaseUser socialUser, String facebookAccessToken);
}