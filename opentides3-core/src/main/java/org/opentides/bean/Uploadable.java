package org.opentides.bean;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;


/**
 * 
 * @author AJ
 *
 */
public interface Uploadable {

	public abstract List<FileInfo> getFiles();
	
	// Required upon form submit, should a transient variable. 
	public MultipartFile getFile();

}