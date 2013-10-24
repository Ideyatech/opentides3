package org.opentides.social.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.opentides.social.bean.SocialBaseUser;
import org.opentides.social.bean.SocialCredential;
import org.opentides.social.enums.SocialMediaType;
import org.opentides.social.provider.service.FacebookProviderService;
import org.opentides.social.provider.service.impl.FacebookProviderServiceImpl;
import org.opentides.social.service.SocialBaseUserService;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(value = "/facebook")
@Controller
public class FacebookController {

	@Qualifier(FacebookProviderServiceImpl.NAME)
	@Autowired
	private FacebookProviderService facebookProviderService;
	
	@Autowired
	private SocialBaseUserService socialBaseUserService;
	
	private static final Token EMPTY_TOKEN = null;

	@RequestMapping(value = "/connect", method = RequestMethod.GET)
	public String connect(ModelMap modelMap) {
		OAuthService service = facebookProviderService.getOAuthService();
		String authorizationURL = service.getAuthorizationUrl(EMPTY_TOKEN);
		return "redirect:" + authorizationURL;
	}
	
	@RequestMapping(value = "/callback", method = RequestMethod.GET)
	public String callback(ModelMap modelMap, HttpServletRequest request) {
		
		String code = request.getParameter("code");
		OAuthService service = facebookProviderService.getOAuthService();
		Verifier verifier = new Verifier(code);
		Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);

		if (accessToken != null) {
			SocialMediaType socialType = SocialMediaType.FACEBOOK;
			SocialBaseUser user = socialBaseUserService.getSocialUserByToken(socialType, accessToken);
			if (user == null) {
				if(socialBaseUserService.hasAccount(socialType, accessToken)) {
					return "redirect:/facebook/exist";
				} else {
					user = new SocialBaseUser();
					facebookProviderService.registerFacebookAccount(user, accessToken.getToken());
				}
			}
			SocialCredential credential = user.getCredentialByType(socialType);
			facebookProviderService.forceLogin(request, credential.getSocialId(), socialType);
		}
		
		return "redirect:/";
	}
	
	@RequestMapping(value = "/exist", method = RequestMethod.GET)
	public String checkAccount(ModelMap modelMap) {
		return "social-user-exist";
	}
	
}