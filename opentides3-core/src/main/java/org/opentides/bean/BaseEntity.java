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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.log4j.Logger;
import org.opentides.bean.user.SessionUser;
import org.opentides.persistence.listener.EntityCreatedByListener;
import org.opentides.persistence.listener.EntityDateListener;
import org.opentides.util.SecurityUtil;
import org.opentides.web.json.Views;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * 
 * This is the base class for all entity objects (model) of open-tides.
 * 
 * @author allantan
 */
@MappedSuperclass
@EntityListeners({ EntityDateListener.class, EntityCreatedByListener.class })
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @JsonView(Views.SearchView.class)    
    private Long id;
    
    /**
     * Create date.
     */
    @Column(name = "CREATEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    
    /**
     * Last update date.
     */
    @Column(name = "UPDATEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    
    /**
     * Creator of this object.
     */
    @Column(name = "CREATEDBY")
    private String createdBy;
    
    @Version
    @Column(name = "VERSION")
    private Long version;
    
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
     * List containing the different ordering
     */
    @Transient
    private List<SortField> sortFields;
    
    /**
     * Temporary variable to disable filtering of records.
     */
    @Transient
    private transient Boolean disableProtection;
    
    @Transient
    private Map<String, Object> hints;
    
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
        return this.getId() == null || this.getId() <= 0;
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
    public final Date getCreateDate() {
        return this.createDate;
    }

    /**
     * Setter method for create date.
     * 
     * @param createDate create date
     */
    public final void setCreateDate(final Date createDate) {
        if (this.createDate == null) {
            this.createDate = createDate;
        }
    }

    /**
     * Getter method for last update date.
     * 
     * @return last update date.
     */
    public final Date getUpdateDate() {
        return this.updateDate;
    }

    /**
     * Setter method for the last update date.
     * 
     * @param updateDate last update date
     */
    public final void setUpdateDate(final Date updateDate) {
        this.updateDate = updateDate;
    }

	/**
	 * @return the createdBy
	 */
	public final String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public final void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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
	 * @return the version
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

	/**
     * Sets the userId based on Acegi Context
     */
    public final void setUserId() {
        if (this.auditUserId == null) {
            final SessionUser user = SecurityUtil.getSessionUser();
            if (user!=null) {
	            this.auditUserId = user.getId();
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
    
    public List<SortField> getSortFields() {
		return sortFields;
	}
    
    public void setSortFields(List<SortField> sortFields) {
		this.sortFields = sortFields;
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
	
	public Map<String, Object> getHints() {
		return hints;
	}
	
	public void setHints(Map<String, Object> hints) {
		this.hints = hints;
	}
	
	public void addHint(String hintName, Object value) {
		if(this.hints == null) {
			this.hints = new HashMap<String, Object>();
		}
		hints.put(hintName, value);
	}
		
}
