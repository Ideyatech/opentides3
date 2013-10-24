package org.opentides.social.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.opentides.service.impl.BaseCrudServiceImpl;
import org.opentides.social.bean.SocialBaseUser;
import org.opentides.social.bean.SocialCredential;
import org.opentides.social.enums.SocialMediaType;
import org.opentides.social.service.SocialCredentialService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service(SocialCredentialServiceImpl.NAME)
public class SocialCredentialServiceImpl extends BaseCrudServiceImpl<SocialCredential> implements SocialCredentialService {

	public static final String NAME = "socialCredentialService";
	
	@Override
	public void createSocialCredential(SocialMediaType type, String socialId,
			String email, SocialBaseUser socialUser) {
		List<SocialCredential> socialCredentials = new ArrayList<SocialCredential>();
		
		if(socialUser.getSocialCredentials() != null && !socialUser.getSocialCredentials().isEmpty())
			socialCredentials = socialUser.getSocialCredentials();
		
		SocialCredential credential = new SocialCredential();
		credential.setSkipAudit(true);
		credential.setSocialType(type);
		credential.setSocialId(socialId);
		credential.setEmailAddress(email);
		credential.setSocialBaseUser(socialUser);
		socialCredentials.add(credential);

		socialUser.setSocialCredentials(socialCredentials);
	}
	
}
