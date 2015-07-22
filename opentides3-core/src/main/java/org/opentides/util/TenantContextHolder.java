/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 *******************************************************************************/
package org.opentides.util;

import org.apache.log4j.Logger;

/**
 * @author Jeric
 *
 */
public class TenantContextHolder {
	
	private static final Logger _log = Logger.getLogger(TenantContextHolder.class);

	/**
	 * This is used to store the tenant name of the account during log in
	 */
	private static ThreadLocal<String> tenantName = new ThreadLocal<String>();

	/**
	 * This is used to store the schema name of the tenant. This will be
	 * populated by the user authentication service
	 */
	private static ThreadLocal<String> schemaName = new ThreadLocal<String>();
	
	private TenantContextHolder() {
	};

	/**
	 * @return the tenantName
	 */
	public static String getTenantName() {
		return tenantName.get();
	}

	public static void setTenantName(final String tenant) {
		tenantName.set(tenant);
	}

	/**
	 * @param schemaName
	 *            the schemaName to set
	 */
	public static void setSchemaName(final String schema) {
		schemaName.set(schema);
	}

	/**
	 * @return the schemaName
	 */
	public static String getSchemaName() {
		return schemaName.get();
	}

	/**
	 * Clears the context. This should be called after a user has logged out.
	 */
	public static void clearContext() {
		_log.debug("Clearing tenant context.");
		tenantName.remove();
		schemaName.remove();
	}
}
