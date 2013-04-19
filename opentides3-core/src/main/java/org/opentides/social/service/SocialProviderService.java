package org.opentides.social.service;

import org.scribe.oauth.OAuthService;

public interface SocialProviderService {
	
	public OAuthService getOAuthService();
	public OAuthService getOAuthService(String scope);
	
}
