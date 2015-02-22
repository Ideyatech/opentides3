package org.opentides.web.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.opentides.bean.MessageResponse;
import org.opentides.bean.Notification;
import org.opentides.bean.Notification.Medium;
import org.opentides.bean.user.BaseUser;
import org.opentides.service.UserService;
import org.opentides.service.impl.NotificationService;
import org.opentides.util.DateUtil;
import org.opentides.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * My Notification Controller
 * 
 * @author allanctan
 *
 */
@RequestMapping(value="/your-notifications")
@Controller
public class YourNotificationController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(YourNotificationController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private NotificationService notificationService;
	
	/**
	 * This method displays the 'your-notification' page and allows filtering by date.
	 * The default date range is last 7 days.
	 * 
	 * @param sd
	 * @param ed
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="/page", method = RequestMethod.GET)
	public String viewForm( @RequestParam(required=false) String sd, 
							@RequestParam(required=false) String ed, 
							ModelMap modelMap) {
		Date startDate = DateUtil.getDateFrom(-7, new Date());
		Date endDate = new Date();
		if (!StringUtil.isEmpty(sd)) {
			try {
				startDate=DateUtil.stringToDate(sd, "MM/dd/yyyy");
			} catch (ParseException e) {
			}
		}
		if (!StringUtil.isEmpty(ed)) {
			try {
				endDate=DateUtil.stringToDate(ed, "MM/dd/yyyy");
			} catch (ParseException e) {
			}
		}
		BaseUser user = userService.getCurrentUser();
		Notification n = new Notification();
		n.setRecipientUser(user);
		n.setMedium(Medium.POPUP.toString());
		n.setStartDate(startDate);
		n.setEndDate(endDate);
		modelMap.put("notifications", notificationService.findByExample(n,true));
		modelMap.put("startDate", startDate);
		modelMap.put("endDate",endDate);
		return "/base/your-notification"; 
	}

	/**
	 * This method resets the poll time interval setting.
	 * Invoked when user changes the poll settings.
	 * Note: This is only applicable when notification is running in polling mode.
	 * It is recommended to use NIO/websocket mode.
	 * 
	 * @return
	 */
	@RequestMapping(value="/reset-polling", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> resetPolling() {
//		systemSettings.updateSettings();
		List<MessageResponse> messages = new ArrayList<MessageResponse>();
		Map<String, Object> model = new HashMap<String, Object>();
		MessageResponse message = new MessageResponse(
				MessageResponse.Type.notification, new String[] {"message.system-settings-applied"}, null);
		message.setMessage("System settings successfully applied.");
		messages.add(message);		
		model.put("messages", messages);
		return model; 
	}

	/**
	 * This method clears up the list of notifications of the user.
	 * Invoke this method once notification panel has been displayed to the user.
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/pop-up-clear/{userId}", method = RequestMethod.GET)
	public @ResponseBody String clearNotification(@PathVariable("userId")Long userId) {
		if (userId > 0)	
			notificationService.clearPopup(userId);
		return "cleared"; 
	}
	
	@RequestMapping(value="/pop-up/{userId}", method = RequestMethod.GET, produces="application/json")
	public @ResponseBody Map<String, Object> getNotifications(@PathVariable("userId")Long userId, 
			HttpServletRequest request) {
		if (userId > 0) {
	        // parameter for timezone can be passed to compute for time difference.
	        int	tzDiff = StringUtil.convertToInt(request.getParameter("tz"), 0);
			return notificationService.getPopupNotification(userId, tzDiff);
		} else
			return new HashMap<String, Object>();
	}
	
	@RequestMapping(value="/count/{userId}", method = RequestMethod.GET, produces="application/json")
	public @ResponseBody String countNotifications(@PathVariable("userId")Long userId) {		
		if (userId > 0)
			return ""+notificationService.countNewPopup(userId);
		else
			return "0";
	}
	
} 
