/*
 * Copyright 2007-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opentides.service;

import java.util.List;

import org.opentides.bean.BaseEntity;


/**
 * This is the interface for base class of all CRUD service modules.
 * 
 * @author allanctan
 *
 * @param <T>
 * @param <ID>
 */
public interface BaseCrudService<T extends BaseEntity> extends BaseService {

	/**
	 * Finds all the objects in the entity.
	 * Overloaded method that assumes permission security is checked when class is marked 
	 * with @CrudSecure.
	 * 
	 * @return List of all objects
	 */
	public List<T> findAll();
	
	/**
	 * Retrieves all the records in the entity.
	 * 
	 * @param bypassSecurity - checks whether permissions security is checked (or not).
	 * @return List of objects

	 * Note: bypassSecurity works only when service is marked with @CrudSecure, 
	 * Otherwise, bypassSecurity is ignored and no security check is performed.
	 */
	public List<T> findAll(boolean bypassSecurity);
	
	/**
	 * Retrieves all the records on this object from start and maximum records of total.
	 * Overloaded method that assumes permission security is checked when class is marked 
	 * with @CrudSecure.
	 *
	 * @param start - starting record index
	 * @param total - maximum number of records to be returned
	 * 
	 * @return List of matching objects
	 */
	public List<T> findAll(int start, int total);
	
	/**
	 * Retrieves all the records on this object from start and maximum records of total.
	 *
	 * @param start - starting record index
	 * @param total - maximum number of records to be returned
	 * @param bypassSecurity - checks whether permissions security is checked (or not).
	 * 
	 * Note: bypassSecurity works only when service is marked with @CrudSecure, 
	 * Otherwise, bypassSecurity is ignored and no security check is performed.
	 * 
	 * @return List of matching objects
	 */
	public List<T> findAll(int start, int total, boolean bypassSecurity);
	
	/**
	 * Retrieves matching records based on the object passed.
	 * Assumes permission security is checked when class is marked
	 * with @CrudSecure.
	 * 
	 * @param T example - search for objects with matching values based on this example.
	 * 
	 * @return List of objects
	 */
	public List<T> findByExample(T example);	
	
	/**
	 * Retrieves matching records based on the object passed.
	 * Overloaded method that assumes permission security is checked when class is marked 
	 * with @CrudSecure.
	 * 
	 * @param T example - search for objects with matching values based on this example
	 * @param start - starting record index
	 * @param total - maximum number of records to be returned
	 * 
	 * @return List of objects
	 */
	public List<T> findByExample(T example, int start, int total);
	
	/**
	 * Retrieves matching records based on the object passed.
	 * 
	 * @param T example - search for objects with matching values based on this example
	 * @param start - starting record index
	 * @param total - maximum number of records to be returned
	 * @param bypassSecurity - checks whether permissions security is checked (or not).
	 * 
	 * Note: bypassSecurity works only when service is marked with @CrudSecure, 
	 * Otherwise, bypassSecurity is ignored and no security check is performed.
	 */
	public List<T> findByExample(T example, int start, int total, boolean bypassSecurity);

	/**
	 * Retrieves matching records based on the object passed as an example.
	 * Overloaded method that assumes permission security is checked when class is marked 
	 * with @CrudSecure.
	 * 
	 * @param T example - search for objects with matching values based on this example.
	 * 
	 * @return List of objects
	 */
	public List<T> findByExample(T example, boolean exactMatch);	
	
	/**
	 * Retrieves matching records based on object passed as an example.
	 * 
	 * @param T example - search for objects with matching values based on this example
	 * @param exactMatch - search for exactly the same value. 
	 * 					   If false, uses like '%<keyword>%' during search.
	 * @param bypassSecurity - checks whether permissions security is checked (or not).
	 * 
	 * Note: bypassSecurity works only when service is marked with @CrudSecure, 
	 * Otherwise, bypassSecurity is ignored and no security check is performed.
	 */
	public List<T> findByExample(T example, boolean exactMatch, boolean bypassSecurity);	
	
	/**
	 * Retrieves matching records based on the object passed as an example.
	 * Overloaded method that assumes permission security is checked when class is marked 
	 * with @CrudSecure.
	 * 
	 * @param T example - search for objects with matching values based on this example.
	 * @param exactMatch - search for exactly the same value. 
	 * 					   If false, uses like '%<keyword>%' during search.
	 * @param start - starting record index
	 * @param total - maximum number of records to be returned
	 * 
	 * @return List of objects
	 */
	public List<T> findByExample(T example, boolean exactMatch, int start, int total);	

	/**
	 * Retrieves matching records based on the object passed as an example.
	 * Overloaded method that assumes permission security is checked when class is marked 
	 * with @CrudSecure.
	 * 
	 * @param T example - search for objects with matching values based on this example.
	 * @param exactMatch - search for exactly the same value. 
	 * 					   If false, uses like '%<keyword>%' during search.
	 * @param start - starting record index
	 * @param total - maximum number of records to be returned
	 * @param bypassSecurity - checks whether permissions security is checked (or not).
	 * 
	 * Note: bypassSecurity works only when service is marked with @CrudSecure, 
	 * Otherwise, bypassSecurity is ignored and no security check is performed.
	 * 
	 * @return List of objects
	 */
	public List<T> findByExample(T example, boolean exactMatch, int start, int total, boolean bypassSecurity);	

