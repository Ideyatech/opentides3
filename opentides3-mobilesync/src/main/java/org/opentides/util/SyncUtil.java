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

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.opentides.bean.BaseEntity;
import org.opentides.editor.Normalizer;

/**
 * 
 * @author allantan
 *
 */
public class SyncUtil {

	private static Logger _log = Logger.getLogger(SyncUtil.class);
	
	/**
	 * List of normalizer to be executed when converting values to SQL.
	 * Use setNormalizer method to add to the list.
	 */
	private static List<Normalizer> normalizer;
	
	/**
	 * Overloaded method to build insert statement, including parent fields.
	 * @param obj
	 * @return
	 */
	public static String[] buildInsertStatement(BaseEntity obj) {
		return buildInsertStatement(obj, null);
	}

	/**
	 * Builds the insert statement for sqlLite.
	 * @return
	 */
	public static String[] buildInsertStatement(BaseEntity obj, Class<?> clazz) {
		if (clazz == null) clazz = obj.getClass();
		StringBuilder sql = new StringBuilder("insert into ");
		StringBuilder columns = new StringBuilder("(");
		StringBuilder values = new StringBuilder("(");
		String[] insertQuery = new String[2];
		StringBuilder param = new StringBuilder("[");
		
		List<String> fields = null;
		Map<String, String> columnFields = null;
		
		fields = CacheUtil.getSynchronizableFields(obj, clazz);
		columnFields = CacheUtil.getColumnNames(obj, clazz);			
		if (!fields.contains("id"))
			fields.add("id");
		int count = 0;
		
		for (String field : fields) {
			String[] pField = StringUtil.splitSafe(field, "\\.");
			Object ret = CrudUtil.retrieveNullableObjectValue(obj, field);
			String column = columnFields.get(pField[0]);

			if (column != null) {
				String[] cols = StringUtil.splitSafe(column, ",");
				if (cols != null && cols.length > 0) {
					for (int i = 0; i < cols.length; i++) {
						String n = normalizeValue(ret, cols[i]);
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
		
		String tableName = SyncUtil.getTableName(obj);
		
		sql.append(tableName).append(" ").append(columns).append(" values ")
				.append(values);
		insertQuery[0] = sql.toString();

		param.append("]");
		insertQuery[1] = param.toString();

		return insertQuery;
	}

	/**
	 * Builds the insert statement for sqlLite.
	 * @return
	 */
	public static String[] buildUpdateStatement(BaseEntity obj, List<String> fields) {
		String[] updateQuery = new String[2];
		
		String tableName = SyncUtil.getTableName(obj);

		Map<String, String> columns = CacheUtil.getColumnNames(obj);

		StringBuilder params = new StringBuilder("[");
		StringBuilder sql = new StringBuilder("update ");
		sql.append(tableName).append(" set ");

		int count = 0;
		for (String field : fields) {
			String[] pField = StringUtil.splitSafe(field, "\\.");
			String column = columns.get(pField[0]);
			Object ret = CrudUtil.retrieveNullableObjectValue(obj, field);
			if (column != null) {
				String[] cols = StringUtil.splitSafe(column, ",");
				if (cols != null && cols.length > 0) {
					for (int i = 0; i < cols.length; i++) {
						String n = normalizeValue(ret, cols[i]);
						if (n.trim().length() > 0) {
							if (count++ > 0) {
								sql.append(",");
								params.append(",");
							}
							sql.append(cols[i]).append("=?");
							params.append(n);
						}
					}
				}
			}

		}
		sql.append(" where id=?");
		if (count > 0) params.append(",");
		params.append(obj.getId());
		params.append("]");
		updateQuery[0] = sql.toString();
		updateQuery[1] = params.toString();
		return updateQuery;
	}
	
	/**
	 * Builds the delete statement for sqlLite.
	 * @return
	 */
	public static String buildDeleteStatement(BaseEntity obj) {
		String tableName = SyncUtil.getTableName(obj);
		StringBuilder sql = new StringBuilder("delete from ");
		sql.append(tableName).append(" where id=?");
		return sql.toString();
	}
	
	/**
	 * Returns the table name of the given entity by checking the
	 * Table annotation or by sql naming convention based on className. 
	 * @param obj
	 */
	public static String getTableName(BaseEntity obj) {
		Annotation annotation = obj.getClass().getAnnotation(Table.class);
		String tableName = NamingUtil.toSQLName(obj.getClass().getSimpleName());
		try {
			if (annotation != null) {
				tableName = (String) annotation.annotationType()
						.getMethod("name").invoke(annotation);
			}
		} catch (Exception e) {
		}
		return tableName;
	}
	
    /**
     * Private helper that converts object into other form for 
     * audit log comparison and display purposes.
     * @param obj
     * @return
     */
	private static String normalizeValue(Object obj, String column) {
		if (obj == null)
			return "";

		if (obj instanceof Collection) {
			// ignore, we are not returning collections.
			return "";
		}
		
		if (normalizer != null && normalizer.size() > 0) {
			for (Normalizer n:normalizer) {
				if (n.handles(obj)) {
					String value = n.normalize(obj, column);
					if (!StringUtil.isEmpty(value)) {
						return value;
					}				
				}
			}
		}
		
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

	/**
	 * @param normalizer the normalizer to set
	 */
	public void setSyncNormalizerList(List<Normalizer> normalizer) {
		SyncUtil.normalizer = normalizer;
	}
	
}