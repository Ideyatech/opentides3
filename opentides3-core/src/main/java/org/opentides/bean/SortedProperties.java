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
package org.opentides.bean;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

/**
 * 
 * Sorts key properties. Used for saving property files.
 * 
 * @author allantan
 * 
 */
public class SortedProperties extends Properties {
	private static final long serialVersionUID = -5457192384055197897L;

	
	public SortedProperties() {
		super();
	}


	public SortedProperties(Properties defaults) {
		super();
		super.putAll(defaults);
	}

	/**
	 * Overrides, called by the store method.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public synchronized Enumeration keys() {
		Enumeration keysEnum = super.keys();

		Vector keyList = new Vector();
		while (keysEnum.hasMoreElements()) {
			keyList.add(keysEnum.nextElement());
		}

		Collections.sort(keyList);
		return keyList.elements();
	}
}