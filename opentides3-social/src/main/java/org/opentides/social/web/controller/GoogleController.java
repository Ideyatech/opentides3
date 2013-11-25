package org.opentides.social.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.opentides.bean.user.BaseUser;
import org.opentides.social.bean.SocialBaseUser;
import org.opentides.social.bean.SocialCredential;
import org.opentides.social.enums.SocialMediaType;
import org.opentides.social.provider.service.GoogleProviderService;
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
 * Controller for creating and linking of account in Google.
 * 
 * @author rabanes
 */
@RequestMapping(value = "/google")
@Controller
public class GoogleController {

	@Autowired
	private GoogleProviderService googleProviderService;
	
	@Autowired
	private SocialBaseUserService socialBaseUserService;
	
	@Autowired
	private SocialCredentialService socialCredentialService;
	
	private static final Token EMPTY_TOKEN = null;

	@RequestMapping(value = "/link", method = RequestMethod.GET)
	public String link(HttpServletRequest request) {
		request.getSession().setAttribute("refererURI", UrlUtil.getRefererURI(request));
		return "redirect:/google/connect";
	}
	
	@RequestMapping(value = "/unlink", method = RequestMethod.GET)
	public String unlink(HttpServletRequest request) {
		SocialBaseUser currentUser = (SocialBaseUser) socialBaseUserService.getBaseUserService().getCurrentUser();
		socialCredentialService.removeSocialCredential(currentUser, SocialMediaType.GOOGLE);
		request.getSession().setAttribute("refererURI", UrlUtil.getRefererURI(request));
		
		return "redirect:/google/callback/link?status=message.unlink-success";
	}
	
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
			
			// Checked if user is linking an account or creating an account
			BaseUser currentUser = socialBaseUserService.getBaseUserService().getCurrentUser();
			if(currentUser != null) {
				if(user == null) {
					googleProviderService.registerGoogleAccount((SocialBaseUser) currentUser, accessToken.getToken());
					return "redirect:/google/callback/link?status=message.link-success";
				} else {
					return "redirect:/google/callback/link?status=message.already-link";
				}
			} else {
				if (user == null) {
					if(socialBaseUserService.hasAccount(socialType, accessToken)) {
						return "redirect:/google/exist";
					} else {
						user = new SocialBaseUser();
						googleProviderService.registerGoogleAccount(user, accessToken.getToken());
					}
				}
			}
			
			SocialCredential credential = user.getCredentialByType(socialType);
			googleProviderService.forceLogin(request, credential.getSocialId(), socialType);
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
		
		modelMap.put("socialType", SocialMediaType.GOOGLE.toString().toLowerCase());
		modelMap.put("status", status);
		modelMap.put("refererURI", refererURI);
		return "/base/social-user-link";
	}
	
}