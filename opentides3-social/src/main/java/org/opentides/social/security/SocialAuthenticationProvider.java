package org.opentides.social.security;

import java.util.ArrayList;
import java.util.List;

import org.opentides.bean.user.BaseUser;
import org.opentides.bean.user.UserAuthority;
import org.opentides.social.enums.SocialMediaType;
import org.opentides.social.service.SocialBaseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Generic provider to handle authentication with Spring Security
 * @author rabanes
 */
public class SocialAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private SocialBaseUserService socialBaseUserService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		SocialAuthenticationToken token = (SocialAuthenticationToken) authentication;

		if (token.getSocialId() == null) {
			throw new BadCredentialsException("User is not authenticated through " + token.getSocialMediaType());
		}

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		SocialMediaType socialMediaType = token.getSocialMediaType();
		String socialId = token.getSocialId();
		BaseUser currentUser = socialBaseUserService.loadBySocialIdAndType(socialId, socialMediaType);
		
		if (currentUser == null) {
			throw new UsernameNotFoundException("User does not exist.");
		}

		for (UserAuthority userAuthority : currentUser.getAuthorities()) {
			authorities.add(new SimpleGrantedAuthority(userAuthority.getAuthority()));
		}

		SocialAuthenticationToken succeedToken = new SocialAuthenticationToken(
				currentUser, 
				token.getSocialId(),
				token.getSocialMediaType(),
				authorities);
		succeedToken.setDetails(authentication.getDetails());

		return succeedToken;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return SocialAuthenticationToken.class.isAssignableFrom(authentication);
	}

}