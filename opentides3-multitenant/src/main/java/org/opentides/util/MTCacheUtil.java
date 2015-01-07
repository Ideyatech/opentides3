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
package org.opentides.util;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.opentides.annotation.RestableFields;
import org.opentides.bean.BaseEntity;

/**
 * Helper class to keep a cache of reusable attributes.
 * 
 * @author allantan
 *
 */
public class MTCacheUtil {
	
    private static final Logger _log = Logger.getLogger(CacheUtil.class);
    
	public static final Map<Class<?>, List<String>> restableFields = new ConcurrentHashMap<Class<?>, List<String>>();
	
	/**
	 * Hide the constructor.
	 */
	private MTCacheUtil() {		
	}
	
	/**
	 * Retrieves searchable fields using getSearchableFields method, if available.
	 * Otherwise, returns the list of field names that are persisted in database. 
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getRestableFields(BaseEntity obj) {
		Class<?> clazz = obj.getClass();
		List<String> ret = restableFields.get(clazz);
		if (ret == null) {
			// check if method annotated with restableFields is available
			List<String> fields = null;
			for (Method m:clazz.getDeclaredMethods()) {
				if (m.isAnnotationPresent(RestableFields.class)) {
					try {
						fields = (List<String>) m.invoke(obj);
					} catch (Exception e) {
						_log.warn("Unable to execute annotated method @RestableFields of " +
								obj.getClass().getSimpleName(), e);
					}
					break;
				}
			}
			if (fields == null) {
				fields = CacheUtil.getPersistentFields(obj);
			}
	        if (_log.isDebugEnabled()) {
		        _log.debug(clazz.getSimpleName()+" contains the following restable fields");
		        for (String field:fields) {
		            _log.debug(field);        	
		        }			
	        }
	        restableFields.put(obj.getClass(), fields);
			ret = restableFields.get(obj.getClass());
		}
		return ret;				
	}
	

}
