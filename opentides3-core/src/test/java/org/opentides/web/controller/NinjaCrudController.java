package org.opentides.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.opentides.annotation.FormBind;

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
	
	public Map<String, Object> sampleMethod(@FormBind(name = "formCommand") Ninja command) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("command", command);
		return model;
	}
	
	public Map<String, Object> sampleMethodWithoutFormBindAnnot(Ninja command) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("command", command);
		return model;
	}
	
}
