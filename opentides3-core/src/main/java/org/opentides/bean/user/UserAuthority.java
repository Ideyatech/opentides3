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

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.opentides.annotation.Auditable;
import org.opentides.annotation.PrimaryField;
import org.opentides.bean.BaseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@AttributeOverride(name = "ID", column = @Column(insertable = false, updatable = false))
@Table(name = "USER_AUTHORITY")
@Auditable
public class UserAuthority extends BaseEntity {
	private static final long serialVersionUID = -2779918759002560767L;

	@Column(name = "USERNAME")
	private String username;

	@PrimaryField
	@Column(name = "AUTHORITY")
	private String authority;
	
	// userGroup is nullable, to support username linkage
	@ManyToOne(optional = false)
	@JoinColumn(name = "USERGROUP_ID", nullable = true)
	@JsonIgnore
	private UserGroup userGroup;

	public UserAuthority() {
		super();
	}

	public UserAuthority(UserGroup userGroup, String authority) {
		this.setUserGroup(userGroup);
		this.setAuthority(authority);
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the userGroup
	 */
	public UserGroup getUserGroup() {
		return userGroup;
	}

	/**
	 * @param userGroup
	 *            the userGroup to set
	 */
	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((authority == null) ? 0 : authority.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
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
		final UserAuthority other = (UserAuthority) obj;
		if (authority == null) {
			if (other.authority != null)
				return false;
		} else if (!authority.equals(other.authority))
			return false;
		return true;
	}

}
