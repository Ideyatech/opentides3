package org.opentides.web.controller;

import java.util.List;

import org.opentides.bean.ChangeLog;
import org.opentides.bean.SyncResults;
import org.opentides.service.ChangeLogService;
import org.opentides.web.json.ResponseView;
import org.opentides.web.json.Views;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
	/**
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping("/{version}")
	@ResponseView(Views.FormView.class)
	public @ResponseBody SyncResults getUpdates(@PathVariable("version") Long version) {
		List<ChangeLog> changes = changeLogService.findAfterVersion(version);
		SyncResults results = new SyncResults();
		if (!changes.isEmpty()) {
			ChangeLog last = changes.get(changes.size()-1);
			results.setLatestVersion(last.getId());
			results.setLogs(changes);
		}
		
		return results;
	}
	
}
