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
import java.util.Map;

import org.opentides.dao.SystemCodesDao;
import org.springframework.beans.factory.config.MapFactoryBean;

/**
 * This class is used to integrating list of user group permissions
 * from Spring config map and database via system codes. The database
 * list is used when integrating plugins (ie. reports, widgets).
 *
 * @author allanctan
 */
public class MapDBFactoryBean extends MapFactoryBean {

	private SystemCodesDao systemCodesDao;
	private String systemCodesCategory;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.MapFactoryBean#createInstance()
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Map<String, String> createInstance() {
		Map<String, String> map = super.createInstance();
		// let's append results from system codes
		if (systemCodesDao!=null) {
			List<SystemCodes> scList = systemCodesDao.findSystemCodesByCategory(systemCodesCategory);
			if (scList!=null) {
				for (SystemCodes sc:scList) {
					map.put(sc.getKey(), sc.getValue());
				}
			}
		}
		return map;
	}

	/**
	 * @param systemCodesDao the systemCodesDao to set
	 */
	public void setSystemCodesDao(SystemCodesDao systemCodesDao) {
		this.systemCodesDao = systemCodesDao;
	}

	/**
	 * @param systemCodesCategory the systemCodesCategory to set
	 */
	public void setSystemCodesCategory(String systemCodesCategory) {
		this.systemCodesCategory = systemCodesCategory;
	}

}
