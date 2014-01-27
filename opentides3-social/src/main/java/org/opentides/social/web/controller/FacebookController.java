package org.opentides.social.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.opentides.bean.user.BaseUser;
import org.opentides.social.bean.SocialBaseUser;
import org.opentides.social.bean.SocialCredential;
import org.opentides.social.enums.SocialMediaType;
import org.opentides.social.provider.service.FacebookProviderService;
import org.opentides.social.service.SocialBaseUserService;
import org.opentides.social.service.SocialCredentialService;
import org.opentides.util.UrlUtil;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for creating and linking of account in Facebook.
 * 
 * @author rabanes
 */
@RequestMapping(value = "/facebook")
@Controller
public class FacebookController {

	@Autowired
	private FacebookProviderService facebookProviderService;
	
	@Autowired
	private SocialBaseUserService socialBaseUserService;
	
	@Autowired
	private SocialCredentialService socialCredentialService;
	
	private static final Token EMPTY_TOKEN = null;

	@RequestMapping(value = "/link", method = RequestMethod.GET)
	public String link(HttpServletRequest request) {
		request.getSession().setAttribute("refererURI", UrlUtil.getRefererURI(request));
		return "redirect:/facebook/connect";
	}
	
	@RequestMapping(value = "/unlink", method = RequestMethod.GET)
	public String unlink(HttpServletRequest request) {
		SocialBaseUser currentUser = (SocialBaseUser) socialBaseUserService.getBaseUserService().getCurrentUser();
		socialCredentialService.removeSocialCredential(currentUser, SocialMediaType.FACEBOOK);
		request.getSession().setAttribute("refererURI", UrlUtil.getRefererURI(request));
		
		return "redirect:/facebook/callback/link?status=message.unlink-success";
	}
	
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
			
			// Checked if user is linking an account or creating an account
			BaseUser currentUser = socialBaseUserService.getBaseUserService().getCurrentUser();
			if(currentUser != null) {
				if(user == null) {
					facebookProviderService.registerFacebookAccount((SocialBaseUser) currentUser, accessToken.getToken());
					return "redirect:/facebook/callback/link?status=message.link-success";
				} else {
					return "redirect:/facebook/callback/link?status=message.already-link";
				}
			} else {
				if (user == null) {
					if(socialBaseUserService.hasAccount(socialType, accessToken)) {
						return "redirect:/facebook/exist";
					} else {
						user = new SocialBaseUser();
						facebookProviderService.registerFacebookAccount(user, accessToken.getToken());
					}
				}
			}
			
			SocialCredential credential = user.getCredentialByType(socialType);
			facebookProviderService.forceLogin(request, credential.getSocialId(), socialType);
		}
		
		return "redirect:/";
	}
	
	@RequestMapping(value = "/exist", method = RequestMethod.GET)
	public String checkAccount(ModelMap modelMap) {
		return "/base/social-user-exist";
	}
	
	@RequestMapping(value = "/callback/link", method = RequestMethod.GET)
	public String linkCallback(ModelMap modelMap, HttpServletRequest request) {
		String status = request.getParameter("status");
		String refererURI = request.getSession().getAttribute("refererURI").toString();
		request.getSession().removeAttribute("refererURI");
		
		modelMap.put("socialType", SocialMediaType.FACEBOOK.toString().toLowerCase());
		modelMap.put("status", status);
		modelMap.put("refererURI", refererURI);
		return "/base/social-user-link";
	}
	
}