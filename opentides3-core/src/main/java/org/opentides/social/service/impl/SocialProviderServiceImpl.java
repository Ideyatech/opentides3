package org.opentides.social.service.impl;

import org.opentides.social.service.SocialProviderService;
import org.scribe.builder.ServiceBuilder;
import org.scribe.oauth.OAuthService;
import org.springframework.stereotype.Service;

/**
 * Class implementation to provide the needed Scribe OAuthService to retrieve
 * necessary data from social media public API.
 */
@Service
public class SocialProviderServiceImpl implements SocialProviderService {

    /**
     * Scribe class denoting the type of social media connection
     */
	protected Class provider;

    /**
     * String key used for validating use of social media's public API
     */
	protected String apiKey;

    /**
     * String secret used for validating use of social media's public API
     */
	protected String apiSecret;

    /**
     * The call URL that will be used by the social media oauth response
     */
	protected String callback;
	
	public SocialProviderServiceImpl() {
		
	}

    /**
     * Method that returns the appropriate OAuthService implementation
     * needed for retrieving data from a social media's public API response.
     *
     * @return oauthService
     */
	@Override
	public OAuthService getOAuthService() {
		OAuthService service = new ServiceBuilder()
	         .provider(provider)
	         .apiKey(apiKey)
	         .apiSecret(apiSecret)
	         .callback(callback)
	         .build();
		return service;
	}

    /**
     * Method for retrieving the appropriate OAuthService implementation
     * needed for retrieving data from a social media's public API response
     * but with a specific "scope" or specific request for data. This is usually
     * needed for OAuth2.0 requests.
     *
     * @param scope
     * @return
     */
	@Override
	public OAuthService getOAuthService(String scope) {
		OAuthService service = new ServiceBuilder()
	         .provider(provider)
	         .apiKey(apiKey)
	         .apiSecret(apiSecret)
	         .callback(callback)
	         .scope(scope)
	         .build();
		return service;
	}

	public void setProvider(Class provider) {
		this.provider = provider;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

}
