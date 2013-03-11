package org.opentides.web.controller;

import javax.annotation.PostConstruct;

import org.opentides.bean.user.BaseUser;
import org.opentides.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author AJ
 *
 */
@RequestMapping("/organization/users/photo")
@Controller 
public class UserPhotoController extends PhotoController<BaseUser> {
	
	@Autowired
	protected UserService userService;
	
	@PostConstruct
	public void init() {
		uploadPage = "/base/user-photo";
	}

	@Override
	protected BaseUser getPhotoable() {
		return userService.getCurrentUser();
	}
	
	@Autowired
	public void setService(UserService userService) {
		this.service = userService;
	}
	
}
