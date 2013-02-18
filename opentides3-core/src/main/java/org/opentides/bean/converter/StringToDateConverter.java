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
package org.opentides.bean.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.opentides.util.StringUtil;
import org.springframework.core.convert.converter.Converter;

/**
 * Generic converter for all Date classes. The conversion format uses
 * date.format settings in the property file.
 * 
 * @author allanctan
 */
public class StringToDateConverter implements Converter<String, Date> {

	private static Logger _log = Logger.getLogger(StringToDateConverter.class);

	private String dateFormat;

	@Override
	public Date convert(String source) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		if (StringUtil.isEmpty(source))
			return null;
		try {
			return sdf.parse(source);
		} catch (ParseException e) {
			String message = "Failed to convert ["+source+"] to date format. Please use '"+dateFormat+"'";
			if (_log.isDebugEnabled())				
				_log.debug(message);
			throw new IllegalArgumentException(message, e);
		}
	}

	/**
	 * @param dateFormat the dateFormat to set
	 */
	public final void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
}
