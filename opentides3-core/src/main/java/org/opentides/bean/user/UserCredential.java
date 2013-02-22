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

package org.opentides.bean.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Cache;
import org.opentides.annotation.Auditable;
import org.opentides.annotation.PrimaryField;
import org.opentides.bean.BaseEntity;
import org.opentides.web.json.Views;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;


@Entity
@Cache
@Table(name="USERS")
@Auditable

public class UserCredential extends BaseEntity {
	private static final long serialVersionUID = -8078097647300665926L;
	
	@PrimaryField
	@Column(name = "USERNAME", unique=true)
	@JsonView(Views.SearchView.class)
	private String username;
	
	@Column(name = "PASSWORD", nullable=false)
	private String password;
	
	private transient String newPassword;

	private transient String confirmPassword;

	@Column(name="ENABLED")
	@JsonView(Views.SearchView.class)
	private Boolean enabled;
	
	@OneToOne
    @JoinColumn(name="USERID", nullable=false)
	@JsonIgnore
	private BaseUser user;
	
	/**
	 * @return the user
	 */
	public BaseUser getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(BaseUser user) {
		this.user = user;
	}
	public UserCredential() {
		enabled=false;
	}

	/**
     * Getter method for username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    /**
     * Setter method for username.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * Getter method for newPassword.
     *
     * @return the newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }
    /**
     * Setter method for newPassword.
     *
     * @param newPassword the newPassword to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    /**
     * Getter method for confirmPassword.
     *
     * @return the confirmPassword
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }
    /**
     * Setter method for confirmPassword.
     *
     * @param confirmPassword the confirmPassword to set
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    /**
     * Getter method for enabled.
     *
     * @return the enabled
     */
    public Boolean getEnabled() {
        return enabled;
    }
    /**
     * Setter method for enabled.
     *
     * @param enabled the enabled to set
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
		
    /**
     * Ensures that password is enrypted according to configured passwordEncoder.
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserCredential other = (UserCredential) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}
