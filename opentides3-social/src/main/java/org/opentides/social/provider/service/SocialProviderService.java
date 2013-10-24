package org.opentides.social.provider.service;

import javax.servlet.http.HttpServletRequest;

import org.opentides.social.enums.SocialMediaType;
import org.scribe.oauth.OAuthService;

public interface SocialProviderService {
	public OAuthService getOAuthService();
	public void forceLogin(HttpServletRequest request, String socialId, SocialMediaType socialType);
}