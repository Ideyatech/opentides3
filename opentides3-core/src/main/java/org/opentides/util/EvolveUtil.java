package org.opentides.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.opentides.bean.BaseEntity;

/**
 * Contains helper methods for evolving a database
 * @author gino
 *
 */
public class EvolveUtil {
	
	private static final Logger _log = Logger.getLogger(EvolveUtil.class);
	private static Map<List<String>, SQLQuery> queryCache = new HashMap<List<String>, SQLQuery>();
	private static boolean enableChangeLog = false;
	
	/**
	 * Hide the constructor.
	 */
	private EvolveUtil() {		
	}
	
	public static void importCSV(String filename, String tableName, Session session, String tenant) 
			throws Exception {
		importCSV(filename, tableName, session, tenant, false);
	}
	
	public static void importCSVAsObject(String filename, String tableName, Session session, String tenant)
			throws Exception {
		importCSV(filename, tableName, session, tenant, true);
	}
	
	public static void importCSV(String filename, String tableName, Session session, String tenant, boolean useHibernate)
			throws Exception {
		int line = 1;
			BufferedReader reader = null;
		try {
			reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(filename)));
			// read the column header
			String csvLine = reader.readLine();
			if (csvLine==null) {
				_log.warn("Import file ["+filename+"] has no contents.");
				return;
			}
			List<String> headers = StringUtil.parseCsvLine(csvLine);
			
