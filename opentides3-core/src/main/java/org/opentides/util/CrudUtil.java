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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.opentides.annotation.Auditable;
import org.opentides.bean.AuditableField;
import org.opentides.bean.BaseEntity;
import org.opentides.bean.SystemCodes;
import org.opentides.exception.CodeGenerationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * @author allanctan
 *
 */
public class CrudUtil {
	
    private static Logger _log = Logger.getLogger(CrudUtil.class);
    
	private static final String SQL_PARAM = ":([^\\s]+)"; 
	private static final Pattern SQL_PARAM_PATTERN = Pattern.compile(
			SQL_PARAM, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	
    /**
     * Creates the logging message for new audit logs 
     * @param obj
     * @return
     */
    public static String buildCreateMessage(BaseEntity obj) {
		StringBuffer message = new StringBuffer();
    	if (obj.getClass().isAnnotationPresent(Auditable.class)) {
			AuditableField pf = CacheUtil.getPrimaryField(obj);    		
    		message.append("<p class='add-message'>Added new ");
        	message.append(buildPrimaryField(pf, obj))
 		   		.append(" with the following: ");

        	// loop through the fields list
    		List<AuditableField> auditFields = CacheUtil.getAuditable(obj);
    		int count = 0;
    		for (AuditableField property:auditFields) {
    			Object ret = retrieveNullableObjectValue(obj, property.getFieldName());
				ret = normalizeValue(ret);
    			if (ret.toString().trim().length()>0 && 
    					!pf.getFieldName().equals(property.getFieldName())) {
    				if (count > 0) 
    					message.append("and ");
    				message.append(property.getTitle())
    					.append("=<span class='field-value'>")
    					.append(ret.toString())
    					.append("</span> ");
    				count++;
    			}
    		}
        	message.append("</p>");
    	}
    	return message.toString();    	
    }
    
    /**
     * Creates the logging message for update audit logs 
     * @param obj
     * @return
     */ 
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static String buildUpdateMessage(BaseEntity oldObject, BaseEntity newObject) {
    	
    	StringBuffer message = new StringBuffer("<p class='change-message'>Changed ");
    	AuditableField pf = CacheUtil.getPrimaryField(oldObject); 
    	message.append(buildPrimaryField(pf, oldObject))
    		   .append(" with the following: ");
    	// loop through the fields list
		List<AuditableField> auditableFields = CacheUtil.getAuditable(oldObject);
		int count = 0;
		for (AuditableField property:auditableFields) {
			Object oldValue = retrieveNullableObjectValue(oldObject, property.getFieldName());
			Object newValue = retrieveNullableObjectValue(newObject, property.getFieldName());
			oldValue = normalizeValue(oldValue);
			newValue = normalizeValue(newValue);
			if (!oldValue.equals(newValue)) {
				if (count > 0) 
					message.append("and ");

				if (Collection.class.isAssignableFrom(oldValue.getClass()) &&
					Collection.class.isAssignableFrom(newValue.getClass()) ) {
					List addedList = new ArrayList();
					addedList.addAll((List) newValue);
					addedList.removeAll((List) oldValue);
					List removedList = new ArrayList();
					removedList.addAll((List) oldValue);
					removedList.removeAll((List) newValue);	
					if (!addedList.isEmpty()) {
						message.append("added ")
								.append(property.getTitle())
								.append(" <span class='field-values-added'>")
								.append(addedList)
								.append("</span> ");
					} 					
					if (!removedList.isEmpty()) {
						if (!addedList.isEmpty()) 
							message.append("and ");
						message.append("removed ")
								.append(property.getTitle())
								.append(" <span class='field-values-removed'>")
								.append(removedList)				
								.append("</span> ");
					} 
				} else {
					if (StringUtil.isEmpty(newValue.toString())) {
						message.append(property.getTitle())
							   .append(" <span class='field-value-removed'>")
							   .append(oldValue.toString())
							   .append("</span> is removed ");							
					} else {
						message.append(property.getTitle());
						if (!StringUtil.isEmpty(oldValue.toString())) 
							message.append(" from <span class='field-value-from'>")
									.append(oldValue.toString())
									.append("</span> ");
						else
							message.append(" is set ");
						message.append("to <span class='field-value-to'>")
							.append(newValue.toString())
							.append("</span> ");						
					}
				}				
				count++;
			}
		}
		message.append("</p>");
    	if (count==0)
    		return "";
    	else
    		return message.toString();    	
    }
    
    /**
     * Creates the logging message for deleted records. 
     * 
     * @param obj
     * @return
     */
    public static String buildDeleteMessage(BaseEntity obj) {
    	StringBuffer message = new StringBuffer("<p class='delete-message'>Deleted ");
    	AuditableField pf = CacheUtil.getPrimaryField(obj);     	
    	message.append(buildPrimaryField(pf, obj))
    		   .append("</p>");
    	return message.toString();    	
    }
    
    /**
     * Private helper to build primary field message.
     * @param pf
     * @param obj
     * @return
     */
    private static String buildPrimaryField(AuditableField pf, BaseEntity obj) {
    	StringBuffer message = new StringBuffer();
    	String shortName = CacheUtil.getReadableName(obj); 
    	message.append(shortName); // class name
    	Object value = retrieveNullableObjectValue(obj, pf.getFieldName());
        if (value!=null && !StringUtil.isEmpty(value.toString())) 
    		message
    		.append(" ")
    		.append(pf.getTitle())
    		.append(":<span class='primary-field'>")
    		.append(value.toString())
    		.append("</span>");
        return message.toString();    	
    }
    
    /**
     * Private helper that converts object into other form for 
     * audit log comparison and display purposes.
     * @param obj
     * @return
     */
    private static Object normalizeValue(Object obj) {
    	// convert date into string
    	if (obj == null)
    		return "";
    	if (obj instanceof Date) {
    		if (DateUtil.hasTime((Date) obj)) {
    			return DateUtil.dateToString((Date) obj, "EEE, dd MMM yyyy HH:mm:ss z");
    		} else
    			return DateUtil.dateToString((Date) obj, "EEE, dd MMM yyyy");    						
		}
    	return obj;
    }

    /**
     * Builds the query string appended to queryByExample
     * @param example
     * @param exactMatch
     * @return
     */
    @SuppressWarnings("rawtypes")
	public static String buildJpaQueryString(BaseEntity example, boolean exactMatch) {
		int count = 0;
		StringBuffer clause = new StringBuffer(" where ");
		List<String> exampleFields = CacheUtil.getSearchableFields(example);
		for (String property:exampleFields) {
			// get the value
			Object ret = retrieveObjectValue(example, property);
			// append the alias
			property = "obj." + property; 
			if (ret!=null) {
				if (String.class.isAssignableFrom(ret.getClass()) 
						&& !exactMatch ) {
					if (ret.toString().trim().length()>0) {
						if (count > 0) clause.append(" and ");
						clause.append(property)
							.append(" like '%")
							.append(StringUtil.escapeSql(ret.toString(), true))
							.append("%'");
						count++;
					}
				}else if(SystemCodes.class.isAssignableFrom(ret.getClass())) {
					SystemCodes sc = (SystemCodes) ret;
					if (!StringUtil.isEmpty(sc.getKey())) {
						if (count > 0) clause.append(" and ");
						clause.append(property)
							.append(".key")
							.append(" = '")
							.append(sc.getKey()+"'");
						count++;
					}
				} else if(BaseEntity.class.isAssignableFrom(ret.getClass())) {
					BaseEntity be = (BaseEntity) ret;
					if (be.getId() != null) {
						if (count > 0) clause.append(" and ");
						clause.append(property)
							.append(".id")
							.append(" = ")
							.append(be.getId());
						count++;
					}
				} else if (Integer.class.isAssignableFrom(ret.getClass()) ||
						   Float.class.isAssignableFrom(ret.getClass()) ||
						   Long.class.isAssignableFrom(ret.getClass()) ||
						   Double.class.isAssignableFrom(ret.getClass()) ||
						   BigDecimal.class.isAssignableFrom(ret.getClass()) ||
						   Boolean.class.isAssignableFrom(ret.getClass()) ) {
					// numeric types doesn't need to be enclosed in single quotes
					if (ret.toString().trim().length()>0) {
						if (count > 0) clause.append(" and ");
						clause.append(property)
							.append(" = ")
							.append(ret.toString());
						count++;
					}
				} else if (Class.class.isAssignableFrom(ret.getClass())){
					if (count > 0) clause.append(" and ");
					Class clazz = (Class) ret;
					clause.append(property)
						.append(" = '")
						.append(clazz.getName())
						.append("'");
					count++;					
				} else if (Collection.class.isAssignableFrom(ret.getClass())) {
					// not supported yet
					_log.warn("FindByExample on type Collection is not supported.");
				} else if (ret.toString().trim().length()>0){
					if (count > 0) clause.append(" and ");
					clause.append(property)
						.append(" = '")
						.append(StringUtil.escapeSql(ret.toString(), false))
						.append("'");
					count++;
				}
			}
		}
	    if (count > 0) 
	    	return clause.toString();
	    else
	    	return "";
    }

	/**
	 * This method retrieves the object value that corresponds to the property specified.
	 * This method can recurse inner classes until specified property is reached.
	 * 
	 * For example:
	 * obj.firstName
	 * obj.address.Zipcode
	 * 
	 * @param obj
	 * @param property
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object retrieveObjectValue(Object obj, String property) {
		if (property.contains(".")) {
			// we need to recurse down to final object
			String props[] = property.split("\\.");
			try {
				Object ivalue = null;
				if (Map.class.isAssignableFrom(obj.getClass())) {
					Map map = (Map) obj;
					ivalue = map.get(props[0]);					
				} else {					
					Method method;
					if (props[0].startsWith("get"))
						method = obj.getClass().getMethod(props[0]);
					else
						method = obj.getClass().getMethod(NamingUtil.toGetterName(props[0]));
					ivalue = method.invoke(obj);
				}
				if (ivalue==null)
					return null;
				// traverse collection objects
				if (Collection.class.isAssignableFrom(ivalue.getClass())) {
					Iterator iter = ((Collection)ivalue).iterator();
					List<Object> ret = new ArrayList<Object>();
					String prop = property.substring(props[0].length()+1);
					while (iter.hasNext()) {
						Object lvalue = iter.next();
						if (lvalue!=null) {
							ret.add(retrieveObjectValue(lvalue,prop));
						}
					}
					return ret;
				}
				return retrieveObjectValue(ivalue,property.substring(props[0].length()+1));
			} catch (Exception e) {
				throw new CodeGenerationException("Failed to retrieve value for "+property, e);
			} 
		} else {
			// let's get the object value directly
			try {
				if (Map.class.isAssignableFrom(obj.getClass())) {
					Map map = (Map) obj;
					return map.get(property);					
				} else {
					Method method;
					if (property.startsWith("get"))
						method = obj.getClass().getMethod(property);
					else
						method = obj.getClass().getMethod(NamingUtil.toGetterName(property));
					return method.invoke(obj);					
				}
			} catch (Exception e) {
				throw new CodeGenerationException("Failed to retrieve value for "+property, e);
			} 
		}
	}
	/**
	 * 
	 * This method retrieves the object value that corresponds to the property specified.
	 * This method supports nullable fields.
	 * 
	 * @see retrieveObjectValue
	 * @param obj
	 * @param property
	 * @return
	 */
	public static Object retrieveNullableObjectValue(Object obj, String property) {
		try {
			return retrieveObjectValue(obj, property);
		} catch (Exception e) {
			return null;
		}
	}


	/**
	 * This method retrieves the object type that corresponds to the property specified.
	 * This method can recurse inner classes until specified property is reached.
	 * 
	 * For example:
	 * obj.firstName
	 * obj.address.Zipcode
	 * 
	 * @param obj
	 * @param property
	 * @return
	 */
	public static Class<?> retrieveObjectType(Object obj, String property) {
		if (property.contains(".")) {
			// we need to recurse down to final object
			String props[] = property.split("\\.");
			try {
				Method method = obj.getClass().getMethod(NamingUtil.toGetterName(props[0]));
				Object ivalue = method.invoke(obj);
				return retrieveObjectType(ivalue,property.substring(props[0].length()+1));
			} catch (Exception e) {
				throw new CodeGenerationException("Failed to retrieve value for "+property, e);
			} 
		} else {
			// let's get the object value directly
			try {
				Method method = obj.getClass().getMethod(NamingUtil.toGetterName(property));
				return method.getReturnType();
			} catch (Exception e) {
				throw new CodeGenerationException("Failed to retrieve value for "+property, e);
			} 
		}
	}
	
	/**
	 * This method evaluates the given expression from the object.
	 * This method now uses Spring Expression Language (SpEL).
	 * 
	 * @param obj
	 * @param expression
	 * @return
	 */
	public static Boolean evaluateExpression(Object obj, String expression) {
		if (StringUtil.isEmpty(expression)) 
			return false;
		try {
			ExpressionParser parser = new SpelExpressionParser();
			Expression exp = parser.parseExpression(expression);
			return exp.getValue(obj, Boolean.class); 		
		} catch (Exception e) {
			_log.debug("Failed to evaluate expression ["+expression+"] for object ["+obj.getClass()+"].");
			_log.debug(e.getMessage());
			return false;
		}
	}
	
	/**
	 * This method will replace SQL parameters with
	 * respective values from the object.
	 * @param sql
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String replaceSQLParameters(String sql, Object obj) {
		// let's get all sql parameter by expression
		Matcher sqlMatcher =  CrudUtil.SQL_PARAM_PATTERN.matcher(sql);
		while (sqlMatcher.find()) {
			String param = sqlMatcher.group(1);
			Object valueObject = CrudUtil.retrieveNullableObjectValue(obj, param); 
			if (valueObject==null) {
				sql = sql.replace(sqlMatcher.group(), "null");				
			} else if (String.class.isAssignableFrom(valueObject.getClass())) {
				sql = sql.replace(sqlMatcher.group(), "'"+valueObject.toString()+"'");
			} else if (Collection.class.isAssignableFrom(valueObject.getClass())) {
				Collection<Object> list = (Collection<Object>) valueObject;
				int ctr=0;
				StringBuffer buff = new StringBuffer();
				for (Object item:list) {
					if (ctr++>0)
						buff.append(", ");
					if (SystemCodes.class.isAssignableFrom(item.getClass())) {
						SystemCodes entity  = (SystemCodes) item;
						// use id 
						buff.append("'")
							.append(entity.getKey())
							.append("'");
					} else
					if (BaseEntity.class.isAssignableFrom(item.getClass())) {
						BaseEntity entity  = (BaseEntity) item;
						// use id 
						buff.append(entity.getId());
					} else
						buff.append("'")
						.append(item.toString())
						.append("'");
				}
				sql = sql.replace(sqlMatcher.group(), buff.toString());				
			} else
				sql = sql.replace(sqlMatcher.group(), valueObject.toString());
		}
		return sql;
	}

	/**
	 * Overloaded method to retrieve all fields, including fields from parent class.
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public static List<Field> getAllFields(Class clazz) {
		return CrudUtil.getAllFields(clazz, true);
	}

	/**
	 * Helper method to retrieve all fields of a class including
	 * fields declared in its superclass.
	 * @param clazz
	 * @param includeParent
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public static List<Field> getAllFields(Class clazz, boolean includeParent) {
		List<Field> fields = new ArrayList<Field>();
		if (BaseEntity.class.isAssignableFrom(clazz) && includeParent)
			fields.addAll(getAllFields(clazz.getSuperclass(), includeParent));
		for (Field field:clazz.getDeclaredFields())
			fields.add(field);
		return fields;
	}
	
	/**
	 * Overloaded method to retrieve all methods, including methods from parent class.
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public static List<Method> getAllMethods(Class clazz) {
		return CrudUtil.getAllMethods(clazz, true);
	}

	/**
	 * Helper method to retrieve all methods of a class including
	 * methods declared in its superclass.
	 * @param clazz
	 * @param includeParent
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public static List<Method> getAllMethods(Class clazz, boolean includeParent) {
		List<Method> methods = new ArrayList<Method>();
		if (BaseEntity.class.isAssignableFrom(clazz) && includeParent)
			methods.addAll(getAllMethods(clazz.getSuperclass(), includeParent));
		for (Method method:clazz.getDeclaredMethods())
			methods.add(method);
		return methods;
	}
}