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

import org.hibernate.type.Type;

/**
 * Container of changed fields used for audit logging.
 * 
 * @author allantan
 */
public class ChangedField {
	
	private Object currentState;
	private Object previousState;
	private String propertyName;
	private Type type;
	
	/**
	 * @param currentState
	 * @param previousState
	 * @param propertyName
	 * @param type
	 */
	public ChangedField(Object currentState, Object previousState,
			String propertyName, Type type) {
		super();
		this.currentState = currentState;
		this.previousState = previousState;
		this.propertyName = propertyName;
		this.type = type;
	}
	/**
	 * @return the currentState
	 */
	public Object getCurrentState() {
		return currentState;
	}
	/**
	 * @return the previousState
	 */
	public Object getPreviousState() {
		return previousState;
	}
	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}
	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}
	
	
}
