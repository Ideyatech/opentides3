package org.opentides.bean;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;


/**
 * Interface for entities that can be uploaded with images.
 * 
 * @author AJ
 */
public interface Photoable {

	/**
	 * Get all {@link ImageInfo} objects attached to the entity
	 * 
	 * @return a list of ImageInfo objects
	 */
	List<ImageInfo> getPhotos();
	
	/**
	 * Get the primary photo of the object
	 * @return
	 */
	ImageInfo getPrimaryPhoto();
	
	// Required upon form submit, should a transient variable. 
	MultipartFile getPhoto();
	
	/**
	 * Add photo
	 * @param photo
	 */
	void addPhoto(ImageInfo photo);

}