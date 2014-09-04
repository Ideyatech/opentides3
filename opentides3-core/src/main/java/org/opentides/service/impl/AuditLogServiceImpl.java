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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opentides.bean.AuditLog;
import org.opentides.bean.BaseEntity;
import org.opentides.dao.AuditLogDao;
import org.opentides.service.AuditLogService;
import org.springframework.stereotype.Service;

/**
 * 
 * 
 * @author allantan
 * @author gino
 *
 */
@Service("auditLogService")
public class AuditLogServiceImpl extends BaseCrudServiceImpl<AuditLog> implements AuditLogService {
	
	/** 
	 * Inner class to do sorting 
	 * Sort with latest audit log first
	 * **/
	private static class CreateDateComparator implements Comparator<AuditLog> {
		@Override
		public int compare(AuditLog arg0, AuditLog arg1) {
			return arg1.getCreateDate().compareTo(arg0.getCreateDate());
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<AuditLog> findLogByReferenceAndClass(String reference,
			List<Class> types) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("reference", reference);
		if (types==null || types.isEmpty())
			return new ArrayList<AuditLog>();
		params.put("entityClass", types);
		return getAuditLogDao().findByNamedQuery("jpql.audit.findByReferenceAndClass",params);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<AuditLog> findLogLikeReferenceAndClass(String reference,
			List<Class> types) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("reference", reference);
		params.put("likeReference", "%"+reference+":%");
		if (types==null || types.isEmpty())
			return new ArrayList<AuditLog>();
		params.put("entityClass", types);
		return getAuditLogDao().findByNamedQuery("jpql.audit.findLikeReferenceAndClass",params);
	}

	@Override
	public void sortByDate(List<AuditLog> logs) {
		Collections.sort(logs, new CreateDateComparator());
		
	}
	
	@Override
	public void logEvent(String message, BaseEntity entity, boolean separateEm) {
		getAuditLogDao().logEvent(message, entity, separateEm);
	}
	
	public AuditLogDao getAuditLogDao() {
		return (AuditLogDao) super.getDao();
	}
	

}
