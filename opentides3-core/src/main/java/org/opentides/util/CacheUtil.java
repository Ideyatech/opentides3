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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Columns;
import org.opentides.annotation.Auditable;
import org.opentides.annotation.BuildDeleteStatement;
import org.opentides.annotation.BuildInsertStatement;
import org.opentides.annotation.BuildUpdateStatement;
import org.opentides.annotation.FormBind;
import org.opentides.annotation.FormBind.Load;
import org.opentides.annotation.PrimaryField;
import org.opentides.annotation.SearchableFields;
import org.opentides.annotation.SynchronizableFields;
import org.opentides.bean.AuditableField;
import org.opentides.bean.BaseEntity;
import org.opentides.bean.JoinTable;

/**
 * Helper class to keep a cache of reusable attributes.
 * 
 * @author allantan
 *
 */
public class CacheUtil {

	private static final Logger _log = Logger.getLogger(CacheUtil.class);

	private static final List<String> excludeFields = new ArrayList<String>();

	public static final Map<Class<?>, List<AuditableField>> auditable = new ConcurrentHashMap<Class<?>, List<AuditableField>>();

	public static final Map<Class<?>, AuditableField> primaryField = new ConcurrentHashMap<Class<?>, AuditableField>();

	public static final Map<Class<?>, String> readableName = new ConcurrentHashMap<Class<?>, String>();

	public static final Map<String, List<String>> persistentFields = new ConcurrentHashMap<String, List<String>>();

	public static final Map<Class<?>, List<String>> synchronizeFields = new ConcurrentHashMap<Class<?>, List<String>>();

	public static final Map<Class<?>, List<String>> searchableFields = new ConcurrentHashMap<Class<?>, List<String>>();
	
	public static final Map<String, JoinTable> joinTableFields = new ConcurrentHashMap<String, JoinTable>();
	
	public static final Map<String, Map<String, String>> columnNames = new ConcurrentHashMap<String, Map<String, String>>();

	private static final Map<Class<?>, Method> formBindNewMethods = new ConcurrentHashMap<Class<?>, Method>();

	private static final Map<Class<?>, Method> formBindUpdateMethods = new ConcurrentHashMap<Class<?>, Method>();

	private static final Map<Class<?>, Method> insertMethods = new ConcurrentHashMap<Class<?>, Method>();

	private static final Map<Class<?>, Method> updateMethods = new ConcurrentHashMap<Class<?>, Method>();

	private static final Map<Class<?>, Method> deleteMethods = new ConcurrentHashMap<Class<?>, Method>();
	
	static {
		excludeFields.add("createDate");
		excludeFields.add("updateDate");
		excludeFields.add("createdBy");
		excludeFields.add("version");
	}

	/**
	 * Hide the constructor.
	 */
	private CacheUtil() {
	}

	/**
	 * Helper method to retrieve a readable name for a given class. This method
	 * tries to access static method named readableName and returns its value if
	 * exist. Otherwise, the method tries to convert the class to a more
	 * readable form. (e.g. InboundDocument becomes Inbound Document);
	 * 
	 * @param entityClass
	 * @return
	 */
	public static String getReadableName(BaseEntity obj) {
		Class<?> clazz = obj.getClass();
		String ret = readableName.get(clazz);
		if (ret == null) {
			if (clazz.isAnnotationPresent(Auditable.class)) {
				String label = (clazz.getAnnotation(Auditable.class)).label();
				if (!StringUtil.isEmpty(label)) {
					ret = label;
				}
			}
			if (ret == null) {
				// no annotation, try readableName method
				try {
					Method method = clazz.getMethod("getReadableName");
					return method.invoke(null).toString().trim();
				} catch (Exception e) {
					String name = clazz.getSimpleName();
					return NamingUtil.toLabel(name);
				}
			}
			if (_log.isDebugEnabled()) {
				_log.debug(clazz.getSimpleName() + " has readable name of '"
						+ ret + "'");
			}
			readableName.put(obj.getClass(), ret);
			ret = readableName.get(obj.getClass());
		}
		return ret;
	}

