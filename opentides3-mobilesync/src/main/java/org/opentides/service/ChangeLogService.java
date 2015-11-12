package org.opentides.service;

import java.util.List;

import org.opentides.bean.ChangeLog;
import org.opentides.bean.SqlStatement;

/**
 * 
 * @author allantan
 *
 */
public interface ChangeLogService extends BaseCrudService<ChangeLog> {

	/**
	 * Returns the target version for the branch and clientCode
	 * @param branchId
	 * @param clientCode
	 * @return
	 */
	public Long findTargetVersion(Long branchId, String clientCode);

	/**
	 * Returns the list of sql update after the given version for the device.
	 * 
	 * @param version
	 * @param branchId
	 * @param clientCode
	 * @return
	 */
	public List<SqlStatement> findUpdates(Long version, Long branchId, String clientCode);

		
	/**
	 * Returns the list of changes after the given version.
	 * 
	 * @param version
	 * @param branchId
	 * @return
	 */
	@Deprecated
	public List<ChangeLog> findAfterVersion(Long version, Long branchId);
		
	/**
	 * Returns the latest change log filtered by id
	 * 
	 * @param branchId
	 * @return
	 */
	@Deprecated	
	public ChangeLog findLatestChange(Long branchId);
	
	
	/**
	 * Find latest changelog id
	 * @return
	 */
	@Deprecated	
	public Long findTargetVersion();
	
	/**
	 * Find latest changelog id by id
	 * @param branchId
	 * @return
	 */
	@Deprecated	
	public Long findTargetVersion(Long branchId);
		
}
