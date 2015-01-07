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

import java.util.List;

/**
 * Container of REST response.
 * 
 * @author allantan
 *
 * 
 */
public class RestResponse<T extends BaseEntity> {

	private T command;
	
	private List<MessageResponse> messages;

	/**
	 * @return the command
	 */
	public final T getCommand() {
		return command;
	}

	/**
	 * @param command the command to set
	 */
	public final void setCommand(T command) {
		this.command = command;
	}

	/**
	 * @return the messages
	 */
	public final List<MessageResponse> getMessages() {
		return messages;
	}

	/**
	 * @param messages the messages to set
	 */
	public final void setMessages(List<MessageResponse> messages) {
		this.messages = messages;
	}
	
}
