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
 * This factory merges two lists into one single list.
 * 
 * @author allantan
 * 
 */
public class MergeListFactoryBean extends
		org.springframework.beans.factory.config.ListFactoryBean {

	private List<Object> secondList;

	/**
	 * Creates the merged instance.
	 */
	@SuppressWarnings("unchecked")
	protected List<Object> createInstance() {
		List<Object> listOrigin =  (List<Object>) super.createInstance();
		listOrigin.addAll(secondList);
		return listOrigin;
	}
	
	/**
	 * Setter method for secondList.
	 *
	 * @param secondList the secondList to set
	 */
	public final void setSecondList(List<Object> secondList) {
		this.secondList = secondList;
	}

}
