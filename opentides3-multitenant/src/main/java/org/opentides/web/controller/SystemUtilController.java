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
package org.opentides.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.opentides.bean.MessageResponse;
import org.opentides.persistence.hibernate.MultiTenantDBEvolveManager;
import org.opentides.persistence.hibernate.MultiTenantSchemaUpdate;
import org.opentides.util.NamingUtil;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Common system utility functionalities.
 * 
 * @author allantan
 *
 */
@RequestMapping("/sysutl")
@Controller 
public class SystemUtilController {

	@Autowired
	protected MessageSource messageSource;

	@Autowired
	private MultiTenantSchemaUpdate multiTenantSchemaUpdate;
	
	@Autowired
	private MultiTenantDBEvolveManager multiTenantDBEvolveManager;

	/**
	 * 
	 * @param schemaName
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value="/schema-update/{schemaName}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Object> schemaUpdate(@PathVariable("schemaName") String schemaName, HttpServletRequest request) {
		final Map<String, Object> response = new HashMap<>();
		final List<MessageResponse> messages = new ArrayList<MessageResponse>();
		if (!StringUtil.isEmpty(schemaName)) {
			if (multiTenantSchemaUpdate.schemaEvolve(schemaName)) {
				messages.add(buildResponse(MultiTenantSchemaUpdate.class,
						"evolve", "success", request.getLocale(), messageSource));
			} else {
				messages.add(buildResponse(MultiTenantSchemaUpdate.class,
						"evolve", "error", request.getLocale(), messageSource));
			}
		}

		response.put("messages", messages);
		return response;
	}

	/**
	 * 
	 * @param schemaName
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/db-evolve/{schemaName}", method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Map<String, Object> evolve(
			@PathVariable("schemaName") String schemaName,
			HttpServletRequest request) {
		final Map<String, Object> response = new HashMap<>();
		final List<MessageResponse> messages = new ArrayList<MessageResponse>();

		multiTenantDBEvolveManager.evolve(schemaName);

		messages.add(buildResponse(MultiTenantDBEvolveManager.class, "evolve",
				"success", request.getLocale(), messageSource));
		response.put("messages", messages);
		
		return response;
	}

	/*
	 * 
	 */
	private static MessageResponse buildResponse(Class<?> clazz, String code,
			String result, Locale locale, MessageSource messageSource) {
		Assert.notNull(clazz);
		String prefix = "message."
				+ NamingUtil.toElementName(clazz.getSimpleName());
		System.out.println("Prefix " + prefix);
		String codes = prefix + "." + code + "-" + result + ",message." + code
				+ "-"
				+ result;
		MessageResponse message = new MessageResponse(
				MessageResponse.Type.notification, codes.split("\\,"), null);
		message.setMessage(messageSource.getMessage(message, locale));

		return message;
	}
}
