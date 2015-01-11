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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.opentides.bean.user.AccountType;
import org.opentides.dao.AccountTypeDao;
import org.opentides.util.StringUtil;
import org.springframework.stereotype.Repository;

/**
 * DAO for Account Type.
 * 
 * @author allantan
 *
 */
@Repository("accountTypeDao")
public class AccountTypeDaoJpaImpl extends BaseEntityDaoJpaImpl<AccountType, Long> implements AccountTypeDao {	
	
	private static final Logger _log = Logger.getLogger(AccountTypeDaoJpaImpl.class);

	@Override
	public AccountType getByName(String name) {
		if (StringUtil.isEmpty(name))
			return null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		List<AccountType> result = findByNamedQuery("jpql.accountType.findByName",
				params);
		if (result == null || result.size() == 0) {
			return null;
		} else {
			return result.get(0);
		}

	}
	
}
