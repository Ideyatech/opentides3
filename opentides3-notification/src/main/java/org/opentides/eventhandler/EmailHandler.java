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
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

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
	
	private List<String> imagesPath;
	
	public void sendEmail(	String[] to, String[] cc, String[] bcc, 
							String subject, String body) {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
			
			mimeMessageHelper.setTo(toInetAddress(to));			
			if(cc!=null) mimeMessageHelper.setCc(toInetAddress(cc));
			if(bcc!=null) mimeMessageHelper.setBcc(toInetAddress(bcc));			
			mimeMessageHelper.setFrom(new InternetAddress(this.fromEmail,this.fromName));			
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(body, true);
	
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
		InternetAddress [] internetAddress = new InternetAddress[strings.length];		
		for(int x = 0; x < strings.length; x++) {
			internetAddress[x] = new InternetAddress(strings[x]);
		}		
		return internetAddress;
	}
}
