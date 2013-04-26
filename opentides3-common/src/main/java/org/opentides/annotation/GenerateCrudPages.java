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

package org.opentides.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generates the crud jsp pages based on open-tides framework.
 * Parameter PageType is available to indicate the variation of pages
 * to be generated described as follows:
 *   - SINGLE - Standalone CRUD Page
 *   - MAIN   - Main CRUD Page with Child Pages (e.g. Order is Main and OrderItem is Child)
 *   - CHILD  - Child CRUD Page
 *  
 * @author allanctan
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface GenerateCrudPages {
	
	enum PageType {
		SINGLE,  // for stand
		MAIN,
		CHILD
	}
	
	/**
	 * Refers to type of page that will be generated. 
	 * Can be single, parent or child.
	 */
	PageType pageType() default PageType.SINGLE;
}
