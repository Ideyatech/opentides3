package org.opentides.security;

import java.util.ArrayList;
import java.util.List;

import org.opentides.bean.user.BaseUser;
import org.opentides.bean.user.UserAuthority;
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

/**
 * Custom authentication provider for opentides3 to allow users to login to the
 * application using social media account.
 */
public class SocialMediaAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	UserService userService;

    /**
     *  This method handles the authentication os users through Social Media means,
     *  i.e. Facebook, Google, Twitter. The method will retrieve the current user record
     *  by the social media ID and retrieve the attributes (roles, basic user stuff, etc)
     *  and put them in the security context using the SocialMediaAuthenticationToken.
     *
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		SocialMediaAuthenticationToken socialMediaAuthenticationToken = (SocialMediaAuthenticationToken) authentication;
		
		if (socialMediaAuthenticationToken.getSocialMediaId() == null) {
			throw new BadCredentialsException("User is not authenticated through " + socialMediaAuthenticationToken.getSocialMediaType());
		}
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		BaseUser currentUser = null;
		SocialMediaType socialMediaType = socialMediaAuthenticationToken.getSocialMediaType();
		String socialId = socialMediaAuthenticationToken.getSocialMediaId();
		
		if(SocialMediaType.FACEBOOK.equals(socialMediaType)) {
			currentUser = userService.getUserByFacebookId(socialId);
		} else if (SocialMediaType.GOOGLE.equals(socialMediaType)) {
			currentUser = userService.getUserByGoogleId(socialId);
		} else if (SocialMediaType.TWITTER.equals(socialMediaType)) {
			currentUser = userService.getUserByTwitterId(socialId);
		}

		if (currentUser == null) {
			throw new UsernameNotFoundException("User does not exist.");
		}
		
		for (UserAuthority userAuthority : currentUser.getAuthorities()) {
			authorities.add(new SimpleGrantedAuthority(userAuthority.getAuthority()));
		}
		
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
