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
import java.util.Map;

import org.opentides.bean.Sequence;
import org.opentides.dao.SequenceDao;
import org.springframework.stereotype.Repository;

/**
 * @author allantan
 * 
 */
@Repository("sequenceDao")
public class SequenceDaoJpaImpl extends BaseEntityDaoJpaImpl<Sequence, Long>
		implements SequenceDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opentides.dao.SequenceDao#incrementValue(java.lang.String)
	 */
	@Override
	public Long incrementValue(String key) {
		return incrementValue(key,1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opentides.dao.SequenceDao#incrementValue(java.lang.String, int)
	 */
	@Override
	public Long incrementValue(String key, int step) {
		synchronized (Sequence.class) {
			Sequence code = loadSequenceByKey(key);
			if (code == null)
				code = new Sequence(key,0l);
			code.setSkipAudit(true); // no need to audit auto-generated keys
			code.incrementValue(step);
			this.saveEntityModel(code);
			return code.getValue();
		}
	}
	
	@Override
	public Long incrementValue(String key, int step, boolean threadSafe) {
		if(threadSafe) {
			return incrementValue(key, step);
		} else {
			Sequence code = loadSequenceByKey(key);
			if (code == null)
				code = new Sequence(key,0l);
			code.setSkipAudit(true); // no need to audit auto-generated keys
			code.incrementValue(step);
			this.saveEntityModel(code);
			return code.getValue();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opentides.dao.SequenceDao#loadSequenceByKey(java.lang.String)
	 */
	@Override
	public Sequence loadSequenceByKey(String key) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("keyName",key);
		return findSingleResultByNamedQuery("jpql.sequence.findByKey", params);
	}

}
