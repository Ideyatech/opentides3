package org.opentides.social.service;

import org.scribe.oauth.OAuthService;

/**
 * Provides the methods in retrieving appropriate
 * Scribe OAuthService implementation depending on the
 * type of social media API.
 *
 */
public interface SocialProviderService {

    /**
     * Method that returns the appropriate OAuthService implementation
     * needed for retrieving data from a social media's public API response.
     *
     * @return oauthService
     */
	public OAuthService getOAuthService();

    /**
     * Method for retrieving the appropriate OAuthService implementation
     * needed for retrieving data from a social media's public API response
     * but with a specific "scope" or specific request for data. This is usually
     * needed for OAuth2.0 requests.
     *
     * @param scope defines the specific data the user requests from oauth handshake
     * @return oauthService
     */
	public OAuthService getOAuthService(String scope);
	
}
