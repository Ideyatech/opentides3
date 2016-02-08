/*
   Licensed to the Apache Software Foundation (ASF) under one
   or more contributor license agreements.  See the NOTICE file
   distributed with this work for additional information
   regarding copyright ownership.  The ASF licenses this file
   to you under the Apache License, Version 2.0 (the
   "License"); you may not use this file except in compliance
   with the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing,
   software distributed under the License is distributed on an
   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
   KIND, either express or implied.  See the License for the
   specific language governing permissions and limitations
   under the License.    
 */
package org.opentides.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.opentides.bean.UserWidgets;
import org.opentides.bean.Widget;
import org.opentides.bean.user.BaseUser;
import org.opentides.bean.user.UserAuthority;
import org.opentides.bean.user.UserGroup;
import org.opentides.dao.UserWidgetsDao;
import org.opentides.service.UserService;
import org.opentides.service.UserWidgetsService;
import org.opentides.service.WidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gino
 *
 */
@Service("userWidgetsService")
public class UserWidgetsServiceImpl extends BaseCrudServiceImpl<UserWidgets> implements
		UserWidgetsService {
	
	private static final Logger _log = Logger.getLogger(UserWidgetsServiceImpl.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private WidgetService widgetService;

	@Override
	public void addUserWidgets(long userId, String selectedWidgets) {
		String[] widgetIds = selectedWidgets.split(",");
		BaseUser user = userService.load(userId);
		for (String widgetId:widgetIds) {
			
			int[] pos = getColumnRowOfUserWidget(userId);
			
			Widget widget = widgetService.load(widgetId);
			if (widget==null) {			
				_log.error("Attempting to add new widget with id["+widgetId+"] but widget does not exist.");
			} else {
				UserWidgets userWidget = new UserWidgets();
				userWidget.setUser(user);
				userWidget.setWidget(widget);
				userWidget.setColumn(pos[0]);
				userWidget.setRow((int)++pos[1]);
				userWidget.setStatus(1);
				getDao().saveEntityModel(userWidget);
			}
		}
		
	}
	
	private int[] getColumnRowOfUserWidget(long userId) {
		int COLUMN = widgetService.getColumnConfig();
		long row = 0;
		int col = 1;
		int[] val = new int[2];
		for (int start=1; start<=COLUMN; start++) {
			long count = countUserWidgetsColumn(start, userId);
			/* I cannot get the proper count when using countByExample
			UserWidgets columnWidgets = new UserWidgets();
			columnWidgets.setUser(user);
			columnWidgets.setColumn(start);
			long count = this.countByExample(columnWidgets, true);
			*/
			if (start == 1) {
				row = count;
				col = start;
			}
			if (row > count) {
				row = count;
				col = start;
			}
		}
		val[0] = col;
		val[1] = (int)row;
		return val;
	}

	@Override
	public void addUserWidgets(long userId, Widget widget) {
		BaseUser user = userService.load(userId);
		addUserWidgets(user, widget);
	}
	
	@Override
	public void addUserWidgets(BaseUser baseUser, Widget widget) {
		int[] pos = getColumnRowOfUserWidget(baseUser.getId());
		
		UserWidgets userWidget = new UserWidgets();
		userWidget.setUser(baseUser);
		userWidget.setWidget(widget);
		userWidget.setColumn(pos[0]);
		userWidget.setRow((int)++pos[1]);
		userWidget.setStatus(1);
		_log.debug("saving widget " + userWidget.getWidget().getName() + " at column " 
				+ userWidget.getColumn()+", row "+ userWidget.getRow());
		getDao().saveEntityModel(userWidget);
	}
	
	@Override
	public void addUserWidgets(List<BaseUser> users, Widget widget) {
		for(BaseUser user : users) {
			addUserWidgets(user, widget);
		}
	}

	@Override
	public List<UserWidgets> findUserWidgets(long userId,
			Integer... widgetStatus) {
		return ((UserWidgetsDao) getDao()).findUserWidgets(userId, widgetStatus);
	}

	@Override
	public UserWidgets findSpecificUserWidgets(BaseUser user, String widgetName) {
		UserWidgets example = new UserWidgets();
		Widget widgetObj = new Widget();
		widgetObj.setName(widgetName);
		example.setUser(user);
		example.setWidget(widgetObj);
		List<UserWidgets> widgets = this.findByExample(example, 0, 1);
		if(widgets.size() > 0){
			return widgets.get(0);
		}else{
			return null;
		}
	}

	@Override
	public void updateUserWidgetsOrder(UserWidgets userWidgets, int column,
			int row) {
		userWidgets.setColumn(column);
		userWidgets.setRow(row);
		this.save(userWidgets);
		
	}

	@Override
	public void updateUserWidgetsStatus(UserWidgets userWidgets, Integer status) {
		if (status == WidgetService.WIDGET_STATUS_REMOVE) {
			this.delete(userWidgets.getId());
		}else {
			userWidgets.setStatus(status);
			this.save(userWidgets);
		}
	}

	@Override
	public long countUserWidgetsColumn(Integer column, long userId) {
		return ((UserWidgetsDao)getDao()).countUserWidgetsColumn(column, userId);
	}

	@Override
	public void removeUserGroupWidgetsWithAccessCodes(UserGroup userGroup,
			List<UserAuthority> userAccessRoles) {
		List<String> rolesList = new ArrayList<String>(userAccessRoles.size());
		for (UserAuthority userAuthority : userAccessRoles) {
			rolesList.add(userAuthority.getAuthority());
		}
		
		//get all the widgets with the access roles from the user
		List<Widget> widgets = widgetService.findWidgetWithAccessCode(rolesList);
		for (Widget widget : widgets) {
			for(BaseUser baseUser : userGroup.getUsers()) {
				((UserWidgetsDao) getDao()).deleteUserWidget(widget.getId(), baseUser.getId());
			}
		}
	}

	@Override
	public void setupUserGroupWidgets(UserGroup userGroup,
			List<UserAuthority> userAccessRoles) {
		List<String> rolesList = new ArrayList<String>(userAccessRoles.size());
		for (UserAuthority userAuthority : userAccessRoles) {
			rolesList.add(userAuthority.getAuthority());
		}
		
		//get all the widgets with the access roles from the user
		List<Widget> widgets = widgetService.findWidgetWithAccessCode(rolesList);
		for(Widget widget : widgets){
			if(widget.getIsShown()){
				for(BaseUser user : userGroup.getUsers()) {
					int[] pos = getColumnRowOfUserWidget(user.getId());
					UserWidgets tempUserWidgets = this.findSpecificUserWidgets(user, widget.getName());
					if(tempUserWidgets == null) {
						UserWidgets userWidgets = new UserWidgets();
						userWidgets.setWidget(widget);
						userWidgets.setUser(user);
						userWidgets.setStatus(1);
						userWidgets.setColumn(pos[0]);
						userWidgets.setRow((int)++pos[1]);
						
						getDao().saveEntityModel(userWidgets);		
					}
				}
			}
		}
		
	}

}
