package org.opentides.service;

import java.util.Map;

/**
 * 
 * @author AJ
 * 
 */
public interface MailingService {

	public void sendEmail(String[] toEmail, String subject, String template,
			Map<String, Object> templateVariables);
}
