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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.opentides.annotation.Auditable;
import org.opentides.annotation.SearchableFields;
import org.opentides.util.StringUtil;
import org.opentides.web.json.Views;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * The Widget entity is responsible for holding the dashboard widgets and and its cache.
 * To make dashboard load efficient, a cache is maintained to reduce large database queries.
 * User must be careful in using cache settings to avoid stale data.
 * 
 * @author ideyatech
 */
@Auditable
@Entity
@Table(name = "WIDGET")
public class Widget extends BaseEntity {
	private static final long serialVersionUID = -4154117306413896179L;
	
	public static final String TYPE_HTML = "html";
	public static final String TYPE_IMAGE = "image";
	
	@JsonView(Views.SearchView.class)
	@Column(name = "NAME", unique = true)
	private String name;
	
	@JsonView(Views.SearchView.class)
	@Column(name = "TITLE")
	private String title;
	
	@JsonView(Views.SearchView.class)
	@Column(name = "IS_SHOWN")
	private Boolean isShown;
	
	// this is the property that will hold the screenshot
	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="SCREENSHOT_ID")
	private List<FileInfo> screenshot;
	
	@JsonView(Views.FormView.class)
	@Column(name = "DESCRIPTION")
	private String description;

	@JsonView(Views.FormView.class)
	@Column(name = "URL", unique = true)
	private String url;
	
	@JsonView(Views.FormView.class)
	@Column(name = "ACCESS_CODE")
	private String accessCode;
	
	@JsonView(Views.FormView.class)
	@Column(name = "CACHE_DURATION")
	private long cacheDuration;

	@JsonView(Views.SearchView.class)
	@Column(name = "LAST_CACHE_UPDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastCacheUpdate;

	@Lob
	@Column(name = "CACHE")
	private byte[] cache;

	@Column(name = "CACHE_TYPE")
	private String cacheType;
	
	@JsonView(Views.FormView.class)
	@Column(name = "IS_USER_DEFINED")
	private Boolean isUserDefined;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="widget", fetch = FetchType.LAZY)
    private List<UserWidgets> userWidget;
	
	@Transient
	private transient boolean isInstalled = false;
	
	public Widget() {
		super();
	}
	
	public Widget(String url, Widget parent) {
		this.setCacheDuration(parent.getCacheDuration());
		this.setAccessCode(parent.getAccessCode());
		this.setUrl(url);
		this.setLastCacheUpdate(parent.getLastCacheUpdate());
	}

	/**
	 * @return the name
	 */
	public String getName() {
		if(!StringUtil.isEmpty(name))
			return name.trim();
		else
			return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		if(!StringUtil.isEmpty(name))
			this.name = name.trim();
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the isShown
	 */
	public Boolean getIsShown() {
		return isShown;
	}

	/**
	 * @param isShown the isShown to set
	 */
	public void setIsShown(Boolean isShown) {
		this.isShown = isShown;
	}

	/**
	 * @return the screenshot
	 */
	public List<FileInfo> getScreenshot() {
		return screenshot;
	}

	/**
	 * @param screenshot the screenshot to set
	 */
	public void setScreenshot(List<FileInfo> screenshot) {
		this.screenshot = screenshot;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the accessCode
	 */
	public String getAccessCode() {
		return accessCode;
	}

	/**
	 * @param accessCode the accessCode to set
	 */
	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	/**
	 * @return the cacheDuration
	 */
	public long getCacheDuration() {
		return cacheDuration;
	}

	/**
	 * @param cacheDuration the cacheDuration to set
	 */
	public void setCacheDuration(long cacheDuration) {
		this.cacheDuration = cacheDuration;
	}

	/**
	 * @return the lastCacheUpdate
	 */
	public Date getLastCacheUpdate() {
		return lastCacheUpdate;
	}

	/**
	 * @param lastCacheUpdate the lastCacheUpdate to set
	 */
	public void setLastCacheUpdate(Date lastCacheUpdate) {
		this.lastCacheUpdate = lastCacheUpdate;
	}

	/**
	 * @return the cache
	 */
	public byte[] getCache() {
		return cache;
	}

	/**
	 * @param cache the cache to set
	 */
	public void setCache(byte[] cache) {
		this.cache = cache;
	}

	/**
	 * @return the cacheType
	 */
	public String getCacheType() {
		return cacheType;
	}

	/**
	 * @param cacheType the cacheType to set
	 */
	public void setCacheType(String cacheType) {
		this.cacheType = cacheType;
	}

	/**
	 * @return the isUserDefined
	 */
	public Boolean getIsUserDefined() {
		return isUserDefined;
	}

	/**
	 * @param isUserDefined the isUserDefined to set
	 */
	public void setIsUserDefined(Boolean isUserDefined) {
		this.isUserDefined = isUserDefined;
	}

	/**
	 * @return the userWidget
	 */
	public List<UserWidgets> getUserWidget() {
		return userWidget;
	}

	/**
	 * @param userWidget the userWidget to set
	 */
	public void setUserWidget(List<UserWidgets> userWidget) {
		this.userWidget = userWidget;
	}

	/**
	 * @return the isInstalled
	 */
	public boolean getIsInstalled() {
		return isInstalled;
	}

	/**
	 * @param isInstalled the isInstalled to set
	 */
	public void setIsInstalled(boolean isInstalled) {
		this.isInstalled = isInstalled;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((accessCode == null) ? 0 : accessCode.hashCode());
		result = prime * result + Arrays.hashCode(cache);
		result = prime * result
				+ (int) (cacheDuration ^ (cacheDuration >>> 32));
		result = prime * result
				+ ((cacheType == null) ? 0 : cacheType.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((isShown == null) ? 0 : isShown.hashCode());
		result = prime * result
				+ ((isUserDefined == null) ? 0 : isUserDefined.hashCode());
		result = prime * result
				+ ((lastCacheUpdate == null) ? 0 : lastCacheUpdate.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((screenshot == null) ? 0 : screenshot.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		Widget other = (Widget) obj;
		if (accessCode == null) {
			if (other.accessCode != null)
				return false;
		} else if (!accessCode.equals(other.accessCode))
			return false;
		if (!Arrays.equals(cache, other.cache))
			return false;
		if (cacheDuration != other.cacheDuration)
			return false;
		if (cacheType == null) {
			if (other.cacheType != null)
				return false;
		} else if (!cacheType.equals(other.cacheType))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (isShown == null) {
			if (other.isShown != null)
				return false;
		} else if (!isShown.equals(other.isShown))
			return false;
		if (isUserDefined == null) {
			if (other.isUserDefined != null)
				return false;
		} else if (!isUserDefined.equals(other.isUserDefined))
			return false;
		if (lastCacheUpdate == null) {
			if (other.lastCacheUpdate != null)
				return false;
		} else if (!lastCacheUpdate.equals(other.lastCacheUpdate))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (screenshot == null) {
			if (other.screenshot != null)
				return false;
		} else if (!screenshot.equals(other.screenshot))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	
	@SearchableFields
	public List<String> searchProperties() {
		List<String> searchProps = new ArrayList<>();
		searchProps.add("name");
		searchProps.add("title");
		searchProps.add("url");
		searchProps.add("accessCode");
		return searchProps;
	}
	
}