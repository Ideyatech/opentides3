/*
\   Licensed to the Apache Software Foundation (ASF) under one
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
package org.opentides.util;

import java.util.Date;

/**
 * @author allantan
 *
 */
public class TimeUtil {

	/**
	 * 
	 * Displays a user friendly time format similar to social network.
	 * 
	 * Rules are as follows:
	 * less than 1 minute, returns "just now"
	 * less than 5 minutes, returns "moments ago"
	 * 5-59 minutes ago, returns "x minutes ago"
	 * 1-2 hour ago, return "about an hour ago" 
	 * less than 24 hours, returns "x hours ago"
	 * 1-2 days ago, return "yesterday"
	 * if less than 1 week, returns "x days ago"
	 * if less than 2 weeks, return "last week"
	 * otherwise, return "date"
	 * 
	 * @param d
	 * @return
	 */
	public static String prettyTime(Date d) {
		long ms = System.currentTimeMillis() - d.getTime();
		long secs = ms==0 ? 0 : ms/1000;
		if (secs < 0) 
			return "on " + DateUtil.dateToString(d, "MMMM d, yyyy");			
		else if (secs <= 60)
			return "just now";
		else if (secs <= 300)
			return "moments ago";
		else if (secs <= 3000)
			return secs/60 +" minutes ago";
		else if (secs <= 6600)
			return "about an hour ago";
		else if (secs <= 82800)
			return secs/3600 + " hours ago";
		else if (secs <= 172800)
			return "yesterday";
		else if (secs <= 604800)
			return secs/86400 + " days ago";
		else if (secs <= 1209600)
			return "last week";
		else			
			return "last " + DateUtil.dateToString(d, "MMMM d, yyyy");			
	}
}
