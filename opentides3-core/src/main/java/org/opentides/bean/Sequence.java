/*
 * This source code is property of Ideyatech,Inc.
 * All rights reserved. 
 * 
 * AttacheSequence.java
 * Created on Jul 24, 2012 5:34:32 PM
 */
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
	
	public Sequence(String key) {
		this(key,1l);
	}
	
	public Sequence(String key, Long value) {
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
	public final void setKey(String key) {
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
	public final void setValue(Long value) {
		this.value = value;
	}
	
	/**
	 * Increment the value by the given step
	 */
	public final synchronized void incrementValue(int step) {
		if(this.value == null) {
			this.value = Long.valueOf(1l);
		} else {
			this.value += step;
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		Sequence other = (Sequence) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
	
}