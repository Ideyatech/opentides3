/**
 * 
 */
package org.opentides.bean;

import java.util.Date;

import org.opentides.web.json.Views;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * @author allantan
 *
 */
public class JSONNotification {

	@JsonView(Views.SearchView.class)    
	private Date createDate;
	
	/**
	 * Message to be displayed for notification
	 */
	@JsonView(Views.SearchView.class)    	
	private String message;
			
	/**
	 * Type of notification
	 */
	@JsonView(Views.SearchView.class)    
	private String medium;
	
    /**
     * Primary key of object being notified.
     */	
	@JsonView(Views.SearchView.class)    
    private Long entityId;
    
    /**
     * Class type of object being notified.
     */
	@JsonView(Views.SearchView.class)    
    private String entityClass;

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(String entityClass) {
		this.entityClass = entityClass;
	}

}
