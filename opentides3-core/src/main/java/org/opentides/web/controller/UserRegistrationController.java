package org.opentides.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.opentides.annotation.Valid;
import org.opentides.bean.user.BaseUser;
import org.opentides.web.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Registration Controller
 * 
 * @author AJ
 *
 */
@RequestMapping(value="/register")
@Controller
public class UserRegistrationController {
	
	@Autowired
	protected MessageSource messageSource;
	
	@Autowired
	private UserValidator userValidator;
	
	@RequestMapping(method = RequestMethod.GET)
	public String viewForm(ModelMap modelMap, HttpServletRequest request) {
		
		BaseUser baseUser = new BaseUser();
		modelMap.addAttribute("baseUser", baseUser);
		return "user-registration"; 

	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String registerUser(@Valid BaseUser baseUser,  BindingResult result) {
		
		System.out.println("##### REGISTERING + " + baseUser.getUsername());
		
		return "user-registration"; 

	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(userValidator);
	}
}
