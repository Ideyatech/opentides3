package org.opentides.service;

import org.opentides.bean.FileInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author AJ
 *
 */
public interface FileUploadService {
	
	public FileInfo upload(MultipartFile multipartFile);
	
	public FileInfo upload(MultipartFile multipartFile, String destination);
	
}