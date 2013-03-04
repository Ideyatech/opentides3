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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.Cache;
import org.opentides.annotation.Auditable;
import org.opentides.annotation.PrimaryField;
import org.opentides.annotation.SearchableFields;
import org.opentides.bean.BaseEntity;
import org.opentides.bean.PhotoInfo;
import org.opentides.bean.Photoable;
import org.opentides.util.StringUtil;
import org.opentides.web.json.Views;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Cache
@Table(name = "USER_PROFILE")
@Auditable
public class BaseUser extends BaseEntity implements Photoable {

	private static final long serialVersionUID = 7634675501487373408L;
	
	@Column(name = "FIRSTNAME")
	@JsonView(Views.FormView.class)
	private String firstName;

	@Column(name = "LASTNAME")
	@JsonView(Views.FormView.class)
	private String lastName;
	
	@Column(name = "MIDDLENAME", nullable=true)
	@JsonView(Views.FormView.class)
	private String middleName;

	@Column(name = "EMAIL", unique=true)
	@JsonView(Views.SearchView.class)
	private String emailAddress;
	
	@Column(name = "OFFICE", nullable=true)
	@JsonView(Views.SearchView.class)
	private String office;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
	@JsonView(Views.SearchView.class)
	private UserCredential credential;

	@ManyToMany
	@JoinTable(name = "USER_GROUP", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "GROUP_ID") })
	@JsonView(Views.SearchView.class)
	private Set<UserGroup> groups;
	
	@Column(name = "LASTLOGIN")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonView(Views.DisplayView.class)
	private Date lastLogin;	
	
	@Column(name="LANGUAGE")
	private String language;
	
	@Column(name="LAST_LOGIN_IP")
	@JsonView(Views.DisplayView.class)
	private String lastLoginIP;
	
	@Column(name="PREV_LOGIN_IP")
	@JsonView(Views.DisplayView.class)
	private String prevLoginIP;

	@Column(name="LAST_FAILED_IP")
	@JsonView(Views.DisplayView.class)
	private String lastFailedIP;

	@Column(name="TOTAL_LOGIN_COUNT")
	@JsonView(Views.DisplayView.class)
	private Long totalLoginCount;
	
	@Column(name="FAILED_LOGIN_COUNT")
	@JsonView(Views.DisplayView.class)
	private Long failedLoginCount;

	public BaseUser() {
		super();
		this.setCredential(new UserCredential());
		groups = new HashSet<UserGroup>();
	}
	
	/**
	 * Creates a clone of this object containing basic information including the following:
	 * firstName, lastName, middleName, emailAddress and lastLogin.
	 * This function is used to populate the user object associated to AuditLog.
	 * 
	 * Note: groups and credentials are not cloned.
	 * @param clone
	 * @return
	 */
	public BaseUser cloneUserProfile() {
		BaseUser clone = new BaseUser();
		clone.firstName    = this.firstName;
		clone.lastName     = this.lastName;
		clone.middleName   = this.middleName;
		clone.emailAddress = this.emailAddress;
		clone.office	   = this.office;
		clone.language	   = this.language;
		clone.lastLogin    = this.lastLogin;
		clone.credential   = this.credential;
		clone.lastFailedIP = this.lastFailedIP;
		clone.lastLoginIP  = this.lastLoginIP;
		clone.prevLoginIP  = this.prevLoginIP;
		clone.totalLoginCount  = this.totalLoginCount;
		clone.failedLoginCount = this.failedLoginCount;
		return clone;
	}

	public void addGroup(UserGroup group) {
		if (group == null)
			throw new IllegalArgumentException("Null group.");
		if (groups != null)
			groups.remove(group);
		groups.add(group);
	}

	public void removeGroup(UserGroup group) {
		if (group == null)
			throw new IllegalArgumentException("Null group.");
		if (groups != null)
			groups.remove(group);
	}
	
	/**
	 * Returns the complete name by concatenating lastName and firstName
	 * 
	 * @return
	 */
	@JsonView(Views.SearchView.class)
	public String getCompleteName() {
		String name = "";
		if (!StringUtil.isEmpty(getFirstName())) {
			name += getFirstName() + " ";
		}
		if (!StringUtil.isEmpty(getLastName())) {
			name += getLastName() + " ";
		}
		return name;
	}

	/**
	 * Returns Last Name, First Name Middle Name
	 * 
	 * @return
	 */
	@JsonView(Views.SearchView.class)
	public String getFullName() {
		String name = "";
		if (!StringUtil.isEmpty(getLastName())) {
			name += getLastName() + ", ";
		}
		if (!StringUtil.isEmpty(getFirstName())) {
			name += getFirstName();
		}
		if (!StringUtil.isEmpty(getMiddleName())) {
			name += " " + getMiddleName();
		}
		return name;
	}
	
	public boolean hasPermission(String permission) {
		for (UserGroup group : groups) {
			for (UserAuthority userRole : group.getAuthorities()) {
				if (permission.equals(userRole.getAuthority())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return getCompleteName();
	}
	
	@SearchableFields
	public List<String> searchableFields() {
		List<String> props = new ArrayList<String>();
		props.add("firstName");
		props.add("lastName");
		props.add("emailAddress");
		return props;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((emailAddress == null) ? 0 : emailAddress.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final BaseUser other = (BaseUser) obj;
		if (emailAddress == null) {
			if (other.emailAddress != null)
				return false;
		} else if (!emailAddress.equals(other.emailAddress))
			return false;
		return true;
	}

	/**
	 * Getter method for firstName.
	 *
	 * @return the firstName
	 */
	public final String getFirstName() {
		return firstName;
	}

	/**
	 * Setter method for firstName.
	 *
	 * @param firstName the firstName to set
	 */
	public final void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Getter method for lastName.
	 *
	 * @return the lastName
	 */
	public final String getLastName() {
		return lastName;
	}

	/**
	 * Setter method for lastName.
	 *
	 * @param lastName the lastName to set
	 */
	public final void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Getter method for middleName.
	 *
	 * @return the middleName
	 */
	public final String getMiddleName() {
		return middleName;
	}

	/**
	 * Setter method for middleName.
	 *
	 * @param middleName the middleName to set
	 */
	public final void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * Getter method for emailAddress.
	 *
	 * @return the emailAddress
	 */
	public final String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * Setter method for emailAddress.
	 *
	 * @param emailAddress the emailAddress to set
	 */
	public final void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the office
	 */
	public final String getOffice() {
		return office;
	}

	/**
	 * @param office the office to set
	 */
	public final void setOffice(String office) {
		this.office = office;
	}

	/**
	 * Getter method for credential.
	 *
	 * @return the credential
	 */
	public final UserCredential getCredential() {
		return credential;
	}

	/**
	 * Setter method for credential.
	 *
	 * @param credential the credential to set
	 */
	public final void setCredential(UserCredential credential) {
		this.credential = credential;
		credential.setUser(this);		
	}

	/**
	 * Getter method for groups.
	 *
	 * @return the groups
	 */
	public final Set<UserGroup> getGroups() {
		return groups;
	}

	/**
	 * Setter method for groups.
	 *
	 * @param groups the groups to set
	 */
	public final void setGroups(Set<UserGroup> groups) {
		this.groups = groups;
	}
	
	/**
	 * Returns the list of groups for display purposes
	 * @return
	 */
	@JsonView(value=Views.SearchView.class) 
	public final String getDisplayGroups() {
		StringBuffer display = new StringBuffer();
		int count = 0;
		for (UserGroup group:groups) {
			if (count++ > 0)
				display.append(", ");
			display.append(group.getName());
		}
		return display.toString();
		
	}

	/**
	 * Getter method for lastLogin.
	 *
	 * @return the lastLogin
	 */
	public final Date getLastLogin() {
		return lastLogin;
	}

	/**
	 * Setter method for lastLogin.
	 *
	 * @param lastLogin the lastLogin to set
	 */
	public final void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	/**
	 * Getter method for language.
	 *
	 * @return the language
	 */
	public final String getLanguage() {
		return language;
	}

	/**
	 * Setter method for language.
	 *
	 * @param language the language to set
	 */
	public final void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * Getter method for lastLoginIP.
	 *
	 * @return the lastLoginIP
	 */
	public final String getLastLoginIP() {
		return lastLoginIP;
	}

	/**
	 * Setter method for lastLoginIP.
	 *
	 * @param lastLoginIP the lastLoginIP to set
	 */
	public final void setLastLoginIP(String lastLoginIP) {
		this.lastLoginIP = lastLoginIP;
	}

	/**
	 * Getter method for prevLoginIP.
	 *
	 * @return the prevLoginIP
	 */
	public final String getPrevLoginIP() {
		return prevLoginIP;
	}

	/**
	 * Setter method for prevLoginIP.
	 *
	 * @param prevLoginIP the prevLoginIP to set
	 */
	public final void setPrevLoginIP(String prevLoginIP) {
		this.prevLoginIP = prevLoginIP;
	}

	/**
	 * Getter method for lastFailedIP.
	 *
	 * @return the lastFailedIP
	 */
	public final String getLastFailedIP() {
		return lastFailedIP;
	}

	/**
	 * Setter method for lastFailedIP.
	 *
	 * @param lastFailedIP the lastFailedIP to set
	 */
	public final void setLastFailedIP(String lastFailedIP) {
		this.lastFailedIP = lastFailedIP;
	}

	/**
	 * Getter method for totalLoginCount.
	 *
	 * @return the totalLoginCount
	 */
	public final Long getTotalLoginCount() {
		return totalLoginCount;
	}

	/**
	 * Setter method for totalLoginCount.
	 *
	 * @param totalLoginCount the totalLoginCount to set
	 */
	public final void setTotalLoginCount(Long totalLoginCount) {
		this.totalLoginCount = totalLoginCount;
	}

	/**
	 * Getter method for failedLoginCount.
	 *
	 * @return the failedLoginCount
	 */
	public final Long getFailedLoginCount() {
		return failedLoginCount;
	}

	/**
	 * Setter method for failedLoginCount.
	 *
	 * @param failedLoginCount the failedLoginCount to set
	 */
	public final void setFailedLoginCount(Long failedLoginCount) {
		this.failedLoginCount = failedLoginCount;
	}
	
	// Photoable requirements
	
	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "USER_PHOTO", 
			joinColumns = { @JoinColumn(name = "USER_ID", referencedColumnName = "ID") }, 
			inverseJoinColumns = @JoinColumn(name = "PHOTO_ID")
	)
	private List<PhotoInfo> photos;
	private transient MultipartFile photo;
	
	@Override
	public List<PhotoInfo> getPhotos() {
		return photos;
	}
	
	@Override
	public void setPhotos(List<PhotoInfo> photos) {
		this.photos = photos;
	}
	
	@Override
	public MultipartFile getPhoto() {
		return photo;
	}
	
	@Override
	public void setPhoto(MultipartFile photo) {
		this.photo = photo;
	}
	
	public void addPhoto(PhotoInfo photoInfo){
		synchronized (photoInfo) {
			if (photos == null){
				photos = new ArrayList<PhotoInfo>();
			}
			photos.add(photoInfo);
		}
	}
	
	// End of Photoable requirements
	
}
