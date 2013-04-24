package org.opentides.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opentides.bean.user.BaseUser;
import org.opentides.enums.SocialMediaType;
import org.opentides.security.SocialMediaAuthenticationToken;
import org.opentides.service.UserService;
import org.opentides.social.service.TwitterServiceProvider;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(value = "/twitter")
@Controller
public class TwitterController {

	@Autowired
	private TwitterServiceProvider twitterServiceProvider;

	@Autowired
	@Qualifier("authenticationManager")
	protected AuthenticationManager authenticationManager;
	
	@Value("${twitter.appID}")
	private String TWITTER_APP_ID;
	
	@Value("${twitter.clientSecret}")
	private String TWITTER_CLIENT_SECRET;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/link", method = RequestMethod.GET)
	public String link(HttpServletRequest request) {
		request.getSession().setAttribute("currentUser", userService.getCurrentUser());
		return "redirect:/twitter/connect";
	}
	
	@RequestMapping(value = "/connect", method = RequestMethod.GET)
	public String connect(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response) {
		
		OAuthService service = twitterServiceProvider.getOAuthService();
		
		Token requestToken = service.getRequestToken();
		request.getSession().setAttribute("ATTR_OAUTH_REQUEST_TOKEN",
				requestToken); // save request token to session (required for Twitter)

		String authorizationURL = service.getAuthorizationUrl(requestToken);
		
		return "redirect:" + authorizationURL;
	}

	@RequestMapping(value = "/callback", method = RequestMethod.GET)
	public String callback(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response) {
		
		String oauthVerifier = (String) request.getParameter("oauth_verifier");
		
		Token requestToken = (Token) request.getSession().getAttribute(
				"ATTR_OAUTH_REQUEST_TOKEN"); // retrieve request token from session then remove
		request.getSession().removeAttribute("ATTR_OAUTH_REQUEST_TOKEN");

		OAuthService service = twitterServiceProvider.getOAuthService();
		Verifier verifier = new Verifier(oauthVerifier);
		Token accessToken = service.getAccessToken(requestToken, verifier);

		if (accessToken != null) {
			
			BaseUser currentUser = (BaseUser) request.getSession().getAttribute("currentUser");
			request.getSession().removeAttribute("currentUser");
			
			if(currentUser != null) {
				userService.registerTwitterAccount(currentUser, TWITTER_APP_ID, TWITTER_CLIENT_SECRET, accessToken);
				return "redirect:/account-settings";
			} else {
				
				BaseUser user = userService.getUserByTwitterAccessToken(TWITTER_APP_ID, TWITTER_CLIENT_SECRET, accessToken);
					
				if (user == null) {
					user = new BaseUser();
					userService.registerTwitterAccount(user, TWITTER_APP_ID, TWITTER_CLIENT_SECRET, accessToken);
				}
				
				// Login user
				
				SocialMediaAuthenticationToken authToken = new SocialMediaAuthenticationToken(
						user, user.getId(), user.getTwitterId(),
						SocialMediaType.TWITTER);
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
