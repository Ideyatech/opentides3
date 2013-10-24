package org.opentides.social.provider.service.impl;

import org.opentides.social.bean.SocialBaseUser;
import org.opentides.social.enums.SocialMediaType;
import org.opentides.social.provider.service.FacebookProviderService;
import org.opentides.social.service.SocialBaseUserService;
import org.opentides.social.service.SocialCredentialService;
import org.scribe.builder.api.FacebookApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;

@Service(FacebookProviderServiceImpl.NAME)
public class FacebookProviderServiceImpl extends SocialProviderServiceImpl implements FacebookProviderService {
	
	public static final String NAME = "facebookProviderService";
	
	@Autowired
	private SocialBaseUserService socialBaseUserService;
	
	@Autowired
	private SocialCredentialService socialCredentialService;
	
	public FacebookProviderServiceImpl() {
		super();
		setProvider(FacebookApi.class);
	}

	@Override
	public void registerFacebookAccount(SocialBaseUser socialUser, String facebookAccessToken) {
		FacebookTemplate facebookTemplate = new FacebookTemplate(facebookAccessToken);
		FacebookProfile profile = facebookTemplate.userOperations().getUserProfile();
		
		socialUser.setSkipAudit(true);
		socialUser.setFirstName(profile.getFirstName());
		socialUser.setLastName(profile.getLastName());
		socialUser.setMiddleName(profile.getMiddleName());
		socialUser.setEmailAddress(profile.getEmail());
		socialCredentialService.createSocialCredential(SocialMediaType.FACEBOOK, profile.getId(), profile.getEmail(), socialUser);
		
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
	@Value("${facebook.clientSecret}")
	@Override
	public void setApiSecret(String apiSecret) {
		super.setApiSecret(apiSecret);
	}

	/* (non-Javadoc)
	 * @see org.opentides.social.service.impl.SocialProviderServiceImpl#setCallback(java.lang.String)
	 */
	@Value("${facebook.callback}")
	@Override
	public void setCallback(String callback) {
		super.setCallback(callback);
	}

	/* (non-Javadoc)
	 * @see org.opentides.social.service.impl.SocialProviderServiceImpl#setApiKey(java.lang.String)
	 */
	@Value("${facebook.appID}")
	@Override
	public void setApiKey(String apiKey) {
		super.setApiKey(apiKey);
	}

	/* (non-Javadoc)
	 * @see org.opentides.social.provider.service.impl.SocialProviderServiceImpl#setScope(java.lang.String)
	 */
	@Value("${facebook.scope}")
	@Override
	public void setScope(String scope) {
		super.setScope(scope);
	}

}
