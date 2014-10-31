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

package org.opentides.aop;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.springframework.aop.AfterReturningAdvice;

/**
 * @author allantan
 *
 */
public class NotifyOnReturnAdvise implements AfterReturningAdvice {

	private static Logger _log = Logger.getLogger(NotifyOnReturnAdvise.class);
	
	/* (non-Javadoc)
	 * @see org.springframework.aop.AfterReturningAdvice#afterReturning(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], java.lang.Object)
	 */
	@Override
	public void afterReturning(Object returnValue, Method m, Object[] args, Object target) throws Throwable {
		_log.debug("Return Value: "+returnValue);
		_log.debug("Method: "+m.getName());
		_log.debug("Args:");
		for (int i=0; i<args.length; i++)
			_log.debug(args[i]);
		_log.debug("Target: "+target);
	}

}
