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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.opentides.persistence.listener.AuditLogListener;

/**
 * This class is responsible for handling all audit functions needed to be
 * attached to the classes.
 * 
 * @author allantan
 */
@Entity
@EntityListeners({ AuditLogListener.class })
@Table(name = "HISTORY_LOG")
public class AuditLog implements Serializable {

    /**
     * Auto-generated class UID.
     */
    private static final long serialVersionUID = 269168041517643087L;

    /**
     * Primary key. Annotation is transfered to getter method to allow
     * overridding from subclass.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    
    /**
     * Create date.
     */
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    
    /**
     * Last update date.
     */
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    
    /**
     * Primary key of object being tracked.
     */
    @Column(name = "ENTITY_ID", nullable = false, updatable = false)
    private Long entityId;
    
    /**
     * Class type of object being tracked.
     */
    @SuppressWarnings({ "rawtypes" })
    @Column(name = "ENTITY_CLASS", nullable = false, updatable = false)
    private Class entityClass;
    
    /**
     * Arbitrary reference to object being tracked.
     * Use this attribute to store single reference string to different 
     * classes that are interrelated.
     */
    @Column(name = "REFERENCE")
    private String reference;
    
    /**
     * Message about the actions done.
     */
    @Lob
    @Column(name = "MESSAGE", nullable = false, updatable = false)
    private String message;
    
    /**
     * Shorter message for display as summary.
     */
    @Column(name = "SHORT_MESSAGE", nullable = false, updatable = false, length=255)
    private String shortMessage;
    
    /**
     * User who performed the change.
     */
    @Column(name = "USER_ID", nullable = false, updatable = false)
    private Long userId;

    /**
     * Name of user performing the change.
     */
    @Column(name = "USER_DISPLAY")
    private String userDisplayName;
    
    /**
     * Office that owns this object.
     * In most cases, this is office of the owner.
     */
    @Column(name = "USER_OFFICE")
    private String ownerOffice;

    /**
     * Temporary reference to object being tracked.
     * Used by AuditLogListener when loading audit log object.
     */
    @Transient
    private transient Object object;
    	
    @Transient
	private transient Date startDate;
	
    @Transient
	private transient Date endDate;
	
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
     * @param ownerOffice group of user who made the change.
     */
    @SuppressWarnings({ "rawtypes" })
    public AuditLog(final String message, 
            final Long entityId, 
            final Class entityClass,
            final String reference,
            final Long userId,
            final String userDisplayName,
            final String ownerOffice) {
        this.message = message;
        this.entityId = entityId;
        this.entityClass = entityClass;
        this.reference = reference;
        this.userId = userId;
        this.setCreateDate(new Date());
        this.setUserDisplayName(userDisplayName);
        this.setOwnerOffice(ownerOffice);
    }
    
    /**
     * 
     * @param ShortMessage
     * @param message
     * @param entityId
     * @param entityClass
     * @param reference
     * @param userId
     * @param owner
     * @param ownerOffice
     */
    @SuppressWarnings("rawtypes")
	public AuditLog(final String ShortMessage,
    		final String message, 
            final Long entityId, 
            final Class entityClass,
            final String reference,
            final Long userId,
            final String userDisplayName,
            final String ownerOffice){
    	this.shortMessage = ShortMessage;
    	this.message = message;
        this.entityId = entityId;
        this.entityClass = entityClass;
        this.reference = reference;
        this.userId = userId;
        this.setCreateDate(new Date());
        this.setUserDisplayName(userDisplayName);
        this.setOwnerOffice(ownerOffice);
    }

	/**
	 * Getter method for id.
	 *
	 * @return the id
	 */
	public final Long getId() {
		return id;
	}

	/**
	 * Setter method for id.
	 *
	 * @param id the id to set
	 */
	public final void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter method for createDate.
	 *
	 * @return the createDate
	 */
	public final Date getCreateDate() {
		return createDate;
	}

	/**
	 * Setter method for createDate.
	 *
	 * @param createDate the createDate to set
	 */
	public final void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * Getter method for updateDate.
	 *
	 * @return the updateDate
	 */
	public final Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * Setter method for updateDate.
	 *
	 * @param updateDate the updateDate to set
	 */
	public final void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
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
	 * Getter method for ShortMessage.
	 *
	 * @return the ShortMessage
	 */
	public final String getShortMessage() {
		return shortMessage;
	}

	/**
	 * Setter method for ShortMessage.
	 *
	 * @param ShortMessage the ShortMessage to set
	 */
	public final void setShortMessage(String ShortMessage) {
		this.shortMessage = ShortMessage;
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
	 * Getter method for ownerOffice.
	 *
	 * @return the ownerOffice
	 */
	public final String getOwnerOffice() {
		return ownerOffice;
	}

	/**
	 * Setter method for ownerOffice.
	 *
	 * @param ownerOffice the ownerOffice to set
	 */
	public final void setOwnerOffice(String ownerOffice) {
		this.ownerOffice = ownerOffice;
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
	
}
