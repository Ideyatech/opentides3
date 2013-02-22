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

package org.opentides.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.opentides.bean.AuditLog;
import org.opentides.bean.BaseEntity;
import org.opentides.bean.user.SessionUser;
import org.opentides.dao.AuditLogDao;
import org.opentides.exception.InvalidImplementationException;
import org.opentides.listener.ApplicationStartupListener;
import org.opentides.util.DatabaseUtil;
import org.opentides.util.DateUtil;
import org.opentides.util.SecurityUtil;
import org.opentides.util.StringUtil;

/**
 * Logging DAO for audit tracking.
 * This class didn't extend BaseCrudDao to avoid conflict on
 * variables used by BaseEntity.
 * 
 * @author allantan
 *
 */
public class AuditLogDaoImpl implements AuditLogDao {	
	
	private static Logger _log = Logger.getLogger(AuditLogDaoImpl.class);
	
	private Properties properties;
	
    // the entity manager
	@PersistenceContext
    private EntityManager em;
	    
	@SuppressWarnings("unchecked")
	public final List<AuditLog> findAll(int start, int total) {
		Query query =  getEntityManager().createQuery("from AuditLog obj");
		if (start > -1) 
			query.setFirstResult(start);
		if (total > -1)
			query.setMaxResults(total);		
        return query.getResultList();
	}
    
	/**
	 * Counts all the audit log.
	 */
	public final long countAll() {
		return (Long) getEntityManager().createQuery("select count(*) from AuditLog obj ").getSingleResult();
	}
/*		
	public final long countByExample(AuditLog example) {
		String whereClause = CrudUtil.buildJpaQueryString(example, false);
		String append = appendClauseToExample(example);
		whereClause = doSQLAppend(whereClause, append);
		if (_log.isDebugEnabled()) _log.debug("Count QBE >> "+whereClause);
		return (Long) getEntityManager().createQuery("select count(*) from AuditLog obj "
				 + whereClause).getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public final List<AuditLog> findByExample(AuditLog example, int start, int total) {
		if (example instanceof Searchable) {
			Searchable criteria = (Searchable) example;
			String whereClause = CrudUtil.buildJpaQueryString(criteria, false);
			String orderClause = " " + appendOrderToExample(example);
			String append = appendClauseToExample(example);
			whereClause = doSQLAppend(whereClause, append);
			if (_log.isDebugEnabled()) _log.debug("QBE >> "+whereClause+orderClause);
			Query query = getEntityManager().createQuery("from AuditLog obj " + 
	        		whereClause + orderClause);
			if (start > -1) 
				query.setFirstResult(start);
			if (total > -1)
				query.setMaxResults(total);	
			return query.getResultList();
		} else {
			throw new InvalidImplementationException("Parameter example ["+example.getClass().getName()+"] is not an instance of Searchable");
		}
	}
*/
	
	public final List<AuditLog> findByNamedQuery(final String name, final Map<String,Object> params) {
		return findByNamedQuery(name, params, -1, -1);
	}
	
	@SuppressWarnings("unchecked")
	public final List<AuditLog> findByNamedQuery(final String name, final Map<String,Object> params, int start, int total) {
		String queryString = getJpqlQuery(name);
		Query queryObject = getEntityManager().createQuery(queryString);
		if (params != null) {
			for (Map.Entry<String, Object> entry:params.entrySet())
				queryObject.setParameter(entry.getKey(), entry.getValue());
		}
		if (start > -1) 
			queryObject.setFirstResult(start);
		if (total > -1)
			queryObject.setMaxResults(total);	
		return queryObject.getResultList();
	}
	
	/**
	 * Helper method to retrieve the jpql query.
	 * @param key
	 * @return
	 */
	public final String getJpqlQuery(String key) {
		String query = (String) properties.get(key);
		if (StringUtil.isEmpty(query)) {
			throw new InvalidImplementationException("Key ["+key+"] is not defined in custom jpql property file.");
		} else
			return query;
	}

	/**
	 * Helper method to append two SQL string.
	 * @param whereClause
	 * @param append
	 * @return
	 */
//	private String doSQLAppend(String whereClause, String append) {
//		if (!StringUtil.isEmpty(append)) {
//			if (StringUtil.isEmpty(whereClause))
//				whereClause += " where ";
//			else
//				whereClause += " and ";
//			whereClause += append;
//		}			
//		return whereClause;
//	}

	/**
	 * Saves the log event into the database.
	 * @param shortMessage
	 * @param message
	 * @param entity
	 */
	public static void logEvent(String shortMessage, String message, BaseEntity entity) { 		
		Long userId = entity.getAuditUserId();
		String officeName = entity.getAuditOfficeName();
		String username = entity.getAuditUsername();
		
		if (ApplicationStartupListener.isApplicationStarted()) {
			if (userId==null) {
				_log.warn("No userId specified for audit logging on object ["+entity.getClass().getName()
							+ "] for message ["+message+"]. Retrieving user from interceptor.");
				SessionUser user = SecurityUtil.getSessionUser();
				userId = user.getRealId();
				officeName = user.getOffice();	
				username = user.getUsername();
			} 
		} else {
			userId = new Long(0);
			officeName = "System Evolve";
		}
		
    	EntityManager em = DatabaseUtil.getEntityManager();
		try { 
		     em.getTransaction().begin();
		     AuditLog record = 
	            new AuditLog(shortMessage,
	            			message, 
	            			entity.getId(), 
	            			entity.getClass(), 
	                        entity.getReference(),
	                        userId,
	                        username,
	                        officeName); 
			em.persist(record);
			em.flush(); 
			em.getTransaction().commit();
		} finally { 
			em.close(); 
		}
    }
	
	
    /* (non-Javadoc)
	 * @see com.ideyatech.core.persistence.impl.BaseEntityDAOJpaImpl#appendClauseToExample(com.ideyatech.core.bean.BaseEntity, boolean)
	 */
	protected String appendOrderToExample(AuditLog example) {
		return "order by createDate desc";
	}
	
	protected String appendClauseToExample(AuditLog example) {
		StringBuilder append = new StringBuilder("");
		if (example.getStartDate() != null){
			if (!StringUtil.isEmpty(append.toString())){
				append.append(" and ");
			}
			String startDate = DateUtil.dateToString(example.getStartDate(), "yyyy-MM-dd");
			append.append(" obj.createDate >= '");
			append.append(startDate + "'");
		}
		
		if (example.getEndDate() != null){
			if (!StringUtil.isEmpty(append.toString())){
				append.append(" and ");
			}			
			String endDate = DateUtil.dateToString(
								DateUtils.addDays(example.getEndDate(), 1), "yyyy-MM-dd");
			append.append(" obj.createDate <= '");
			append.append(endDate + "'");
		}

		if(!StringUtil.isEmpty(example.getLogAction())){
			if (!StringUtil.isEmpty(append.toString())){
				append.append(" and ");
			}
			append.append(" obj.message like '%").append(example.getLogAction()).append("%' ");
		}
		return append.toString();
	}
	
	/**
	 * Setter method for properties.
	 *
	 * @param properties the properties to set
	 */
	public final void setProperties(Properties properties) {
		this.properties = properties;
	}

	public final void setEntityManager(EntityManager em) {
        this.em = em;
    }
	  
    protected final EntityManager getEntityManager() {
        if (em == null)
            throw new IllegalStateException("EntityManager has not been set on DAO before usage");
        return em;
    }

}
