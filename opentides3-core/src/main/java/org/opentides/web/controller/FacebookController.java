package org.opentides.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opentides.bean.user.BaseUser;
import org.opentides.enums.SocialMediaType;
import org.opentides.security.SocialMediaAuthenticationToken;
import org.opentides.service.UserService;
import org.opentides.social.service.FacebookServiceProvider;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(value = "/facebook")
@Controller
public class FacebookController {

	private static final Token EMPTY_TOKEN = null;

	@Autowired
	private FacebookServiceProvider facebookServiceProvider;

	@Autowired
	@Qualifier("authenticationManager")
	protected AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/connect", method = RequestMethod.GET)
	public String connectAccountToFacebook(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response) {

		OAuthService service = facebookServiceProvider.getOAuthService();
		String authorizationURL = service.getAuthorizationUrl(EMPTY_TOKEN)
				.concat("&scope=email,user_about_me,read_stream");

		return "redirect:" + authorizationURL;
	}

	@RequestMapping(value = "/callback", method = RequestMethod.GET)
	public String callbackFacebook(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response) {
		
		String code = request.getParameter("code");
		OAuthService service = facebookServiceProvider.getOAuthService();
		Verifier verifier = new Verifier(code);
		Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);

		if (accessToken != null) {
			
			BaseUser user;
			
			System.out.println(userService.getCurrentUser());
			
			if(userService.getCurrentUser() != null) { // If a user is logged in
				
				// Attempt to connect facebook account on logged-in user
				user = userService.getCurrentUser();
				userService.registerFacebookAccount(user, accessToken.getToken());
				
				//TODO Redirect to account settings page
				
			} else { // Else, either register user or attempt log in
				
				user = userService
						.getUserByFacebookAccessToken(accessToken.getToken());
				
				if (user == null) {
					user = new BaseUser();
					userService.registerFacebookAccount(user, accessToken.getToken());
				}
				
				// Login user
				
				SocialMediaAuthenticationToken authToken = new SocialMediaAuthenticationToken(
						user, user.getId(), user.getFacebookId(),
						SocialMediaType.FACEBOOK);
				Authentication authentication = authenticationManager
						.authenticate(authToken);
				SecurityContext context = SecurityContextHolder.getContext();
				context.setAuthentication(authentication);
				request.getSession()
						.setAttribute(
								HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
								context);
				
			}
			
		}

		return "redirect:/";
	}

}
