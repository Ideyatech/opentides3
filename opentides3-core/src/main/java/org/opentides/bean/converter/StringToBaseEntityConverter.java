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
package org.opentides.bean.converter;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.opentides.bean.factory.support.BaseEntityRegistry;
import org.opentides.service.BaseCrudService;
import org.opentides.util.NamingUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.stereotype.Component;

/**
 * Generic converter for all BaseEntity classes.
 * 
 * This converter assumes that a corresponding service that extends 
 * BaseCrudService is declared. For example, SystemCodes.class has 
 * SystemCodesService.
 * 
 * @author allanctan
 */
@Component("stringToBaseEntityConverter")
public class StringToBaseEntityConverter implements 
		ConditionalGenericConverter, BeanFactoryAware {

	private static Logger _log = Logger.getLogger(StringToBaseEntityConverter.class);

	@Autowired
	private BeanFactory beanFactory;
	
	@Autowired
	private BaseEntityRegistry baseEntityRegistry;
			
	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		Set<ConvertiblePair> convertibleTypes = new HashSet<ConvertiblePair>();
		for (String entity:baseEntityRegistry.getBaseEntities()) {
			try {
				//do not include system codes in this converter
				if (entity.contains("SystemCodes"))
					continue;
				convertibleTypes.add(new ConvertiblePair(String.class, Class.forName(entity)));
				_log.info("Adding generic string converter for " + entity);
			} catch (ClassNotFoundException e) {
				_log.error("Class not found ["+entity+"]",e);
			}
		}
		return convertibleTypes;
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		String serviceName = NamingUtil.toAttributeName(
				NamingUtil.getSimpleName(targetType.getName())) + "Service";
		BaseCrudService<?> service = (BaseCrudService<?>) beanFactory.getBean(serviceName);
		return service.load(source.toString());
	}

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		String serviceName = NamingUtil.toAttributeName(
				NamingUtil.getSimpleName(targetType.getName())) + "Service";
		Object service = beanFactory.getBean(serviceName);
		if (service != null && 
				service.getClass().isAssignableFrom(BaseCrudService.class) )
			return true;			
		return false;
	}
	
    // BeanFactoryAware setter (called by Spring during bean instantiation)
    public final void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

	/**
	 * @param baseEntityRegistry the baseEntityRegistry to set
	 */
	public final void setBaseEntityRegistry(BaseEntityRegistry baseEntityRegistry) {
		this.baseEntityRegistry = baseEntityRegistry;
	}
}