	/**
	 * Retrieves auditable settings from the cache, if available. Returns the
	 * list of field names that are auditable. By default, these are fields that
	 * are not transient and not volatile.
	 * 
	 * @param obj
	 * @return
	 */
	public static List<AuditableField> getAuditable(BaseEntity obj) {
		Class<?> clazz = obj.getClass();
		List<AuditableField> ret = auditable.get(clazz);
		if (ret == null) {
			List<AuditableField> auditableFields = new ArrayList<AuditableField>();
			// no annotated method, use auto-detection
			Auditable annotation = clazz.getAnnotation(Auditable.class);
			if (annotation != null) {
				final List<Field> fields = CrudUtil.getAllFields(clazz,
						annotation.includeParentFields());
				List<String> exclude = Arrays
						.asList(annotation.excludeFields());
				for (Field field : fields) {
					if (CacheUtil.isPersistent(field)
							&& (!exclude.contains(field.getName()))
							&& (!excludeFields.contains(field.getName()))) {
						auditableFields
								.add(new AuditableField(field.getName()));
					}
				}
			}
			if (_log.isDebugEnabled()) {
				_log.debug(clazz.getSimpleName()
						+ " contains the following auditable fields");
				for (AuditableField audit : auditableFields) {
					_log.debug(audit.getTitle() + ":" + audit.getFieldName());
				}
			}
			auditable.put(obj.getClass(), auditableFields);
			ret = auditable.get(obj.getClass());
		}
		return ret;
	}

	/**
	 * Retrieves the attribute that is marked as primary field within the base
	 * entity. If not available, null is returned.
	 * 
	 * @param obj
	 * @return
	 */
	public static AuditableField getPrimaryField(BaseEntity obj) {
		Class<?> clazz = obj.getClass();
		AuditableField ret = primaryField.get(clazz);
		if (ret == null) {
			// loop all fields
			final List<Field> fields = CrudUtil.getAllFields(clazz, true);
			AuditableField pf = null;
			for (Field field : fields) {
				if (field.isAnnotationPresent(PrimaryField.class)) {
					PrimaryField annot = field
							.getAnnotation(PrimaryField.class);
					if (StringUtil.isEmpty(annot.label())) {
						pf = new AuditableField(field.getName());
					} else {
						pf = new AuditableField(field.getName(), annot.label());
					}
					break;
				}
			}

			// loop all methods
			final List<Method> methods = CrudUtil.getAllMethods(clazz, true);
			for (Method method : methods) {
				if (method.isAnnotationPresent(PrimaryField.class)) {
					PrimaryField annot = method
							.getAnnotation(PrimaryField.class);
					if (StringUtil.isEmpty(annot.label())) {
						pf = new AuditableField(method.getName());
					} else {
						pf = new AuditableField(method.getName(), annot.label());
					}
					break;
				}
			}

			if (pf == null)
				pf = new AuditableField("", "");
			primaryField.put(obj.getClass(), pf);
			ret = primaryField.get(obj.getClass());
			if (_log.isDebugEnabled()) {
				_log.debug("Primary Field is " + ret.getTitle() + ":"
						+ ret.getFieldName());
			}
		}
		return ret;
	}

	/**
	 * Retrieves persistent fields, including inherited fields from parent class.
	 * 
	 * @param obj
	 * @return
	 */
	public static List<String> getPersistentFields(BaseEntity obj) {
		Class<?> clazz = obj.getClass();
		return getPersistentFields(obj, clazz, true);
	}
	
	/**
	 * Retrieves persistent fields for specific class.
	 * 
	 * @param obj
	 * @return
	 */
	public static List<String> getPersistentFields(BaseEntity obj, Class<?> clazz) {
		return getPersistentFields(obj, clazz, false);
	}
	
