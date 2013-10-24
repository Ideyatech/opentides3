package org.opentides.social.provider.service.impl;

import org.opentides.social.bean.SocialBaseUser;
import org.opentides.social.enums.SocialMediaType;
import org.opentides.social.provider.service.TwitterProviderService;
import org.opentides.social.service.SocialBaseUserService;
import org.opentides.social.service.SocialCredentialService;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;

@Service(TwitterProviderServiceImpl.NAME)
public class TwitterProviderServiceImpl extends SocialProviderServiceImpl implements TwitterProviderService {

	public static final String NAME = "twitterProviderService";
	
	@Autowired
	private SocialBaseUserService socialBaseUserService;
	
	@Autowired
	private SocialCredentialService socialCredentialService;
	
	public TwitterProviderServiceImpl() {
		super();
		setProvider(TwitterApi.Authenticate.class);
	}

	@Override
	public void registerTwitterAccount(SocialBaseUser socialUser, String appId, String clientSecret, Token accessToken) {
		TwitterTemplate twitterTemplate = new TwitterTemplate(appId, clientSecret, accessToken.getToken(), accessToken.getSecret());
		TwitterProfile profile = twitterTemplate.userOperations().getUserProfile();
		
		socialUser.setFirstName(profile.getName());
		socialCredentialService.createSocialCredential(SocialMediaType.TWITTER, String.valueOf(profile.getId()), "", socialUser);
		
		if(socialUser.getId() == null) {
			socialUser.setCredential(socialBaseUserService.generateFakeCredentials());
			socialBaseUserService.registerUser(socialUser, false);
		} else {
			socialBaseUserService.save(socialUser);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.opentides.social.service.impl.SocialProviderServiceImpl#setApiSecret(java.lang.String)
	 */
	@Value("${twitter.clientSecret}")
	@Override
	public void setApiSecret(String apiSecret) {
		super.setApiSecret(apiSecret);
	}

	/* (non-Javadoc)
	 * @see org.opentides.social.service.impl.SocialProviderServiceImpl#setCallback(java.lang.String)
	 */
	@Value("${twitter.callback}")
	@Override
	public void setCallback(String callback) {
		super.setCallback(callback);
	}

	/* (non-Javadoc)
	 * @see org.opentides.social.service.impl.SocialProviderServiceImpl#setApiKey(java.lang.String)
	 */
	@Value("${twitter.appID}")
	@Override
	public void setApiKey(String apiKey) {
		super.setApiKey(apiKey);
	}

}