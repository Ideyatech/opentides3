package org.opentides.social.service.impl;

import org.opentides.social.service.TwitterServiceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Twitter implementation of SocialProviderService class. The needed Scribe Twitter API class
 * is provided, as well as the needed API key, secret and callback URL strings.
 */
@Service("twitter")
public class TwitterServiceProviderImpl extends SocialProviderServiceImpl implements TwitterServiceProvider {

	@SuppressWarnings("rawtypes")
	@Value("${twitter.apiClass}")
	@Override
	public void setProvider(Class provider) {
		super.setProvider(provider);
	}

	@Value("${twitter.appID}")
	@Override
	public void setApiKey(String apiKey) {
		super.setApiKey(apiKey);
	}

	@Value("${twitter.clientSecret}")
	@Override
	public void setApiSecret(String apiSecret) {
		super.setApiSecret(apiSecret);
	}

	@Value("${twitter.callback}")
	@Override
	public void setCallback(String callback) {
		super.setCallback(callback);
	}

}