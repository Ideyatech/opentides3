/*
 * Copyright 2007-2013 the original author or authors.
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

package org.opentides.bean;


/**
 * @author allantan
 *
 */
public class Event extends BaseEntity {

	private static final long serialVersionUID = 2387842214011997013L;

	private String name;
	
	private MessageResponse.Type type;
	
	private String description;
	
	private BaseEntity command;
	
	private BaseCriteria criteria;
	
	private String messageCode;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public MessageResponse.Type getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(MessageResponse.Type type) {
		this.type = type;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the command
	 */
	public BaseEntity getCommand() {
		return command;
	}

	/**
	 * @param command the command to set
	 */
	public void setCommand(BaseEntity command) {
		this.command = command;
	}

	/**
	 * @return the criteria
	 */
	public BaseCriteria getCriteria() {
		return criteria;
	}

	/**
	 * @param criteria the criteria to set
	 */
	public void setCriteria(BaseCriteria criteria) {
		this.criteria = criteria;
	}

	/**
	 * @return the messageCode
	 */
	public String getMessageCode() {
		return messageCode;
	}

	/**
	 * @param messageCode the messageCode to set
	 */
	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}
	
}
