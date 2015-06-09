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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.opentides.annotation.Auditable;
import org.opentides.annotation.PrimaryField;
import org.opentides.annotation.SearchableFields;
import org.opentides.bean.BaseEntity;
import org.opentides.bean.ImageInfo;
import org.opentides.bean.ImageUploadable;
import org.opentides.util.StringUtil;
import org.opentides.web.json.Views;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "USER_PROFILE")
@Auditable
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseUser extends BaseEntity implements ImageUploadable {

	private static final long serialVersionUID = 7634675501487373408L;

	@Column(name = "FIRSTNAME")
	@JsonView(Views.FormView.class)
	private String firstName;

	@Column(name = "LASTNAME")
	@JsonView(Views.FormView.class)
	private String lastName;

	@Column(name = "MIDDLENAME", nullable = true)
	@JsonView(Views.FormView.class)
	private String middleName;

	@Column(name = "EMAIL", unique = true)
	@JsonView(Views.SearchView.class)
	private String emailAddress;

	@Column(name = "OFFICE", nullable = true)
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

	@Column(name = "LANGUAGE")
	private String language;

	@Column(name = "LAST_LOGIN_IP")
	@JsonView(Views.DisplayView.class)
	private String lastLoginIP;

	@Column(name = "PREV_LOGIN_IP")
	@JsonView(Views.DisplayView.class)
	private String prevLoginIP;

	@Column(name = "LAST_FAILED_IP")
	@JsonView(Views.DisplayView.class)
	private String lastFailedIP;

	@Column(name = "TOTAL_LOGIN_COUNT")
	@JsonView(Views.DisplayView.class)
	private Long totalLoginCount;

	@Column(name = "FAILED_LOGIN_COUNT")
	@JsonView(Views.DisplayView.class)
	private Long failedLoginCount;

	@Column(name = "LAST_FAILED_LOGIN_MILLIS")
	@JsonView(Views.DisplayView.class)
	private Long lastFailedLoginMillis;

	// ImageUploadable requirements
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinTable(name = "USER_PHOTO", joinColumns = { @JoinColumn(name = "USER_ID", referencedColumnName = "ID") }, inverseJoinColumns = @JoinColumn(name = "PHOTO_ID"))
	@JsonView(Views.FormView.class)
	private List<ImageInfo> images;

	@Transient
	private transient MultipartFile photo;

	public BaseUser() {
		super();
		this.setCredential(new UserCredential());
		images = new ArrayList<ImageInfo>();
		groups = new HashSet<UserGroup>();
	}

	/**
	 * Creates a clone of this object containing basic information including the
	 * following: firstName, lastName, middleName, emailAddress and lastLogin.
	 * This function is used to populate the user object associated to AuditLog.
	 * 
	 * Note: groups and credentials are not cloned.
	 * 
	 * @param clone
	 * @return
	 */
	public BaseUser cloneUserProfile() {
		final BaseUser clone = new BaseUser();
		clone.firstName = firstName;
		clone.lastName = lastName;
		clone.middleName = middleName;
		clone.emailAddress = emailAddress;
		clone.office = office;
		clone.language = language;
		clone.lastLogin = lastLogin;
		clone.credential = credential;
		clone.lastFailedIP = lastFailedIP;
		clone.lastLoginIP = lastLoginIP;
		clone.prevLoginIP = prevLoginIP;
		clone.totalLoginCount = totalLoginCount;
		clone.failedLoginCount = failedLoginCount;
		return clone;
	}

	public void addGroup(final UserGroup group) {
		if (group == null) {
			throw new IllegalArgumentException("Null group.");
		}
		if (groups != null) {
			groups.remove(group);
		}
		groups.add(group);
	}

	public void removeGroup(final UserGroup group) {
		if (group == null) {
			throw new IllegalArgumentException("Null group.");
		}
		if (groups != null) {
			groups.remove(group);
		}
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
		return name.trim();
	}

	/**
	 * Returns the username from credential object
	 * 
	 * @return
	 */
	@PrimaryField(label = "Username")
	public String getUsername() {
		if (credential != null) {
			return credential.getUsername();
		} else {
			return null;
		}
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

	/**
	 * Checks if this user has the given permission.
	 * 
	 * @param permission
	 *            the permission to check
	 * @return true if user has the given permission, false otherwise
	 */
	public boolean hasPermission(final String permission) {
		if (groups == null) {
			return false;
		}
		for (final UserGroup group : groups) {
			for (final UserAuthority userRole : group.getAuthorities()) {
				if (permission.equals(userRole.getAuthority())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Get all authorities of the user
	 * 
	 * @return a list of {@link UserAuthority} objects
	 */
	public List<UserAuthority> getAuthorities() {
		final List<UserAuthority> permissions = new ArrayList<UserAuthority>();
		for (final UserGroup group : groups) {
			for (final UserAuthority userAuthority : group.getAuthorities()) {
				permissions.add(userAuthority);
			}
		}
		return permissions;
	}

	@Override
	public String toString() {
		return getCompleteName();
	}

	@SearchableFields
	public List<String> searchableFields() {
		final List<String> props = new ArrayList<String>();
		props.add("firstName");
		props.add("lastName");
		props.add("emailAddress");
		props.add("credential.username");
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
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final BaseUser other = (BaseUser) obj;
		if (emailAddress == null) {
			if (other.emailAddress != null) {
				return false;
			}
		} else if (!emailAddress.equals(other.emailAddress)) {
			return false;
		}
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
	 * @param firstName
	 *            the firstName to set
	 */
	public final void setFirstName(final String firstName) {
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
	 * @param lastName
	 *            the lastName to set
	 */
	public final void setLastName(final String lastName) {
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
	 * @param middleName
	 *            the middleName to set
	 */
	public final void setMiddleName(final String middleName) {
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
	 * @param emailAddress
	 *            the emailAddress to set
	 */
	public final void setEmailAddress(final String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the office
	 */
	public final String getOffice() {
		return office;
	}

	/**
	 * @param office
	 *            the office to set
	 */
	public final void setOffice(final String office) {
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
	 * @param credential
	 *            the credential to set
	 */
	public final void setCredential(final UserCredential credential) {
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
	 * @param groups
	 *            the groups to set
	 */
	public final void setGroups(final Set<UserGroup> groups) {
		this.groups = groups;
	}

	/**
	 * Returns the list of groups for display purposes
	 * 
	 * @return
	 */
	@JsonView(value = Views.SearchView.class)
	public final String getDisplayGroups() {
		final StringBuilder display = new StringBuilder();
		int count = 0;
		for (final UserGroup group : groups) {
			if (count++ > 0) {
				display.append(", ");
			}
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
	 * @param lastLogin
	 *            the lastLogin to set
	 */
	public final void setLastLogin(final Date lastLogin) {
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
	 * @param language
	 *            the language to set
	 */
	public final void setLanguage(final String language) {
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
	 * @param lastLoginIP
	 *            the lastLoginIP to set
	 */
	public final void setLastLoginIP(final String lastLoginIP) {
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
	 * @param prevLoginIP
	 *            the prevLoginIP to set
	 */
	public final void setPrevLoginIP(final String prevLoginIP) {
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
	 * @param lastFailedIP
	 *            the lastFailedIP to set
	 */
	public final void setLastFailedIP(final String lastFailedIP) {
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
	 * @param totalLoginCount
	 *            the totalLoginCount to set
	 */
	public final void setTotalLoginCount(final Long totalLoginCount) {
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
	 * @param failedLoginCount
	 *            the failedLoginCount to set
	 */
	public final void setFailedLoginCount(final Long failedLoginCount) {
		this.failedLoginCount = failedLoginCount;
	}

	/**
	 * Increment login count by 1
	 */
	public void incrementFailedLoginCount() {
		if (failedLoginCount == null) {
			failedLoginCount = 0l;
		}
		failedLoginCount++;
	}

	/**
	 * Set failedLoginCount to 0
	 */
	public void resetFailedLoginCount() {
		failedLoginCount = 0l;
	}

	/**
	 * @return the lastFailedLoginMillis
	 */
	public Long getLastFailedLoginMillis() {
		return lastFailedLoginMillis;
	}

	/**
	 * @param lastFailedLoginMillis
	 *            the lastFailedLoginMillis to set
	 */
	public void setLastFailedLoginMillis(final Long lastFailedLoginMillis) {
		this.lastFailedLoginMillis = lastFailedLoginMillis;
	}

	@Override
	public List<ImageInfo> getImages() {
		return images;
	}

	@Override
	public MultipartFile getImage() {
		return photo;
	}

	@Override
	public ImageInfo getPrimaryImage() {
		if (!CollectionUtils.isEmpty(images)) {
			for (final ImageInfo imageInfo : images) {
				if (imageInfo.getIsPrimary()) {
					return imageInfo;
				}
			}
		}
		return new ImageInfo();
	}

	public void setPhotos(final List<ImageInfo> photos) {
		images = photos;
	}

	public void setPhoto(final MultipartFile photo) {
		this.photo = photo;
	}

	@Override
	public void addImage(final ImageInfo photoInfo) {
		synchronized (photoInfo) {
			if (images == null) {
				images = new ArrayList<ImageInfo>();
			}
			images.add(photoInfo);
		}
	}

	// End of ImageUploadable requirements

	/**
	 * 
	 * @return class name
	 */
	public String getUserClass() {
		return this.getClass().getName();
	}

}
