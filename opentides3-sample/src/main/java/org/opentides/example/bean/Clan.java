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
package org.opentides.example.bean;

import java.util.Set;

import org.opentides.bean.BaseEntity;

/**
 * @author allantan
 *
 */
public class Clan extends BaseEntity {

	private static final long serialVersionUID = -5332804497128647838L;

	private String name;
	
	private String description;

	private Ninja leader;
	
	private Set<Ninja> members;

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
	 * @return the leader
	 */
	public final Ninja getLeader() {
		return leader;
	}

	/**
	 * @param leader the leader to set
	 */
	public final void setLeader(Ninja leader) {
		this.leader = leader;
	}

	/**
	 * @return the members
	 */
	public final Set<Ninja> getMembers() {
		return members;
	}

	/**
	 * @param members the members to set
	 */
	public final void setMembers(Set<Ninja> members) {
		this.members = members;
	}
	
}
