#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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
package ${package}.web.controller;

import ${package}.bean.Ninja;
import ${package}.service.NinjaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * This is the controller class for Ninja
 * 
 * So we get to know more about our Ninjas
 * 
 * @author AJ
 * 
 */
@RequestMapping("/ninja/profile")
@Controller
public class NinjaController {

	@Autowired
	NinjaService ninjaService;
	
	@RequestMapping(method = RequestMethod.GET, value="{id}")
	public final String displayDetails(@PathVariable("id") Long id, Model model){
		
		Ninja ninja = ninjaService.load(id);
		
		model.addAttribute("ninja", ninja);
		
		return "app/ninja-details";
	}

}
