/**
 * 
 */
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
import org.opentides.util.StringUtil;
import org.opentides.web.validator.ConfirmPasswordResetValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author brentgalamay
 */
@RequestMapping("confirm-password-reset")
@Controller
public class ConfirmPasswordResetController {
	@Autowired
	private UserService userService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ConfirmPasswordResetValidator confirmPasswordResetValidator;

	public static final String SECURE_SESSION_KEY = "confirmedPasswordReset";

	public static final String SECURE_SESSION_CODE = "aYotNaP0!";

	@ModelAttribute("action")
	private String action() {
		return "request";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String displayPage(ModelMap modelMap) {
		return "forgot-password";
	}

	@ModelAttribute("passwordReset")
	private PasswordReset formBackingObject() {
		return new PasswordReset();
	}

	@ModelAttribute("currentUser")
	private BaseUser currentUser() {
		return userService.getCurrentUser();
	}

	@RequestMapping(value = "confirm")
	public String confirm(
			@Valid @ModelAttribute("passwordReset") PasswordReset passwd,
			BindingResult result, HttpServletRequest request, ModelMap model) {
		boolean success = false;
		List<String> messages = new ArrayList<String>();

		if (StringUtil.isEmpty(passwd.getCipher())) {
			success = userService.confirmPasswordReset(
					passwd.getEmailAddress(), passwd.getToken());
			if (!success) {
				messages.add("error.unauthorized-password-reset-by-email-token");
			}
		} else {
			success = userService.confirmPasswordResetByCipher(passwd);
			if (!success) {
				messages.add("error.unauthorized-password-reset-by-cipher");
			}
		}
		if (success) {
			model.put("passwordReset", passwd);
			request.getSession().setAttribute(SECURE_SESSION_KEY,
					SECURE_SESSION_CODE + passwd.getEmailAddress());
			// next page is to change password
			model.put("action", "change");
		}
		model.put("messages", messages);
		model.put("displayForm", true);
		return "forgot-password";
	}

	@RequestMapping(method = RequestMethod.POST, value = "change-password", produces = "application/json")
	public String changePassword(
			@Valid @ModelAttribute("currentUser") BaseUser user,
			BindingResult result, HttpServletRequest request) {

		Map<String, Object> model = new HashMap<String, Object>();
		List<MessageResponse> messages = new ArrayList<MessageResponse>();

		if (result.hasErrors()) {
			messages.addAll(CrudUtil.convertErrorMessage(result,
					request.getLocale(), messageSource));
			model.put("messages", messages);
			return "forgot-password";
		}

		user.getCredential().setPassword(
				userService.encryptPassword(user.getCredential()
						.getNewPassword()));

		userService.save(user);
		messages.addAll(CrudUtil.buildSuccessMessage(user, "update",
				request.getLocale(), messageSource));

		model.put("messages", messages);
		return "forgot-password";
	}
}
