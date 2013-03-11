package org.opentides.bean;

import java.util.List;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * 
 * @author AJ
 *
 */
public interface Photoable {

	public abstract List<PhotoInfo> getPhotos();
	public abstract void setPhotos(List<PhotoInfo> photos);
	
	// Required upon form submit, suggested to be a transient variable. 
	public CommonsMultipartFile getPhoto();
	public void setPhoto(CommonsMultipartFile photo);
	
	public void addPhoto(PhotoInfo photoInfo);

}