package org.opentides.service;

import java.util.List;

import org.opentides.bean.ChangeLog;

/**
 * 
 * @author allantan
 *
 */
public interface ChangeLogService extends BaseCrudService<ChangeLog> {

	/**
	 * Returns the list of changes after the given version.
	 * 
	 * @param version
	 * @param branchId
	 * @return
	 */
	public List<ChangeLog> findAfterVersion(Long version, Long branchId);
	
	/**
	 * Returns the latest change log
	 * 
	 * @param
	 * @return
	 */
	public ChangeLog findLatestChange();
	
	/**
	 * Returns the latest change log filtered by id
	 * 
	 * @param branchId
	 * @return
	 */
	public ChangeLog findLatestChange(Long branchId);
	
	/**
	 * Find latest changelog id
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
