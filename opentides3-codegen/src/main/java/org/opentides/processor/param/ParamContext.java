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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.opentides.bean.Definition;
import org.opentides.bean.FieldDefinition;
import org.opentides.exception.CodeGenerationException;


public class ParamContext {
	// holder of parameters configured for annotations
	private static Map<String, Definition> defnHolder = new HashMap<String, Definition>();
	
	/**
	 * Adds parameter to the given key (classname of annotated object).
	 * @param key - classname of the annotated object
	 * @param param
	 */
	public static void addDefinition(Definition defn) {
		if (defnHolder.containsKey(defn.toString())) {
			if (FieldDefinition.class.isAssignableFrom(defn.getClass())) {
				FieldDefinition fd = (FieldDefinition) defn;
				FieldDefinition d = (FieldDefinition) defnHolder.get(defn.toString());
				for (String key:fd.getAnnotations().keySet()) {
					d.addAnnotation(fd.getAnnotations().get(key));
				}
			}
		} else
			defnHolder.put(defn.toString(), defn);		
	}
	
	/**
	 * Returns the list of fields with annotation for the given className.
	 * 
	 * @param className
	 * @return
	 */
	public static Set<FieldDefinition> getFieldDefinitions(String className) {
		Set<FieldDefinition> fields = new HashSet<FieldDefinition>();
		for (String fd:defnHolder.keySet()) {
			if (fd.startsWith(className))
				fields.add((FieldDefinition) defnHolder.get(fd));
		}
		return fields;
	}
		
	public static Definition getDefinition(String key) {
		return defnHolder.get(key);
	}
	
	public static Set<String> getKeys() {
		return defnHolder.keySet();
	}
}
