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

package org.opentides.persistence.interceptor;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.CallbackException;
import org.hibernate.collection.internal.PersistentBag;
import org.hibernate.type.Type;
import org.opentides.annotation.Synchronizable;
import org.opentides.bean.BaseEntity;
import org.opentides.bean.ChangeLog;
import org.opentides.bean.ChangedField;
import org.opentides.bean.ChangedRecord;
import org.opentides.bean.JoinTable;
import org.opentides.bean.SystemCodes;
import org.opentides.bean.user.BaseUser;
import org.opentides.bean.user.UserCredential;
import org.opentides.context.ApplicationContextProvider;
import org.opentides.job.NotifyDevices;
import org.opentides.util.CacheUtil;
import org.opentides.util.StringUtil;
import org.opentides.util.SyncUtil;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * This interceptor is used by mobile sync to process hibernate operations and
 * save the corresponding SQL statement in the change log. The change log is
 * sent to the mobile devices for sync of database operations.
 * 
 * @author allantan
 * 
 */
public class SynchronizableInterceptor extends AuditLogInterceptor {

	private static final long serialVersionUID = 5476081576394866928L;

	private static final Logger _log = Logger
			.getLogger(SynchronizableInterceptor.class);

	private static boolean disableAuditLog = false;

	protected Set<PersistentBag> insertCollection = Collections
			.synchronizedSet(new HashSet<PersistentBag>());
	
	protected Set<PersistentBag> updateCollection = Collections
			.synchronizedSet(new HashSet<PersistentBag>());
	
	protected Set<ChangedRecord> updateRecords = Collections
			.synchronizedSet(new HashSet<ChangedRecord>());

