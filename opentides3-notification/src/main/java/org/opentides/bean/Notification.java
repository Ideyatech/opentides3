/*
 * Copyright 2007-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opentides.bean;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.opentides.bean.user.BaseUser;
import org.opentides.util.StringUtil;
import org.opentides.web.json.Views;

import com.fasterxml.jackson.annotation.JsonView;


/**
 * This class contains all the event triggers. When an event is triggered,
 * a new instance of EventQueue is created. This class is immutable.
 * 
 * @author allantan
 *
 */

@Entity
@Table(name = "NOTIFICATION_LOG")
public class Notification extends BaseEntity {

	public static enum Status {
		NEW,
		PROCESSED,
		FAILED
	}
	
	public static enum Medium {
		POPUP,
		EMAIL,
		SMS
	}
	
	private static final long serialVersionUID = 4857118082796914475L;
	
	/**
	 * Identifier to group multiple recipients in one event.
	 */
	@JsonView(Views.SearchView.class)
	@Column(name = "EVENTGROUP_ID", nullable = false)
	private Long eventGroupId;

	/**
	 * Reference to the type of event related to this notification.
	 */
	@JsonView(Views.SearchView.class)
	@JoinColumn(name = "EVENT_ID")
	private Event event;
	
	/**
	 * Subject used in email subject field.
	 */
	@Column(name="SUBJECT", length=1000)
	private String subject;
		
	/**
	 * Message to be displayed for notification
	 */
	@JsonView(Views.SearchView.class)
	@Column(name = "MESSAGE", length= 4000, nullable = false)
	private String message;
			
	/**
	 * Status of this notification
	 */
	@JsonView(Views.SearchView.class)
	@Column(name = "STATUS", nullable = false)
	private Status status;

	/**
	 * Type of notification
	 */
	@JsonView(Views.SearchView.class)
	@Column(name = "MEDIUM", nullable = false)
	private Medium medium;
	
    /**
     * Primary key of object being notified.
     */
    @Column(name = "ENTITY_ID", nullable = false, updatable = false)
    private Long entityId;
    
    /**
     * Class type of object being notified.
     */
    @JsonView(Views.SearchView.class)
    @SuppressWarnings({ "rawtypes" })
    @Column(name = "ENTITY_CLASS", nullable = false, updatable = false)
    private Class entityClass;

    /**
     * Email address or mobile number to receive the notification.
     */
    @Column(name = "RECEIPIENT_REF") 
    private String recipientReference;

    /**
     * User to receive the notification.
     */
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.EAGER)    
    @JoinColumn(name = "USER_ID") 
    private BaseUser recipientUser;

    @Column(name="REMARKS", length=4000)
    private String remarks;
    
    @Transient
	private transient Date startDate;
	
    @Transient
	private transient Date endDate;

    public Notification() {
		super();
	}

	/**
	 * @return the eventGroupId
	 */
	public Long getEventGroupId() {
		return eventGroupId;
	}

	/**
	 * @param eventGroupId the eventGroupId to set
	 */
	public void setEventGroupId(Long eventGroupId) {
		this.eventGroupId = eventGroupId;
	}

	/**
	 * @return the event
	 */
	public Event getEvent() {
		return event;
	}

	/**
	 * @param event the event to set
	 */
	public void setEvent(Event event) {
		this.event = event;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
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
	 * Returns the message for display on notification log
	 * @return
	 */
	public String getMessageDisplay() {
		String display = message;
		if (medium.equals(Medium.EMAIL)) {
			if (StringUtil.isEmpty(subject))
				display = "No Subject";
			else
				display = subject;
		} 
		if (display.length()>70)
			display = display.substring(0, 70) + "...";
		return display;
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
	 * @return the entityId
	 */
	public Long getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	/**
	 * @return the entityClass
	 */
	public Class getEntityClass() {
		return entityClass;
	}

	/**
	 * @param entityClass the entityClass to set
	 */
	public void setEntityClass(Class entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 * @return the medium
	 */
	public Medium getMedium() {
		return medium;
	}

	/**
	 * @param medium the medium to set
	 */
	public void setMedium(Medium medium) {
		this.medium = medium;
	}

	/**
	 * @return the recipientReference
	 */
	public String getRecipientReference() {
		return recipientReference;
	}

	/**
	 * @param recipientReference the recipientReference to set
	 */
	public void setRecipientReference(String recipientReference) {
		this.recipientReference = recipientReference;
	}

	/**
	 * @return the recipientUser
	 */
	public BaseUser getRecipientUser() {
		return recipientUser;
	}

	/**
	 * @param recipientUser the recipientUser to set
	 */
	public void setRecipientUser(BaseUser recipientUser) {
		this.recipientUser = recipientUser;
	}
	
	/**
	 * Displays the recipient details.
	 * 
	 * @return
	 */
	public String getRecipientDisplay() {
		StringBuffer display = new StringBuffer();
		if (medium == null) 
			return "No Medium Indicated";
		if (medium.equals(Medium.POPUP) && recipientUser!=null) 
			display.append("Notify "+recipientUser.getCompleteName()).append(" ");
		if (medium.equals(Medium.EMAIL))
			display.append("Email to ").append(recipientReference);
		if (medium.equals(Medium.SMS))
			display.append("SMS to").append(recipientReference);
		return display.toString();
	}

	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


}
