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

import org.opentides.web.json.Views;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * Model for holding messages to be displayed for the user.
 * 
 * @author allantan
 *
 */
public class MessageResponse extends DefaultMessageSourceResolvable {

	public enum Type {
		error, 			// displays error message (red, fixed)
		warning, 		// displays warning message (yellow, fixed)
		info, 			// displays warning message (fixed, closable)
		notification 	// pops-up for a few seconds
	};

	private static final long serialVersionUID = 667925698806449697L;	

	@JsonView(Views.BaseView.class)
	private final Type type;
	
	@JsonView(Views.BaseView.class)
	private final String objectName;
	
	@JsonView(Views.BaseView.class)
	private final String elementClass;
	
	@JsonView(Views.BaseView.class)
	private String message;
	
	/**
	 * Creates a message response to be rendered on the client.
	 * Use this constructor when displaying success messages (e.g. info, notification or warning).
	 * 
	 * @param elementClass
	 * @param type
	 * @param codes
	 * @param arguments
	 */
	public MessageResponse(String elementClass, Type type, String[] codes, Object[] arguments) {
		this(elementClass, type, null, codes, arguments);
	}

	/**
	 * Creates a message response to be rendered on the client.
	 * Use this constructor when displaying error message with reference to erring field/object.
	 * 
	 * @param elementClass - class name where the message will be inserted.
	 * @param type - type of message (can be error, warning, info or notification)
	 * @param objectName - name of erring object during validation or data binding
	 * @param codes - message codes to be displayed
	 * @param arguments - parameters available for the messages
	 */
	public MessageResponse(String elementClass, Type type, String objectName, String[] codes, Object[] arguments) {
		super(codes, arguments);
		Assert.notNull(type, "Type name must not be null");		
		if (type==Type.error) {
			Assert.notNull(objectName, "objectName name must not be null for error message.");
		}		
		this.type=type;
		this.objectName=objectName;
		this.elementClass=elementClass;
	}

	/**
	 * @return the type
	 */
	public final Type getType() {
		return type;
	}


	/**
	 * @return the objectName
	 */
	public final String getObjectName() {
		return objectName;
	}

	/**
	 * @return the message
	 */
	public final String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public final void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the elementClass
	 */
	public final String getElementClass() {
		return elementClass;
	}

}
