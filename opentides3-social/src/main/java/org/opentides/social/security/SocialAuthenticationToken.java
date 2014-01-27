package org.opentides.social.security;

import java.util.Collection;
import java.util.List;

import org.opentides.bean.user.BaseUser;
import org.opentides.bean.user.SessionUser;
import org.opentides.social.enums.SocialMediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * AuthenticationToken to hold user details that is going to be authenticated by the SocialAuthenticationProvider.
 * @author rabanes
 */
public class SocialAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 8079278000065128059L;

	private BaseUser user;
	private String socialId;
	private SocialMediaType socialMediaType;
	private List<GrantedAuthority> authorities;
	
	public SocialAuthenticationToken(BaseUser user, String socialMediaId, SocialMediaType socialMediaType, List<GrantedAuthority> authorities) {
		super(authorities);
		this.user = user;
		this.socialId = socialMediaId;
		this.socialMediaType = socialMediaType;
		this.authorities = authorities;
		super.setAuthenticated(true);
	}
	
	public SocialAuthenticationToken(String socialMediaId, SocialMediaType socialMediaType) {
		this(null, socialMediaId, socialMediaType, null);
	}
	
	public SocialAuthenticationToken(BaseUser user, String socialMediaId, SocialMediaType socialMediaType) {
		this(user, socialMediaId, socialMediaType, null);
	}
	
	public SocialAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
	}

	@Override
	public Object getCredentials() {
		return new SessionUser(user, authorities);
	}

	@Override
	public Object getPrincipal() {
		return new SessionUser(user, authorities);
	}
	
	public BaseUser getUser() {
		return user;
	}

	public void setUser(BaseUser user) {
		this.user = user;
	}
	
	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	public SocialMediaType getSocialMediaType() {
		return socialMediaType;
	}

	public void setSocialMediaType(SocialMediaType socialMediaType) {
		this.socialMediaType = socialMediaType;
	}

}