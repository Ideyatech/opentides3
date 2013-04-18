package org.opentides.security;

import java.util.ArrayList;
import java.util.List;

import org.opentides.bean.user.BaseUser;
import org.opentides.enums.SocialMediaType;
import org.opentides.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class SocialMediaAuthenticationProvider implements AuthenticationProvider {

	private String[] roles;
	
	@Autowired
	UserService userService;
	
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		SocialMediaAuthenticationToken socialMediaAuthenticationToken = (SocialMediaAuthenticationToken) authentication;
		
		if (socialMediaAuthenticationToken.getSocialMediaId() == null) {
			throw new BadCredentialsException("User is not authenticated through " + socialMediaAuthenticationToken.getSocialMediaType());
		}
		
		if (roles == null) {
			roles = new String[] {};
		}
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		
		BaseUser currentUser = null;
		SocialMediaType socialMediaType = socialMediaAuthenticationToken.getSocialMediaType();
		String socialId = socialMediaAuthenticationToken.getSocialMediaId();
		
		if(SocialMediaType.FACEBOOK.equals(socialMediaType)) {
			currentUser = userService.getUserByFacebookId(socialId);
		}

		if (currentUser == null) {
			throw new UsernameNotFoundException("User does not exist.");
		}
		
		socialMediaAuthenticationToken.setUser(currentUser);
		//authorities.add(new SimpleGrantedAuthority(currentUser.get));
		
		SocialMediaAuthenticationToken succeedToken = new SocialMediaAuthenticationToken(
				currentUser, currentUser.getId(),
				socialMediaAuthenticationToken.getSocialMediaId(),
				socialMediaAuthenticationToken.getSocialMediaType(),
				authorities);
		succeedToken.setDetails(authentication.getDetails());
		
		return succeedToken;
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return SocialMediaAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
