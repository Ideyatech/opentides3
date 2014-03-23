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
package org.opentides.bean;

import org.springframework.web.multipart.MultipartFile;

/**
 * This bean is responsible for holding uploaded information
 * via ajax.
 * 
 * @author allantan
 */
public class AjaxUpload {
	
	private MultipartFile attachment;
	
	private String fileId;

	/**
	 * @return the attachment
	 */
	public final MultipartFile getAttachment() {
		return attachment;
	}

	/**
	 * @param attachment the attachment to set
	 */
	public final void setAttachment(MultipartFile attachment) {
		this.attachment = attachment;
	}

	/**
	 * @return the fileId
	 */
	public final String getFileId() {
		return fileId;
	}

	/**
	 * @param fileId the fileId to set
	 */
	public final void setFileId(String fileId) {
		this.fileId = fileId;
	}
}
