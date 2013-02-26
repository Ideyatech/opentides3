/*
 * Copyright 2007-2013 the original author or authors.
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

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation for marking service classes as secured.
 * Crud methods in secure service classes are authorized by spring security
 * with the following conventions.
 * 
 * VIEW_<ENTITY>   - For loading the entity
 * ADD_<ENTITY>    - For adding/creating new entity
 * EDIT_<ENTITY>   - For editing/updating existing entity
 * DELETE_<ENTITY> - For deleting the entity
 *   
 * @author allanctan
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface CrudSecure {
	String value() default "";
}
