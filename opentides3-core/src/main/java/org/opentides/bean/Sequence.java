/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 *******************************************************************************/
package org.opentides.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Class used for generating auto incremented value. 
 * @author gbusok
 */
@Entity
@Table(name = "SEQUENCE_")
public class Sequence extends BaseEntity {
	
	private static final long serialVersionUID = -3185573414375387564L;
	
	@Column(name = "KEY_", unique = true)
	private String key;
	
	@Column(name = "VALUE_")
	private Long value;

	public Sequence() {
	}
	
	public Sequence(final String key) {
		this(key,1l);
	}
	
	public Sequence(final String key, final Long value) {
		this.key = key;
		this.value = value;
	}
	
	/**
	 * @return the key
	 */
	public final String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public final void setKey(final String key) {
		this.key = key;
	}

	/**
	 * @return the value
	 */
	public final Long getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public final void setValue(final Long value) {
		this.value = value;
	}
	
	/**
	 * Increment the value by the given step
	 */
	public final synchronized void incrementValue(final int step) {
		if(value == null) {
			value = Long.valueOf(1l);
		} else {
			value += step;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Sequence other = (Sequence) obj;
		if (key == null) {
			if (other.key != null) {
				return false;
			}
		} else if (!key.equals(other.key)) {
			return false;
		}
		return true;
	}
	
}