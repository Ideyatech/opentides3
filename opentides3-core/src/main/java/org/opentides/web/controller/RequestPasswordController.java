/**
 * 
 */
package org.opentides.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.opentides.annotation.Valid;
import org.opentides.bean.user.PasswordReset;
import org.opentides.dao.UserDao;
import org.opentides.service.UserService;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author brentgalamay
 */
@RequestMapping("request-password-reset")
@Controller
public class RequestPasswordController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDao userDao;
	
	@ModelAttribute("action")
	private String action() {
		return "request";
	}

	@ModelAttribute("displayForm")
	private boolean displayForm() {
		return true;
	}

	@ModelAttribute("passwordReset")
	private PasswordReset formBackingObject() {
		return new PasswordReset();
	}

	@RequestMapping(method = RequestMethod.GET)
	public String displayPage() {
		return "forgot-password";
	}

	@RequestMapping(method = RequestMethod.POST, value = "request")
	public String request(
			@Valid @ModelAttribute("passwordReset") PasswordReset passwd,
			HttpServletRequest request, ModelMap model) {
		List<String> messages = new ArrayList<String>();
		if (StringUtil.isEmpty(passwd.getEmailAddress())) {
			messages.add("error.required.emailAddress");
		}
		if (!StringUtil.isEmpty(passwd.getEmailAddress()) && !userDao.isRegisteredByEmail(passwd.getEmailAddress())) {
			messages.add("msg.email-address-is-not-registered");
		}
		if (!messages.isEmpty()) {
			model.put("messages", messages);
			return "forgot-password";
		}
		try {
			userService.requestPasswordReset(passwd.getEmailAddress());
			messages.add("msg.instructions-for-reset-sent");
		} catch (Exception ce) {
			messages.add("msg.failed-to-send-email");
		}
		model.put("messages", messages);
		return "forgot-password";
	}

}