	/**
	 * Retrieves persistent fields from the cache, if available. Otherwise,
	 * returns the list of field names that are persisted in database. These
	 * includes all non-transient fields. This method uses reflection and
	 * annotation to generate the list of fields.
	 * 
	 * @param obj
	 * @return
	 */
	public static List<String> getPersistentFields(BaseEntity obj, Class<?> clazz, boolean includeParent) {
		if (clazz == null) clazz = obj.getClass();
		String clazzCode = clazz.toString() + ";p=" + includeParent;
		List<String> ret = persistentFields.get(clazzCode);
		if (ret == null) {
			List<String> persistents = new ArrayList<String>();
			final List<Field> fields = CrudUtil.getAllFields(clazz, includeParent);
			for (Field field : fields) {
				if (CacheUtil.isPersistent(field)) {
					persistents.add(field.getName());
				}
			}
			if (_log.isDebugEnabled()) {
				_log.debug(clazz.getSimpleName()
						+ " contains the following persistent fields");
				for (String persistent : persistents) {
					_log.debug(persistent);
				}
			}
			persistentFields.put(clazzCode, persistents);
			ret = persistentFields.get(clazzCode);
		}
		return ret;
	}

	/**
	 * Overloaded method to retrieve fields from cache, including parent fields.
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, String> getColumnNames(BaseEntity obj) {
		return getColumnNames(obj, obj.getClass(), true);
	}

	/**
	 * Overloaded method to retrieve fields from cache, including parent fields.
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, String> getColumnNames(BaseEntity obj, Class<?> clazz) {
		return getColumnNames(obj, clazz, true);
	}

	/**
	 * Retrieves persistent fields from the cache, if available. Otherwise,
	 * returns the list of field names that are persisted in database. These
	 * includes all non-transient fields. This method uses reflection and
	 * annotation to generate the list of fields.
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, String> getColumnNames(BaseEntity obj, Class<?> clazz, boolean includeParent) {
		if (clazz==null) clazz = obj.getClass();
		String clazzCode = clazz.toString() + ";p=" + includeParent;
		Map<String, String> ret = columnNames.get(clazzCode);
		if (ret == null) {
			Map<String, String> columns = new HashMap<String, String>();
			final List<Field> fields = CrudUtil.getAllFields(clazz, includeParent);
			for (Field field : fields) {
				if (CacheUtil.isPersistent(field)) {
					Annotation annotation = field.getAnnotation(Column.class);
					if(annotation == null){
						annotation  = field.getAnnotation(JoinColumn.class);
					}
					if (annotation != null) {
						try {
							String name = (String) annotation.annotationType()
									.getMethod("name").invoke(annotation);
							columns.put(field.getName(), name);
						} catch (Exception e) {
							_log.warn(
									"Unable to execute annotated method @Column of "
											+ obj.getClass().getSimpleName(), e);
						}
					} else {
						annotation = field.getAnnotation(Columns.class);
				        if (annotation != null) {
				            try {
				                Column[] cols = (Column[])annotation.annotationType().getMethod("columns").invoke(annotation);
				                if(cols != null && cols.length > 0){
				                	String name = "";
				                	for(int i=0; i< cols.length; i++){
				                		if(i > 0){
				                			name += ",";
				                		}
				                		name += cols[i].name();
				                	}
				                	columns.put(field.getName(), name);
				                }
				            } catch (Exception ex) {
				            	_log.warn(
										"Unable to execute annotated method @Columns of "
												+ obj.getClass().getSimpleName(), ex);
				            }
				        }
					}
				}
			}
			if (_log.isDebugEnabled()) {
				_log.debug(clazz.getSimpleName()
						+ " contains the following persistent fields");
				for(String fieldName: columns.keySet()){
					_log.debug(fieldName);
				}
			}			
			columnNames.put(clazzCode, columns);
			ret = columnNames.get(clazzCode);
		}
		return ret;
	}

	/**
	 * Retrieves searchable fields using getSearchableFields method, if
	 * available. Otherwise, returns the list of field names that are persisted
	 * in database.
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getSearchableFields(BaseEntity obj) {
		Class<?> clazz = obj.getClass();
		List<String> ret = searchableFields.get(clazz);
		if (ret == null) {
			// check if method annotated with searchableFields is available
			List<String> fields = null;
			for (Method m : clazz.getDeclaredMethods()) {
				if (m.isAnnotationPresent(SearchableFields.class)) {
					try {
						fields = (List<String>) m.invoke(obj);
					} catch (Exception e) {
						_log.warn(
								"Unable to execute annotated method @SearchableFields of "
										+ obj.getClass().getSimpleName(), e);
					}
					break;
				}
			}
			if (fields == null) {
				fields = CacheUtil.getPersistentFields(obj);
			}
			if (_log.isDebugEnabled()) {
				_log.debug(clazz.getSimpleName()
						+ " contains the following searchable fields");
				for (String field : fields) {
					_log.debug(field);
				}
			}
			searchableFields.put(obj.getClass(), fields);
			ret = searchableFields.get(obj.getClass());
		}
		return ret;
	}
	
	/**
	 * Retrieves synchronizable fields using getSynchronizableFields method, if
	 * available. Otherwise, returns the list of field names that are persisted
	 * in database.
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getSynchronizableFields(BaseEntity obj) {
		return getSynchronizableFields(obj, obj.getClass());
	}
	
	/**
	 * Retrieves synchronizable fields using getSynchronizableFields method, if
	 * available. Otherwise, returns the list of field names that are persisted
	 * in database.
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getSynchronizableFields(BaseEntity obj, Class<?> clazz) {
		if (clazz==null) clazz = obj.getClass();
		List<String> ret = synchronizeFields.get(clazz);
		if (ret == null) {
			// check if method annotated with synchronizableFields is available
			List<String> fields = null;
			for (Method m : clazz.getDeclaredMethods()) {
				if (m.isAnnotationPresent(SynchronizableFields.class)) {
					try {
						fields = (List<String>) m.invoke(obj);
					} catch (Exception e) {
						_log.warn(
								"Unable to execute annotated method @SynchronizableFields of "
										+ clazz.getSimpleName(), e);
					}
					break;
				}
			}
			if (fields == null) {
				fields = CacheUtil.getPersistentFields(obj, clazz);
			}
			if (_log.isDebugEnabled()) {
				_log.debug(clazz.getSimpleName()
						+ " contains the following synchronizable fields");
				for (String field : fields) {
					_log.debug(field);
				}
			}
			synchronizeFields.put(clazz, fields);
			ret = synchronizeFields.get(clazz);
		}
		return ret;
	}
	
	/**
	 * Retrieves list of jointable fields, if available.
	 * 
	 * @param obj
	 * @return
	 */
	public static JoinTable getJoinTableFields(BaseEntity owner, Class<?> collection) {
		String code = owner.getClass().toString() + ":" + collection.toString();
		JoinTable ret = joinTableFields.get(code);
		if (ret == null) {
			// check if method annotated with JoinTable is available
			JoinTable join = null;
			final List<Field> fields = CrudUtil.getAllFields(owner.getClass());
			for (Field field : fields) {
		        if (field.getGenericType() instanceof ParameterizedType) {
		        	Class<?> fieldClazz = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
		        	if (fieldClazz.equals(collection)) {
						String tableName = "";
						String column1 = "";
						String column2 = "";
						Annotation annotation = field.getAnnotation(javax.persistence.JoinTable.class);
						if (annotation != null) {
							try {
								tableName = (String) annotation.annotationType()
										.getMethod("name").invoke(annotation);
								
								JoinColumn[] joinColumns = (JoinColumn[]) annotation
										.annotationType().getMethod("joinColumns")
										.invoke(annotation);
								
								JoinColumn[] inverseJoinColumn = (JoinColumn[]) annotation
										.annotationType()
										.getMethod("inverseJoinColumns")
										.invoke(annotation);
								
								if (joinColumns != null && joinColumns.length > 0) {
									column1 = joinColumns[0].name();
								}

								if (inverseJoinColumn != null
										&& inverseJoinColumn.length > 0) {
									column2 = inverseJoinColumn[0].name();
								}
								
								if (!StringUtil.isEmpty(tableName) && 
									!StringUtil.isEmpty(column1) &&
									!StringUtil.isEmpty(column2)) {
									 join = new JoinTable(tableName, column1, column2);
									 break;
								}
							} catch (Exception e) {
								_log.warn(
										"Unable to retrieve 'name' of field with @JoinTable of "
												+ owner.getClass().getSimpleName(), e);
							}
						}
		        	}
		        }
			}
			if (join!=null) {
				joinTableFields.put(code, join);
				ret = joinTableFields.get(code);				
			} else {
				_log.error("Unable to retrieve @JoinTable settings of " + collection.getSimpleName() +" in class " + owner.getClass().getSimpleName());
			}
		}
		return ret;
	}

