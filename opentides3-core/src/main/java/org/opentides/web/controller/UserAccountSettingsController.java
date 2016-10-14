package org.opentides.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.opentides.annotation.Valid;
import org.opentides.bean.MessageResponse;
import org.opentides.bean.user.BaseUser;
import org.opentides.bean.user.PasswordReset;
import org.opentides.service.UserService;
import org.opentides.util.CrudUtil;
import org.opentides.web.validator.ChangePasswordValidator;
import org.opentides.web.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/account-settings")
@Controller 
public class UserAccountSettingsController {
	
	@Autowired
	private UserService userService;

	@Autowired
	protected MessageSource messageSource;
	
	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private ChangePasswordValidator passwordValidator;
	
	@ModelAttribute("user")
	private BaseUser formBackingObject() {
		return userService.getCurrentUser();
	}
	
	@ModelAttribute("passwordReset")
	private PasswordReset newPasswordReset() {
		return new PasswordReset();
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String displayPage(ModelMap modelMap){
		return "/base/account-settings";
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/basic-info", produces = "application/json")
	public @ResponseBody Map<String, Object> updateBasicInfo(@Valid @ModelAttribute("user") BaseUser user,
			BindingResult result, HttpServletRequest request) {
		
		Map<String, Object> model = new HashMap<String, Object>();
		List<MessageResponse> messages = new ArrayList<MessageResponse>();
		
		if(result.hasErrors()) {
			messages.addAll(CrudUtil.convertErrorMessage(result,
					request.getLocale(), messageSource));
			model.put("messages", messages);
			return model;
		}
		
		userService.save(user);
		messages.addAll(CrudUtil.buildSuccessMessage(user, "update", request.getLocale(), messageSource));

		model.put("messages", messages);
		return model;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/change-password", produces = "application/json")
	public @ResponseBody Map<String, Object> changePassword(@ModelAttribute("user") BaseUser user, 
			@ModelAttribute("passwordReset") PasswordReset passwordReset, HttpServletRequest request) {
		
		Map<String, Object> model = new HashMap<String, Object>();
		List<MessageResponse> messages = new ArrayList<MessageResponse>();
		
		BindException errors = new BindException(passwordReset, "passwordReset");
		passwordValidator.validate(passwordReset, errors);
		
		if(errors.hasErrors()) {
			messages.addAll(CrudUtil.convertErrorMessage(errors,
					request.getLocale(), messageSource));
			model.put("messages", messages);
			return model;
		}
		
		user.getCredential().setPassword(
				userService.encryptPassword(user.getCredential().getNewPassword())
				);
		
		userService.save(user);
		messages.addAll(CrudUtil.buildSuccessMessage(user, "update", request.getLocale(), messageSource));

		model.put("messages", messages);
		return model;
	}

	@InitBinder("user")
	protected void initBinder(WebDataBinder binder){
		binder.setValidator(userValidator);
	}
	
}
