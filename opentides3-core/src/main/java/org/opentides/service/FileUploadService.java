package org.opentides.service;

import java.io.File;

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
	
	public FileInfo upload(MultipartFile multipartFile, String destination, boolean retainFilename);
	
	public FileInfo upload(File file);
	
	public FileInfo upload(File file, String destination);
	
	public FileInfo upload(File file, String destination, boolean retainFilename);
	
}