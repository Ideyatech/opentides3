package org.opentides.web.controller;

import org.opentides.annotation.Valid;
import org.opentides.bean.user.PasswordReset;
import org.opentides.dao.UserDao;
import org.opentides.service.UserService;
import org.opentides.util.StringUtil;
import org.opentides.web.validator.ChangePasswordAdditionalValidator;
import org.opentides.web.validator.ChangePasswordValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author brentgalamay
 */
@RequestMapping("change-password-reset")
@Controller
public class ChangePasswordController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ChangePasswordController.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ChangePasswordValidator changePasswordValidator;

	@Autowired(required = false)
	private ChangePasswordAdditionalValidator additionalValidator;

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
			BindingResult result,
			HttpServletRequest request, ModelMap model) {
		List<String> messages = new ArrayList<>();

		if(result.hasErrors()) {
			for(ObjectError error : result.getAllErrors()) {
				messages.add(messageSource.getMessage(error, request.getLocale()));
			}
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

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(changePasswordValidator);
		LOGGER.info("Is additional validators null [{}] ", additionalValidator == null);
		if(additionalValidator != null) {
			binder.addValidators(additionalValidator);
		}
	}

}
