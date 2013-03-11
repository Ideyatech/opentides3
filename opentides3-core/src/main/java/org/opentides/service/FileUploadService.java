package org.opentides.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author AJ
 *
 */
public interface FileUploadService {
	
	public String upload(MultipartFile multipartFile);
	
}