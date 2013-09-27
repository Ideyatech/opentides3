package org.opentides.bean;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;


/**
 * 
 * @author AJ
 *
 */
public interface Photoable {

	public abstract List<ImageInfo> getPhotos();
	
	// Required upon form submit, should a transient variable. 
	public MultipartFile getPhoto();

}