	/**
	 * Retrieves the method annotated with @FormBind(mode=Load.NEW), if
	 * available. Otherwise, null method is returned.
	 * 
	 * @param clazz
	 * @return
	 */
	public static Method getNewFormBindMethod(Class<?> clazz) {
		Method method = formBindNewMethods.get(clazz);
		if (method == null) {
			for (Method m : clazz.getMethods()) {
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
	 * Retrieves the method annotated with @FormBind(mode=Load.UPDATE), if
	 * available. Otherwise, null method is returned.
	 * 
	 * @param clazz
	 * @return
	 */
	public static Method getUpdateFormBindMethod(Class<?> clazz) {
		Method method = formBindUpdateMethods.get(clazz);
		if (method == null) {
			for (Method m : clazz.getMethods()) {
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
	
	/**
	 * Retrieves the method annotated with @BuildInsertStatement, if
	 * available. Otherwise, null method is returned.
	 * 
	 * @param clazz
	 * @return
	 */
	public static Method getInsertMethod(Class<?> clazz) {
		Method method = insertMethods.get(clazz);
		if (method == null) {
			for (Method m : clazz.getMethods()) {
				if (m.isAnnotationPresent(BuildInsertStatement.class)) {
					insertMethods.put(clazz, m);
					method = insertMethods.get(clazz);
					break;
				}
			}
		}
		return method;
	}
	
	/**
	 * Retrieves the method annotated with @BuildUpdateStatement, if
	 * available. Otherwise, null method is returned.
	 * 
	 * @param clazz
	 * @return
	 */
	public static Method getUpdateMethod(Class<?> clazz) {
		Method method = insertMethods.get(clazz);
		if (method == null) {
			for (Method m : clazz.getMethods()) {
				if (m.isAnnotationPresent(BuildUpdateStatement.class)) {
					updateMethods.put(clazz, m);
					method = updateMethods.get(clazz);
					break;
				}
			}
		}
		return method;
	}
	
	/**
	 * Retrieves the method annotated with @BuildDeleteStatement, if
	 * available. Otherwise, null method is returned.
	 * 
	 * @param clazz
	 * @return
	 */
	public static Method getDeleteMethod(Class<?> clazz) {
		Method method = deleteMethods.get(clazz);
		if (method == null) {
			for (Method m : clazz.getMethods()) {
				if (m.isAnnotationPresent(BuildDeleteStatement.class)) {
					deleteMethods.put(clazz, m);
					method = deleteMethods.get(clazz);
					break;
				}
			}
		}
		return method;
	}
	

	/**
	 * Private helper to check if field is persistent.
	 * @param field
	 * @return
	 */
	private static boolean isPersistent(Field field) {
		return (!Modifier.isTransient(field.getModifiers()))
			&& (!Modifier.isVolatile(field.getModifiers()))
			&& (!Modifier.isStatic(field.getModifiers()))
			&& (!Modifier.isFinal(field.getModifiers()))
			&& (!field.isAnnotationPresent(Transient.class));
	}
}
