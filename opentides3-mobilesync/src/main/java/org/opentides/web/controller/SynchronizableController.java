package org.opentides.web.controller;

import java.util.List;

import org.opentides.bean.ChangeLog;
import org.opentides.bean.SyncEndpoint;
import org.opentides.bean.SyncResults;
import org.opentides.service.ChangeLogService;
import org.opentides.service.SyncEndpointService;
import org.opentides.web.json.ResponseView;
import org.opentides.web.json.Views;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author allanctan
 *
 */
@Controller
@RequestMapping("/sync")
public class SynchronizableController {
	
	@Autowired
	private ChangeLogService changeLogService;
	
	@Autowired
	private SyncEndpointService syncEndpointService;
	
	/**
	 * Returns the list of updates that should be applied from the given 
	 * version. The version is the latest version of the endpoint. 
	 * Returns the list of updates after the version upto the most current 
	 * update.
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping(value="/{endpoint}/{version}", method = RequestMethod.GET, produces = "application/json")
	@ResponseView(Views.FormView.class)
	public @ResponseBody SyncResults getUpdates(@PathVariable("endpoint") String endpoint,
												@PathVariable("version") Long version) {
		List<ChangeLog> changes = changeLogService.findAfterVersion(version);
		SyncResults results = new SyncResults();
		if (!changes.isEmpty()) {
			ChangeLog last = changes.get(changes.size()-1);
			results.setLatestVersion(last.getId());
			results.setLogs(changes);
		}
		
		return results;
	}
	
	/**
	 * Updates the version of the endpoint. Endpoint should invoke 
	 * this method when 
	 * 
	 * @return
	 */
	@RequestMapping("/success/{endpoint}/{version}")
	public String updateEndpoint(@PathVariable("endpoint") String endpoint,
								 @PathVariable("version") Long version) {
		SyncEndpoint sep = new SyncEndpoint();
		sep.setClientCode(endpoint);
		sep.setSyncVersion(version);
		syncEndpointService.save(sep);
		return "Success";
	}
	
}
