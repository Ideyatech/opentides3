/*
   Licensed to the Apache Software Foundation (ASF) under one
   or more contributor license agreements.  See the NOTICE file
   distributed with this work for additional information
   regarding copyright ownership.  The ASF licenses this file
   to you under the Apache License, Version 2.0 (the
   "License"); you may not use this file except in compliance
   with the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing,
   software distributed under the License is distributed on an
   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
   KIND, either express or implied.  See the License for the
   specific language governing permissions and limitations
   under the License.    
 */
package org.opentides.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.opentides.bean.user.BaseUser;
import org.opentides.persistence.listener.AuditLogListener;
import org.opentides.web.json.Views;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * This class is responsible for handling all audit functions needed to be
 * attached to the classes.
 * 
 * @author allantan
 */
@Entity
@EntityListeners({ AuditLogListener.class })
@Table(name = "HISTORY_LOG")
public class AuditLog extends BaseEntity {

    /**
     * Auto-generated class UID.
     */
    private static final long serialVersionUID = 269168041517643087L;

    /**
     * Primary key of object being tracked.
     */
    @Column(name = "ENTITY_ID", nullable = false, updatable = false)
    private Long entityId;
    
    /**
     * Class type of object being tracked.
     */
    @JsonView(Views.SearchView.class)
    @SuppressWarnings({ "rawtypes" })
    @Column(name = "ENTITY_CLASS", nullable = false, updatable = false)
    private Class entityClass;
    
    /**
     * Arbitrary reference to object being tracked.
     * Use this attribute to store single reference string to different 
     * classes that are interrelated.
     */
    @JsonView(Views.SearchView.class)
    @Column(name = "REFERENCE")
    private String reference;
    
    /**
     * Message about the actions done.
     */
    @JsonView(Views.SearchView.class)
    @Column(name = "MESSAGE", nullable = false, updatable = false, length = 4000)
    private String message;
        
    /**
     * User who performed the change.
     */
    @JsonView(Views.SearchView.class)
    @Column(name = "USER_ID", nullable = false, updatable = false)
    private Long userId;

    /**
     * Name of user performing the change.
     */
    @JsonView(Views.SearchView.class)
    @Column(name = "USER_DISPLAY")
    private String userDisplayName;
    
    /**
     * Temporary reference to object being tracked.
     * Used by AuditLogListener when loading audit log object.
     */
    @Transient
    private transient Object object;
    	
    /**
     * Temporary reference to used who made the change.
     * Used by AuditLogListener when loading audit log object.
     */
    @Transient
    private transient BaseUser user;

    @Transient
	private transient Date startDate;
    
    @Transient
    private Date startDateForSearch;
	
    @Transient
	private transient Date endDate;
    
    @Transient
    private Date endDateForSearch;
	
	@Transient
	private transient String logAction;
    
	/**
     * Default constructor.
     */
    public AuditLog(){
    }
    
    /**
     * Standard constructor.
     * 
     * @param message message to log. If blank, message is automatically generated.
     * @param entityId id of object being tracked.
     * @param entityClass class name of object being tracked.
     * @param reference reference for group query.
     * @param userId user id of who made the change.
     * @param owner username of who made the change.
     */
    @SuppressWarnings({ "rawtypes" })
    public AuditLog(final String message, 
            final Long entityId, 
            final Class entityClass,
            final String reference,
            final Long userId,
            final String userDisplayName) {
        this.message = message;
        this.entityId = entityId;
        this.entityClass = entityClass;
        this.reference = reference;
        this.userId = userId;
        this.setCreateDate(new Date());
        this.setUserDisplayName(userDisplayName);
    }

	/**
	 * Getter method for entityId.
	 *
	 * @return the entityId
	 */
	public final Long getEntityId() {
		return entityId;
	}

	/**
	 * Setter method for entityId.
	 *
	 * @param entityId the entityId to set
	 */
	public final void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	/**
	 * Getter method for entityClass.
	 *
	 * @return the entityClass
	 */
	@SuppressWarnings("rawtypes")
	public final Class getEntityClass() {
		return entityClass;
	}

	/**
	 * Setter method for entityClass.
	 *
	 * @param entityClass the entityClass to set
	 */
	@SuppressWarnings("rawtypes")
	public final void setEntityClass(Class entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 * Getter method for reference.
	 *
	 * @return the reference
	 */
	public final String getReference() {
		return reference;
	}

	/**
	 * Setter method for reference.
	 *
	 * @param reference the reference to set
	 */
	public final void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * Getter method for message.
	 *
	 * @return the message
	 */
	public final String getMessage() {
		return message;
	}

	/**
	 * Setter method for message.
	 *
	 * @param message the message to set
	 */
	public final void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Getter method for userId.
	 *
	 * @return the userId
	 */
	public final Long getUserId() {
		return userId;
	}

	/**
	 * Setter method for userId.
	 *
	 * @param userId the userId to set
	 */
	public final void setUserId(Long userId) {
		this.userId = userId;
	}


	/**
	 * @return the userDisplayName
	 */
	public final String getUserDisplayName() {
		return userDisplayName;
	}

	/**
	 * @param userDisplayName the userDisplayName to set
	 */
	public final void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}

	/**
	 * Getter method for object.
	 *
	 * @return the object
	 */
	public final Object getObject() {
		return object;
	}

	/**
	 * Setter method for object.
	 *
	 * @param object the object to set
	 */
	public final void setObject(Object object) {
		this.object = object;
	}

	/**
	 * @return the user
	 */
	public final BaseUser getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public final void setUser(BaseUser user) {
		this.user = user;
	}

	/**
	 * Getter method for startDate.
	 *
	 * @return the startDate
	 */
	public final Date getStartDate() {
		return startDate;
	}

	/**
	 * Setter method for startDate.
	 *
	 * @param startDate the startDate to set
	 */
	public final void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Getter method for endDate.
	 *
	 * @return the endDate
	 */
	public final Date getEndDate() {
		return endDate;
	}

	/**
	 * Setter method for endDate.
	 *
	 * @param endDate the endDate to set
	 */
	public final void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Getter method for logAction.
	 *
	 * @return the logAction
	 */
	public final String getLogAction() {
		return logAction;
	}

	/**
	 * Setter method for logAction.
	 *
	 * @param logAction the logAction to set
	 */
	public final void setLogAction(String logAction) {
		this.logAction = logAction;
	}

	@JsonView(Views.SearchView.class)
	public String getUserNameDisplay() {
		if(this.user != null) {
			return "by " + this.user.getFirstName() + " " + this.user.getLastName();
		}
		
		return "";
	}
	
	@JsonView(Views.SearchView.class)
	public String getCreateDateForDisplay() {
		if(getCreateDate() != null)
			return new SimpleDateFormat("MMM. dd, yyyy hh:mm aa").format(getCreateDate());
		return "";
	}
	
	public void setStartDateForSearch(Date startDateForSearch) {
		this.startDateForSearch = startDateForSearch;
	}
	
	public Date getStartDateForSearch() {
		return startDateForSearch;
	}
	
	public void setEndDateForSearch(Date endDateForSearch) {
		this.endDateForSearch = endDateForSearch;
	}
	
	public Date getEndDateForSearch() {
		return endDateForSearch;
	}
	
}
