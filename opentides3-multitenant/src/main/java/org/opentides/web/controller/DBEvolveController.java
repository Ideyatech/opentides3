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
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.opentides.annotation.Valid;
import org.opentides.bean.user.PasswordReset;
import org.opentides.dao.SequenceDao;
import org.opentides.persistence.evolve.DBEvolve;
import org.opentides.persistence.evolve.DBEvolveManager;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author allantan
 *
 */
@RequestMapping("/db-evolve")
@Controller
public class DBEvolveController {

	@Autowired
	private SequenceDao sequenceDao;
	
	@Autowired
	private DBEvolveManager evolveManager;
	
	@RequestMapping(method=RequestMethod.GET)
	public String evolve() {
		return "/base/db-evolve";
	}
	
	@ModelAttribute("latestVersion")
	private Long latestVersion() {
		return evolveManager.getLatestVersion();
	}

	@ModelAttribute("currentVersion")
	private Long currentVersion() {
		if (evolveManager.getCurrentVersion()==null)
			return 0l;
		else
			return evolveManager.getCurrentVersion().getValue();
	}
	
	@ModelAttribute("evolveList")
	private List<DBEvolve> evolveList() {
		List<DBEvolve> evolveList = new ArrayList<DBEvolve>();
		
		// get number of latest evolve script
		long currVersion   = currentVersion();
		
		// execute new evolve scripts
		for (DBEvolve evolve:evolveList) {
			if (evolve.getVersion() > currVersion) {
				evolveList.add(evolve);				
			}
		}
		return evolveList;
	}
}
