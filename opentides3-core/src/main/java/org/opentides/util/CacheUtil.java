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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.opentides.annotation.Auditable;
import org.opentides.annotation.FormBind;
import org.opentides.annotation.FormBind.Load;
import org.opentides.bean.AuditableField;
import org.opentides.bean.BaseEntity;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * Helper class to keep a cache of reusable attributes.
 * 
 * @author allantan
 *
 */
public class CacheUtil {
	
    private static Logger _log = Logger.getLogger(CacheUtil.class);

	public static final Map<Class<?>, List<AuditableField>> auditable  = new ConcurrentHashMap<Class<?>, List<AuditableField> >();
	
	public static final Map<Class<?>, List<String>> persistentFields = new ConcurrentHashMap<Class<?>, List<String>>();
	
	private static final Map<Class<?>, Method> formBindNewMethods = new ConcurrentHashMap<Class<?>, Method>();
	
	private static final Map<Class<?>, Method> formBindUpdateMethods = new ConcurrentHashMap<Class<?>, Method>();

	/**
	 * Retrieves auditable settings from the cache, if available.
     * Returns the list of field names that are auditable. By default, these 
     * are fields that are not transient and not volatile.
     * 
	 * @param obj
	 * @return
	 */
	public static List<AuditableField> getAuditable(BaseEntity obj) {
		Class<?> clazz = obj.getClass();
		List<AuditableField> ret = auditable.get(clazz);
		if (ret == null) {
			List<AuditableField> auditableFields = new ArrayList<AuditableField>();
	        Auditable annotation = clazz.getAnnotation(Auditable.class);
	        if (annotation!=null) {
	            final List<Field> fields = CrudUtil.getAllFields(clazz, annotation.includeParentFields());
	            String[] exclude = annotation.excludeFields();
	            for (Field field : fields) {        	
	            	if ( (!Modifier.isTransient(field.getModifiers())) &&
	            		 (!Modifier.isVolatile(field.getModifiers())) &&
	            		 (!Modifier.isStatic(field.getModifiers())) &&            		 
	            		 (!field.isAnnotationPresent(Transient.class)) &&
	            		 (!Arrays.asList(exclude).contains(field.getName())) ) {            		
	                    auditableFields.add(new AuditableField(field.getName()));
	                }
	            }
	        }
	        _log.info(clazz.getSimpleName()+" contains the following auditable fields");
	        for (AuditableField audit:auditableFields) {
	            _log.info(audit.getTitle() + ":" + audit.getFieldName());        	
	        }
			auditable.put(obj.getClass(), auditableFields);
			ret =  auditable.get(obj.getClass());
		}
		return ret;		
	}
	
	/**
	 * Retrieves persistent fields from the cache, if available.
	 * Otherwise, returns the list of field names that are persisted in database. 
     * These includes all non-transient fields.
     * This method uses reflection and annotation to generate the list of fields.
	 * 
	 * @param obj
	 * @return
	 */
	public static List<String> getPersistentFields(BaseEntity obj) {
		Class<?> clazz = obj.getClass();				
		List<String> ret = persistentFields.get(clazz);
		if (ret == null) {
	    	List<String> persistents = new ArrayList<String>();
	    	final List<Field> fields = CrudUtil.getAllFields(clazz);
	        for (Field field : fields) {        	
	        	if ( (!Modifier.isTransient(field.getModifiers())) &&
	        		 (!Modifier.isVolatile(field.getModifiers())) &&
	        		 (!Modifier.isStatic(field.getModifiers())) &&
	        		 (!field.isAnnotationPresent(Transient.class)) ) {            		
	        		persistents.add(field.getName());
	            }
	        }
	        _log.info(clazz.getSimpleName()+" contains the following persistent fields");
	        for (String persistent:persistents) {
	            _log.info(persistent);        	
	        }			
			persistentFields.put(obj.getClass(), persistents);
			ret =  persistentFields.get(obj.getClass());
		}
		return ret;				
	}
	
	/**
	 * Retrieves the method annotated with @FormBind(mode=Load.NEW), if available. 
	 * Otherwise, null method is returned.
	 * 
	 * @param clazz
	 * @return
	 */
	public static Method getNewFormBindMethod(Class<?> clazz) {
		AnnotationUtils.findAnnotation(clazz, FormBind.class);
		Method method = formBindNewMethods.get(clazz);
		if (method==null) {
			for (Method m:clazz.getDeclaredMethods()) {
				if (m.isAnnotationPresent(FormBind.class)) {
					FormBind ann = m.getAnnotation(FormBind.class);
					if (ann.mode().equals(Load.NEW)) {
						formBindNewMethods.put(clazz, m);
						method = formBindNewMethods.get(clazz);
						break;
					}
				}
			}			
		}
		return method;
	}
	
	/**
	 * Retrieves the method annotated with @FormBind(mode=Load.UPDATE), if available. 
	 * Otherwise, null method is returned.
	 * 
	 * @param clazz
	 * @return
	 */
	public static Method getUpdateFormBindMethod(Class<?> clazz) {
		AnnotationUtils.findAnnotation(clazz, FormBind.class);
		Method method = formBindUpdateMethods.get(clazz);
		if (method==null) {
			for (Method m:clazz.getMethods()) {
				if (m.isAnnotationPresent(FormBind.class)) {
					FormBind ann = m.getAnnotation(FormBind.class);
					if (ann.mode().equals(Load.UPDATE)) {
						formBindUpdateMethods.put(clazz, m);
						method = formBindUpdateMethods.get(clazz);
						break;
					}
				}
			}			
		}
		return method;
	}
}
