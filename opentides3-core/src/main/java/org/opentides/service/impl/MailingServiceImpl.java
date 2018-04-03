package org.opentides.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.opentides.service.MailingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import org.opentides.util.ValidatorUtil;

/**
 * 
 * @author AJ
 *
 */
@Service
public class MailingServiceImpl implements MailingService {
	
	private static final Logger _log = Logger.getLogger(MailingServiceImpl.class);
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	@Autowired(required = false)
	private TaskExecutor taskExecutor;
	
	@Value("${mail.server.username:#{applicationSettings['mail.server.username']}}")
	private String adminEmail;
	
	@Value("${mail.server.password:#{applicationSettings['mail.server.password']}}")
	private String adminPassword;
	
	@Value("#{applicationSettings['mail.server.domain']}")
	private String host;
	
	@Value("#{applicationSettings['mail.server.port']}")
	private String port;
	
	@Value("#{applicationSettings['mail.from-name']}")
	private String mailFrom;
	
	@Value("#{applicationSettings['mail.from-email-address']}")
	private String mailFromEmail;
	
	@Value("#{applicationSettings['application.name']}")
	private String applicationName;


	@Override
	public void sendEmail(String[] toEmail, String subject, String template,
			Map<String, Object> templateVariables) {
		sendEmail(mailFrom, toEmail, subject, template, templateVariables);
	}

	@Override
	public void sendEmail(String fromMail, String[] toEmail, String subject, String template,
			Map<String, Object> templateVariables) {

		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        
		Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(adminEmail, adminPassword);
            }
        });
		
		MimeMessage message = new MimeMessage(session);
		Multipart multipart = new MimeMultipart();
		MimeBodyPart htmlPart = new MimeBodyPart();

		try {
			if(ValidatorUtil.isEmail(fromMail)){
				message.setFrom(new InternetAddress(fromMail, adminEmail));
				//message.setReplyTo(new InternetAddress[]{new InternetAddress(fromMail)});
			} else{
				message.setFrom(new InternetAddress(mailFromEmail, fromMail));
				//message.setReplyTo(new InternetAddress[]{new InternetAddress(mailFromEmail)});
			}
			
			message.setSender(new InternetAddress(mailFromEmail, adminEmail));
			for (String addr : toEmail) {
				message.addRecipient(RecipientType.TO,
						new InternetAddress(addr));
			}
			message.setSubject(subject);
			
			@SuppressWarnings("deprecation")
			String body = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "/" + template, templateVariables);
			
			htmlPart.setContent(body, "text/html");

			multipart.addBodyPart(htmlPart);
			message.setContent(multipart);

			send(message);
			
		} catch (AddressException e) {
			_log.error("Failed to send email.", e);
		} catch (MessagingException e) {
			_log.error("Failed to send email.", e);
		} catch (UnsupportedEncodingException e) {
			_log.error("Failed to send email.", e);
		}
		
	}
	
	@PostConstruct
	public void postInit() {
		if(taskExecutor == null) {
			//set default taskExecutor
			taskExecutor = new ThreadPoolTaskExecutor();
			((ThreadPoolTaskExecutor)taskExecutor).setCorePoolSize(5);
			((ThreadPoolTaskExecutor)taskExecutor).setMaxPoolSize(10);
			((ThreadPoolTaskExecutor)taskExecutor).setQueueCapacity(25);
			((ThreadPoolTaskExecutor)taskExecutor).setWaitForTasksToCompleteOnShutdown(true);
		}
	}
	
	private void send(final Message message) {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Transport.send(message);
				} catch (MessagingException e) {
					_log.error("Failed to send email.", e);
				}
			}
		});
	}
	
}
