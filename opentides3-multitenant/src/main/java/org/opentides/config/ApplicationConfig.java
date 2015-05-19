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

package org.opentides.config;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.opentides.exception.InvalidImplementationException;
import org.opentides.rest.impl.BaseCrudRestServiceImpl;
import org.opentides.service.UserService;
import org.opentides.util.UrlUtil;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * This is the configuration class that is responsible for injecting the correct
 * service.
 * 
 * @author allantan
 */
@Configuration
public class ApplicationConfig {

	private static final Logger _log = Logger
			.getLogger(ApplicationConfig.class);

	@Autowired
	protected BeanFactory beanFactory;

	@Autowired
	private ServletContext servletContext;

	@Value("#{applicationSettings['user.enableProxyService']}")
	private Boolean enableProxyService = false;

	@Value("#{applicationSettings['user.proxyURL']}")
	private String proxyURL = "http://localhost";

	@SuppressWarnings("rawtypes")
	@Bean
	@Primary
	public UserService getUserService() throws Exception {
		String serviceBean = "userService";
		_log.debug("Proxy service is set to " + enableProxyService);
		if (enableProxyService) {
			boolean found = false;
			for (String endPoint : UrlUtil.getEndPoints()) {
				endPoint += servletContext.getContextPath();
				_log.debug("Checking endpoint " + endPoint);
				if (proxyURL.contains("localhost")) {
					endPoint = endPoint.replaceAll("(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)", "localhost");
					_log.debug("Endpoint changed to " + endPoint);
				}
				if (proxyURL.startsWith(endPoint)) {
					found = true;
					_log.debug("Proxy URL found [" + proxyURL + "]");
				}
			}
			
			if (!found) {
				serviceBean = "restUserService";
			}
		}
		if (beanFactory.containsBean(serviceBean)) {
			final UserService service = (UserService) beanFactory
					.getBean(serviceBean);
			if (service != null
					&& BaseCrudRestServiceImpl.class.isAssignableFrom(service
							.getClass())) {
				((BaseCrudRestServiceImpl) service).setServerURL(proxyURL
						+ "/organization/users");
			}
			return service;
		} else {
			throw new InvalidImplementationException(
					"Cannot find userService bean [" + serviceBean
							+ "] for dependency injection.");
		}
	}
}
