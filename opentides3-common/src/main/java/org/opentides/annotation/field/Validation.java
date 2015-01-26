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

package org.opentides.annotation.field;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generates an validation codes
 * <b>Sample Usage:</b><br />
 * &emsp;&emsp;&emsp;<code>@Validation (label = "textFieldLabel", isRequired = "true")</code><br />
 * @author allanctan
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface Validation {

	/**
	 * Refers to fields that are required for a specified class; default value is <code><b>false</b></code>.
	 * 
	 */	
	boolean isRequired() default false;
	
	
	/**
	 * Refers to the fields that are of email format; default value is <code><b>false</b></code>.
	 * 
	 */	
	boolean isEmailFormat() default false;
	
	
	/**
	 * Refers to the fields that are of numeric format; default value is <code><b>false</b></code>.
	 * 
	 */	
	boolean isNumberFormat() default false;
	
	/**
	 * Refers to the fields that are of format money; default value is <code><b>false</b></code>.
	 * 
	 */	
	boolean isMoneyFormat() default false;
	
	/**
	 * Refers to the fields that are of format date; default value is <code><b>false</b></code>.
	 * 
	 */
	boolean isDateFormat() default false;
	
	/**
	 * Refers to the fields that are of date format and whose values must be before current date; default value is <code><b>false</b></code>.
	 * 
	 */
	boolean rejectPastDate() default false;
	
	/**
	 * Refers to the fields that are of date format and whose values must be after current date; default value is <code><b>false</b></code>.
	 * 
	 */
	boolean rejectFutureDate() default false;	
	
	/**
	 * Refers to the fields that have to follow a specified format; default value is <code><b>""</b></code>.
	 * 
	 */
	String regex() default "";
	
	/**
	 * Refers to the fields that have a limited length; default value is <code><b>-1</b></code>.
	 * 
	 */
	long maxLength() default -1;
	
	/**
	 * Refers to the fields whose values should not exceed a given value; default value is <code><b>-1</b></code>.
	 * 
	 */
	long maxAllowValue() default -1;
	
	/**
	 * Refers to the fields whose values should not be below a given value; default value is <code><b>-1</b></code>.
	 * 
	 */
	long minAllowValue() default -1;
}
