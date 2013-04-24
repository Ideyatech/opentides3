package org.opentides.web.controller;

import org.opentides.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/account-settings")
@Controller 
public class UserAccountSettingsController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String displayPage(ModelMap modelMap){
		
		modelMap.addAttribute("user", userService.getCurrentUser());
		
		return "/base/account-settings";
	}

	
}
