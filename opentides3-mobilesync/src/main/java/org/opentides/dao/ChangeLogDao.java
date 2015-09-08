/* 
 * CashCountDao.java
 */
package org.opentides.dao;

import java.util.List;

import org.opentides.bean.ChangeLog;
import org.opentides.bean.SqlStatement;

/**
 * This is the dao interface for ChangeLog.
 * Scaffold generated by opentides3 on Oct 06, 2014 11:59:58. 
 * @author opentides
 */
 
public interface ChangeLogDao extends BaseEntityDao<ChangeLog, Long> {

	/**
	 * Returns the latest change log filtered by id
	 * @param branchId
	 * @return
	 */
	@Deprecated
	public ChangeLog findLatestChangeByBranch(Long branchId);
	
	/**
	 * Returns the updates for the given branch and clientCode
	 * @param version
	 * @param branchId
	 * @param clientCode
	 * @return
	 */
	public List<SqlStatement> findUpdates(Long version, Long branchId,
			String clientCode);
	
	/**
	 * Find latest change id
	 * @return
	 */
	public Long findTargetVersion();
	
	
	/**
	 * Find latest changelog id by id
	 * @param branchId
	 * @return
	 */
	public Long findTargetVersion(Long branchId);
	
}