package org.opentides.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.opentides.bean.JsonKeyValue;
import org.opentides.bean.user.Tenant;
import org.opentides.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author gino
 *
 */
@Controller
@RequestMapping("/mt-ajax")
public class AjaxRequestController {
	
	@Value("${ajax.maxResult}")
	private int ajaxMaxResult;
		
	@Autowired
	private TenantService tenantService;
	
	/**
	 * Returns list of tenants for auto-complete.
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping("/tenants-list")
	public @ResponseBody List<JsonKeyValue> getTenantsList(@RequestParam("q") String name) {
		List<JsonKeyValue> values = new ArrayList<>();
		List<Tenant> tenants = tenantService.findByNamedQuery("jpql.tenant.findLikeCompany", -1, ajaxMaxResult, false, name+"%");
		for(Tenant t : tenants) {
			JsonKeyValue keyValue = new JsonKeyValue(t.getId(), t.getCompany() + " (" + t.getSchema()+")");
			values.add(keyValue);
		}
		return values;
	}
	
}
