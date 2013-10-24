package org.opentides.social.provider.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.opentides.social.enums.SocialMediaType;
import org.opentides.social.provider.service.SocialProviderService;
import org.opentides.social.security.SocialAuthenticationToken;
import org.opentides.util.StringUtil;
import org.scribe.builder.ServiceBuilder;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
public class SocialProviderServiceImpl implements SocialProviderService {

	@Autowired
    protected AuthenticationManager authenticationManager;
	
	@SuppressWarnings("rawtypes")
	protected Class provider;
	
	protected String apiKey;
	
	protected String apiSecret;
	
	protected String callback;
	
	protected String scope;
	
	public SocialProviderServiceImpl() {
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public OAuthService getOAuthService() {
		OAuthService service = null;
		if(!StringUtil.isEmpty(scope)) {
			service = new ServiceBuilder()
	         .provider(provider)
	         .apiKey(apiKey)
	         .apiSecret(apiSecret)
	         .callback(callback)
	         .scope(scope)
	         .build();
		} else {
			service = new ServiceBuilder()
	         .provider(provider)
	         .apiKey(apiKey)
	         .apiSecret(apiSecret)
	         .callback(callback)
	         .build();
		}
		return service;
	}

	@Override
	public void forceLogin(HttpServletRequest request, String socialId, SocialMediaType socialType) {
		SocialAuthenticationToken authToken = new SocialAuthenticationToken(socialId, socialType);
		Authentication authentication = authenticationManager.authenticate(authToken);
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(authentication);
		request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
	}

	/**
	 * @param provider the provider to set
	 */
	@SuppressWarnings("rawtypes")
	public void setProvider(Class provider) {
		this.provider = provider;
	}

	/**
	 * @param apiSecret the apiSecret to set
	 */
	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}

	/**
	 * @param callback the callback to set
	 */
	public void setCallback(String callback) {
		this.callback = callback;
	}

	/**
	 * @param apiKey the apiKey to set
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

}