package org.opentides.social.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.opentides.social.bean.SocialBaseUser;
import org.opentides.social.bean.SocialCredential;
import org.opentides.social.enums.SocialMediaType;
import org.opentides.social.provider.service.GoogleProviderService;
import org.opentides.social.service.SocialBaseUserService;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(value = "/google")
@Controller
public class GoogleController {

	@Autowired
	private GoogleProviderService googleProviderService;
	
	@Autowired
	private SocialBaseUserService socialBaseUserService;
	
	private static final Token EMPTY_TOKEN = null;

	@RequestMapping(value = "/connect", method = RequestMethod.GET)
	public String connect(ModelMap modelMap) {
		OAuthService service = googleProviderService.getOAuthService();
		String authorizationURL = service.getAuthorizationUrl(EMPTY_TOKEN);
		return "redirect:" + authorizationURL;
	}
	
	@RequestMapping(value = "/callback", method = RequestMethod.GET)
	public String callback(ModelMap modelMap, HttpServletRequest request) {
		
		String code = request.getParameter("code");
		OAuthService service = googleProviderService.getOAuthService();
		Verifier verifier = new Verifier(code);
		Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);

		if (accessToken != null) {
			SocialMediaType socialType = SocialMediaType.GOOGLE;
			SocialBaseUser user = socialBaseUserService.getSocialUserByToken(socialType, accessToken);
			if (user == null) {
				if(socialBaseUserService.hasAccount(socialType, accessToken)) {
					return "redirect:/google/exist";
				} else {
					user = new SocialBaseUser();
					googleProviderService.registerGoogleAccount(user, accessToken.getToken());
				}
			}
			SocialCredential credential = user.getCredentialByType(socialType);
			googleProviderService.forceLogin(request, credential.getSocialId(), socialType);
		}
		
		return "redirect:/";
	}
	
	@RequestMapping(value = "/exist", method = RequestMethod.GET)
	public String checkAccount(ModelMap modelMap) {
		return "social-user-exist";
	}
	
}