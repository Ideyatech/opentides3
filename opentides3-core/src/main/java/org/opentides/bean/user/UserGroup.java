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

package org.opentides.bean.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.opentides.annotation.Auditable;
import org.opentides.annotation.PrimaryField;
import org.opentides.bean.BaseEntity;
import org.opentides.util.StringUtil;
import org.opentides.web.json.Views;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "USERGROUP")
@Auditable
public class UserGroup extends BaseEntity{
	private static final long serialVersionUID = 1959110420702540834L;

	@PrimaryField
	@Column(name = "NAME", unique = true, nullable = false)
	@JsonView(Views.SearchView.class)
	private String name;

	@Column(name = "DESCRIPTION")
	@JsonView(Views.SearchView.class)
	private String description;

	@ManyToMany(mappedBy = "groups")
	@JsonIgnore	
	private Set<BaseUser> users;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "userGroup", fetch = FetchType.EAGER)
	private Set<UserAuthority> authorities;

	@Transient
	private transient List<UserAuthority> removeList = new ArrayList<UserAuthority>(); // list of user roles for deletion
	
	@Transient
	private transient List<UserAuthority> addedList = new ArrayList<UserAuthority>(); // list of user roles to add
	
	@Transient
	private transient List<String> authorityNames; // used for checkboxes in UI
	
	@Transient
	private transient Map<String, String> userAuthorityMap; // user for permission matrix in UI

	public UserGroup() {
		authorities = new HashSet<UserAuthority>();
		authorityNames = new ArrayList<String>();
	}

	/**
	 * @return the roleNames
	 */
	@JsonView(Views.FormView.class)
	public List<String> getAuthorityNames() {
		if (authorityNames == null || authorityNames.isEmpty())
			syncAuthorityNames();
		return authorityNames;
	}

	/**
	 * @param authorityNames
	 *            the authorityNames to set
	 */
	public void setAuthorityNames(List<String> authorityNames) {
		removeList = new ArrayList<UserAuthority>();
		addedList = new ArrayList<UserAuthority>();
		this.authorityNames = new ArrayList<String>();
		if (authorityNames == null) {
			for (UserAuthority auth : authorities) {
				removeList.add(auth);
			}
			for (UserAuthority auth : removeList) {
				this.removeAuthority(auth);
			}
			return;
		}
		this.authorityNames.addAll(authorityNames);
		for (UserAuthority role : authorities) {
			if (authorityNames.contains(role.getAuthority())) {
				// we keep the role, and remove the name
				authorityNames.remove(role.getAuthority());
			} else {
				// this role has been removed
				removeList.add(role);
			}
		}
		for (UserAuthority role : removeList) {
			this.removeAuthority(role);
		}
		// now we need to add what's left in rNames
		for (String name : authorityNames) {
			addedList.add(new UserAuthority(this, name));
			authorities.add(new UserAuthority(this, name));
		}
	}

	/**
	 * Add authority to group authorities
	 * 
	 * @param authority
	 * @return true if add successful otherwise false
	 */
	public boolean addAuthority(UserAuthority authority) {
		if (authority == null)
			throw new IllegalArgumentException("Empty authority is not allowed.");
		if (authorities != null) {
			authorities.remove(authority);
		}
		authority.setUserGroup(this);
		return authorities.add(authority);
	}

	/**
	 * Remove authority from group authorities
	 * 
	 * @param authority
	 * @return true if remove successful otherwise false
	 */
	public boolean removeAuthority(UserAuthority authority) {
		if (authority != null)
			return authorities.remove(authority);
		else
			return false;
	}
	

	/**
	 * Remove role from group authority by name
	 * 
	 * @param role
	 * @return true if remove successful otherwise false
	 */
	public boolean removeAuthority(String authorityName) {
		if (StringUtil.isEmpty(authorityName))
			return false;
		for(UserAuthority role: getAuthorities()) {
			if (authorityName.equals(role.getAuthority())) {
				removeAuthority(role);
				return true;
			}
		}
		return false;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getVerticalTitle() {
		StringBuffer vert = new StringBuffer(); 
		for (int i=0; i<name.length(); i++) {
			vert.append(name.charAt(i)).append("<br/>");
		}
		return vert.toString();
	}
	
	@JsonView(Views.SearchView.class)
	public Integer getPermissionCount() {
		if (authorities!=null)
			return authorities.size();
		else 
			return 0;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<UserAuthority> getAuthorities() {
		return authorities;
	}

	public void mapAuthorityMatrix(Map<String, String> allRoles) {
		userAuthorityMap = new HashMap<String, String>();
		List<String> roleKeys = new ArrayList<String>();
		for (UserAuthority role:this.authorities)
			roleKeys.add(role.getAuthority());
		for (String key:allRoles.keySet()) {
			if (roleKeys.contains(key)) {
				userAuthorityMap.put(key, allRoles.get(key));
			}
		}
	}
	
	public Map<String, String> getUserAuthorityMap() {
		return userAuthorityMap;
	}
	
	public void setAuthorities(Set<UserAuthority> authorities) {
		this.authorities = authorities;
		syncAuthorityNames();
	}

	public void syncAuthorityNames() {
		authorityNames = new ArrayList<String>();
		for (UserAuthority auth : authorities) {
			authorityNames.add(auth.getAuthority());
		}
	}
	
	/**
	 * Getter method for removeList.
	 *
	 * @return the removeList
	 */
	public final List<UserAuthority> getRemoveList() {
		return removeList;
	}

	/**
	 * Getter method for addedList.
	 *
	 * @return the addedList
	 */
	public List<UserAuthority> getAddedList() {
		return addedList;
	}

	/**
	 * @return the users
	 */
	public Set<BaseUser> getUsers() {
		return users;
	}

	/**
	 * @param users
	 *            the users to set
	 */
	public void setUsers(Set<BaseUser> users) {
		this.users = users;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((name == null) ? 0 : name.hashCode());
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
		final UserGroup other = (UserGroup) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}