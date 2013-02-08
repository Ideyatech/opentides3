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
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author allanctan
 *
 */
public class ValidatorUtil {
	/**
	 * Checks for valid email address
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (StringUtil.isEmpty(email))	
			return false;
		//Initialize reg ex for email. 
		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		return isMatchRegex(email, expression);
	}
	
	/**
	 * Checks for valid phone number format.
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isPhoneNumber(String phoneNumber) {
		if (StringUtil.isEmpty(phoneNumber))
			return false;
		//String expression = "^\\d*|([0-9]{2,3}[-.]\\d+$)|(\\(\\d{2,3}\\)[-.]\\d+$)|(\\d+[-.]\\d+$)|(\\d+[-.]\\d+[-.]\\d+$|^[+]\\d*)";
		String expression = "([\\d]*|[\\s]*|[-]*)*";
		return isMatchRegex(phoneNumber, expression);
	}
	
	/**
	 * Checks for regex matching with case insensitive option
	 * @param input
	 * @param regex
	 * @return
	 */
	public static boolean isMatchRegex(String input, String regex) {
		boolean isValid=false;
		CharSequence inputStr = input;
		Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if(matcher.matches()){
			isValid = true;
		}
		return isValid;		
	}
	
	/**
	 * Checks for a valid numeric value [Regex ^\\d*$].
	 * @param number
	 * @return
	 */
	public static boolean isNumeric(String number) {
		if (StringUtil.isEmpty(number))	
			return false;
		String expression = "-?\\d+(.\\d+)?";
		return isMatchRegex(number, expression);		
	}
	
	/**
	 * public static validator method to validate the inputed date range
	 * @param dateField1 - the date field to be placed at the left of comparison
	 * @param dateField2 - the date field to be placed at the right of comparison
	 * @param dateFormat - the date format to be used (e.g. MM/dd/yyyy)
	 * @return boolean - true if dateField1 < dateField2 otherwise return false;
	 */
	public static boolean isValidDateRange(Date dateField1, Date dateField2, String dateFormat) {
		if (dateField1 == null || dateField2 == null)
			return false;
		String strDate1 =  DateUtil.dateToString(dateField1, dateFormat);  
		String strDate2 =  DateUtil.dateToString(dateField2, dateFormat);  
		try {
			dateField1  = DateUtil.stringToDate(strDate1,dateFormat);
			dateField2 = DateUtil.stringToDate(strDate2, dateFormat);
		} catch (ParseException ee) {
			return false;
		}
		if (dateField1.getTime() <= dateField2.getTime()) 
			return true;
		 else
			return false;
	}
}
