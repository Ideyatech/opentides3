package org.opentides.web.controller;

import org.opentides.bean.Notification;
import org.opentides.bean.Notification.Medium;
import org.opentides.bean.user.BaseUser;
import org.opentides.service.UserService;
import org.opentides.service.impl.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * My Notification Controller
 * 
 * @author allanctan
 *
 */
@RequestMapping(value="/your-notifications")
@Controller
public class MyNotificationController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private NotificationService notificationService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String viewForm(ModelMap modelMap) {
		BaseUser user = userService.getCurrentUser();
		Notification n = new Notification();
		n.setRecipientUser(user);
		n.setMedium(Medium.POPUP);
		modelMap.put("notifications", notificationService.findByExample(n, 0, 100));
		return "/base/my-notifications"; 
	}
} 
