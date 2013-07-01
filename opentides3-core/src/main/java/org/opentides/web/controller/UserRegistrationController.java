package org.opentides.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.opentides.annotation.Valid;
import org.opentides.bean.MessageResponse;
import org.opentides.bean.user.BaseUser;
import org.opentides.service.UserGroupService;
import org.opentides.service.UserService;
import org.opentides.util.CrudUtil;
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
import org.springframework.web.bind.annotation.ResponseBody;

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
	protected UserService userService;
	
	@Autowired
	protected UserGroupService userGroupService;
	
	@Autowired
	private UserValidator userValidator;
	
	@RequestMapping(method = RequestMethod.GET)
	public String viewForm(ModelMap modelMap, HttpServletRequest request) {
		
		BaseUser baseUser = new BaseUser();
		baseUser.addGroup(userGroupService.load(1L));
		modelMap.addAttribute("baseUser", baseUser);
		return "user-registration"; 

	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Map<String, Object> registerUser(@Valid BaseUser baseUser,  BindingResult result, HttpServletRequest request) {
		
		Map<String, Object> model = new HashMap<String, Object>();
		List<MessageResponse> messages = new ArrayList<MessageResponse>();
		
		if(result.hasErrors()) {
			messages.addAll(CrudUtil.convertErrorMessage(result,
					request.getLocale(), messageSource));
			model.put("messages", messages);
			return model;
		}
		
		userService.registerUser(baseUser, true);
		messages.addAll(CrudUtil.buildSuccessMessage(baseUser, "user-registration", request.getLocale(), messageSource));
		
		model.put("email", baseUser.getEmailAddress());
		model.put("messages", messages);
		return model; 

	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(userValidator);
	}
}
