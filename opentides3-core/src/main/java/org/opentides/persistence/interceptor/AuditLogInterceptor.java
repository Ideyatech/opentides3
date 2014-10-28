/**
 * 
 */
package org.opentides.persistence.interceptor;

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
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.opentides.bean.AuditLog;
import org.opentides.bean.BaseEntity;
import org.opentides.dao.impl.AuditLogDaoImpl;
import org.opentides.util.CrudUtil;
import org.opentides.util.DatabaseUtil;
import org.opentides.util.StringUtil;

/**
 * This is the interceptor responsible in tracking audit trails.
 * Source is patterned after the book "Java Persistence with Hibernate" - page 546 onwards
 * and merged with http://www.hibernate.org/318.html
 * 
 * @author allantan
 */
public class AuditLogInterceptor extends EmptyInterceptor {
	private static final long serialVersionUID = 582549003254963262L;
	
	private static final Logger _log = Logger.getLogger(AuditLogInterceptor.class);
	
    private Set<BaseEntity> inserts      = Collections.synchronizedSet(new HashSet<BaseEntity>()); 
    private Set<BaseEntity> updates      = Collections.synchronizedSet(new HashSet<BaseEntity>()); 
    private Set<BaseEntity> deletes      = Collections.synchronizedSet(new HashSet<BaseEntity>()); 
    private Map<Long, BaseEntity> oldies = Collections.synchronizedMap(new HashMap<Long, BaseEntity>()); 
    
    @Override
    public boolean onSave(Object entity, 
                          Serializable id, 
                          Object[] state, 
                          String[] propertyNames, 
                          Type[] types) 
            throws CallbackException { 
        if (entity instanceof BaseEntity && !(entity instanceof AuditLog)) {
        	synchronized(inserts) {
        		inserts.add((BaseEntity)entity);
        	}
        }
        return false; 
    } 
    
	/* (non-Javadoc)
	 * @see org.hibernate.EmptyInterceptor#onDelete(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
	 */
	@Override
	public void onDelete(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
        if (entity instanceof BaseEntity && !(entity instanceof AuditLog)) {
        	synchronized(deletes) {
        		deletes.add((BaseEntity)entity);
        	}
        }
	}

    @Override
    public boolean onFlushDirty(Object entity, 
                                Serializable id, 
                                Object[] currentState, 
                                Object[] previousState, 
                                String[] propertyNames, 
                                Type[] types) 
            throws CallbackException { 
        if (entity instanceof BaseEntity && !(entity instanceof AuditLog)) {
        	EntityManager em = null;
        	try {
        		BaseEntity auditable = (BaseEntity) entity;
            	if (!auditable.isSkipAudit()) {
		        	// Use the id and class to get the pre-update state from the database
            		em = DatabaseUtil.getEntityManager();
		        	BaseEntity old = (BaseEntity) em.find(entity.getClass(), auditable.getId());
		        	
		        	if (old == null)
		        		return false; // old object is not yet persisted?
		        	synchronized(oldies) {
		        		oldies.put(old.getId(), old);
		        	}
		        	synchronized(updates) {
		        		updates.add((BaseEntity)entity);
		        	}
            	}
        	} catch (Throwable e) {
        		_log.error(e,e);
        	}
        }
        return false; 
    } 
    
	@SuppressWarnings("rawtypes")
	@Override
    public void postFlush(Iterator iterator) 
                    throws CallbackException { 
        try { 
        	synchronized(inserts) {
	        	for (BaseEntity entity:inserts) {
	        		if (!entity.isSkipAudit()) {
	        			String auditMessage = null;
	        			
	        			if (StringUtil.isEmpty(entity.getAuditMessage())){
	        				auditMessage = CrudUtil.buildCreateMessage(entity);
	        			}else{
	        				auditMessage = entity.getAuditMessage();
	        			}
	        			
	        			AuditLogDaoImpl.logEvent(auditMessage, entity);	
	        		}
	        	}
        	}
        	synchronized(deletes) {
	        	for (BaseEntity entity:deletes) {
	        		if (!entity.isSkipAudit()) {
	        			String auditMessage = null;
	        			
	        			if (StringUtil.isEmpty(entity.getAuditMessage())){
	        				auditMessage = CrudUtil.buildDeleteMessage(entity);
	        			}else{
	        				auditMessage = entity.getAuditMessage();
	        			}	        			
	        			AuditLogDaoImpl.logEvent(auditMessage, entity);       				
	        		}
	        	}        	
        	}
        	synchronized (updates) {
               	for (BaseEntity entity:updates) {
	        		if (!entity.isSkipAudit()) {
	        			String auditMessage = null;
	        			
	        			if (StringUtil.isEmpty(entity.getAuditMessage())){
	        				BaseEntity old = oldies.get(entity.getId());
			        		auditMessage = CrudUtil.buildUpdateMessage(old, entity);
	        			}else{
	        				auditMessage = entity.getAuditMessage();
	        			}
	        			if (!StringUtil.isEmpty(auditMessage))
	        				AuditLogDaoImpl.logEvent(auditMessage, entity);	
	        		}
               	}
        	}
        } catch (Throwable e) {
    		_log.error(e,e);
    	} finally {
    		synchronized (inserts) {
	            inserts.clear(); 
			} 
    		synchronized (updates) {
	            updates.clear();
    		}
    		synchronized (deletes) {
	            deletes.clear();
    		}
	    	synchronized (oldies) {
	            oldies.clear();
    		}
        } 
    }
	
	@Override
	public void onCollectionUpdate(Object collection, Serializable key)
			throws CallbackException {
		_log.debug("This is a test to determine if a collection is updated");
	}
	
}