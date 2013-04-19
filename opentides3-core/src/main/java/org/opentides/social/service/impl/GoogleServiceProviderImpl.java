package org.opentides.social.service.impl;

import org.opentides.social.service.GoogleServiceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GoogleServiceProviderImpl extends SocialProviderServiceImpl implements GoogleServiceProvider {

	@SuppressWarnings("rawtypes")
	@Value("${google.apiClass}")
	@Override
	public void setProvider(Class provider) {
		super.setProvider(provider);
	}

	@Value("${google.appID}")
	@Override
	public void setApiKey(String apiKey) {
		super.setApiKey(apiKey);
	}

	@Value("${google.clientSecret}")
	@Override
	public void setApiSecret(String apiSecret) {
		super.setApiSecret(apiSecret);
	}

	@Value("${google.callback}")
	@Override
	public void setCallback(String callback) {
		super.setCallback(callback);
	}

}
