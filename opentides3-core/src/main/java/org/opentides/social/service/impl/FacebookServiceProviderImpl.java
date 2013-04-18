package org.opentides.social.service.impl;

import org.opentides.social.service.FacebookServiceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FacebookServiceProviderImpl extends SocialProviderServiceImpl implements FacebookServiceProvider {

	@Value("${facebook.apiClass}")
	@Override
	public void setProvider(Class provider) {
		super.setProvider(provider);
	}

	@Value("${facebook.appID}")
	@Override
	public void setApiKey(String apiKey) {
		super.setApiKey(apiKey);
	}

	@Value("${facebook.clientSecret}")
	@Override
	public void setApiSecret(String apiSecret) {
		super.setApiSecret(apiSecret);
	}

	@Value("${facebook.callback}")
	@Override
	public void setCallback(String callback) {
		super.setCallback(callback);
	}
	
}
