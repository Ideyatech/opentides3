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
package org.opentides.context.support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * Extension of ReloadableResourceBundleMessageSource to support pattern based
 * matching of resource files. Please note that this matcher is limited and 
 * designed for pattern like xyz-messages.properties.
 * 
 * @author allantan
 */
public class PatternResourceBundleMessageSource extends ReloadableResourceBundleMessageSource {

	private static Log _log = LogFactory.getLog(PatternResourceBundleMessageSource.class);
	
	private final ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
	
	/* (non-Javadoc)
	 * @see org.springframework.context.support.ReloadableResourceBundleMessageSource#setBasenames(java.lang.String[])
	 */
	@Override
	public void setBasenames(String... basenames) {
		List<String> names = new ArrayList<String>();
		if (basenames != null) {
			for (int i = 0; i < basenames.length; i++) {
				Resource[] resources;
				try {
					resources = patternResolver.getResources(basenames[i]);
					String prefix = basenames[i].substring(0,basenames[i].indexOf('*'));
					if(basenames[i].startsWith("classpath*")) {
						prefix = basenames[i].substring(0, basenames[i].indexOf('*')) + 
								 basenames[i].substring(basenames[i].indexOf("*") + 1, basenames[i].lastIndexOf('*')); 
					}
					for (int j=0; j< resources.length; j++) {
						String filename = resources[j].getFilename();
						// assumes messages are stored as properties file
						names.add(prefix + filename.replace(".properties",""));
					}
				} catch (IOException e) {
					_log.error("Failed to load resource for ["+basenames[i]+"].",e);
				}
			}
		}
		super.setBasenames(names.toArray(new String[names.size()]));
	}
	
}
