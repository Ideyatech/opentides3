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
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.opentides.bean.user.SessionUser;
import org.opentides.persistence.listener.EntityDateListener;
import org.opentides.util.SecurityUtil;
import org.opentides.web.json.Views;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * 
 * This is the base class for all entity objects (model) of open-tides.
 * 
 * @author allantan
 */
@MappedSuperclass
@EntityListeners({ EntityDateListener.class })
public abstract class BaseEntity implements Serializable {
    
	private static final long serialVersionUID = 6411733051595827829L;

	/**
     * Class logger.
     */
    private static Logger _log = Logger.getLogger(BaseEntity.class);
    
    /**
     * Primary key using ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @JsonView(Views.SearchView.class)    
    private Long id;
    
    /**
     * Create date.
     */
    @Column(name = "CREATEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createDate;
    
    /**
     * Last update date.
     */
    @Column(name = "UPDATEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar updateDate;
    
    /**
     * Random id generated used for session mapping.
     * This id is used to avoid security hacks when user attempts
     * to alter the reference id from the web page.
     */
    private transient String secureId;
    
    /**
     * Id of user who created, updated or deleted the entity.
     * Used by AuditLog. 
     */
    @Transient
    private transient Long auditUserId;
    
    /**
     * Username who created, updated or deleted the entity.
     * Used by AuditLog. 
     */
    @Transient
    private transient String auditUsername;
    
    /**
     * Office of the user who created, updated or deleted the entity.
     * Used by AuditLog. 
     */
    @Transient
    private transient String auditOfficeName;
    
    /**
     * Indicator whether to skip audit log or not.
     */
	@Transient
	private transient Boolean skipAudit;

	/**
	 * Storage for keeping audit log message.
	 */
	@Transient
	private transient String auditMessage;
	
	/**
	 * Storage for keeping short audit log message.
	 */
	@Transient
	private transient String shortMessage;

	/**
     * Temporary variable for order direction (e.g. ASC or DESC).
     */
    @Transient
    private transient String orderFlow;

    /**
     * Temporary variable for order field
     */
    @Transient
    private transient String orderOption;
    
    /**
     * Temporary variable to disable filtering of records.
     */
    @Transient
    private transient Boolean disableProtection;
    

    /**
     * Setter method of id.
     * 
     * @param id primary key
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
	 * Getter method of id
     * @return
     */
    public Long getId() {
    	return this.id;
    }

    /**
     * Checks if object is new (not persisted).
     * 
     * @return true if new, else false.
     */
    public final boolean isNew() {
        return this.getId() == null;
    }

    /**
     * Checks if object is new (not persisted).
     * @See isNew()
     * 
     * @return true if new, else false.
     */
    public final boolean getIsNew() {
        return this.isNew();
    }

    /**
     * Getter method for create date.
     * 
     * @return create date
     */
    public final Calendar getCreateDate() {
        return this.createDate;
    }

    /**
     * Setter method for create date.
     * 
     * @param createDate create date
     */
    public final void setCreateDate(final Calendar createDate) {
        if (this.createDate == null) {
            this.createDate = createDate;
        }
    }

    /**
     * Getter method for last update date.
     * 
     * @return last update date.
     */
    public final Calendar getUpdateDate() {
        return this.updateDate;
    }

    /**
     * Setter method for the last update date.
     * 
     * @param updateDate last update date
     */
    public final void setUpdateDate(final Calendar updateDate) {
        this.updateDate = updateDate;
    }

    /**
	 * @return the secureId
	 */
	public final String getSecureId() {
		return secureId;
	}

	/**
	 * @param secureId the secureId to set
	 */
	public final void setSecureId(String secureId) {
		this.secureId = secureId;
	}

	/**
     * Getter method of user id.
     * 
     * @return the auditUserId
     */
    public final Long getAuditUserId() {
        return this.auditUserId;
    }

    /**
     * Setter method of user id.
     * 
     * @param auditUserId the auditUserId to set
     */
    public final void setAuditUserId(final Long auditUserId) {
        this.auditUserId = auditUserId;
    }

    /**
     * Getter method of username.
     * 
     * @return the auditUsername
     */
    public final String getAuditUsername() {
        return this.auditUsername;
    }

    /**
     * Setter method of username.
     * 
     * @param auditUsername the auditUsername to set
     */
    public final void setAuditUsername(final String auditUsername) {
        this.auditUsername = auditUsername;
    }

    /**
     * Getter method of office name.
     * It is recommended that office is referenced in SystemCodes 
     * under category 'OFFICE'.
     * 
     * @return the auditOfficeName
     */
    public final String getAuditOfficeName() {
        return this.auditOfficeName;
    }

    /**
     * Setter method of office name.
     * It is recommended that office is referenced in SystemCodes 
     * under category 'OFFICE'.
     *
     * @param auditOfficeName the auditOfficeName to set
     */
    public final void setAuditOfficeName(final String auditOfficeName) {
        this.auditOfficeName = auditOfficeName;
    }

    /**
     * Sets the userId based on Acegi Context
     */
    public final void setUserId() {
        if (this.auditUserId == null) {
            final SessionUser user = SecurityUtil.getSessionUser();
            if (user!=null) {
	            this.auditUserId = user.getRealId();
	            this.auditOfficeName = user.getOffice();
	            this.auditUsername = user.getUsername();
            }        	
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final BaseEntity other = (BaseEntity) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }
	
	public String getPrimaryField() {
		return "";
	}
	
	public String getPrimaryFieldLabel() {
		return "";		
	}

	/**
	 * Getter method for skipAudit.
	 *
	 * @return the skipAudit
	 */
	public Boolean isSkipAudit() {
		if (skipAudit==null) 
			return false;
		return skipAudit;
	}

	/**
	 * Setter method for skipAudit.
	 *
	 * @param skipAudit the skipAudit to set
	 */
	public void setSkipAudit(Boolean skipAudit) {
		this.skipAudit = skipAudit;
	}

	/**
	 * Getter method for auditMessage.
	 *
	 * @return the auditMessage
	 */
	public String getAuditMessage() {
		return auditMessage;
	}

	/**
	 * Setter method for auditMessage.
	 *
	 * @param auditMessage the auditMessage to set
	 */
	public void setAuditMessage(String auditMessage) {
		this.auditMessage = auditMessage;
	}

    /**
     * Default getReference. Override for specific reference.
     * @return
     */
	public String getReference() {
		return null;
	} 
	
	/**
	 * Getter method for shortMessage.
	 *
	 * @return the shortMessage
	 */
	public String getShortMessage() {
		return shortMessage;
	}

	/**
	 * Setter method for shortMessage.
	 *
	 * @param shortMessage the shortMessage to set
	 */
	public void setShortMessage(String shortMessage) {
		this.shortMessage = shortMessage;
	}

    /**
     * Getter method of order flow.
     * 
     * @return the orderFlow
     */
    public final String getOrderFlow() {
        return this.orderFlow;
    }

    /**
     * Setter method of order flow.
     * 
     * @param orderFlow
     *            the orderFlow to set
     */
    public final void setOrderFlow(final String orderFlow) {
        if ("ASC".equalsIgnoreCase(orderFlow)
                || "DESC".equalsIgnoreCase(orderFlow)) {
            this.orderFlow = orderFlow;
        } else {
            _log.warn("Attempt to set orderOption with value [" + orderFlow
                    + "] for class [" + this.getClass().getSimpleName() + "].");
        }
    }

    /**
     * Getter method for order option.
     * 
     * @return the orderOption
     */
    public final String getOrderOption() {
        return this.orderOption;
    }

    /**
     * Setter method for order option.
     * 
     * @param orderOption
     *            the orderOption to set
     */
    public final void setOrderOption(final String orderOption) {
        // TODO: Add validation to ensure orderOption refers to valid fields.
        this.orderOption = orderOption;
    }

	/**
	 * @return the disableProtection
	 */
	public final Boolean getDisableProtection() {
		if (disableProtection==null)
			return false;
		return disableProtection;
	}

	/**
	 * @param disableProtection the disableProtection to set
	 */
	public final void setDisableProtection(Boolean disableProtection) {
		this.disableProtection = disableProtection;
	}    
}
