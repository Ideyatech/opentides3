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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.opentides.annotation.Auditable;
import org.opentides.annotation.BuildInsertStatement;
import org.opentides.annotation.RestableFields;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author allantan
 *
 */
@Entity
@Table(name = "MT_USER_PROFILE")
@Auditable
public class MultitenantUser extends BaseUser {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6799194771431927231L;
	
	@ManyToOne
	@JoinColumn(name = "TENANT_ID")
	@JsonIgnore
	private Tenant tenant;

	@RestableFields
	public List<String> restableFields() {
		final List<String> props = new ArrayList<String>();
		props.add("id");
		props.add("firstName");
		props.add("lastName");
		props.add("emailAddress");
		props.add("tenant");
		props.add("groups");
		props.add("credential.username");
		props.add("credential.newPassword");
		props.add("credential.confirmPassword");
		props.add("credential.enabled");
		return props;
	}

	/**
	 * @return the tenant
	 */
	public final Tenant getTenant() {
		return tenant;
	}

	/**
	 * @param tenant the tenant to set
	 */
	public final void setTenant(final Tenant tenant) {
		this.tenant = tenant;
	}
}
