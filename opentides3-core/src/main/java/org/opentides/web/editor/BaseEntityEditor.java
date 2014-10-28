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
package org.opentides.web.editor;

import java.beans.PropertyEditorSupport;

import org.apache.log4j.Logger;
import org.opentides.bean.BaseEntity;
import org.opentides.service.BaseCrudService;
import org.opentides.util.StringUtil;

/**
 * PropertyEditor for all objects extending BaseEntity.
 * Used for mapping drop-down objects to forms values.
 * @author allantan
 *
 */
public class BaseEntityEditor extends PropertyEditorSupport {
	
	private static final Logger _log = Logger.getLogger(BaseEntityEditor.class);
	
	private BaseCrudService<? extends BaseEntity> entityService;

	public BaseEntityEditor(BaseCrudService<? extends BaseEntity> entityService) {
		this.entityService = entityService;
	}
	
	@Override
	public String getAsText() {
		if (getValue() != null) {
			BaseEntity editor = (BaseEntity) getValue();
			return editor.getId().toString();
		}
		return "";
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		try {
			if (!StringUtil.isEmpty(text)) {
				BaseEntity entity = entityService.load(text);
				setValue(entity);
			} else
				setValue(null);
		} catch (IllegalArgumentException iae) {
			_log.error("Failed to convert BaseEntity via ["+entityService.getClass().getName()+"]",iae);
			throw iae;
		}
	}

	/**
	 * @param systemCodesService
	 *            the systemCodesService to set
	 */
	public void setEntityService(
			BaseCrudService<? extends BaseEntity> entityService) {
		this.entityService = entityService;
	}
}
