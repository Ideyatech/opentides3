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
package org.opentides.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.opentides.bean.Widget;
import org.opentides.bean.user.BaseUser;

/**
 * @author opentides 1
 *
 */
public interface WidgetService extends BaseCrudService<Widget> {
	
	// not shown
	public static final int WIDGET_STATUS_REMOVE    = 0;
	// shown
	public static final int WIDGET_STATUS_SHOW      = 1;
	// minimized
	public static final int WIDGET_STATUS_MINIMIZE  = 2;

	/**
	 * Returns the widget object based on the given name
	 * @param name
	 * @return
	 */
	public Widget findByName(String name);
	
	/**
	 * Returns the widget object based on the given url
	 * @param url
	 * @return
	 */
	public Widget findByUrl(String url);
	
	/**
	 * Returns all the widgets that are available and accessible
	 * to the current user.
	 * @return
	 */
	public List<Widget> getCurrentUserWidgets();
	
	/**
	 * Requests for the widget page results for the given name.
	 * @param name
	 * @return
	 */
	public Widget requestWidget(String widgetUrl, String name, HttpServletRequest req);
	
	/**
	 * How many column does the dashboard has?
	 * @return int
	 */
	public int getColumnConfig();
	
	/**
	 * Returns all the default widgets for the user
	 * @param user
	 * @return
	 */
	public List<Widget> findDefaultWidget(BaseUser user);
	
	/**
	 * Returns the widgets with the given access codes/roles
	 * @param accessCodes
	 * @return
	 */
	public List<Widget> findWidgetWithAccessCode(List<String> accessCodes);

}
