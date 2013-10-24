package org.opentides.social.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opentides.social.bean.SocialBaseUser;
import org.opentides.social.bean.SocialCredential;
import org.opentides.social.enums.SocialMediaType;
import org.opentides.social.provider.service.TwitterProviderService;
import org.opentides.social.service.SocialBaseUserService;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(value = "/twitter")
@Controller
public class TwitterController {

	@Autowired
	private TwitterProviderService twitterProviderService;
	
	@Autowired
	private SocialBaseUserService socialBaseUserService;
	
	@Value("${twitter.appID}")
	private String TWITTER_APP_ID;
	
	@Value("${twitter.clientSecret}")
	private String TWITTER_CLIENT_SECRET;
	
	@RequestMapping(value = "/connect", method = RequestMethod.GET)
	public String connect(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
		OAuthService service = twitterProviderService.getOAuthService();
		
		// save request token to session (required for Twitter)
		Token requestToken = service.getRequestToken();
		request.getSession().setAttribute("ATTR_OAUTH_REQUEST_TOKEN", requestToken);

		String authorizationURL = service.getAuthorizationUrl(requestToken);
		
		return "redirect:" + authorizationURL;
	}

	@RequestMapping(value = "/callback", method = RequestMethod.GET)
	public String callback(ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response) {
		
		String oauthVerifier = (String) request.getParameter("oauth_verifier");
		
		// retrieve request token from session then remove
		Token requestToken = (Token) request.getSession().getAttribute("ATTR_OAUTH_REQUEST_TOKEN"); 
		request.getSession().removeAttribute("ATTR_OAUTH_REQUEST_TOKEN");

		OAuthService service = twitterProviderService.getOAuthService();
		Verifier verifier = new Verifier(oauthVerifier);
		Token accessToken = service.getAccessToken(requestToken, verifier);

		if (accessToken != null) {
			SocialMediaType socialType = SocialMediaType.TWITTER;
			SocialBaseUser user = socialBaseUserService.getSocialUserByToken(socialType, accessToken);
			if (user == null) {
				user = new SocialBaseUser();
				twitterProviderService.registerTwitterAccount(user, TWITTER_APP_ID, TWITTER_CLIENT_SECRET, accessToken);
			}
			SocialCredential credential = user.getCredentialByType(socialType);
			twitterProviderService.forceLogin(request, credential.getSocialId(), socialType);
		}

		return "redirect:/";
	}
	
}
