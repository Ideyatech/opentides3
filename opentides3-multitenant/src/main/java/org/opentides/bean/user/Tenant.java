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
package org.opentides.bean.user;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.opentides.annotation.Auditable;
import org.opentides.bean.BaseEntity;
import org.opentides.web.json.Views;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * @author allantan
 *
 */
@Entity
@Table(name = "TENANT")
@Auditable
public class Tenant extends BaseEntity {

	private static final long serialVersionUID = 7098049941717350892L;

	@Column(name="COMPANY")
	@JsonView(Views.SearchView.class)	
	private String company;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "OWNER_ID", nullable = false)
	@JsonView(Views.SearchView.class)
	private MultitenantUser owner;
	
	@OneToMany(mappedBy = "tenant")
	@JsonView(Views.FormView.class)
	private Set<MultitenantUser> users;
	
	@Column(name="_SCHEMA")
	@JsonView(Views.SearchView.class)
	private String schema;

	@Column(name="DB_VERSION")	
	@JsonView(Views.FormView.class)	
	private Long dbVersion;
	
	@ManyToOne
	@JoinColumn(name = "ACCOUNT_TYPE_ID")
	@JsonView(Views.SearchView.class)	
	private AccountType accountType;
	
	@Column(name = "EXPIRATION_DATE")
	@JsonView(Views.SearchView.class)	
    @Temporal(TemporalType.TIMESTAMP)
	private Date expirationDate;
	
	@Transient
	private transient String name;
	
	/**
	 * @return the owner
	 */
	public final MultitenantUser getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public final void setOwner(final MultitenantUser owner) {
		this.owner = owner;
	}

	/**
	 * @return the users
	 */
	public final Set<MultitenantUser> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public final void setUsers(final Set<MultitenantUser> users) {
		this.users = users;
	}

	/**
	 * @return the schema
	 */
	public final String getSchema() {
		return schema;
	}

	/**
	 * @param schema the schema to set
	 */
	public final void setSchema(final String schema) {
		this.schema = schema;
	}

	/**
	 * @return the dbVersion
	 */
	public final Long getDbVersion() {
		return dbVersion;
	}

	/**
	 * @param dbVersion the dbVersion to set
	 */
	public final void setDbVersion(final Long dbVersion) {
		this.dbVersion = dbVersion;
	}

	/**
	 * @return the accountType
	 */
	public final AccountType getAccountType() {
		return accountType;
	}

	/**
	 * @param accountType the accountType to set
	 */
	public final void setAccountType(final AccountType accountType) {
		this.accountType = accountType;
	}

	/**
	 * @return the expirationDate
	 */
	public final Date getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expirationDate the expirationDate to set
	 */
	public final void setExpirationDate(final Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * @return the company
	 */
	public final String getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public final void setCompany(final String company) {
		this.company = company;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public final void setName(final String name) {
		this.name = name;
	}
	
	// payment details go here as well.
	
	
}
