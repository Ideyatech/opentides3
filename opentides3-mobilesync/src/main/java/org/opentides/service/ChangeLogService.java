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
	 * @return
	 */
	public List<ChangeLog> findAfterVersion(Long version);
		
}
