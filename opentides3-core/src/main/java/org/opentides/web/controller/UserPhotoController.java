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
 * This is the controller class for {@link BaseUser} photo. It extends the
 * {@link BasePhotoController} class. Mapped to {@code /user/photo}.
 * 
 * @author AJ
 */
@RequestMapping("/user/photo") /* Define the request mapping */
@Controller 
public class UserPhotoController extends BasePhotoController<BaseUser> {


	/**
	 * Defines the upload and adjust photo pages to {@code /base/user-photo-upload } and {@code /base/user-photo-adjust } respectively.
	 */
	@PostConstruct
	public void init() {
		uploadPage = "/base/user-photo-upload";
		adjustPhoto = "/base/user-photo-adjust";
	}
	
	
	/* (non-Javadoc)
	 * @see org.opentides.web.controller.BasePhotoController#getPhotoable(java.lang.Long)
	 */
	/**
	 * Defines the command object using the parameter {@code id}.
	 * 
	 * @param id the id of the command object
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
