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

package org.opentides.util;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.opentides.bean.BaseEntity;

/**
 * 
 * @author allantan
 *
 */
public class SyncUtil {

	/**
	 * Overloaded method to build insert statement, including parent fields.
	 * @param obj
	 * @return
	 */
	public static String[] buildInsertStatement(BaseEntity obj) {
		return buildInsertStatement(obj, null, null);
	}

	/**
	 * Builds the insert statement for sqlLite.
	 * @return
	 */
	public static String[] buildInsertStatement(BaseEntity obj, String tableName, Class<?> clazz) {
		StringBuilder sql = new StringBuilder("insert into ");
		StringBuilder columns = new StringBuilder("(");
		StringBuilder values = new StringBuilder("(");
		String[] insertQuery = new String[2];
		StringBuilder param = new StringBuilder("[");
		
		List<String> fields = null;
		Map<String, String> columnFields = null;
		
		if (clazz == null) {
			clazz = obj.getClass();
			fields = CacheUtil.getPersistentFields(obj);
			if (!fields.contains("id"))
				fields.add("id");
			columnFields = CacheUtil.getColumnNames(obj);
		} else {
			fields = CacheUtil.getPersistentFields(obj, clazz);
			if (!fields.contains("id"))
				fields.add("id");
			columnFields = CacheUtil.getColumnNames(obj, clazz);			
		}
		int count = 0;
		
		for (String field : fields) {
			Object ret = CrudUtil.retrieveNullableObjectValue(obj, field);
			String column = columnFields.get(field);
			if (column != null) {
				String[] cols = column.split(",");
				if (cols != null && cols.length > 0) {
					for (int i = 0; i < cols.length; i++) {
						String n = normalizeValue(ret);
						if (n.trim().length() > 0) {
							if (count++ > 0) {
								columns.append(",");
								values.append(",");
								param.append(",");
							}
							columns.append(cols[i]);
							values.append("?");
							param.append(n);

						}
					}
				}				
			}
		}

		columns.append(")");
		values.append(")");
		
		if (StringUtil.isEmpty(tableName))
			tableName = NamingUtil.toSQLName(obj.getClass().getSimpleName());

		sql.append(tableName).append(" ").append(columns).append(" values ")
				.append(values);
		insertQuery[0] = sql.toString();

		param.append("]");
		insertQuery[1] = param.toString();

		return insertQuery;
	}
	
	/**
	 * Overloaded method to build update statement, including parent fields.
	 * @param obj
	 * @return
	 */	
	public static String buildUpdateStatement(BaseEntity obj, List<String> fields) {
		return SyncUtil.buildUpdateStatement(obj, fields, null, true);
	}
	
	/**
	 * Builds the insert statement for sqlLite.
	 * @return
	 */
	public static String buildUpdateStatement(BaseEntity obj, List<String> fields, String tableName, boolean includeParent) {
		if (tableName == null)
			tableName = NamingUtil.toSQLName(obj.getClass().getSimpleName());
		
		Map<String, String> columns = CacheUtil.getColumnNames(obj);
		
		StringBuilder sql = new StringBuilder("update ");
		sql.append(tableName)
		   .append(" set ");		
		
		int count = 0;
		for (String field:fields) {
			String columnName = columns.get(field);
			Object ret = CrudUtil.retrieveNullableObjectValue(obj, field);
			String n = normalizeValue(ret);
			if (n.trim().length() > 0) {
				if (count++ > 0) {
					sql.append(",");
				}
				sql.append(columnName)
				   .append("=")
				   .append(n);
			}
		}
		sql.append(" where id=")
		   .append(obj.getId());
		return sql.toString();
	}
	
	/**
	 * Builds the delete statement for sqlLite.
	 * @return
	 */
	public static String buildDeleteStatement(BaseEntity obj) {
		String tableName = NamingUtil.toSQLName(obj.getClass().getSimpleName());
		StringBuilder sql = new StringBuilder("delete from ");
		sql.append(tableName).append(" where id=").append(obj.getId());
		return sql.toString();
	}
	
	/**
	 * Override this method for additional transformation of values.
	 * 
	 * @param obj
	 * @return
	 */
	public static String doNormalizeValue(Object obj) {
		return "";
	}
	
    /**
     * Private helper that converts object into other form for 
     * audit log comparison and display purposes.
     * @param obj
     * @return
     */
	private static String normalizeValue(Object obj) {
		if (obj == null)
			return "";

		if (obj instanceof Collection) {
			// ignore, we are not returning collections.
			return "";
		}

		String value = SyncUtil.doNormalizeValue(obj);
		if (!StringUtil.isEmpty(value)) {
			return value;
		}
		
//		if (obj instanceof Money) {
//			Money money = (Money) obj;
//
//			if (column.toLowerCase().contains("currency")) {
//				return "'" + money.getCurrencyUnit().getCurrencyCode() + "'";
//			} else {
//				return "" + money.getAmount();
//			}
//		}

		// no quotes needed
		if (obj instanceof Long || obj instanceof Integer
				|| obj instanceof BigDecimal || obj instanceof Double)
			return "" + obj;

		// convert date into string
		if (obj instanceof Date) {
			if (DateUtil.hasTime((Date) obj)) {
				return "'"
						+ DateUtil.dateToString((Date) obj,
								"yyyy-MM-dd HH:mm:ss") + "'";
			} else
				return "'" + DateUtil.dateToString((Date) obj, "yyyy-MM-dd")
						+ "'";
		}

//		if (obj instanceof DateTime) {
//			DateTime dateTime = (DateTime) obj;
//			return "'" + dateTime.toString("yyyy-MM-dd HH:mm:ss") + "'";
//		}

		if (obj instanceof Boolean) {
			return (((Boolean) obj).booleanValue()) ? "1" : "0";
		}

		if (obj instanceof BaseEntity
				|| BaseEntity.class.isAssignableFrom(obj.getClass())) {
			return "" + ((BaseEntity) obj).getId();
		}

		if (obj.toString().length() > 0) {
			String queryValue = "" + obj;
			queryValue = queryValue.replaceAll("'", "\\\\'");
			return "'" + queryValue + "'";
		} else {
			return "";
		}
	}
}
