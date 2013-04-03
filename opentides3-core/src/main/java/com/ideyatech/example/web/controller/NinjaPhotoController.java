package com.ideyatech.example.web.controller;

import org.opentides.web.controller.BasePhotoController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ideyatech.example.bean.Ninja;


/**
 * Photo implementation for Ninja
 * 
 * A good Ninja takes a picture of himself.
 * 
 * @author AJ
 */
@RequestMapping("/ninja/photo") /* Define the request mapping */
@Controller 
public class NinjaPhotoController extends BasePhotoController<Ninja> {

	@Override
	protected Ninja getPhotoable(@RequestParam Long id) {
		return service.load(id);
	}
	
}
