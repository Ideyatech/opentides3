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

import org.opentides.util.NamingUtil;

/**
 * AuditableField contains definition of fields used for audit logging. Classes
 * that implemented Auditable are required to implement get which
 * returns list of AuditableField. Only fields defined with AuditableField are
 * tracked for audit logging.
 * 
 * @author allantan
 * 
 */
public class AuditableField {

    /**
     * Field title displayed to user.
     */
    private String title;

    /**
     * Field name in Java.
     */
    private String fieldName;

    /**
     * Creates a new AuditableField instance.
     * 
     * @param fieldName
     *            Field name in Java
     * @param title
     *            Field title displayed to user
     */
    public AuditableField(final String fieldName, final String title) {
        this.fieldName = fieldName;
        this.title = title;
    }

    /**
     * Creates a new AuditableField based on fieldName. fieldTitle is
     * automatically created using NamingUtil.toLabel()
     * 
     * @param fieldName
     *            name of Java attribute
     */
    public AuditableField(final String fieldName) {
        this.fieldName = fieldName;
        title = NamingUtil.toLabel(fieldName);
    }

    /**
     * Getter method of title.
     * 
     * @return the title field
     */
    public final String getTitle() {
        return title;
    }

    /**
     * Setting method of title.
     * 
     * @param title
     *            the title to set
     */
    public final void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Getter method of field name.
     * 
     * @return the fieldName
     */
    public final String getFieldName() {
        return fieldName;
    }

    /**
     * Setter method of field name.
     * 
     * @param fieldName
     *            the fieldName to set
     */
    public final void setFieldName(final String fieldName) {
        this.fieldName = fieldName;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fieldName == null) ? 0 : fieldName.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AuditableField other = (AuditableField) obj;
		if (fieldName == null) {
			if (other.fieldName != null) {
				return false;
			}
		} else if (!fieldName.equals(other.fieldName)) {
			return false;
		}
		if (title == null) {
			if (other.title != null) {
				return false;
			}
		} else if (!title.equals(other.title)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return title;
	}
    
}
