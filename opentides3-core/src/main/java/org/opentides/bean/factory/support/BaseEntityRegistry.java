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
package org.opentides.bean.factory.support;

import java.util.ArrayList;
import java.util.List;

import org.opentides.bean.BaseEntity;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

/**
 * This class retrieves all classes that extends BaseEntity.
 * 
 * @author allanctan
 *
 */
public class BaseEntityRegistry implements InitializingBean {

	private List<String> packagesToScan;
	
	private List<String> baseEntities;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (packagesToScan==null)
			packagesToScan = new ArrayList<String>();
		packagesToScan.add("org.opentides.bean");
		baseEntities = new ArrayList<String>();
		BeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
		ClassPathBeanDefinitionScanner s = new ClassPathBeanDefinitionScanner(registry);
		TypeFilter tf = new AssignableTypeFilter(BaseEntity.class);
		s.resetFilters(false);
		s.addIncludeFilter(tf);		
		s.scan(packagesToScan.toArray(new String[packagesToScan.size()]));
		for (String name:registry.getBeanDefinitionNames()) {
			Class<?> clazz = Class.forName(registry.getBeanDefinition(name).getBeanClassName());
			if (BaseEntity.class.isAssignableFrom(clazz))
				baseEntities.add(registry.getBeanDefinition(name).getBeanClassName());			
		}
	}

	/**
	 * @return the baseEntities
	 */
	public final List<String> getBaseEntities() {
		return baseEntities;
	}

	/**
	 * @param packagesToScan the packagesToScan to set
	 */
	public final void setPackagesToScan(List<String> packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

}
