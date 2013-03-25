package org.opentides.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/form-builder")
@Controller 
public class FormBuilderController {
	
	@RequestMapping(method = RequestMethod.GET)
	public String displayPage(){
		
		System.out.println("Displaying form builder page...");
		
		return "/base/form-builder";
	}

	
}
