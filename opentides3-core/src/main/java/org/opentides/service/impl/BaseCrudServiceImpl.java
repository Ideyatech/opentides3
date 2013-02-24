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
	 * Retrieves all the records on this object.
	 *
	 * @return List of objects
	 */
	public final List<T> findAll(final int start, final int total) {
		checkAccess("SEARCH");
		return dao.findAll(start, total);
	}

	/**
	 * Retrieves all the records on this object.
	 * 
	 * @return List of objects
	 */
	public final List<T> findAll() {
		checkAccess("SEARCH");
		return dao.findAll();
	}

	/**
	 * Retrieves matching records based on the object passed.
	 * 
	 * @return List of objects
	 */
	public final List<T> findByExample(T example, int start, int total) {
		checkAccess("SEARCH");
		return dao.findByExample(example, start, total);
	}

	/**
	 * Retrieves matching records based on the object passed.
	 * 
	 * @return List of objects
	 */
	public final List<T> findByExample(T example) {
		checkAccess("SEARCH");
		return dao.findByExample(example);
	}

	/**
	 * Retrieves matching records based on the object passed.
	 * 
	 * @return List of objects
	 */
	public final List<T> findByExample(T example, boolean exactMatch) {
		checkAccess("SEARCH");
		return dao.findByExample(example, exactMatch);
	}

	/**
	 * Retrieves matching records based on the object passed.
	 * 
	 * @return List of objects
	 */
	public final List<T> findByExample(T example, boolean exactMatch,
			int start, int total) {
		checkAccess("SEARCH");
		return dao.findByExample(example, exactMatch, start, total);
	}

	/**
	 * Counts all the record of this object
	 */
	public final long countAll() {
		return dao.countAll();
	}

	/**
	 * Counts the matching record of this object
	 */
	public final long countByExample(T example) {
		return dao.countByExample(example);
	}

	/**
	 * Counts the matching record of this object
	 */
	public final long countByExample(T example, boolean exactMatch) {
		return dao.countByExample(example, exactMatch);
	}

	/**
	 * Loads an object based on the given id
	 * 
	 * @param id
	 *            to load
	 * @return object
	 */
	public final T load(String sid) {
		if (StringUtil.isEmpty(sid)) {
			throw new InvalidImplementationException("ID parameter is empty.");
		} else {
			try {
				Long id = Long.parseLong(sid);
				return load(id);
			} catch (NumberFormatException nfe) {
				throw new InvalidImplementationException("ID parameter is not numeric.");
			}
		}
	}
	

	/**
	 * Loads an object based on the given id
	 * 
	 * @param id
	 *            to load
	 * @return object
	 */
	public final T load(Long id) {
		checkAccess("VIEW");
		return dao.loadEntityModel(id, true, false);
	}

	/**
	 * Loads an object based on the given id
	 * 
	 * @param id
	 *            to load
	 * @param filter - apply security filter?
	 * @return object
	 */
	public final T load(Long id, boolean filter) {
		checkAccess("VIEW");
		return dao.loadEntityModel(id, filter, false);
	}

	/**
	 * Loads an object based on the given id
	 * 
	 * @param id
	 *            to load
	 * @param filter - apply security filter?
	 * @return object
	 */
	public final T load(String sid, boolean filter) {
		if (StringUtil.isEmpty(sid)) {
			throw new InvalidImplementationException("id parameter is empty.");
		} else {
			try {
				Long id = Long.parseLong(sid);
				return load(id, filter);
			} catch (NumberFormatException nfe) {
				throw new InvalidImplementationException("id parameter is not numeric.");
			}
		}
	}
	
	/**
	 * Save the object via DAO.
	 * 
	 * @param object
	 *            to save
	 */
	@Transactional
	public final void save(T obj) {
		if (obj.getIsNew())
			checkAccess("ADD");
		else
			checkAccess("EDIT");
		dao.saveEntityModel(obj);
	}

	/**
	 * Deletes the object from the given id.
	 * 
	 * @param sid -
	 *            id to delete
	 */
	@Transactional
	public final void delete(String sid) {
		if (StringUtil.isEmpty(sid)) {
			throw new InvalidImplementationException("ID parameter is empty.");
		} else {
			try {
				Long id = Long.parseLong(sid);
				delete(id);
			} catch (NumberFormatException nfe) {
				throw new InvalidImplementationException("ID parameter is not numeric.");
			}
		}
	}

	/**
	 * Deletes the object from the given id.
	 * 
	 * @param sid -
	 *            id to delete
	 */
	@Transactional
	public final void delete(Long id) {
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