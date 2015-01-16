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

package org.opentides.persistence.interceptor;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.CallbackException;
import org.opentides.annotation.Synchronizable;
import org.opentides.bean.BaseEntity;
import org.opentides.bean.ChangeLog;
import org.opentides.util.CacheUtil;
import org.opentides.util.CrudUtil;
import org.opentides.util.DatabaseUtil;
import org.opentides.util.DateUtil;
import org.opentides.util.NamingUtil;

/**
 * @author allantan
 *
 */
public class SynchronizableInterceptor extends AuditLogInterceptor {

	private static final long serialVersionUID = 5476081576394866928L;
	
	private static final Logger _log = Logger.getLogger(AuditLogInterceptor.class);

	/* (non-Javadoc)
	 * @see org.opentides.persistence.interceptor.AuditLogInterceptor#postFlush(java.util.Iterator)
	 */
	@Override
	public void postFlush(Iterator iterator) throws CallbackException {
		
        try { 
        	synchronized(inserts) {
	        	for (BaseEntity entity:inserts) {
	        		if (entity.getClass().isAnnotationPresent(Synchronizable.class)) {
	        			String insertStmt = buildInsertStatement(entity);
	        			SynchronizableInterceptor.saveLog(entity, ChangeLog.INSERT, "", insertStmt);
	        		}
	        	}
        	}
        	synchronized(deletes) {
	        	for (BaseEntity entity:deletes) {
	        		if (entity.getClass().isAnnotationPresent(Synchronizable.class)) {
	        			String deleteStmt = buildDeleteStatement(entity);
	        			SynchronizableInterceptor.saveLog(entity, ChangeLog.DELETE, "", deleteStmt);
	        		}
	        	}        	
        	}
        	synchronized (updates) {
               	for (BaseEntity entity:updates) {
	        		if (entity.getClass().isAnnotationPresent(Synchronizable.class)) {
	        			BaseEntity old = oldies.get(entity.getId());	        			
       					List<String> fields = CrudUtil.getUpdatedFields(old, entity);
       					String updateStmt = buildUpdateStatement(entity, fields);
	        			SynchronizableInterceptor.saveLog(entity, ChangeLog.UPDATE, 
	        					StringUtils.join(fields, ","), updateStmt);
	        		}
             	}
        	}
        } catch (Throwable e) {
    		_log.error(e,e);
    	}         
		super.postFlush(iterator);
	}

	/**
	 * Builds the insert statement for sqlLite.
	 * @return
	 */
	public String buildInsertStatement(BaseEntity obj) {
		StringBuilder sql = new StringBuilder("insert into ");
		StringBuilder columns = new StringBuilder("(");
		StringBuilder values = new StringBuilder("(");
		
		List<String> fields = CacheUtil.getPersistentFields(obj);
		int count = 0;
		for (String field:fields) {
			Object ret = CrudUtil.retrieveNullableObjectValue(obj, field);
			String n = normalizeValue(ret);
			if (n.trim().length() > 0) {
				if (count++ > 0) {
					columns.append(",");
					values.append(",");
				}
				columns.append(field);
				values.append(n);
			}
		}
		columns.append(")");
		values.append(")");
		String tableName = NamingUtil.toSQLName(obj.getClass().getSimpleName());
		sql.append(tableName)
		   .append(" ").append(columns)
		   .append(" values ").append(values);
		return sql.toString();
	}
	
	/**
	 * Builds the insert statement for sqlLite.
	 * @return
	 */
	public String buildUpdateStatement(BaseEntity obj, List<String> fields) {
		String tableName = NamingUtil.toSQLName(obj.getClass().getSimpleName());

		StringBuilder sql = new StringBuilder("update ");
		sql.append(tableName)
		   .append(" set ");		
		
		int count = 0;
		for (String field:fields) {
			Object ret = CrudUtil.retrieveNullableObjectValue(obj, field);
			String n = normalizeValue(ret);
			if (n.trim().length() > 0) {
				if (count++ > 0) {
					sql.append(",");
				}
				sql.append(field)
				   .append("=")
				   .append(n);
			}
		}
		sql.append(" where id=")
		   .append(obj.getId());
		return sql.toString();
	}
	
	/**
	 * Builds the delete statement for sqlLite.
	 * @return
	 */
	public String buildDeleteStatement(BaseEntity obj) {
		String tableName = NamingUtil.toSQLName(obj.getClass().getSimpleName());
		StringBuilder sql = new StringBuilder("delete from ");
		sql.append(tableName).append(" where id=").append(obj.getId());
		return sql.toString();
	}
	
	/**
	 * Saves the change log into the database.
	 * @param shortMessage
	 * @param message
	 * @param entity
	 */
	public static void saveLog(BaseEntity entity, int action, String updateFields, String sqlCommand) { 
    	EntityManager em = DatabaseUtil.getEntityManager();    	
		try {
			em.getTransaction().begin();
			ChangeLog cl = new ChangeLog(entity.getId(), entity.getClass(), action, updateFields, sqlCommand);			
			em.persist(cl);
			em.flush(); 
			em.getTransaction().commit();
		} catch (Exception ex) {
			_log.error("Failed to save change log on ["+entity.getClass().getSimpleName()+"]", ex);
		} finally {
			if(em != null && em.isOpen()) {
				em.close(); 
			}
		}
    }
	
    /**
     * Private helper that converts object into other form for 
     * audit log comparison and display purposes.
     * @param obj
     * @return
     */
    private static String normalizeValue(Object obj) {
    	if (obj == null)
    		return "";
    	
    	if (obj instanceof Collection) { 
    		//ignore, we are not returning collections.
    		return "";
    	}
    	
    	// no quotes needed
    	if (obj instanceof Long    ||
    		obj instanceof Integer ||
    		obj instanceof BigDecimal )
    		return "" + obj;
    	
    	// convert date into string
    	if (obj instanceof Date) {
    		if (DateUtil.hasTime((Date) obj)) {
    			return "'" + DateUtil.dateToString((Date) obj, "MM/dd/yy HH:mm:ss z") + "'";
    		} else
    			return "'" + DateUtil.dateToString((Date) obj, "MM/dd/yy") + "'";
		}
    	
    	if (obj instanceof Boolean) {
    		return (((Boolean) obj).booleanValue()) ? "1" : "0";
    	}
    	
    	if (BaseEntity.class.isAssignableFrom(obj.getClass())) {
    		return "" + ((BaseEntity) obj).getId();
    	}
    	
    	if (obj.toString().length() > 0)
    		return "'" + obj + "'";
    	else
    		return "";
    }
}
