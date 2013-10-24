package org.opentides.social.provider.service.impl;

import org.opentides.social.bean.Google2Api;
import org.opentides.social.bean.SocialBaseUser;
import org.opentides.social.enums.SocialMediaType;
import org.opentides.social.provider.service.GoogleProviderService;
import org.opentides.social.service.SocialBaseUserService;
import org.opentides.social.service.SocialCredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.google.api.legacyprofile.LegacyGoogleProfile;
import org.springframework.stereotype.Service;

@Service(GoogleProviderServiceImpl.NAME)
public class GoogleProviderServiceImpl extends SocialProviderServiceImpl implements GoogleProviderService {
	
	public static final String NAME = "googleProviderService";
	
	@Autowired
	private SocialBaseUserService socialBaseUserService;
	
	@Autowired
	private SocialCredentialService socialCredentialService;
	
	public GoogleProviderServiceImpl() {
		super();
		setProvider(Google2Api.class);
	}

	@Override
	public void registerGoogleAccount(SocialBaseUser socialUser, String googleAccessToken) {
		Google googleTemplate = new GoogleTemplate(googleAccessToken);
		LegacyGoogleProfile profile = googleTemplate.userOperations().getUserProfile();
		
		socialUser.setFirstName(profile.getFirstName());
		socialUser.setLastName(profile.getLastName());
		socialUser.setEmailAddress(profile.getEmail());
		socialCredentialService.createSocialCredential(SocialMediaType.GOOGLE, profile.getId(), profile.getEmail(), socialUser);
		
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
	@Value("${google.clientSecret}")
	@Override
	public void setApiSecret(String apiSecret) {
		super.setApiSecret(apiSecret);
	}

	/* (non-Javadoc)
	 * @see org.opentides.social.service.impl.SocialProviderServiceImpl#setCallback(java.lang.String)
	 */
	@Value("${google.callback}")
	@Override
	public void setCallback(String callback) {
		super.setCallback(callback);
	}

	/* (non-Javadoc)
	 * @see org.opentides.social.service.impl.SocialProviderServiceImpl#setApiKey(java.lang.String)
	 */
	@Value("${google.appID}")
	@Override
	public void setApiKey(String apiKey) {
		super.setApiKey(apiKey);
	}

	/* (non-Javadoc)
	 * @see org.opentides.social.provider.service.impl.SocialProviderServiceImpl#setScope(java.lang.String)
	 */
	@Value("${google.scope}")
	@Override
	public void setScope(String scope) {
		super.setScope(scope);
	}

}