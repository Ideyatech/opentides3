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

package org.opentides.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtil {
	
	public static String dateToString(Date obj, String format) {
    	if (obj == null)
    		return "";    	
        SimpleDateFormat dtFormatter = new SimpleDateFormat(format);
        return dtFormatter.format(obj);	
	}
    
    public static Date stringToDate(String strDate, String format) throws ParseException {
        SimpleDateFormat dtFormatter = new SimpleDateFormat(format);
        if (StringUtil.isEmpty(strDate))
        	throw new ParseException("Cannot convert empty string to Date.", 0);
       	return dtFormatter.parse(strDate.trim());
    }
    
    public static Date convertFlexibleDate(String strDate, String[] formats) throws ParseException {
    	if (StringUtil.isEmpty(strDate))
    		return null;    	
    	for (int i=0; i<formats.length; i++) {
            try {
            	SimpleDateFormat dtFormatter = new SimpleDateFormat(formats[i]);
            	dtFormatter.setLenient(false);
            	return dtFormatter.parse(strDate.trim());
            } catch (ParseException e) {
            	// do nothing... try other format
            }    	
    	}
    	// we are unable to convert
    	throw new ParseException("No matching date format for "+strDate, 0);    	
    }

    public static String convertShortDate(Date obj) {
    	return dateToString(obj, "yyyyMMdd");
    }
    
    public static Date convertShortDate(String str) throws ParseException {
    	return stringToDate(str, "yyyyMMdd");
    }

    public static Date convertShortDate(String str, Date defaultDate) {
    	try {
    		return stringToDate(str, "yyyyMMdd");
    	} catch (ParseException pex) {
    		return defaultDate;
    	}
    }
 
    public static Date convertFlexibleDate(String strDate) throws ParseException {
    	if (StringUtil.isEmpty(strDate))
    		throw new ParseException("Cannot convert empty string to Date.", 0);

    	String[] formats = { "MM/dd/yyyy",
    					     "MM-dd-yyyy", 
    					     "yyyyMMdd",
    					     "yyyy-MM-dd",
						     "MMM dd yyyy",
						     "MMM dd, yyyy",
    					     "MMM yyyy",
						     "MM/yyyy",
						     "MM-yyyy",
						     "yyyy"};

   		return convertFlexibleDate(strDate, formats);
    }   
    
    public static boolean compareNullableDates(Date date1, Date date2) {
    	if ((date1==null) && (date2==null))
    		return true;
    	if (date1!=null) {
    		if (date1.equals(date2))
    			return true;
    		else
    			return false;
    	}
    	return false;
    }
    
    /**
     * Checks if the given date has a time recorded or just plain 00:00:00.000
     * @param date
     * @return
     */
    @SuppressWarnings("deprecation")
	public static boolean hasTime(Date date) {
    	return (date.getHours() + date.getMinutes() +date.getSeconds() > 0);
    }
    /**
	 * Get current date according to client's time zone.
	 * @param calendar - adapting calendar
	 * @param timeZone - client time zone
	 * @return adapt calendar to client time zone
	 */
	public static Date getClientCurrentDate(final Calendar calendar, final TimeZone timeZone) {
	    Calendar result = new GregorianCalendar(timeZone);
	    result.setTimeInMillis(calendar.getTimeInMillis() +
	            timeZone.getOffset(calendar.getTimeInMillis()) -
	            TimeZone.getDefault().getOffset(calendar.getTimeInMillis()));
	    result.getTime();
	    return result.getTime();
	}
}
