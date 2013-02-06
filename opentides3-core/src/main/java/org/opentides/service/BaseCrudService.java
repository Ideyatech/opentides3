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
package org.opentides.service;

import java.util.List;

import org.opentides.bean.BaseEntity;
import org.opentides.dao.BaseEntityDao;


/**
 * @author allanctan
 *
 * @param <T>
 * @param <ID>
 */
public interface BaseCrudService<T extends BaseEntity> extends BaseService {
	public void save(T entity);
	public List<T> findAll();	
	public List<T> findAll(int start, int total);
	public List<T> findByExample(T example);	
	public List<T> findByExample(T example, int start, int total);
	public List<T> findByExample(T example, boolean exactMatch);	
	public List<T> findByExample(T example, boolean exactMatch, int start, int total);	
	public long countAll();
	public long countByExample(T example);
	public long countByExample(T example, boolean exactMatch);
	public T load(String sid);
	public T load(Long id);
	public T load(String sid, boolean filter);
	public T load(Long id, boolean filter);
	public void delete(String sid);
	public void delete(Long id);
	public void setDao(BaseEntityDao<T,Long>  dao);
}
