package org.opentides.social.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opentides.bean.user.UserGroup;
import org.opentides.service.UserGroupService;
import org.opentides.service.impl.BaseCrudServiceImpl;
import org.opentides.social.bean.SocialBaseUser;
import org.opentides.social.bean.SocialCredential;
import org.opentides.social.enums.SocialMediaType;
import org.opentides.social.service.SocialCredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for social credentials operations.
 * 
 * @author rabanes
 */
@Service(SocialCredentialServiceImpl.NAME)
public class SocialCredentialServiceImpl extends BaseCrudServiceImpl<SocialCredential> implements SocialCredentialService {

	public static final String NAME = "socialCredentialService";
	
	@Autowired
	private UserGroupService userGroupService;
	
	@Override
	public void createSocialCredential(SocialMediaType type, String socialId,
			String email, SocialBaseUser socialUser) {
		List<SocialCredential> socialCredentials = new ArrayList<SocialCredential>();
		
		SocialCredential credential = new SocialCredential();
		if(socialUser.getSocialCredentials() != null && !socialUser.getSocialCredentials().isEmpty())
			socialCredentials = socialUser.getSocialCredentials();

		credential.setSkipAudit(true);
		credential.setSocialType(type);
		credential.setSocialId(socialId);
		credential.setEmailAddress(email);
		credential.setSocialBaseUser(socialUser);
		socialCredentials.add(credential);

		socialUser.setSocialCredentials(socialCredentials);
		
		UserGroup group = userGroupService.getDefaultUserGroup();
		if(group != null)
			socialUser.addGroup(group);
	}

	@Override
	public void removeSocialCredential(SocialBaseUser socialBaseUser, SocialMediaType type) {
		List<SocialCredential> socialCredentials = new ArrayList<SocialCredential>(socialBaseUser.getSocialCredentials());
		for (Iterator<SocialCredential> iterator = socialCredentials.iterator(); iterator.hasNext();) {
			SocialCredential socialCredential = iterator.next();
			if(type.equals(socialCredential.getSocialType())) {
				socialBaseUser.getSocialCredentials().remove(socialCredential);
				this.delete(socialCredential.getId());
			}
		}
	}

}
