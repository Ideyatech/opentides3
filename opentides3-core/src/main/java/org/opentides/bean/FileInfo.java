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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.opentides.web.json.Views;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "FILE_INFO")
public class FileInfo extends BaseEntity {

	private static final long serialVersionUID = -6814443831130229864L;

	@JsonView(Views.SearchView.class)
	@Column(name = "FILENAME", nullable = false)
	private String filename;

	@Column(name = "FULL_PATH", length = 2000)
	private String fullPath;

	@Column(name = "FILE_SIZE", nullable = false)
	private Long fileSize;

	@Column(name = "ORIGINAL_FILENAME")
	private String originalFileName;
	
	@Column(name = "CONTENT_TYPE")
	private String contentType;
	
	// immutable
	@Column(name = "FILE_VERSION")
	private Long fileVersion;
	
	// immutable
	@Column(name = "FILE_ID")
	private String fileId;
	
	@Column(name = "IS_ARCHIVED")
	private Boolean isArchived;

	/**
	 * Default constructor
	 */
	public FileInfo() {
		
	}

	/**
	 * File Version and File Id are Immutable attributes
	 * 
	 * @param fileVersion
	 * @param fileId
	 */
	public FileInfo(Long fileVersion, String fileId) {
		this.fileVersion = fileVersion;
		this.fileId = fileId;
	}

	/**
	 * Returns the filesize in KB
	 * 
	 * @return
	 */
	public Long getFileSizeInKB() {
		return Long.valueOf(this.fileSize.longValue() / 1024L);
	}

	/**
	 * Increments the file version
	 */
	public synchronized final void incrementVersion() {
		if (fileVersion == null) 
			fileVersion = 1l;
		else
			fileVersion++;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the fullPath
	 */
	public String getFullPath() {
		return fullPath;
	}

	/**
	 * @param fullPath the fullPath to set
	 */
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	/**
	 * @return the fileSize
	 */
	public Long getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * @return the originalFileName
	 */
	public String getOriginalFileName() {
		return originalFileName;
	}

	/**
	 * @param originalFileName the originalFileName to set
	 */
	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the fileVersion
	 */
	public Long getFileVersion() {
		return fileVersion;
	}
	
	/**
	 * @return the fileId
	 */
	public final String getFileId() {
		return fileId;
	}
	
	public Boolean getIsArchived() {
		return isArchived;
	}
	
	public void setIsArchived(Boolean isArchived) {
		this.isArchived = isArchived;
	}
	
	@PrePersist
    public void prePersist() {
        if(this.isArchived==null) {
            this.isArchived = false;
        }
	}

}