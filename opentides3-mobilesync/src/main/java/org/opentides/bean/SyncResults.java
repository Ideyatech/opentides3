/**
 * 
 */
package org.opentides.bean;

import java.util.List;

import org.opentides.web.json.Views;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * @author allantan
 *
 */
public class SyncResults implements Serializable{
	
	private static final long serialVersionUID = -61347292723741700L;

	@JsonView(Views.FormView.class)	
	private long latestVersion;
	
	@JsonView(Views.FormView.class)	
	private List<ChangeLog> logs;
	
	@JsonView(Views.FormView.class)	
	private String imageBase64;
	
	@JsonView(Views.FormView.class)	
	private Long imageId;

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
	
	/**
	 * @return the imageBase64
	 */
	public final String getImage() {
		return imageBase64;
	}

	/**
	 * @param imageBase64 the imageBase64 to set
	 */
	public final void setImage(String imageBase64) {
		this.imageBase64 = imageBase64;
	}
	
	/**
	 * @return the imageId
	 */
	public final Long getImageId() {
		return imageId;
	}

	/**
	 * @param imageIdthe imageId to set
	 */
	public final void setImageId(Long imageId) {
		this.imageId = imageId;
	}
	
	
}
