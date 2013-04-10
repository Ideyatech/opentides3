package org.opentides.bean.impl;

import java.util.List;

import org.opentides.bean.FileInfo;
import org.springframework.web.multipart.MultipartFile;


/**
 * 
 * @author AJ
 *
 */
public interface Uploadable {

	public abstract List<FileInfo> getFiles();
	public abstract void setFiles(List<FileInfo> Files);
	
	// Required upon form submit, should a transient variable. 
	public MultipartFile getFile();
	public void setFile(MultipartFile File);
	
	public void addFile(FileInfo FileInfo);

}