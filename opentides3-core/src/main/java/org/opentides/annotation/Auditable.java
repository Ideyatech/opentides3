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
 * Annotation for entity classes that will be audited 
 * based on open-tides framework.
 *
 * @author allanctan
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface Auditable {
		
	/**
	 * Label to be used as reference when building log messages for the entity.
	 * Label should be human readable.
	 * If empty, opentides will automatically convert class name to be more human readable.
	 * 
	 */
	String label() default "";

	/**
	 * Indicates if fields from parent class will be included
	 * in audit logging. This parameter is Optional.
	 * By default, parent fields are included.
	 * 
	 * @return
	 */
	boolean includeParentFields() default true;

	/**
	 * Refers to the fields that will be excluded from audit logs.
	 * By default, all non-transient fields are included in audit logging.
	 * This parameter is optional.
	 * <b>Example:</b><br />
	 * &emsp;&emsp;&emsp;<code>@Auditable (excludeFields = {"version", "employeeId", "excludedField"})</code>
	 */
	String[] excludeFields() default "";

}
