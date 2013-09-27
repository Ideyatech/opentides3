package org.opentides.web.controller;

import javax.annotation.PostConstruct;

import com.ideyatech.bean.Ninja;

/**
 * This class is going to be use to unit test {@link BaseCrudController}.
 * BaseCrudController is an abstract class so we need a concrete class
 * to properly test it.
 * 
 * @author gino
 *
 */
public class NinjaCrudController extends BaseCrudController<Ninja> {
	
	@PostConstruct
	public void init() {
		singlePage = "/base/ninja-codes-crud";
	}
}
