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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.opentides.persistence.hibernate.MultiTenantSchemaUpdate;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
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
	private MultiTenantSchemaUpdate multiTenantSchemaUpdate;
	
	@RequestMapping(method = RequestMethod.GET, value="/schema-update/{schemaName}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Object> schemaUpdate(@PathVariable("schemaName") String schemaName, HttpServletRequest request) {
		if (StringUtil.isEmpty(schemaName)) {
			multiTenantSchemaUpdate.schemaEvolve(schemaName);
		}
		return null;
	}

}
