package org.opentides.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opentides.bean.user.BaseUser;
import org.opentides.enums.SocialMediaType;
import org.opentides.security.SocialMediaAuthenticationToken;
import org.opentides.service.UserService;
import org.opentides.social.service.GoogleServiceProvider;
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

@RequestMapping(value = "/google")
@Controller
public class GoogleController {

	private static final Token EMPTY_TOKEN = null;

	@Autowired
	private GoogleServiceProvider googleServiceProvider;
	
	@Autowired
	@Qualifier("authenticationManager")
	protected AuthenticationManager authenticationManager;

	@Autowired
	UserService userService;

	private static final String SCOPE = "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";
	
	@RequestMapping(value = "/link", method = RequestMethod.GET)
	public String link(HttpServletRequest request) {
		request.getSession().setAttribute("currentUser", userService.getCurrentUser());
		return "redirect:/google/connect";
	}
	
	@RequestMapping(value = "/connect", method = RequestMethod.GET)
	public String connect(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response) {
		
		OAuthService service = googleServiceProvider.getOAuthService(SCOPE);
		String authorizationURL = service.getAuthorizationUrl(EMPTY_TOKEN);

		return "redirect:" + authorizationURL;
	}
	
	@RequestMapping(value = "/callback", method = RequestMethod.GET)
	public String callback(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response) {
		
		String code = request.getParameter("code");
		OAuthService service = googleServiceProvider.getOAuthService();
		Verifier verifier = new Verifier(code);
		Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);

		if (accessToken != null) {
			
			BaseUser currentUser = (BaseUser) request.getSession().getAttribute("currentUser");
			request.getSession().removeAttribute("currentUser");
			
			if(currentUser != null) {
				userService.registerGoogleAccount(currentUser, accessToken.getToken());
				return "redirect:/account-settings";
			} else {
				
				BaseUser user = userService.getUserByGoogleAccessToken(accessToken.getToken());
					
				if (user == null) {
					user = new BaseUser();
					userService.registerGoogleAccount(user, accessToken.getToken());
				}
				
				// Login user
				
				SocialMediaAuthenticationToken authToken = new SocialMediaAuthenticationToken(
						user, user.getId(), user.getGoogleId(),
						SocialMediaType.GOOGLE);
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
