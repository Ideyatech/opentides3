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

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

/**
 * @author Jeric
 *
 */
public class MultitenancyUtil {

	private static final Logger _log = Logger.getLogger(MultitenancyUtil.class);

	/**
	 * @param schema
	 * @param connection
	 */
	public static void switchSchema(final String schema,
			final Connection connection) {
		try {
			_log.debug("Altering connection to schema [" + schema + "]");
			connection.createStatement().execute("USE " + schema);
		} catch (final SQLException e) {
			throw new HibernateException(
					"Could not alter JDBC connection to specified schema ["
							+ schema + "]", e);
		}
	}
}
