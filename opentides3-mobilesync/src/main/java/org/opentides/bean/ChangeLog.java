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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.opentides.exception.InvalidImplementationException;
import org.opentides.web.json.Views;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This class is recording all the changes made on the database.
 * This covers all changes on the entities 
 * 
 * @author allantan
 */
@Entity
@Table(name = "CHANGE_LOG")
@JsonInclude(Include.NON_NULL)
public class ChangeLog extends BaseEntity {

	/**
	 * Constants of actions that can be performed.
	 */
	public static final int INSERT = 1;
	public static final int UPDATE = 2;
	public static final int DELETE = 3;
		
    /**
     * Auto-generated class UID.
     */
	private static final long serialVersionUID = -5765174497512230824L;

	/**
     * Primary key of object being tracked.
     */
    @Column(name = "ENTITY_ID", nullable = false, updatable = false)
    private Long entityId;
    
    /**
     * Class type of object being tracked.
     */
    @SuppressWarnings({ "rawtypes" })
    @Column(name = "ENTITY_CLASS", nullable = false, updatable = false)
    private Class entityClass;

    /**
     * Type of change performed.
     */
    @Column(name = "ACTION")
    private int action;
    
    /**
     * Contains the list of fields for updating.
     */
    @Column(name = "UPDATE_FIELDS")    
    private String updateFields;
    
    /**
     * SQL statement to be executed in the device.
     */
    @Column(name = "SQL_COMMAND")
    @JsonView(Views.FormView.class)
    private String sqlCommand;
    
	/**
     * Default constructor.
     */
    public ChangeLog(){
    }
    
	/**
	 * @param entityId
	 * @param entityClass
	 * @param action
	 * @param updateFields
	 */
	@SuppressWarnings("rawtypes")
	public ChangeLog(Long entityId, Class entityClass, int action,
			String updateFields, String sqlCommand) {
		super();
		this.entityId = entityId;
		this.entityClass = entityClass;
		this.action = action;
		this.updateFields = updateFields;
		this.sqlCommand = sqlCommand;
	}

	/**
	 * @param entityId
	 * @param entityClass
	 * @param action
	 */
	@SuppressWarnings("rawtypes")	
	public ChangeLog(Long entityId, Class entityClass, int action) {
		super();
		this.entityId = entityId;
		this.entityClass = entityClass;
		this.action = action;
	}

	/**
	 * @return the entityId
	 */
	public final Long getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public final void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	/**
	 * @return the entityClass
	 */
	@SuppressWarnings("rawtypes")	
	public final Class getEntityClass() {
		return entityClass;
	}

	/**
	 * @param entityClass the entityClass to set
	 */
	@SuppressWarnings("rawtypes")
	public final void setEntityClass(Class entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 * @return the action
	 */
	public final int getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public final void setAction(int action) {
		if ((action < 1) && (action > 3))
			throw new InvalidImplementationException("Invalid value for action on change log. "
					+ "Should be limited to '1' for Insert; '2' for Update; '3' for Delete.");
		this.action = action;
	}

	/**
	 * @return the updateFields
	 */
	public final String getUpdateFields() {
		return updateFields;
	}

	/**
	 * @param updateFields the updateFields to set
	 */
	public final void setUpdateFields(String updateFields) {
		this.updateFields = updateFields;
	}

	/**
	 * @return the sqlCommand
	 */
	public final String getSqlCommand() {
		return sqlCommand;
	}

	/**
	 * @param sqlCommand the sqlCommand to set
	 */
	public final void setSqlCommand(String sqlCommand) {
		this.sqlCommand = sqlCommand;
	}
	
	
	
}
