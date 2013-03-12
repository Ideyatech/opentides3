package com.ideyatech.example.web.controller;

import org.opentides.web.controller.PhotoController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ideyatech.example.bean.Ninja;


/**
 * Photo implementation for Ninja
 * 
 * Ninjas have photos too!
 * 
 * @author AJ
 */
@RequestMapping("/ninja/photo") /* Define the request mapping */
@Controller 
public class NinjaPhotoController extends PhotoController<Ninja> {

	@Override
	protected Ninja getPhotoable(@RequestParam Long id) {
		return service.load(id);
	}
	
}
