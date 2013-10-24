package org.opentides.social.provider.service;

import org.opentides.social.bean.SocialBaseUser;
import org.scribe.model.Token;

public interface TwitterProviderService extends SocialProviderService {
	public void registerTwitterAccount(SocialBaseUser socialUser, String appId, String clientSecret, Token accessToken);
}
