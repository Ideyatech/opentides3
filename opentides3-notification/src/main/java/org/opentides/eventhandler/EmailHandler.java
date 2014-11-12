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
package org.opentides.eventhandler;

import java.io.File;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 * @author allantan
 *
 */
@Component(value="emailHandler")
public class EmailHandler {
	
	private static Logger _log = Logger.getLogger(EmailHandler.class);
	
	@Autowired
	private JavaMailSenderImpl javaMailSender;
	
	@Value("${mail.from-email-address}")
	private String fromEmail;
	
	@Value("${mail.from-name}")
	private String fromName;
	
	@Value("#{notificationSettings['mail.default-template']}")
	private String mailVmTemplate;

	
	@Autowired
	private VelocityEngine velocityEngine;
	
	private List<String> imagesPath;
	
	public void sendEmail(String[] to, String subject, String body) {
		sendEmail(to, new String[] {}, new String[] {}, "", subject, body, null);
	}

	public void sendEmail(String[] to, String[] cc, String[] bcc, String replyTo,
							String subject, String body) {
		sendEmail(to, cc, bcc, replyTo, subject, body, null);
	}
	
	public void sendEmail(	String[] to, String[] cc, String[] bcc, String replyTo,
							String subject, String body, File[] attachments) {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
			
			mimeMessageHelper.setTo(toInetAddress(to));	
			InternetAddress[] ccAddresses = toInetAddress(cc);
			if (ccAddresses!=null) mimeMessageHelper.setCc(ccAddresses);
			InternetAddress[] bccAddresses = toInetAddress(bcc);			
			if (bccAddresses!=null) mimeMessageHelper.setBcc(bccAddresses);
			if (!StringUtil.isEmpty(replyTo)) mimeMessageHelper.setReplyTo(replyTo);
			Map<String, Object> templateVariables = new HashMap<String, Object>();
			
			templateVariables.put("message-title", subject);
			templateVariables.put("message-body",body);
			
			StringWriter writer = new StringWriter();
			VelocityEngineUtils.mergeTemplate(velocityEngine, mailVmTemplate, "UTF-8", templateVariables, writer);
			
			mimeMessageHelper.setFrom(new InternetAddress(this.fromEmail,this.fromName));			
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(writer.toString(), true);
			
			// check for attachment
			if (attachments!= null && attachments.length > 0) {
				for (File attachment:attachments) {
					mimeMessageHelper.addAttachment(attachment.getName(), attachment);					
				}				
			}
			
			/**
			 * The name of the identifier should be image
			 * the number after the image name is the counter 
			 * e.g. <img src="cid:image1" />
			 */
			if(imagesPath != null && imagesPath.size() > 0) {
				int x = 1;
				for(String path : imagesPath) {
					FileSystemResource res = new FileSystemResource(new File(path));
					String imageName = "image" + x;
					mimeMessageHelper.addInline(imageName, res);
					x++;
				}
			}
			javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			_log.error(e, e);
		} catch (UnsupportedEncodingException uee) {
			_log.error(uee, uee);
		}
	}
	
	public InternetAddress[] toInetAddress(String [] strings) throws AddressException {
		if (strings == null)
			return null;
		int count = 0;
		for(int x = 0; x < strings.length; x++) {
			if (StringUtil.isEmpty(strings[x]) || strings[x].trim().length()==0)
				strings[x] = null;
			else
				count++;				
		}
		if (count==0) return null;
		InternetAddress [] internetAddress = new InternetAddress[count];
		for(int x = 0; x < strings.length; x++) {
			if (strings[x] != null) 
				internetAddress[x] = new InternetAddress(strings[x]);
		}		
		return internetAddress;
	}
}