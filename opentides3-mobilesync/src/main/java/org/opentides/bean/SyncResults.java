/**
 * 
 */
package org.opentides.bean;

import java.util.List;

import org.opentides.web.json.Views;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * @author allantan
 *
 */
public class SyncResults {

	@JsonView(Views.FormView.class)	
	private long latestVersion;
	
	@JsonView(Views.FormView.class)	
	private List<ChangeLog> logs;

	/**
	 * @return the latestVersion
	 */
	public final long getLatestVersion() {
		return latestVersion;
	}

	/**
	 * @param latestVersion the latestVersion to set
	 */
	public final void setLatestVersion(long latestVersion) {
		this.latestVersion = latestVersion;
	}

	/**
	 * @return the logs
	 */
	public final List<ChangeLog> getLogs() {
		return logs;
	}

	/**
	 * @param logs the logs to set
	 */
	public final void setLogs(List<ChangeLog> logs) {
		this.logs = logs;
	}
	
	
}