			while ((csvLine = reader.readLine()) != null) {
				List<String> values = StringUtil.parseCsvLine(csvLine);
				List<String> tmpHeaders = new ArrayList<String>(headers);
				if (headers.size() != values.size() )
					_log.error("Column number mismatch. "
							+ "Failed to import line #:" + line
							+ " with data as follows: \n[" + csvLine + "].");
				// get all columns with null values
				List<Integer> nullColumns = new ArrayList<Integer>();
				for (int i=values.size()-1; i>=0; i--) {
					String value = values.get(i);					
					if (StringUtil.isEmpty(value)) {
						nullColumns.add(i);
					}
				}
				// remove headers and values with null values
				for (int index:nullColumns) {
					tmpHeaders.remove(index);
					values.remove(index);
				}
				// execute this query
				if(useHibernate) {
					EvolveUtil.executeHqlQuery(tableName, tmpHeaders, values, session, tenant);
				} else {
					EvolveUtil.executeQuery(tableName, tmpHeaders, values, session, tenant);
				}
				line++;
			}
		} catch (Exception e) {
			_log.error("Failed to import csv file [" + filename + "] at line #"+line, e);
			throw e;
		} finally {
			if (reader != null) {
				try { reader.close(); } catch (Exception e) {};
			}
		}
		return;
	}
	
	/**
	 * Private helper that inserts record into the given tableName and values.
	 * This method keeps a static reference to all SQLQueries issued.
	 * 
	 * @param tableName
	 * @param headers
	 * @param values
	 * @param session
	 */
	private static void executeQuery(String tableName, List<String> headers, List<String> values, Session session, String tenant) {
		SQLQuery query;
		if (queryCache.containsKey(headers)) {
			query = queryCache.get(headers);
		} else {
			StringBuilder baseQuery = new StringBuilder(100);
			baseQuery.append("INSERT INTO ").append(tableName).append("(");
			StringBuilder valueQuery = new StringBuilder(30);
			int count = 0;
			for (String column : headers) {
				if (count++ > 0) {
					baseQuery.append(",");
					valueQuery.append(",");
				}
				baseQuery.append(column);
				valueQuery.append("?");
			}
			baseQuery.append(") VALUES (").append(valueQuery).append(")");
			query = session.createSQLQuery(baseQuery.toString());
			queryCache.put(headers, query);
		}
		int index=0;
		for (String value : values) {
				query.setParameter(index++, value);
		}
		query.executeUpdate();
		
		if (enableChangeLog) {
			insertChangeLog(query.getQueryString(), values.toString(), session, tenant);
		}		

	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void executeHqlQuery(String className, List<String> headers, List<String> values, Session session, String tenant) 
			throws Exception {
		Object entity = Class.forName(className).newInstance();
		if(!(entity instanceof BaseEntity)) {
			throw new Exception("Entity is not an instance of BaseEntity");
		}
		for(int i = 0; i < headers.size(); i++) {
			String property = headers.get(i);
			String value = values.get(i);
			String[] props = property.split("\\.");
			Class<?> type = null;
			Method method = null;
			if (props.length > 1) {
				type = CrudUtil.retrieveObjectType(entity, props[0]);
				method = entity.getClass().getMethod(NamingUtil.toSetterName(props[0]), CrudUtil.retrieveObjectType(entity, props[0]));
			} else {	
				type = CrudUtil.retrieveObjectType(entity, property);
				method = entity.getClass().getMethod(NamingUtil.toSetterName(property), CrudUtil.retrieveObjectType(entity, property));
			}
			_log.debug("Casting value : [" + value + "] to " + type.getName());
			if (Enum.class.isAssignableFrom(type)) {
				method.invoke(entity, Enum.valueOf((Class<Enum>)type, value));
			} else if (BaseEntity.class.isAssignableFrom(type)) {
				if (props.length > 1) {
					try {
						Query q = session.createQuery("from " + type.getSimpleName() + " where " + props[1] + " = ?").setParameter(0, value);
						List result = q.list();
						if (result.size() > 0)
							method.invoke(entity, result.get(0));
						else 
							throw new Exception("Cannot find entity " + type.getSimpleName() + " using " + value);
					} catch(Exception e) {
						_log.error("Unable to convert "+value+" to "+type.getName(), e);
						throw new Exception("Unable to convert "+value+" to "+type.getName(),e);
					}
				} else {
					Long id = StringUtil.convertToLong(value, -1l);
					if (id > 0)
						method.invoke(entity, session.load(type, id));
					else {
						_log.error("Unable to load id "+value+" of "+type.getName());
						throw new Exception("Unable to load id "+value+" of "+type.getName());						
					}
				}
			} else {
				Constructor c =
					type.getConstructor(new Class[]{String.class});
				if (c != null)
					method.invoke(entity, c.newInstance(value));
				else {
					_log.error("Unable to convert "+value+" to "+type.getName());
					throw new Exception("Unable to convert "+value+" to "+type.getName());
				}
			}
		}
		BaseEntity baseEntity = (BaseEntity) entity;
		baseEntity.setDbName(tenant);
		session.persist(baseEntity);	
	}
		
	private static void insertChangeLog(String sqlQuery, String values, Session session, String tenant) {
		if (enableChangeLog) {
			String changeLogSql = "INSERT INTO change_log" + 
					" (`CREATEDATE`, "
					+ " `VERSION`, " 
					+ " `ACTION`, " 
					+ " `ENTITY_CLASS`, "
					+ " `ENTITY_ID`, " 
					+ " `SQL_COMMAND`,  "
					+ " `PARAMS`) " 
					+ " VALUES  " 
						+ " (?,?,?,?,?,?,?);";
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String dateStr = sdf.format(new Date());		
			if (!StringUtil.isEmpty(tenant))
				session.createSQLQuery("USE " + tenant).executeUpdate();
			Query changeLogQuery = session.createSQLQuery(changeLogSql);
			changeLogQuery.setParameter(0, dateStr);
			changeLogQuery.setParameter(1, 0); //
			changeLogQuery.setParameter(2, 1); //INSERT
			changeLogQuery.setParameter(3, EvolveUtil.class);
			changeLogQuery.setParameter(4, 0l); // Unknown
			changeLogQuery.setParameter(5, sqlQuery);
			changeLogQuery.setParameter(6, values);					
			changeLogQuery.executeUpdate();				
		}		
	}

	/**
	 * @return the enableChangeLog
	 */
	public static boolean isEnableChangeLog() {
		return enableChangeLog;
	}

	/**
	 * @param enableChangeLog the enableChangeLog to set
	 */
	public static void setEnableChangeLog(boolean enableChangeLog) {
		EvolveUtil.enableChangeLog = enableChangeLog;
	}
	
}
