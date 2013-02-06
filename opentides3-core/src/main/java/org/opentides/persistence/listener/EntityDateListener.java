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

package org.opentides.persistence.listener;

import java.util.Calendar;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.opentides.bean.BaseEntity;


/**
 * This listener is listening in BaseEntity to ensure 
 * create date and update date are populated.
 * @author allantan
 * 
 */
public class EntityDateListener {

	@PrePersist
	public void setDates(BaseEntity entity) {
		// set dateCreated and dateUpdated fields
		Calendar now = Calendar.getInstance();
		if (entity.getCreateDate() == null) {
			entity.setCreateDate(now);
		}
		entity.setUpdateDate(now);
	}

	@PreUpdate
	public void updateDates(BaseEntity entity) {
		// set dateUpdated fields
		entity.setUpdateDate(Calendar.getInstance());
	}
}
