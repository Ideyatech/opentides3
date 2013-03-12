package org.opentides.web.controller;

import javax.annotation.PostConstruct;

import org.opentides.bean.user.BaseUser;
import org.opentides.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Photo implementation for BaseUser
 * 
 * @author AJ
 */
@RequestMapping("/organization/users/photo") /* Define the request mapping */
@Controller 
public class UserPhotoController extends PhotoController<BaseUser> {

	/*
	 * Define the JSP page, may not be required for Photo implementation
	 */
	@PostConstruct
	public void init() {
		uploadPage = "/base/user-photo-upload";
		cropPage = "/base/user-photo-crop";
	}
	
	/*
	 * Define the command, must implement Photoable
	 */
	@Override
	@ModelAttribute("command")
	protected BaseUser getPhotoable(@RequestParam Long id) {
		if(id == null)
			return ((UserService) service).getCurrentUser();
		else 
			return service.load(id);
	}

	// BaseUser thing, not required for Photo implementation
	@Autowired
	public void setService(UserService userService) {
		this.service = userService;
	}

}
