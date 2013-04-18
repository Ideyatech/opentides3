package org.opentides.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opentides.service.UserService;
import org.opentides.social.service.FacebookServiceProvider;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
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
	
	@RequestMapping(value="/connect", method=RequestMethod.GET)
    public String connectAccountToFacebook(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
	
		OAuthService service = facebookServiceProvider.getOAuthService();
		String authorizationURL = service.getAuthorizationUrl(EMPTY_TOKEN).concat("&scope=email,user_about_me,read_stream");
		
		return "redirect:" + authorizationURL;
    }
	
}
