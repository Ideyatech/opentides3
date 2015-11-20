package org.opentides.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Standard message format for transferring data from device to server
 * and vice versa.
 * 
 * @author allantan
 *
 */
@JsonInclude(Include.NON_NULL)
public class SyncMessage {

	public static enum Status {
		ok, ng
	}
	
	private String clientCode;
	
	private String command;
	
	private String message;
	
	private Status status;
	
	private Long startVersion;
	
	private Long endVersion;

	private Long targetVersion;
	
	private Long deviceVersion;
	
	private Long maxLookupId;
	
	private List<SqlStatement> queries;
	
	public SyncMessage(String clientCode, String command,
			Long startVersion, Long endVersion, Long targetVersion, List<SqlStatement> queries) {
		super();
		this.clientCode = clientCode;
		this.command = command;
		this.startVersion = startVersion;
		this.endVersion = endVersion;
		this.targetVersion = targetVersion;
		this.queries = queries;	
	}

	public SyncMessage(String clientCode, String command, String message, Status status) {
		super();
		this.clientCode = clientCode;
		this.command = command;
		this.message = message;
		this.status  = status;
	}

	/**
	 * @return the clientCode
	 */
	public String getClientCode() {
		return clientCode;
	}

	/**
	 * @param clientCode the clientCode to set
	 */
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @param command the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @return the startVersion
	 */
	public Long getStartVersion() {
		return startVersion;
	}

	/**
	 * @param startVersion the startVersion to set
	 */
	public void setStartVersion(Long startVersion) {
		this.startVersion = startVersion;
	}

	/**
	 * @return the endVersion
	 */
	public Long getEndVersion() {
		return endVersion;
	}

	/**
	 * @param endVersion the endVersion to set
	 */
	public void setEndVersion(Long endVersion) {
		this.endVersion = endVersion;
	}

	/**
	 * @return the targetVersion
	 */
	public Long getTargetVersion() {
		return targetVersion;
	}

	/**
	 * @param targetVersion the targetVersion to set
	 */
	public void setTargetVersion(Long targetVersion) {
		this.targetVersion = targetVersion;
	}	

	/**
	 * @return the deviceVersion
	 */
	public Long getDeviceVersion() {
		return deviceVersion;
	}

	/**
	 * @param deviceVersion the deviceVersion to set
	 */
	public void setDeviceVersion(Long deviceVersion) {
		this.deviceVersion = deviceVersion;
	}

	/**
	 * @return the maxLookupId
	 */
	public Long getMaxLookupId() {
		return maxLookupId;
	}

	/**
	 * @param maxLookupId the maxLookupId to set
	 */
	public void setMaxLookupId(Long maxLookupId) {
		this.maxLookupId = maxLookupId;
	}

	/**
	 * @return the queries
	 */
	public List<SqlStatement> getQueries() {
		return queries;
	}

	/**
	 * @param queries the queries to set
	 */
	public void setQueries(List<SqlStatement> queries) {
		this.queries = queries;
	}

}
