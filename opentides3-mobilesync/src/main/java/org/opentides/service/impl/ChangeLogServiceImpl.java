/**
 * 
 */
package org.opentides.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opentides.bean.ChangeLog;
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
	public List<ChangeLog> findAfterVersion(Long version) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("version", version);
		return getDao().findByNamedQuery("jpql.mobilesync.findChangesAfterVersion", map, 0, 250);
	}
	
	@Override
	public ChangeLog findLatestChange() {
		List<ChangeLog> allLogs = findAll();
		if(!allLogs.isEmpty()) {
			return allLogs.get(allLogs.size() - 1);
		}
		
		return null;
	}

}