	@Override
	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) throws CallbackException {
		if (entity instanceof BaseEntity) {
			List<ChangedField> changedFields = new ArrayList<ChangedField>();
			List<String> fields = CacheUtil.getSynchronizableFields((BaseEntity)entity, entity.getClass());

			for (int i = 0; i < propertyNames.length; i++) {
				if (currentState[i] != previousState[i]) {
					String fieldName = propertyNames[i];
					boolean sync = false;
					// sync only fields declared
					for (String syncName:fields) {
						if (syncName.startsWith(fieldName)) {
							fieldName = syncName;
							sync = true;
						}
					}
					if (sync)
						changedFields.add(new ChangedField(currentState[i],
							previousState[i], fieldName, types[i]));
				}
			}
			if (changedFields.size() > 0) {
				synchronized(updateRecords) {
					updateRecords.add(new ChangedRecord(entity, id, changedFields));					
				}
			}
		}
		return false;
	}

	@Override
	public void onCollectionRecreate(Object collection, Serializable key)
			throws CallbackException {
		if (isSynchronizable(collection)) {
			synchronized (insertCollection) {
				insertCollection.add((PersistentBag) collection);
			}
		}
	}

	@Override
	public void onCollectionRemove(Object collection, Serializable key)
			throws CallbackException {
		if (isSynchronizable(collection)) {
			PersistentBag entries = (PersistentBag) collection;
			BaseEntity owner = (BaseEntity) entries.getOwner();
			Class<?> clazz = owner.getClass();
			Class<?> clazz2 = entries.get(0).getClass();

			JoinTable join = CacheUtil.getJoinTableFields(owner, clazz2);
			if (join != null) {
				StringBuffer statementBuffer = new StringBuffer();
				statementBuffer.append("delete from ")
						.append(join.getTableName()).append(" where ")
						.append(join.getColumn1()).append(" = ?")
						.append(" and ").append(join.getColumn2())
						.append(" = ?");
				String stmt = statementBuffer.toString();
				for (Object obj : entries) {
					BaseEntity entity = (BaseEntity) obj;
					this.saveLog(owner, ChangeLog.DELETE, "", stmt,
							"[" + owner.getId() + "," + entity.getId() + "]");
				}
			}
		}
	}

	@Override
	public void onCollectionUpdate(Object collection, Serializable key)
			throws CallbackException {
		if (isSynchronizable(collection)) {
			synchronized (updateCollection) {
				updateCollection.add((PersistentBag) collection);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opentides.persistence.interceptor.AuditLogInterceptor#postFlush(java
	 * .util.Iterator)
	 */
	@Override
	public void postFlush(Iterator iterator) throws CallbackException {
		synchronized (SynchronizableInterceptor.class) {
			try {
				for (BaseEntity entity : deletes) {
					if (isEntitySynchronizable(entity)) {
						String deleteStmt = SyncUtil
								.buildDeleteStatement(entity);
						this.saveLog(entity, ChangeLog.DELETE, "", deleteStmt,
								null);
					}
				}

				for (BaseEntity entity : inserts) {
					if (isEntitySynchronizable(entity)) {
						Method m = CacheUtil.getInsertMethod(entity.getClass());
						if (m == null) {
							String[] insertStmt = SyncUtil
									.buildInsertStatement(entity);
							this.saveLog(entity, ChangeLog.INSERT, "",
									insertStmt[0], insertStmt[1]);
						} else {
							List<String[]> stmts = (List<String[]>) m.invoke(entity);
							for (String[] stmt : stmts)
								this.saveLog(entity, ChangeLog.INSERT, "",
										stmt[0], stmt[1]);
						}
					}
				}
				
				for (PersistentBag collection : insertCollection) {
					if (collection.size() > 0) {
						BaseEntity owner = (BaseEntity) collection.getOwner();
						JoinTable join = CacheUtil.getJoinTableFields(owner,
								collection.get(0).getClass());
						if (join != null) {
							StringBuffer statementBuffer = new StringBuffer();
							statementBuffer.append("insert into ")
									.append(join.getTableName()).append(" (")
									.append(join.getColumn1()).append(",")
									.append(join.getColumn2())
									.append(") VALUES (?,?)");
							String stmt = statementBuffer.toString();
							for (Object obj : collection) {
								BaseEntity entity = (BaseEntity) obj;
								this.saveLog(
										owner,
										ChangeLog.INSERT,
										"",
										stmt,
										"[" + owner.getId() + ","
												+ entity.getId() + "]");
							}
						}
					}
				}
				
				for (ChangedRecord record:updateRecords) {
					if (isEntitySynchronizable(record.getEntity())) {
						BaseEntity entity = (BaseEntity) record.getEntity();
						Method m = CacheUtil.getUpdateMethod(entity.getClass());
						if (m == null) {
							List<String> fields = new ArrayList<String>();
							for (ChangedField field:record.getChangedFields()) {
								fields.add(field.getPropertyName());
							}
							String[] updateStmt = SyncUtil
									.buildUpdateStatement(entity, fields);
							this.saveLog(entity, ChangeLog.UPDATE,
									StringUtils.join(fields, ","), updateStmt[0],
									updateStmt[1]);
						} else {
							List<String[]> stmts = (List<String[]>) m.invoke(entity);
							for (String[] stmt : stmts)
								this.saveLog(entity, ChangeLog.UPDATE, "",
										stmt[0], stmt[1]);
						}						
					}
				}		
				
				for (PersistentBag collection : updateCollection) {
					if (collection.size() > 0) {
						BaseEntity owner = (BaseEntity) collection.getOwner();
						JoinTable join = CacheUtil.getJoinTableFields(owner,
								collection.get(0).getClass());
						if (join != null) {
							// delete old record
							this.saveLog(owner, ChangeLog.DELETE, "",
									"delete from " + join.getTableName()
											+ " where " + join.getColumn1()
											+ " = ?", "[" + owner.getId() + "]");
							// insert the new
							StringBuffer statementBuffer = new StringBuffer();
							statementBuffer.append("insert into ")
									.append(join.getTableName()).append(" (")
									.append(join.getColumn1()).append(",")
									.append(join.getColumn2())
									.append(") VALUES (?,?)");
							String stmt = statementBuffer.toString();
							for (Object obj : collection) {
								BaseEntity entity = (BaseEntity) obj;
								this.saveLog(
										owner,
										ChangeLog.INSERT,
										"",
										stmt,
										"[" + owner.getId() + ","
												+ entity.getId() + "]");
							}
						}
					}
				}
				
				// should we record auditLog from superclass?
				if (!disableAuditLog)
					super.postFlush(iterator);

			} catch (Throwable e) {
				_log.error(e, e);
			} finally {
				inserts.clear();
				insertCollection.clear();
				updates.clear();
				updateRecords.clear();
				updateCollection.clear();
				deletes.clear();
				oldies.clear();
			}
		}
	}

	/**
	 * Saves the change log into the database.
	 * 
	 * @param shortMessage
	 * @param message
	 * @param entity
	 */
	public void saveLog(BaseEntity entity, int action, String updateFields,
			String sqlCommand, String param) {

		JdbcTemplate jTemplate = connectDb(entity.getDbName());

		try {
			String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
					.format(new Date());

			String changeLogSql = "INSERT INTO CHANGE_LOG" + " (`CREATEDATE`, "
					+ " `VERSION`, " + " `ACTION`, " + " `ENTITY_CLASS`, "
					+ " `ENTITY_ID`, " + " `SQL_COMMAND`,  " + " `PARAMS`, "
					+ " `UPDATE_FIELDS`" + ") " + " VALUES (?,?,?,?,?,?,?,?); ";

			Object[] params = new Object[] { dateStr, 0, action,
					entity.getClass().getName(),
					(entity.getId() == null) ? 0 : entity.getId(), sqlCommand,
					param, updateFields };

			int[] types = new int[] { Types.VARCHAR, Types.BIGINT,
					Types.INTEGER, Types.VARCHAR, Types.BIGINT, Types.BLOB,
					Types.BLOB, Types.VARCHAR };

			jTemplate.update(changeLogSql, params, types);
			NotifyDevices.notifySync("/" + entity.getDbName() + "/*");
		} catch (Exception ex) {
			_log.error("Failed to save change log on ["
					+ entity.getClass().getSimpleName() + "]", ex);
		}
	}

	/**
	 * @param disableAuditLog
	 *            the disableAuditLog to set
	 */
	public static void setDisableAuditLog(boolean disableAuditLog) {
		SynchronizableInterceptor.disableAuditLog = disableAuditLog;
	}

	public JdbcTemplate connectDb(String schemaName) {
		// Get jdbc template
		JdbcTemplate jTemplate = (JdbcTemplate) ApplicationContextProvider
				.getApplicationContext().getBean("jdbcTemplate");

		if (!StringUtil.isEmpty(schemaName))
			jTemplate.execute("USE " + schemaName);

		return jTemplate;
	}

	/**
	 * Private helper that checks if the given collection should be
	 * synchronized.
	 * 
	 * @param collection
	 * @return
	 */
	protected boolean isSynchronizable(Object collection) {
		if (collection instanceof PersistentBag) {
			PersistentBag entries = (PersistentBag) collection;
			if (entries.size() > 0) {
				BaseEntity owner = (BaseEntity) entries.getOwner();
				Class<?> clazz = owner.getClass();
				Class<?> clazz2 = entries.get(0).getClass();
				if (clazz.isAnnotationPresent(Synchronizable.class)
						&& clazz2.isAnnotationPresent(Synchronizable.class)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Private helper that checks if the given fields should be synchronized.
	 * 
	 * @param entity
	 * @return
	 */
	protected boolean isEntitySynchronizable(Object entity) {
		return (entity.getClass().isAnnotationPresent(Synchronizable.class)
				|| (entity instanceof SystemCodes)
				|| (entity instanceof UserCredential) || (entity instanceof BaseUser));
	}
}
