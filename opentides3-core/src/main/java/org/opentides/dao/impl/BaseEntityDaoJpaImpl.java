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

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.opentides.annotation.Protected;
import org.opentides.bean.BaseEntity;
import org.opentides.bean.user.SessionUser;
import org.opentides.dao.BaseEntityDao;
import org.opentides.exception.InvalidImplementationException;
import org.opentides.listener.ApplicationStartupListener;
import org.opentides.util.CrudUtil;
import org.opentides.util.SecurityUtil;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Base class for all business data access objects. Extend this class to inherit
 * all CRUD operations.
 * This implementation is patterned after GenericEJB3DAO from Hibernate CaveatEmptor.
 *
 * For opentides3.0, this has been updated to support Eclipselink. 
 *
 * @author allanctan
 */
public class BaseEntityDaoJpaImpl<T extends BaseEntity,ID extends Serializable> 
		implements BaseEntityDao<T, ID>  {
	
	private static Logger _log = Logger.getLogger(BaseEntityDaoJpaImpl.class);

	@Autowired
    private Properties jpqlProperties;
	// contains the class type of the bean    
    private Class<T> entityBeanType;
    // list of filters available
    private Map<String, String> securityFilter;
    // the entity manager
	@PersistenceContext
    private EntityManager em;
   
	private int batchSize = 20;
	
	@SuppressWarnings("unchecked")
	public BaseEntityDaoJpaImpl() {
		// identify the bean we are processing for this DAO 
		try {
	        this.entityBeanType = (Class<T>) ((ParameterizedType) getClass()
	                .getGenericSuperclass()).getActualTypeArguments()[0];
		} catch (ClassCastException cc) {
			// if dao is extended from the generic dao class
			this.entityBeanType = (Class<T>) ((ParameterizedType) getClass().getSuperclass()
	                .getGenericSuperclass()).getActualTypeArguments()[0];
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override	
	public final List<T> findAll() {
    	return findAll(-1,-1);
    }
    
	/**
	 * {@inheritDoc}
	 */
	@Override	
	@SuppressWarnings("unchecked")
	public final List<T> findAll( int start, int total) {
		Query query =  getEntityManager().createQuery("select obj from " + 
        		getEntityBeanType().getName() +" obj ");
		if (start > -1) 
			query.setFirstResult(start);
		if (total > -1)
			query.setMaxResults(total);		
        return query.getResultList();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override	
	public final List<T> findByNamedQuery(final String name, final Map<String,Object> params) {
		return findByNamedQuery(name, params, -1, -1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override	
	public final List<T> findByNamedQuery(final String name, final Object... params) {
		return findByNamedQuery(name, -1, -1, params);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override	
	@SuppressWarnings("unchecked")
	public final List<T> findByNamedQuery(final String name, final Map<String,Object> params, int start, int total) {
		String queryString = getJpqlQuery(name);
		Query queryObject = getEntityManager().createQuery(queryString);
		if (params != null) {
			for (Map.Entry<String, Object> entry:params.entrySet()) {
				if (entry.getKey()!=null && entry.getKey().startsWith("hint.")) {
					queryObject.setHint(entry.getKey().substring(5), entry.getValue());
				} else {
					queryObject.setParameter(entry.getKey(), entry.getValue());
				}
			}
		}
		if (start > -1) 
			queryObject.setFirstResult(start);
		if (total > -1)
			queryObject.setMaxResults(total);	
		return queryObject.getResultList();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override	
	@SuppressWarnings("unchecked")
	public final List<T> findByNamedQuery(final String name, int start, int total, final Object... params) {
		String queryString = getJpqlQuery(name);
		Query queryObject = getEntityManager().createQuery(queryString);
		if (params != null) {
			int i=1;
			for (Object obj:params) {
				queryObject.setParameter(i, obj);
				i++;
			}
		}
		if (start > -1) 
			queryObject.setFirstResult(start);
		if (total > -1)
			queryObject.setMaxResults(total);	
		return queryObject.getResultList();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override	
	@SuppressWarnings({ "unchecked" })
	public final T findSingleResultByNamedQuery(final String name, final Map<String,Object> params) {
		String queryString = getJpqlQuery(name);
		Query queryObject = getEntityManager().createQuery(queryString);
		if (params != null) {
			for (Map.Entry<String, Object> entry:params.entrySet())
				queryObject.setParameter(entry.getKey(), entry.getValue());
		} 
		try {
		    return (T) queryObject.getSingleResult();
		} catch (NoResultException nre) {
		    return null;		    
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override	
	@SuppressWarnings({ "unchecked" })
	public final T findSingleResultByNamedQuery(final String name, final Object... params) {
		String queryString = getJpqlQuery(name);
		Query queryObject = getEntityManager().createQuery(queryString);
		if (params != null) {
			int i=0;
			for (Object obj:params) {
				queryObject.setParameter(i, obj);				
			}
		} 
		try {
		    return (T) queryObject.getSingleResult();
		} catch (NoResultException nre) {
		    return null;		    
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override	
	public int executeByNamedQuery(final String name, final Map<String, Object> params) {
		String queryString = getJpqlQuery(name);
		Query queryObject = getEntityManager().createQuery(queryString);
		if (params != null) {
			for (Map.Entry<String, Object> entry:params.entrySet())
				queryObject.setParameter(entry.getKey(), entry.getValue());
		} 
		return queryObject.executeUpdate();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override	
	public int executeByNamedQuery(final String name, final Object... params) {
		String queryString = getJpqlQuery(name);
		Query queryObject = getEntityManager().createQuery(queryString);
		if (params != null) {
			int i=0;
			for (Object obj:params) {
				queryObject.setParameter(i, obj);				
			}
		} 
		return queryObject.executeUpdate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override	
	public final List<T> findByExample(final T example, boolean exactMatch) {
		return findByExample(example, exactMatch, -1,-1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override	
	public final List<T> findByExample(final T example) {
		return findByExample(example,false,-1,-1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override	
	public final List<T> findByExample(final T example, int start, int total) {
		return findByExample(example,false,start,total);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override	
	@SuppressWarnings("unchecked")
	public final List<T> findByExample(final T example, boolean exactMatch, int start, int total) {
		String whereClause = CrudUtil.buildJpaQueryString(example, exactMatch);
		String orderClause = " " + appendOrderToExample(example);
		String filterClause = this.buildSecurityFilterClause(example);
		String append = appendClauseToExample(example, exactMatch);
		whereClause = doSQLAppend(whereClause, append);
		whereClause = doSQLAppend(whereClause, filterClause);
		if (_log.isDebugEnabled()) 
			_log.debug("QBE >> "+whereClause+orderClause);
		Query query = getEntityManager().createQuery("select (obj) from " + 
        		getEntityBeanType().getName() + " obj "+ whereClause + orderClause);
		if (start > -1) 
			query.setFirstResult(start);
		if (total > -1)
			query.setMaxResults(total);	
		return query.getResultList();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override	
	public final long countAll() {
		return (Long) getEntityManager().createQuery("select count(obj) from " + 
				getEntityBeanType().getName() + " obj " +
				doSQLAppend("", this.buildSecurityFilterClause(null))).getSingleResult();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override	
	public final long countByExample(final T example) {
		return countByExample(example,false);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override	
	public final long countByExample(final T example, final boolean exactMatch) {
		String whereClause = CrudUtil.buildJpaQueryString(example, exactMatch);
		String filterClause = this.buildSecurityFilterClause(example);
		String append = appendClauseToExample(example, exactMatch);
		whereClause = doSQLAppend(whereClause, append);
		whereClause = doSQLAppend(whereClause, filterClause);
		if (_log.isDebugEnabled()) _log.debug("Count QBE >> "+whereClause);
		return (Long) getEntityManager().createQuery("select count(obj) from " + 
				getEntityBeanType().getName() + " obj " + whereClause).getSingleResult();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final T loadEntityModel(final ID id) {
		return this.loadEntityModel(id, true, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final T loadEntityModel(final ID id, final boolean filter, final boolean lock) {
        T entity;
        if (filter && securityFilter!=null) {        	
			String filterClause = this.getSecurityFilter();
			String whereClause = doSQLAppend(filterClause, "obj.id = "+id);			
			Query query = getEntityManager().createQuery("from " + 
	        		getEntityBeanType().getName() + " obj where "+ whereClause);
			try {
				return (T) query.getSingleResult();
			} catch(NoResultException nre) {
				return null;
			}
        } else {
        	entity = getEntityManager().find(getEntityBeanType(), id);
        }
        if (lock)
        	getEntityManager().lock(entity, javax.persistence.LockModeType.WRITE);
        return entity;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override	
	public final void deleteEntityModel(final ID id) {
		T obj = getEntityManager().find(getEntityBeanType(), id);
		deleteEntityModel(obj);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override	
	public final void deleteEntityModel(final T obj) {
		setAuditUserId(obj);
		getEntityManager().remove(obj);
		getEntityManager().flush();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void saveEntityModel(final T obj) {
		// if class is auditable, we need to ensure userId is present
		_log.debug("Saving object " + obj.getClass());
		setAuditUserId(obj);
		_log.debug("User ID is " + obj.getAuditUserId());
		if (obj.isNew()) 
			getEntityManager().persist(obj);
		else {
			getEntityManager().merge(obj);
			getEntityManager().flush();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveAllEntityModel(final Collection<T> objects) {
		int ctr = 0;
		for (T t : objects) {
			saveEntityModel(t);
			if (++ctr % batchSize == 0){
				getEntityManager().flush();
				getEntityManager().clear();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getJpqlQuery(String key) {
		String query = (String) jpqlProperties.get(key);
		if (StringUtil.isEmpty(query)) {
			throw new InvalidImplementationException("Key ["+key+"] is not defined in custom jpql property file.");
		} else
			return query;
	}
	
	/**
	 * Returns the class that is handled by this Dao.
	 * 
	 * @return
	 */
    public final Class<T> getEntityBeanType() {
        return entityBeanType;
    }

    /**
     * Setter for the jpql property file.
     * 
     * @param properties
     */
	public final void setJpqlProperties(Properties properties) {
		this.jpqlProperties = properties;
	}	

	/**
	 * Setter method for entity manager.
	 * @param em
	 */
	public final void setEntityManager(EntityManager em) {
        this.em = em;
    }
	  
	/**
	 * @param securityFilter the securityFilter to set
	 */
	public final void setSecurityFilter(Map<String, String> filter) {
		this.securityFilter = filter;
	}

	/**
	 * Returns the entity manager for this Dao.
	 * 
	 * @return
	 */
    protected final EntityManager getEntityManager() {
        if (em == null)
            throw new IllegalStateException("EntityManager has not been set on Dao before usage");
        return em;
    }

	/**
	 * Override this method to append additional query conditions for findByExample.
	 * Useful for date range and other complex queries.
	 * @param example
	 * @param exactMatch
	 * @return
	 */
	protected String appendClauseToExample(T example, boolean exactMatch) {
		return "";
	}
	
	/**
	 * Override this method to append order clause to findByExample.
	 * @param example
	 * @return
	 */
	protected String appendOrderToExample(T example) {
		String clause="";
		//for search list ordering
		if(!StringUtil.isEmpty(example.getOrderOption()) && !StringUtil.isEmpty(example.getOrderFlow())){
			clause=" ORDER BY "+ example.getOrderOption() +" "+ example.getOrderFlow() +"";
		}
		return clause;
	}
	
	/**
	 * Private helper to build clause for security based filtering restriction 
	 * @param example
	 * @return
	 */
	private String buildSecurityFilterClause(T example) {
		// no protection implemented during application startup 
		// and when not implementing BaseProtectedEntity
		
		if ( 	ApplicationStartupListener.isApplicationStarted() && 
				example != null &&
				example.getClass().isAnnotationPresent(Protected.class) &&
				!example.getDisableProtection()
			) {
			return getSecurityFilter();
		}
		// no security needed
		return "";
	}
	
	/**
	 * Private helper to retrieve applicable security filter
	 * for the user and entity.
	 */
	private String getSecurityFilter() {
		// retrieve list of available security filters
		for (String key:securityFilter.keySet()) {
			if (SecurityUtil.currentUserHasPermission(key)) {
				String filterClause = securityFilter.get(key);
				SessionUser sessionUser = SecurityUtil.getSessionUser();
				return CrudUtil.replaceSQLParameters(filterClause, sessionUser);
			}
		}
		// no filter found?!? fine, let's disable access then...
		return "1!=1";		
	}
	
	/**
	 * Private helper that appends where statement on jpql.
	 * 
	 * @param whereClause
	 * @param append
	 * @return
	 */
	private String doSQLAppend(String whereClause, String append) {
		if (!StringUtil.isEmpty(append)) {
			if (StringUtil.isEmpty(whereClause))
				whereClause += " where ";
			else
				whereClause += " and ";
			whereClause += append;
		}			
		return whereClause;
	}
	
	/**
	 * Sets the userId within the web session for audit logging.
	 * @param obj
	 */
	private void setAuditUserId(T obj) {
		if (obj.getAuditUserId()==null)
			obj.setUserId();			
	}

	/**
	 * Sets the size by flushing when saving multiple entities
	 * on saveAllEntityModel method.
	 * 
	 * @param batchSize
	 */
	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}
}
