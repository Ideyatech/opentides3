package org.opentides.bean.impl;

import java.util.List;

import org.opentides.bean.PhotoInfo;
import org.springframework.web.multipart.MultipartFile;


/**
 * 
 * @author AJ
 *
 */
public interface Photoable {

	public abstract List<PhotoInfo> getPhotos();
	public abstract void setPhotos(List<PhotoInfo> photos);
	
	// Required upon form submit, suggested to be a transient variable. 
	public MultipartFile getPhoto();
	public void setPhoto(MultipartFile photo);
	
	public void addPhoto(PhotoInfo photoInfo);

}