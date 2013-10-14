package org.opentides.service;

import java.util.List;

import org.opentides.bean.AuditLog;

/**
 * Interface for AuditLog
 * 
 * @author allantan
 *
 */
public interface AuditLogService extends BaseCrudService<AuditLog> {
	
	@SuppressWarnings("rawtypes")
	public List<AuditLog> findLogByReferenceAndClass(String reference, List<Class> types);

	@SuppressWarnings("rawtypes")
	public List<AuditLog> findLogLikeReferenceAndClass(String reference, List<Class> types);
	
	public void sortByDate(List<AuditLog> logs);

}
