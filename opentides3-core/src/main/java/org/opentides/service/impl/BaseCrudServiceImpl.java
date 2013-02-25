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
package org.opentides.service.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.PostConstruct;

import org.opentides.annotation.CrudSecure;
import org.opentides.bean.BaseEntity;
import org.opentides.dao.BaseEntityDao;
import org.opentides.exception.InvalidImplementationException;
import org.opentides.service.BaseCrudService;
import org.opentides.util.NamingUtil;
import org.opentides.util.SecurityUtil;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * This is a base implementation that supports CRUD operations.
 *
 * @author allanctan
 *
 */
public class BaseCrudServiceImpl<T extends BaseEntity> extends
		BaseServiceDefaultImpl implements BaseCrudService<T> {

	// contains the class type of the bean    
    private Class<T> entityBeanType;
    
    // corresponding dao for the service
	protected BaseEntityDao<T, Long> dao;
	
	@Autowired
	private BeanFactory beanFactory;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<T> findAll() {
		return findAll(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<T> findAll(final boolean bypassSecurity) {
		if (!bypassSecurity)
			checkAccess("SEARCH");
		return dao.findAll();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<T> findAll(final int start, final int total) {
		return findAll(start, total, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<T> findAll(final int start, final int total, final boolean bypassSecurity) {
		if (!bypassSecurity) 
			checkAccess("SEARCH");
		return dao.findAll(start, total);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<T> findByExample(final T example) {
		checkAccess("SEARCH");
		return dao.findByExample(example);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<T> findByExample(final T example, final int start, final int total) {
		return findByExample(example, start, total, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<T> findByExample(final T example, final int start, final int total,
			final boolean bypassSecurity) {
		if (!bypassSecurity) 
			checkAccess("SEARCH");
		return dao.findByExample(example, start, total);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<T> findByExample(T example, boolean exactMatch) {
		return findByExample(example, exactMatch, false);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<T> findByExample(T example, boolean exactMatch,
			boolean bypassSecurity) {
		if (!bypassSecurity)
			checkAccess("SEARCH");
		return dao.findByExample(example, exactMatch);
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public final List<T> findByExample(T example, boolean exactMatch,
			int start, int total) {
		return findByExample(example, exactMatch, start, total, false);
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public List<T> findByExample(T example, boolean exactMatch, int start,
			int total, boolean bypassSecurity) {
		if (!bypassSecurity)
			checkAccess("SEARCH");
		return dao.findByExample(example, exactMatch, start, total);
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public final long countAll() {
		return dao.countAll();
	}


	/**
	 * {@inheritDoc}
	 */		@Override
	public final long countByExample(T example) {
		return dao.countByExample(example);
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public final long countByExample(T example, boolean exactMatch) {
		return dao.countByExample(example, exactMatch);
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public final T load(String sid) {
		if (StringUtil.isEmpty(sid)) {
			throw new InvalidImplementationException("ID parameter is empty.");
		} else {
			try {
				Long id = Long.parseLong(sid);
				return load(id, true, false);
			} catch (NumberFormatException nfe) {
				throw new InvalidImplementationException("ID parameter is not numeric.");
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */	
	@Override
	public final T load(Long id) {
		return dao.loadEntityModel(id, true, false);
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public final T load(String sid, boolean filter) {
		return load(sid, filter, false);
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public T load(String sid, boolean filter, boolean bypassSecurity) {
		if (!bypassSecurity)
			checkAccess("VIEW");
		if (StringUtil.isEmpty(sid)) {
			throw new InvalidImplementationException("id parameter is empty.");
		} else {
			try {
				Long id = Long.parseLong(sid);
				if (filter)
					return dao.loadEntityModel(id, filter, false);
				else
					return dao.loadEntityModel(id);
			} catch (NumberFormatException nfe) {
				throw new InvalidImplementationException("id parameter is not numeric.");
			}
		}		
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public final T load(Long id, boolean filter) {
		return load(id, filter, false);
	}
	
	/**
	 * {@inheritDoc}
	 */	
	@Override
	public T load(Long id, boolean filter, boolean bypassSecurity) {
		if (!bypassSecurity)
			checkAccess("VIEW");		
		if (filter)
			return dao.loadEntityModel(id, filter, false);
		else
			return dao.loadEntityModel(id);
	}
	
	/**
	 * {@inheritDoc}
	 */	
	@Override
	public final void save(T entity) {
		save(entity, false);
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	@Transactional
	public void save(T entity, boolean bypassSecurity) {
		if (!bypassSecurity) {
			if (entity.getIsNew())
				checkAccess("ADD");
			else
				checkAccess("EDIT");			
		}
		dao.saveEntityModel(entity);
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public final void delete(String sid) {
		delete(sid, false);
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	@Transactional
	public void delete(String sid, boolean bypassSecurity) {
		if (!bypassSecurity)
			checkAccess("DELETE");
		if (StringUtil.isEmpty(sid)) {
			throw new InvalidImplementationException("ID parameter is empty.");
		} else {
			try {
				Long id = Long.parseLong(sid);
				dao.deleteEntityModel(id);
			} catch (NumberFormatException nfe) {
				throw new InvalidImplementationException("ID parameter is not numeric.");
			}
		}		
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public final void delete(Long id) {
		delete(id, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void delete(Long id, boolean bypassSecurity) {
		if (!bypassSecurity)
			checkAccess("DELETE");
		dao.deleteEntityModel(id);		
	}
	
	/**
	 * Make sure DAO is properly set.
	 */
	@PostConstruct
	@SuppressWarnings("unchecked")
	public final void afterPropertiesSet() throws Exception {
		try {
	        this.entityBeanType = (Class<T>) ((ParameterizedType) getClass()
	                .getGenericSuperclass()).getActualTypeArguments()[0];	        
		} catch (ClassCastException cc) {
			// if dao is extended from the generic dao class
			this.entityBeanType = (Class<T>) ((ParameterizedType) getClass().getSuperclass()
	                .getGenericSuperclass()).getActualTypeArguments()[0];
		}
		// try setting up service and validator by convention
		String attributeName = NamingUtil.toAttributeName(this.entityBeanType.getSimpleName());
        String daoBean   = attributeName + "Dao";
        
        if (this.dao==null && beanFactory.containsBean(daoBean))
        	this.dao = (BaseEntityDao<T,Long>) beanFactory.getBean(daoBean);
        
        Assert.notNull(this.dao, this.getClass().getSimpleName() + 
        					" is not associated with a dao class [" + daoBean +
        					"]. Please check your configuration.");
	}
	
	/**
	 * Private helper class that checks for permission on performing
	 * the action.
	 * @param action
	 */
	private void checkAccess(String action) {
		if (this.getClass().isAnnotationPresent(CrudSecure.class)) {
			String name = this.getClass().getAnnotation(CrudSecure.class).value();
			if (StringUtil.isEmpty(name))
				name = this.entityBeanType.getSimpleName().toUpperCase();			
			if (!SecurityUtil.currentUserHasPermission(action+"_"+name))
				throw new AccessDeniedException("Unauthorized access. You do not have permission to perform "+action+" on " + this.entityBeanType + ".");			
		}		
	}

	/**
	 * @return the dao
	 */
	public BaseEntityDao<T, Long> getDao() {
		return dao;
	}

}