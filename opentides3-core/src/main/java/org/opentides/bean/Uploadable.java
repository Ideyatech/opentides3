package org.opentides.bean;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;


/**
 * 
 * @author AJ
 *
 */
public interface Uploadable {

	/**
	 * Get all uploaded files.
	 * @return
	 */
	public List<FileInfo> getFiles();
	
	/**
	 * Add a file 
	 * @param fileInfo
	 */
	public void addFile(FileInfo fileInfo);
	
	// Required upon form submit, should a transient variable. 
	public MultipartFile getFile();

}