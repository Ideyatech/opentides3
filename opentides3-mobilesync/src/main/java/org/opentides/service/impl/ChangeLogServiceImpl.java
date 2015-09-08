/**
 * 
 */
package org.opentides.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opentides.bean.ChangeLog;
import org.opentides.bean.SqlStatement;
import org.opentides.dao.ChangeLogDao;
import org.opentides.service.ChangeLogService;
import org.springframework.stereotype.Service;

/**
 * @author allantan
 *
 */
@Service("changeLogService")
public class ChangeLogServiceImpl extends BaseCrudServiceImpl<ChangeLog> implements
		ChangeLogService {

	@Override
	public List<ChangeLog> findAfterVersion(Long version, Long branchId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("version", version);
		map.put("branchId", branchId);
		return getDao().findByNamedQuery("jpql.mobilesync.findChangesAfterVersion", map, 0, 250);
	}
	

	@Override
	public List<SqlStatement> findUpdates(Long version, Long branchId,
			String clientCode) {
		return ((ChangeLogDao) getDao()).findUpdates(version, branchId, clientCode);
		
	}
	
	@Override
	public Long findTargetVersion(){
		return ((ChangeLogDao)getDao()).findTargetVersion();
		
	}
	
	@Override
	public Long findTargetVersion(Long branchId){
		
		return ((ChangeLogDao)getDao()).findTargetVersion(branchId);
		
	}

	@Override
	public ChangeLog findLatestChange(Long branchId) {
		return ((ChangeLogDao)getDao()).findLatestChangeByBranch(branchId);
	}
}
