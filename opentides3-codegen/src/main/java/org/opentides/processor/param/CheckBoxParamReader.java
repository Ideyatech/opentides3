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

import org.opentides.bean.AnnotationDefinition;
import org.opentides.bean.BeanDefinition;
import org.opentides.bean.Definition;
import org.opentides.bean.FieldDefinition;
import org.opentides.util.AnnotationUtil;

public class CheckBoxParamReader implements	ParamReader {

	/*
	public Map<String, Object> getParameters(Field field) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		String[] options = field.getAnnotation(CheckBox.class).options();
		String categoryName = field.getAnnotation(CheckBox.class).category();
		// put list type params (e.g. category, options or object)
		params.putAll(populateListTypeParams(field, categoryName, options));
				
		if (!BaseParamReader.isValidation()) {
			BaseParamReader.setValidation(field.getAnnotation(CheckBox.class).requiredField());
		}
		
		if (!StringUtil.isEmpty(field.getAnnotation(CheckBox.class).label())) {
			params.put("label", field.getAnnotation(CheckBox.class).label());
		}
		
		params.put("isListed", field.getAnnotation(CheckBox.class).listed());
		params.put("isSearchable", field.getAnnotation(CheckBox.class).searchable());
		params.put("isRequiredField", field.getAnnotation(CheckBox.class).requiredField());
		params.put("springParams", field.getAnnotation(CheckBox.class).springParams());
		params.put("fieldType", "checkBox");
		params.putAll(super.getStandardParams(field));
		
		return params;
	}
	*/

	@Override
	public Definition getDefinition(TypeElement te, Element e) {
		String fieldName = e.toString();
		String fieldType = e.asType().toString(); 
		BeanDefinition bean = new BeanDefinition(e.getEnclosingElement().toString());
		AnnotationDefinition annotationDefn = AnnotationUtil.getAnnotationDefinition(te, e);
		FieldDefinition fieldDefn = new FieldDefinition(bean, fieldName, fieldType, 
				""+annotationDefn.getParams().get("label"));
		fieldDefn.addAnnotation(annotationDefn);
		AnnotationUtil.buildListDefinition(fieldDefn, annotationDefn);
		return fieldDefn;
	}
}
