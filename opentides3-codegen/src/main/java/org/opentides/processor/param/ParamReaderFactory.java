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


/**
 * @author jc
 * @author allantan
 *
 */
public class ParamReaderFactory {

	/**
	 * Creates an instance of ParamReader based on annotation class
	 * @param annotation
	 * @return
	 */
	public static ParamReader getReader(String annotation) {
		if ("GenerateCrudController".equals(annotation)) {
			return new ClassParamReader();
		} else if ("GenerateCrudPages".equals(annotation)) {
			return new ClassParamReader();
		} else if ("GenerateDao".equals(annotation)) {
			return new ClassParamReader();
		} else if ("GenerateService".equals(annotation)) {
			return new ClassParamReader();
		} else if("GenerateValidator".equals(annotation)) {
			return new ClassParamReader();
		} else {
			return new FieldParamReader();
		} 		
	}
}
