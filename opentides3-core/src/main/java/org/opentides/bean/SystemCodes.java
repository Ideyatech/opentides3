/*
 * Copyright 2002-2007 the original author or authors.
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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.opentides.annotation.Auditable;
import org.opentides.annotation.GenerateCrudController;
import org.opentides.annotation.GenerateDao;
import org.opentides.annotation.GenerateService;
import org.opentides.annotation.PrimaryField;
import org.opentides.web.json.Views;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * The system codes class is a lookup table used for drop-downs. Developer must
 * ensure category is consistently used in the application.
 */
@GenerateCrudController
@GenerateService
@GenerateDao
@Entity
@Table(name = "SYSTEM_CODES", 
	indexes= {	@Index(columnList="CATEGORY_"),
			 	@Index(columnList="CREATEDATE")})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Auditable(label = "System Codes")
@JsonInclude(Include.NON_NULL)

public class SystemCodes extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -4142599915292096152L;
	private static final int CATEGORY_LIST_LENGTH = 52;

	@JsonView(Views.SearchView.class)
	@Column(name = "KEY_", unique = true, nullable = false)
	private String key;

	@PrimaryField
	@JsonView(Views.SearchView.class)
	@Column(name = "VALUE_", nullable = false)
	private String value;

	@JsonView(Views.SearchView.class)
	@Column(name = "CATEGORY_", nullable = false)
	private String category;

	@JsonView(Views.FormView.class)
	@Column(name = "NUMBER_VALUE")
	private Long numberValue;

	@Column(name = "SORT_ORDER")
	private Integer sortOrder;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonView(Views.FormView.class)
	@JoinColumn(name = "PARENT_", referencedColumnName = "KEY_")
	private SystemCodes parent;
	
	@JsonView(Views.SearchView.class)
	@Column(name = "PARENT_", insertable = false, updatable = false)
	private String parentKey;
	
	@Transient
	private String parentString;

	public SystemCodes() {
	}

	public SystemCodes(String key) {
		setKey(key);
	}

	public SystemCodes(String category, String key, String value) {
		setCategory(category);
		setKey(key);
		setValue(value);
	}

	public SystemCodes(String category, String key, String value,
			boolean skipAudit) {
		setCategory(category);
		setKey(key);
		setValue(value);
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
		this.key = key.trim().toUpperCase();
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
		this.category = category.trim().toUpperCase();
	}

	/**
	 * @return the sortOrder
	 */
	public final Integer getSortOrder() {
		return sortOrder;
	}

	/**
	 * @param sortOrder
	 *            the sortOrder to set
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

	/**
	 * @return the parent
	 */
	public SystemCodes getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 * @throws Exception
	 */
	public void setParent(SystemCodes parent) {
		this.parent = parent;
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
	 * 
	 * @return
	 */
	public String getTruncatedValue() {
		if (value == null)
			return null;
		if (value.length() > CATEGORY_LIST_LENGTH) {
			return value.substring(0, CATEGORY_LIST_LENGTH - 4) + "...";
		}
		return value;
	}

	/**
	 * Check if the parents is null and not equal to itself.
	 * 
	 * @param parent
	 * @return
	 */
	public boolean isParentValid() {
		SystemCodes parent = this.getParent();
		while (parent != null) {
			if (this.key.equals(parent.getKey())) {
				return false;
			}
			parent = parent.getParent();
		}
		return true;
	}

	public String getParentKey() {
		if(this.parentKey == null) {
			return "";
		}
		return this.parentKey;
	}
	
	public void setParentKey(String parentKey) {
		this.parentKey = parentKey;
	}
	
	public String getParentString() {
		return parentString;
	}
	
	public void setParentString(String parentString) {
		this.parentString = parentString;
	}
	
	@JsonView(Views.SearchView.class)
	public String getParentKeyString() {
		//if parentString is not null, this is for insert/update else search
		if(parentString != null) {
			//use parentString for displaying parent after insert
			return parentString;
		} else {
			//use parentKey for search
			return parentKey == null ? "" : parentKey;
		}
	}

}