package org.opentides.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.opentides.annotation.Valid;
import org.opentides.bean.user.PasswordReset;
import org.opentides.dao.UserDao;
import org.opentides.service.UserService;
import org.opentides.util.StringUtil;
import org.opentides.web.validator.ChangePasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author brentgalamay
 */
@RequestMapping("change-password-reset")
@Controller
public class ChangePasswordController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ChangePasswordValidator changePasswordValidator;

	@ModelAttribute("action")
	private String action() {
		return "change";
	}

	@ModelAttribute("passwordReset")
	private PasswordReset formBackingObject() {
		return new PasswordReset();
	}

	@RequestMapping(method = RequestMethod.GET)
	public String displayPage(ModelMap modelMap) {
		return "forgot-password";
	}

	@RequestMapping(method = RequestMethod.POST, value = "change")
	public String change(
			@Valid @ModelAttribute("passwordReset") PasswordReset passwd,
			HttpServletRequest request, ModelMap model) {
		List<String> messages = new ArrayList<String>();
		if (StringUtil.isEmpty(passwd.getEmailAddress())) {
			messages.add("error.required.emailAddress");
		}
		if (!StringUtil.isEmpty(passwd.getEmailAddress()) && !userDao.isRegisteredByEmail(passwd.getEmailAddress())) {
			messages.add("msg.email-address-is-not-registered");
		}
		if (StringUtil.isEmpty(passwd.getPassword())) {
			messages.add("error.required.password");
		}
		if (StringUtil.isEmpty(passwd.getConfirmPassword())) {
			messages.add("error.required.confirmPassword");
		}
		if (!StringUtil.isEmpty(passwd.getPassword()) && (passwd.getPassword().length()<6)) {
			messages.add("err.your-password-should-be-at-least-6-characters-long");
		}
		if (!StringUtil.isEmpty(passwd.getPassword()) && !StringUtil.isEmpty(passwd.getConfirmPassword()) &&
				!passwd.getPassword().equals(passwd.getConfirmPassword()) ) {				
			messages.add("err.please-confirm-your-password");
		}
		if (!messages.isEmpty()) {
			model.put("messages", messages);
			model.put("displayForm", true);
			return "forgot-password";
		}
		boolean success = false;
		// let's check if password reset session is properly set.
		String secureCode = (String) request.getSession().getAttribute(
				ConfirmPasswordResetController.SECURE_SESSION_KEY);
		if (!StringUtil.isEmpty(secureCode)
				&& secureCode
						.startsWith(ConfirmPasswordResetController.SECURE_SESSION_CODE)
				&& (passwd.getEmailAddress()
						.equals(secureCode
								.substring(ConfirmPasswordResetController.SECURE_SESSION_CODE
										.length())))) {
			success = userService.resetPassword(passwd);
		}

		if (success) {
			messages.add("msg.password-change-successful");
		} else {
			messages.add("error.unauthorized-access-to-change-password");
		}
		model.put("messages", messages);
		return "forgot-password";
	}

}