	/**
	 * Counts all the record of this object.
	 * 
	 * Note: No permission checking on count operations.
	 */
	public long countAll();
	
	/**
	 * Counts the matching record of this object
	 * 
	 * Note: No permission checking on count operations.
	 */
	public long countByExample(T example);
	
	/**
	 * Counts the matching record of this object
	 * 
	 * Note: No permission checking on count operations.
	 */
	public long countByExample(T example, boolean exactMatch);
	
	/**
	 * Loads an object based on the given id.
	 * Overloaded method that assumes permission security is checked when class is marked 
	 * with @CrudSecure.
	 * 
	 * @param sid - object to load
	 * @return object
	 */
	public T load(String sid);
	
	/**
	 * Loads an object based on the given id.
	 * Overloaded method that assumes permission security is checked when class is marked 
	 * with @CrudSecure.
	 * 
	 * @param id - object to load
	 * @return object
	 */
	public T load(Long id);
	
	/**
	 * Loads an object based on the given id.
	 * Overloaded method that assumes permission security is checked when class is marked 
	 * with @CrudSecure.
	 * 
	 * @param id - to load
	 * @param filter - apply security filter. If yes, security filter will be used when 
	 *                 retrieving the object.
	 * @return object
	 */
	public T load(String sid, boolean filter);
	
	/**
	 * Loads an object based on the given id
	 * 
	 * @param id - to load
	 * @param filter - apply security filter. If yes, security filter will be used when 
	 *                 retrieving the object.
	 * @param bypassSecurity - checks whether permissions security is checked (or not).
	 * 
	 * Note: bypassSecurity works only when service is marked with @CrudSecure, 
	 * Otherwise, bypassSecurity is ignored and no security check is performed.
	 * 
	 * @return object
	 */
	public T load(String sid, boolean filter, boolean bypassSecurity);
	
	/**
	 * Loads an object based on the given id
	 * Overloaded method that assumes permission security is checked when class is marked 
	 * with @CrudSecure.
	 * 
	 * @param id - to load
	 * @param filter - apply security filter. If yes, security filter will be used when 
	 *                 retrieving the object.
	 * @return object
	 */
	public T load(Long id, boolean filter);
	
	/**
	 * Loads an object based on the given id
	 * 
	 * @param id - to load
	 * @param filter - apply security filter. If yes, security filter will be used when 
	 *                 retrieving the object.
	 * @param bypassSecurity - checks whether permissions security is checked (or not).
	 * 
	 * Note: bypassSecurity works only when service is marked with @CrudSecure, 
	 * Otherwise, bypassSecurity is ignored and no security check is performed.
	 * 
	 * @return object
	 */
	public T load(Long id, boolean filter, boolean bypassSecurity);
	
	/**
	 * Save the object via DAO, assumes permission security is in 
	 * checked when marked with @CrudSecure
	 * 
	 * @param object
	 *            to save
	 */
	public void save(T entity);
	
	/**
	 * Save the object via DAO.
	 * 
	 * @param object - to save
	 * @param bypassSecurity - checks whether permissions security is checked (or not).
	 * 
	 * Note: bypassSecurity works only when service is marked with @CrudSecure, 
	 * Otherwise, bypassSecurity is ignored and no security check is performed.
	 */
	public void save(T entity, boolean bypassSecurity);
	
	/**
	 * Deletes the object from the given id.
	 * Overloaded method that assumes permission security is checked when class is marked 
	 * with @CrudSecure.
	 * 
	 * @param sid - id to delete
	 */
	public void delete(String sid);
	
	/**
	 * Deletes the object from the given id.
	 * 
	 * @param sid - id to delete
	 * @param bypassSecurity - checks whether permissions security is checked (or not).
	 * 
	 * Note: bypassSecurity works only when service is marked with @CrudSecure, 
	 * Otherwise, bypassSecurity is ignored and no security check is performed.
	 */	
	public void delete(String sid, boolean bypassSecurity);
	
	/**
	 * Deletes the object from the given id.
	 * Overloaded method that assumes permission security is checked when class is marked 
	 * with @CrudSecure.
	 * 
	 * @param sid -
	 *            id to delete
	 */	
	public void delete(Long id);
	
	/**
	 * Deletes the object from the given id.
	 * @param bypassSecurity - checks whether permissions security is checked (or not).
	 * 
	 * Note: bypassSecurity works only when service is marked with @CrudSecure, 
	 * Otherwise, bypassSecurity is ignored and no security check is performed.
	 * 
	 * @param id - id to delete
	 */	
	public void delete(Long id, boolean bypassSecurity);
}
