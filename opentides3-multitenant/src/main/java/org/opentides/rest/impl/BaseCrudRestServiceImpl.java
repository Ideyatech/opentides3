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

package org.opentides.rest.impl;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.opentides.bean.BaseEntity;
import org.opentides.bean.RestResponse;
import org.opentides.bean.SearchResults;
import org.opentides.exception.InvalidImplementationException;
import org.opentides.service.BaseCrudService;
import org.opentides.service.impl.BaseServiceDefaultImpl;
import org.opentides.util.CrudUtil;
import org.opentides.util.MTCacheUtil;
import org.opentides.util.StringUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * This is a base implementation that supports CRUD operations 
 * invoking REST services following the convention used by 
 * BaseCrudController.
 *
 * @author allanctan
 *
 */
public class BaseCrudRestServiceImpl<T extends BaseEntity> extends
		BaseServiceDefaultImpl implements BaseCrudService<T> {
	
	private static final Logger _log = Logger.getLogger(BaseCrudRestServiceImpl.class);
	
	// Server context where REST service is available (e.g. http://202.23.23.103:8080/ss/organization/user)
	protected String serverURL;
	
	// Rest template
	protected RestTemplate restTemplate;

	/**
	 * This attribute contains the class type of the bean.
	 */
	protected Class<T> entityBeanType;
	
	/**
	 * Parameter when invoking RestTemplate with Basic Authentication.
	 */
	protected HttpEntity<String> entity; 


	@Override
	public List<T> findByNamedQuery(String name, Map<String, Object> params) {
		return findByNamedQuery(name, params, -1, -1);
	}

	@Override
	public List<T> findByNamedQuery(String name, Map<String, Object> params,
			int start, int total) {
		return findByNamedQuery(name, params, start, total, false);
	}

	@Override
	public List<T> findByNamedQuery(String name, Map<String, Object> params,
			int start, int total, boolean bypassSecurity) {
		throw new UnsupportedOperationException("findByNamedQuery is not supported via REST call using BaseCrudRestServiceImpl.");
	}

	@Override
	public List<T> findByNamedQuery(String name, int start, int total,
			boolean bypassSecurity, Object... params) {
		throw new UnsupportedOperationException("findByNamedQuery is not supported via REST call using BaseCrudRestServiceImpl.");
	}

	@Override
	public T findSingleResultByNamedQuery(String name,
			Map<String, Object> params) {
		return findSingleResultByNamedQuery(name, params, false);
	}

	@Override
	public T findSingleResultByNamedQuery(String name,
			Map<String, Object> params, boolean bypassSecurity) {
		throw new UnsupportedOperationException("findSingleResultByNamedQuery is not supported via REST call using BaseCrudRestServiceImpl.");
	}

	@Override
	public T findSingleResultByNamedQuery(String name, Object... params) {
		return findSingleResultByNamedQuery(name, false, params);
	}

	@Override
	public T findSingleResultByNamedQuery(String name, boolean bypassSecurity,
			Object... params) {
		throw new UnsupportedOperationException("findSingleResultByNamedQuery is not supported via REST call using BaseCrudRestServiceImpl.");
	}

	@Override
	public int executeByNamedQuery(String name, Map<String, Object> params) {
		throw new UnsupportedOperationException("executeByNamedQuery is not supported via REST call using BaseCrudRestServiceImpl.");
	}

	@Override
	public int executeByNamedQuery(String name, Object... params) {
		throw new UnsupportedOperationException("executeByNamedQuery is not supported via REST call using BaseCrudRestServiceImpl.");
	}

	@Override
	public List<T> findAll() {
		return findAll(false);
	}

	@Override
	public List<T> findAll(boolean bypassSecurity) {
		throw new UnsupportedOperationException("findAll is not supported via REST call using BaseCrudRestServiceImpl.");
	}

	@Override
	public List<T> findAll(int start, int total) {
		return findAll(start, total, false);
	}

	@Override
	public List<T> findAll(int start, int total, boolean bypassSecurity) {
		throw new UnsupportedOperationException("findAll is not supported via REST call using BaseCrudRestServiceImpl.");

	}

	@Override
	public List<T> findByExample(T example) {
		throw new UnsupportedOperationException("findByExample is not supported via REST call using BaseCrudRestServiceImpl.");
	}

	@Override
	public List<T> findByExample(T example, int start, int total) {
		return findByExample(example, start, total, false);
	}

	@Override
	public List<T> findByExample(T example, int start, int total,
			boolean bypassSecurity) {
		throw new UnsupportedOperationException("findByExample is not supported via REST call using BaseCrudRestServiceImpl.");
	}

	@Override
	public List<T> findByExample(T example, boolean exactMatch) {
		return findByExample(example, exactMatch, false);
	}

	@Override
	public List<T> findByExample(T example, boolean exactMatch,
			boolean bypassSecurity) {
		throw new UnsupportedOperationException("findByExample is not supported via REST call using BaseCrudRestServiceImpl.");
	}

	@Override
	public List<T> findByExample(T example, boolean exactMatch, int start,
			int total) {
		return findByExample(example, exactMatch, start, total, false);
	}

	@Override
	public List<T> findByExample(T example, boolean exactMatch, int start,
			int total, boolean bypassSecurity) {
		throw new UnsupportedOperationException("findByExample is not supported via REST call using BaseCrudRestServiceImpl.");
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResults<T> search(T command, int page) {
		String parameter = CrudUtil.buildURLParameters(command);
	    ResponseEntity<SearchResults> response = 
	      restTemplate.exchange(serverURL+"?p="+page+"&"+parameter, 
	    		  HttpMethod.GET, entity, SearchResults.class);
	    SearchResults<T> resource = response.getBody();
		return resource;
	}

	@Override
	public long countAll() {
		throw new UnsupportedOperationException("countAll is not supported via REST call using BaseCrudRestServiceImpl.");

	}

	@Override
	public long countByExample(T example) {
		throw new UnsupportedOperationException("countByExample is not supported via REST call using BaseCrudRestServiceImpl.");
	}

	@Override
	public long countByExample(T example, boolean exactMatch) {
		throw new UnsupportedOperationException("countByExample is not supported via REST call using BaseCrudRestServiceImpl.");
	}

	@Override
	public T load(String sid) {
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

	@Override
	public T load(Long id) {
	    ResponseEntity<T> response = 
	      restTemplate.exchange(serverURL+"/"+id, HttpMethod.GET, entity, entityBeanType);
	    T resource = response.getBody();
		return resource;
	}

	@Override
	public T load(String sid, boolean filter) {
		_log.warn("Ignoring filter parameter via REST call on loading of "+entityBeanType.getSimpleName());
		return this.load(sid);
	}

	@Override
	public T load(String sid, boolean filter, boolean bypassSecurity) {
		_log.warn("Ignoring filter and bypassSecurity parameter via REST call on loading of "+entityBeanType.getSimpleName());
		return this.load(sid);
	}

	@Override
	public T load(Long id, boolean filter) {
		_log.warn("Ignoring filter parameter via REST call on loading of "+entityBeanType.getSimpleName());
		return this.load(id);
	}

	@Override
	public T load(Long id, boolean filter, boolean bypassSecurity) {	
		_log.warn("Ignoring filter and bypassSecurity parameter via REST call on loading of "+entityBeanType.getSimpleName());
		return this.load(id);
	}

	@Override
	public void save(T command) {
		Map<String, String> vars = new HashMap<String, String>();
        vars.put("id", command.getId()==null?"":command.getId().toString());
		
//		ResponseEntity<T> response = 
//			      restTemplate.exchange(serverURL+"/"+sid, HttpMethod.POST, command, entityBeanType);
//		T resource = response.getBody();
//				return resource;
//				
		// T response = restTemplate.postForObject(serverURL+"/"+sid, request, entityBeanType);
        
		// setup entity(headers) for basic authentication.
        List<String> restableFields = MTCacheUtil.getRestableFields(command);
        Map<String,Object> map = CrudUtil.buildMapValues(command, restableFields);
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<String, Object>();
        for (String key:map.keySet()) {
        	form.add(key,map.get(key));
        }
        RestResponse<T> r = new RestResponse<T>();
        RestResponse<T> response = restTemplate.postForObject(serverURL+"/{id}", 
        		form, r.getClass(), vars);
		response.toString();
	}

	@Override
	public void save(T command, boolean bypassSecurity) {
		_log.warn("Ignoring bypassSecurity parameter via REST call on saving of "+entityBeanType.getSimpleName());
		this.save(command);
	}

	@Override
	public void delete(String sid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String sid, boolean bypassSecurity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id, boolean bypassSecurity) {
		// TODO Auto-generated method stub
		
	} 
	
	/**
	 * Make sure RestTemplate is properly set.
	 */
	@SuppressWarnings("unchecked")
	@PostConstruct
	public final void afterPropertiesSet() throws Exception {
		try {
			this.entityBeanType = (Class<T>) ((ParameterizedType) getClass()
					.getGenericSuperclass()).getActualTypeArguments()[0];
		} catch (ClassCastException cc) {
			// if dao is extended from the generic dao class
			this.entityBeanType = (Class<T>) ((ParameterizedType) getClass()
					.getSuperclass().getGenericSuperclass())
					.getActualTypeArguments()[0];
		}

		// setup entity(headers) for basic authentication.
		HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); 
	    this.entity = new HttpEntity<String>(headers);
		
	    // setup the rest template
		if (restTemplate == null) {
			restTemplate = new RestTemplate();
		}

		Assert.notNull(this.entityBeanType,
				"Unable to retrieve entityBeanType for "
						+ this.getClass().getSimpleName());

	}
	
	/**
	 * @param serverURI the serverURI to set
	 */
	public final void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}

	@Override
	public void setPageSize(Integer pageSize) {
		// TODO Auto-generated method stub	
	}
	
}
