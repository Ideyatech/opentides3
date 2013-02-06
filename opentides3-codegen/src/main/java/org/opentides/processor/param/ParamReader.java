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

package org.opentides.processor.param;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import org.opentides.bean.Definition;

/**
 * @author allanctan
 *
 */
public interface ParamReader {

	/**
	 * Builds the definition needed for the annotation. 
	 * Implementations of this interface will be specific for 
	 * each annotation (e.g. CheckBoxParamReader).
	 * 
	 * @param te
	 * @param e
	 * @return
	 */
	public Definition getDefinition(TypeElement te, Element e);
	
}
