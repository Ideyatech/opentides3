package org.opentides.security;

import java.util.Collection;
import java.util.List;

import org.opentides.bean.user.BaseUser;
import org.opentides.bean.user.SessionUser;
import org.opentides.enums.SocialMediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * Custom authentication token that holds the attributes that will be put
 * into the security context when a user tries to login using social media
 * accounts. This token will be used in an AuthenticationProvider class.
 *
 */
public class SocialMediaAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 8079278000065128059L;

	private BaseUser user;
	private Long userId;
	private String socialMediaId;
	private SocialMediaType socialMediaType;
	private List<GrantedAuthority> authorities;

    /**
     * Default constructor
     * @param user
     * @param userId
     * @param socialMediaId
     * @param socialMediaType
     * @param authorities
     */
	public SocialMediaAuthenticationToken(BaseUser user, Long userId,
			String socialMediaId, SocialMediaType socialMediaType,
			List<GrantedAuthority> authorities) {
		super(authorities);
		this.user = user;
		this.userId = userId;
		this.socialMediaId = socialMediaId;
		this.socialMediaType = socialMediaType;
		this.authorities = authorities;
		super.setAuthenticated(true);
	}

    /**
     *
     * @param user
     * @param userId
     * @param socialMediaId
     * @param socialMediaType
     */
	public SocialMediaAuthenticationToken(BaseUser user, Long userId,
			String socialMediaId, SocialMediaType socialMediaType) {
		this(user, userId, socialMediaId, socialMediaType, null);
	}

    /**
     *
     * @param authorities
     */
	public SocialMediaAuthenticationToken(
			Collection<? extends GrantedAuthority> authorities) {
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
