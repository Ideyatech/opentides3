package org.opentides.security;

import java.util.Collection;
import java.util.List;

import org.opentides.bean.user.BaseUser;
import org.opentides.enums.SocialMediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class SocialMediaAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 8079278000065128059L;

	private BaseUser user;
	private Long userId;
	private String socialMediaId;
	private SocialMediaType socialMediaType;
	
	public SocialMediaAuthenticationToken(BaseUser user, Long userId,
			String socialMediaId, SocialMediaType socialMediaType,
			List<GrantedAuthority> authorities) {
		super(authorities);
		this.user = user;
		this.userId = userId;
		this.socialMediaId = socialMediaId;
		this.socialMediaType = socialMediaType;
		super.setAuthenticated(true);
	}
	
	public SocialMediaAuthenticationToken(
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
	}

	@Override
	public Object getCredentials() {
		return null; //TODO
	}

	@Override
	public Object getPrincipal() {
		return null; //TODO
	}
	
	public BaseUser getUser() {
		return user;
	}

	public void setUser(BaseUser user) {
		this.user = user;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getSocialMediaId() {
		return socialMediaId;
	}

	public void setSocialMediaId(String socialMediaId) {
		this.socialMediaId = socialMediaId;
	}

	public SocialMediaType getSocialMediaType() {
		return socialMediaType;
	}

	public void setSocialMediaType(SocialMediaType socialMediaType) {
		this.socialMediaType = socialMediaType;
	}

}
