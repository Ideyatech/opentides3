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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.opentides.annotation.Auditable;
import org.opentides.annotation.GenerateCrudController;
import org.opentides.annotation.GenerateDao;
import org.opentides.annotation.GenerateService;
import org.opentides.annotation.PrimaryField;
import org.opentides.util.StringUtil;
import org.opentides.web.json.Views;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * The system codes class is a lookup table used for drop-downs. Developer must
 * ensure category is consistently used in the application.
 */
@GenerateCrudController
@GenerateService
@GenerateDao
@Entity
@Table(name = "SYSTEM_CODES")
@Cache(type = CacheType.SOFT, size = 64000)
@Auditable
public class SystemCodes extends BaseEntity implements Serializable {


	private static final long serialVersionUID = -4142599915292096152L;
	private static final int CATEGORY_LIST_LENGTH = 52;

	@PrimaryField
	@JsonView(Views.SearchView.class)
	@Column(name = "KEY_", unique = true, nullable=false)
	private String key;

	@JsonView(Views.SearchView.class)
	@Column(name = "VALUE_", nullable=false)
	private String value;
	
	@JsonView(Views.SearchView.class)
	@Column(name="CATEGORY_", nullable=false)
	private String category;

	@JsonView(Views.SearchView.class)
	@Column(name="NUMBER_VALUE")
	private Long numberValue;
	
	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	public SystemCodes() {
	}

	public SystemCodes(String key) {
		this.key = key;
	}

	public SystemCodes(String category, String key, String value) {
		this.category = category;
		this.key = key;
		this.value = value;
	}

	public SystemCodes(String category, String key, String value,
			boolean skipAudit) {
		this.category = category;
		this.key = key;
		this.value = value;
		this.setSkipAudit(skipAudit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return key + ":" + value;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key.trim();
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value.trim();
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the sortOrder
	 */
	public final Integer getSortOrder() {
		return sortOrder;
	}

	/**
	 * @param sortOrder the sortOrder to set
	 */
	public final void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * @return the numberValue
	 */
	public synchronized Long getNumberValue() {
		return this.numberValue;
	}

	/**
	 * @param numberValue
	 *            the numberValue to set
	 */
	public synchronized void setNumberValue(Long numberValue) {
		this.numberValue = numberValue;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SystemCodes other = (SystemCodes) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	/**
	 * Displays the value in truncated form.
	 * @return
	 */
	public String getTruncatedValue() {
		if (value ==null) 
			return null;
		if (value.length() > CATEGORY_LIST_LENGTH) {
			return value.substring(0, CATEGORY_LIST_LENGTH - 4) + "...";
		}
		return value;
	}

}