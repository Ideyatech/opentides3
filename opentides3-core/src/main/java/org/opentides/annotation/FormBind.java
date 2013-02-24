/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opentides.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.ModelAttribute;
/**
 * Annotation that performs same behavior as {@link ModelAttribute}
 * with additional functions designed as Opentides standard. 
 * Parameters are: 
 *     (1) name - this is the name of the model to be retrieved
 *     (2) mode - applicable for method annotation only to indicate
 *                if object returned is for creating new records
 *                or updating existing records.
 * 
 * @author allanctan
 */

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FormBind {
	
	public enum Load { NEW, UPDATE };

	String name() default "";
	Load mode() default Load.UPDATE;
}
