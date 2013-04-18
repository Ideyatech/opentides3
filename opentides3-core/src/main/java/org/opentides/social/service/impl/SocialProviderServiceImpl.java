package org.opentides.social.service.impl;

import org.opentides.social.service.SocialProviderService;
import org.scribe.builder.ServiceBuilder;
import org.scribe.oauth.OAuthService;
import org.springframework.stereotype.Service;

@Service
public class SocialProviderServiceImpl implements SocialProviderService {

	protected Class provider;
	
	protected String apiKey;
	
	protected String apiSecret;
	
	protected String callback;
	
	public SocialProviderServiceImpl() {
		
	}
	
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
