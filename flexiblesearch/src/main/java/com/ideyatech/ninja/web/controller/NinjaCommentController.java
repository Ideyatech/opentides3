package com.ideyatech.ninja.web.controller;

import com.ideyatech.ninja.bean.Ninja;
import org.opentides.web.controller.BaseCommentController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Comment implementation for Ninja
 * 
 * As a sign of gratitude, we shall give comments to Ninjas.
 * 
 * @author AJ
 */
@RequestMapping("/ninja/comment") /* Define the request mapping */
@Controller 
public class NinjaCommentController extends BaseCommentController<Ninja> {
	
}
