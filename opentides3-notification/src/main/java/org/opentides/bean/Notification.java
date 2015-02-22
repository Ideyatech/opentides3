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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
		IN_PROCESS,
		PROCESSED,
		FAILED
	}
	
	public static enum Medium {
		NONE,
		POPUP,
		EMAIL,
		SMS
	}
	
	private static final long serialVersionUID = 4857118082796914475L;
	
    /**
     * Notify date.
     */
    @Column(name = "NOTIFY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date notifyDate;

	/**
	 * Identifier to group multiple recipients in one event.
	 */
	@JsonView(Views.SearchView.class)
	@Column(name = "EVENTGROUP_ID", nullable = true)
	private Long eventGroupId;

	/**
	 * Subject used in email subject field.
	 */
	@JsonView(Views.SearchView.class)
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
	private String status;

	/**
	 * Type of notification
	 */
	@JsonView(Views.SearchView.class)
	@Column(name = "MEDIUM", nullable = false)
	private String medium;
	
    /**
     * Primary key of object being notified.
     */	
	@JsonView(Views.SearchView.class)    
    @Column(name = "ENTITY_ID", updatable = false)
    private Long entityId;
    
    /**
     * Class type of object being notified.
     */
    @SuppressWarnings({ "rawtypes" })
	@JsonView(Views.SearchView.class)    
    @Column(name = "ENTITY_CLASS", updatable = false)
    private Class entityClass;

    /**
     * Email address or mobile number to receive the notification.
     */
	@JsonView(Views.SearchView.class)    
    @Column(name = "RECIPIENT_REF") 
    private String recipientReference;
    
	@JsonView(Views.SearchView.class)
    @Column(name="EMAIL_REPLYTO")
    private String emailReplyTo;
    
	@JsonView(Views.SearchView.class)
    @Column(name="EMAIL_CC")
    private String emailCC;
	
	@JsonView(Views.FullView.class)
	@Column(name="ATTACHMENT")
	private String attachment;

    /**
     * User to receive the notification.
     */
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.EAGER)    
    @JoinColumn(name = "USER_ID") 
	@JsonView(Views.FullView.class)
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

	public Date getNotifyDate() {
		if (notifyDate==null)
			return getCreateDate();
		else
			return notifyDate;
	}

	public void setNotifyDate(Date notifyDate) {
		this.notifyDate = notifyDate;
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
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = "[TMS] "+ subject;
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
	@SuppressWarnings("rawtypes")
	public Class getEntityClass() {
		return entityClass;
	}

	/**
	 * @param entityClass the entityClass to set
	 */
	@SuppressWarnings("rawtypes")
	public void setEntityClass(Class entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the medium
	 */
	public String getMedium() {
		return medium;
	}

	/**
	 * @param medium the medium to set
	 */
	public void setMedium(String medium) {
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
	 * @return the emailReplyTo
	 */
	public String getEmailReplyTo() {
		return emailReplyTo;
	}

	/**
	 * @param emailReplyTo the emailReplyTo to set
	 */
	public void setEmailReplyTo(String emailReplyTo) {
		this.emailReplyTo = emailReplyTo;
	}

	/**
	 * @return the emailCC
	 */
	public String getEmailCC() {
		return emailCC;
	}

	/**
	 * @param emailCC the emailCC to set
	 */
	public void setEmailCC(String emailCC) {
		this.emailCC = emailCC;
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
	 * @return the attachment
	 */
	public String getAttachment() {
		return attachment;
	}

	/**
	 * @param attachment the attachment to set
	 */
	public void setAttachment(String attachment) {
		this.attachment = attachment;
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
