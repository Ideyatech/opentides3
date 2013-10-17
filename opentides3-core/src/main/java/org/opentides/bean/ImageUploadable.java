package org.opentides.bean;

import java.util.List;

import org.opentides.service.BaseCrudService;
import org.opentides.web.controller.ImageController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Interface for entities that can be uploaded with images. 
 * 
 * <p>The controller {@link ImageController}
 * can already be use to handle basic image processing like image upload and cropping. But note
 * that the Photoable entity needs to extend {@link BaseEntity} and create its own implementation
 * of {@link BaseCrudService} to be able to use it.</p>
 * 
 * @author AJ
 * @author gino
 */
public interface ImageUploadable {

	/**
	 * Get all {@link ImageInfo} objects attached to the entity
	 * 
	 * @return a list of ImageInfo objects
	 */
	List<ImageInfo> getPhotos();

	/**
	 * Gets the primary photo. 
	 * You may want to consider using isPrimary field of ImageInfo.
	 * 
	 * @return
	 */
	ImageInfo getPrimaryPhoto();

	/**
	 * Required upon form submit, should be a transient variable.
	 * 
	 * @return
	 */
	MultipartFile getPhoto();

	/**
	 * Add {@link ImageInfo} object to the list of photos.
	 * 
	 * @param photo
	 */
	void addPhoto(ImageInfo photo);

}