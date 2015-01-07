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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.opentides.annotation.Auditable;
import org.opentides.bean.BaseEntity;
import org.opentides.web.json.Views;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * @author allantan
 *
 */
@Entity
@Table(name = "ACCOUNT_TYPE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Auditable(label = "Account Type")
@JsonInclude(Include.NON_NULL)
public class AccountType extends BaseEntity {

	private static final long serialVersionUID = 8389290044783947156L;

	// duration of subscription, in days.
	public enum Period {
		MONTHLY,
		QUARTERLY,
		ANNUAL
	}
	
	// name of the account type
	@JsonView(Views.SearchView.class)
	@Column(name="NAME")
	private String name;
	
	// description of the account type
	@JsonView(Views.SearchView.class)
	@Column(name="DESCRIPTION")
	private String description;
	
	// amount of subscription
	@JsonView(Views.SearchView.class)
	@Column(name="AMOUNT")
	private Double amount;
	
	// duration of subscription
	@JsonView(Views.SearchView.class)
	@Column(name="PERIOD")
	private Period period;
	
	// is account type active for offering or not
	@JsonView(Views.SearchView.class)
	@Column(name="ACTIVE")	
	private Boolean active;

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public final void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public final String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public final void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the amount
	 */
	public final Double getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public final void setAmount(Double amount) {
		this.amount = amount;
	}

	/**
	 * @return the period
	 */
	public final Period getPeriod() {
		return period;
	}

	/**
	 * @param period the period to set
	 */
	public final void setPeriod(Period period) {
		this.period = period;
	}

	/**
	 * @return the active
	 */
	public final Boolean getActive() {
		return active;
	}

	@JsonView(Views.SearchView.class)
	public final String getActiveDisplay() {
		if (active!=null && active)
			return "Active";
		else
			return "Not Active";
	}
	/**
	 * @param active the active to set
	 */
	public final void setActive(Boolean active) {
		this.active = active;
	}
	
	/**
	 * Returns the subscription details.
	 */
	@JsonView(Views.SearchView.class)
	public final String getSubscription() {
		StringBuilder subs = new StringBuilder();
		if (amount > 0) {
			subs.append(amount);
		} else {
			subs.append("Free");
		}
		subs.append(" - ");
		if (period != null) subs.append(period);
		return subs.toString();
	}
}
