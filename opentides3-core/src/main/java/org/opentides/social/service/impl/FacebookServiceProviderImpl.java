package org.opentides.social.service.impl;

import org.opentides.social.service.FacebookServiceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Facebook implementation of SocialProviderService class. The needed Scribe Facebook API class
 * is provided, as well as the needed API key, secret and callback URL strings.
 */
@Service
public class FacebookServiceProviderImpl extends SocialProviderServiceImpl implements FacebookServiceProvider {

	@SuppressWarnings("rawtypes")
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
