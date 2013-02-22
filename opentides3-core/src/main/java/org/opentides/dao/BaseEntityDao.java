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

package org.opentides.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.opentides.bean.BaseEntity;


/**
 * An interface shared by all business data access objects.
 * <p>
 * All CRUD (create, read, update, delete) basic data access operations are
 * isolated in this interface and shared across all DAO implementations. 
 * This implementation is patterned after GenericDAO from Hibernate CaveatEmptor.
 *
 * @author allanctan
 */
public interface BaseEntityDao<T extends BaseEntity, ID extends Serializable> {	
	/**
	 * Returns all entries of this entity
	 * @param start - start index to fetch results
	 * @param total - total number of records to return
	 * @return List of entity
	 */
	public List<T> findAll();
	
	/**
	 * Returns all entries of this entity
	 * @return List of entity
	 */
	public List<T> findAll(int start, int total);
	
	
	/**
	 * Returns all entries found by the named query
	 * @return List of entity
	 */
	public List<T> findByNamedQuery(String name, Map<String, Object> params);
	
	/**
	 * Returns all entries found by the named query with start and limit
	 * @return List of entity
	 */
	public List<T> findByNamedQuery(final String name, final Map<String,Object> params, int start, int total);
	
	/**
	 * Returns an entry found by the named query
	 * @return Single entity
	 */
	public T findSingleResultByNamedQuery(String name, Map<String,Object> params);

	/**
	 * Executes SQL operation by named query. 
	 * Use for DDL operations.
	 * 
	 * @param name
	 * @param params
	 * @return
	 */
	public int executeByNamedQuery(String name, Map<String,Object> params);

	/**
	 * Performs a query by example.
	 * 
	 * @param example - example to match
	 * @param exactMatch - true to find only exact matches, otherwise use like
	 * @return
	 */
	public List<T> findByExample(T example, boolean exactMatch);
	
	/**
	 * Performs a query by example.
	 * 
	 * @param example - example to match
	 * @param exactMatch - true to find only exact matches, otherwise use like
	 * @param start - start index to fetch results
	 * @param total - total number of records to return
	 * @return
	 */
	public List<T> findByExample(T example, boolean exactMatch, int start, int total);
	
	/**
	 * Performs a query by example. Assumes String will be match using like.
	 * 
	 * @param example - example to match
	 * @return
	 */
	public List<T> findByExample(T example);
	
	/**
	 * Performs a query by example.
	 * 
	 * @param example - example to match
	 * @param start - start index to fetch results
	 * @param total - total number of records to return
	 * @return
	 */
	public List<T> findByExample(T example, int start, int total);
	
	/**
	 * Counts all entries of this entity
	 * @return number of entity
	 */
	public long countAll();
	
	/**
	 * Counts all the matching entries with the given example
	 */
	public long countByExample(T example);
	
	/**
	 * Counts all the matching entries with the given example
	 * @param example - example to match
	 * @param exactMatch - true to find only exact matches, otherwise use like
	 */
	public long countByExample(T example, boolean exactMatch);
    
	/**
	 * Returns instance of the entity. Assuming filter and lock is false.
	 * @param id
	 * @return
	 */
	public T loadEntityModel(ID id);
	
	/**
	 * Returns instance of the entity.
	 * @param id
	 * @param filter - should we append filter clause
	 * @param lock - do we acquire a lock for writing
	 * @return
	 */
	public T loadEntityModel(ID id, boolean filter, boolean lock);
	
	/**
	 * Removes the entity
	 * @param id
	 */
	public void deleteEntityModel(ID id);

	/**
	 * Removes the entity
	 * @param obj
	 */
	public void deleteEntityModel(T obj);

	/**
	 * Add or update the entity as appropriate
	 * @param obj
	 */
	public void saveEntityModel(T obj);
	
	/**
	 * Add or update a collection of objects
	 * @param objects
	 */
	public void saveAllEntityModel(Collection<T> objects);
	
	/**
	 * Sets the size by flushing when saving multiple entities
	 * on saveAllEntityModel method.
	 * 
	 * @param batchSize
	 */
	public void setBatchSize(int batchSize);
	
	/**
	 * Retrieves the jpql statement from preloaded properties file.
	 * @param key - key of jpql to retrieve
	 * @return - jpql statement of the key
	 */
	public String getJpqlQuery(String key);	
}
