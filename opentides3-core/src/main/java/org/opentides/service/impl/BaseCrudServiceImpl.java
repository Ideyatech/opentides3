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

import java.util.List;

import org.opentides.bean.BaseEntity;
import org.opentides.dao.BaseEntityDao;
import org.opentides.exception.InvalidImplementationException;
import org.opentides.service.BaseCrudService;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

/**
 * This is a base implementation that supports CRUD operations.
 *
 * @author allanctan
 *
 */
public class BaseCrudServiceImpl<T extends BaseEntity> extends
		BaseServiceDefaultImpl implements BaseCrudService<T>, InitializingBean {

	private BaseEntityDao<T, Long> dao;

	/**
	 * Retrieves all the records on this object.
	 *
	 * @return List of objects
	 */
	public final List<T> findAll(final int start, final int total) {
		return dao.findAll(start, total);
	}

	/**
	 * Retrieves all the records on this object.
	 * 
	 * @return List of objects
	 */
	public final List<T> findAll() {
		return dao.findAll();
	}

	/**
	 * Retrieves matching records based on the object passed.
	 * 
	 * @return List of objects
	 */
	public final List<T> findByExample(T example, int start, int total) {
		return dao.findByExample(example, start, total);
	}

	/**
	 * Retrieves matching records based on the object passed.
	 * 
	 * @return List of objects
	 */
	public final List<T> findByExample(T example) {
		return dao.findByExample(example);
	}

	/**
	 * Retrieves matching records based on the object passed.
	 * 
	 * @return List of objects
	 */
	public final List<T> findByExample(T example, boolean exactMatch) {
		return dao.findByExample(example, exactMatch);
	}

	/**
	 * Retrieves matching records based on the object passed.
	 * 
	 * @return List of objects
	 */
	public final List<T> findByExample(T example, boolean exactMatch,
			int start, int total) {
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
		return dao.loadEntityModel(id, false, false);
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
		dao.deleteEntityModel(id);
	}

	/**
	 * Make sure DAO is properly set.
	 */
	public final void afterPropertiesSet() throws Exception {
		if (dao == null) {
			throw new IllegalArgumentException("Must specify a dao for "
					+ getClass().getName());
		}
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public final void setDao(BaseEntityDao<T, Long> dao) {
		this.dao = dao;
	}

	/**
	 * @return the dao
	 */
	public BaseEntityDao<T, Long> getDao() {
		return dao;
	}
}