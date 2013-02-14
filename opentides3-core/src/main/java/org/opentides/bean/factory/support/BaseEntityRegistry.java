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
import org.springframework.stereotype.Component;

/**
 * This class retrieves all classes that extends BaseEntity.
 * 
 * @author allanctan
 *
 */
@Component
public class BaseEntityRegistry implements InitializingBean {

	private List<String> packages;
	
	private List<String> baseEntities;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (packages==null)
			packages = new ArrayList<String>();
		packages.add("org.opentides.bean");
		baseEntities = new ArrayList<String>();
		BeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
		ClassPathBeanDefinitionScanner s = new ClassPathBeanDefinitionScanner(registry);
		TypeFilter tf = new AssignableTypeFilter(BaseEntity.class);
		s.resetFilters(false);
		s.addIncludeFilter(tf);		
		s.scan(packages.toArray(new String[packages.size()]));		
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
	 * @param packages the packages to set
	 */
	public final void setPackages(List<String> packages) {
		this.packages = packages;
	}

}
