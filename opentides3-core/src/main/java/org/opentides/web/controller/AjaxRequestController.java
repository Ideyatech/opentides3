package org.opentides.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.opentides.bean.JsonKeyValue;
import org.opentides.bean.user.BaseUser;
import org.opentides.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author gino
 *
 */
@Controller
@RequestMapping("/ajax")
public class AjaxRequestController {
	
	@Value("${ajax.maxResult}")
	private int ajaxMaxResult;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping("/users-list")
	public @ResponseBody List<JsonKeyValue> getUsersList(@RequestParam("q") String name) {
		List<JsonKeyValue> values = new ArrayList<>();
		List<BaseUser> users = this.userService.findUsersLikeLastName(name, ajaxMaxResult);
		for(BaseUser user : users) {
			JsonKeyValue keyValue = new JsonKeyValue(user.getId(), user.getFullName());
			values.add(keyValue);
		}
		return values;
	}
	
}
