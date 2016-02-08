package org.opentides.rest.impl;

import java.util.List;

import org.opentides.bean.SearchResults;
import org.opentides.bean.user.BaseUser;
import org.opentides.bean.user.PasswordReset;
import org.opentides.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service(value="restUserService")
public class UserServiceImpl extends BaseCrudRestServiceImpl<BaseUser> 
		implements UserService{

	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public String encryptPassword(String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setupAdminUser() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateLogin(
			AuthenticationSuccessEvent authenticationSuccessEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateLogout(Authentication auth) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<SessionInformation> getAllLoggedUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void forceLogout(String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isUserLockedOut(String username, long maxAttempts,
			long lockOutTime) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateFailedLogin(String username, long timestamp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unlockUser(String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<BaseUser> findUsersLikeLastName(String name, int maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BaseUser> findAllUsersWithAuthority(String authority) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseUser loadByUsername(String username) {
	    ResponseEntity<SearchResults> response = 
	      restTemplate.exchange(super.serverURL+"?username="+username, 
	    		  HttpMethod.GET, entity, SearchResults.class);
	    SearchResults<BaseUser> resource = response.getBody();
	    if (!resource.getResults().isEmpty()) {
	    	return objectMapper.convertValue(resource.getResults().get(0), BaseUser.class);
	    } else
	    	return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseUser loadByEmailAddress(String emailAddress) {
	    ResponseEntity<SearchResults> response = 
	  	      restTemplate.exchange(super.serverURL+"?emailAddress="+emailAddress, 
	  	    		  HttpMethod.GET, entity, SearchResults.class);
	  	    SearchResults<BaseUser> resource = response.getBody();
	  	    if (!resource.getResults().isEmpty()) 
	  	    	return objectMapper.convertValue(resource.getResults().get(0), BaseUser.class);
	  	    else
	  	    	return null;
	}

	@Override
	public BaseUser getCurrentUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerUser(BaseUser baseUser, boolean sendEmail) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean resetPassword(PasswordReset passwd) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean confirmPasswordResetByCipher(PasswordReset passwd) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean confirmPasswordReset(String emailAddress, String token) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void requestPasswordReset(String emailAddress) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<SessionInformation> getAllLoggedUsersPagenation(int start,
			int total) {
		// TODO Auto-generated method stub
		return null;
	}
}
