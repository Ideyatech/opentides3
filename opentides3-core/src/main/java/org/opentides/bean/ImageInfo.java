package org.opentides.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity for holding uploaded images.
 * 
 * @author AJ
 * @author allantan
 */
@Entity
@Table(name = "IMAGE_INFO")
public class ImageInfo extends BaseEntity {

	private static final long serialVersionUID = -2041575993856318770L;

	@Column(name = "FULL_PATH", length = 2000)
	private String fullPath;

	@Column(name = "ORIGINAL_FILENAME")
	private String originalFileName;
	
	@Column(name = "FILE_SIZE", nullable = false)
	private Long fileSize;

	@Column(name = "key")
	private String key;

	public ImageInfo() {
		super();
	}

	public ImageInfo(FileInfo fileInfo) {
		this.fullPath = fileInfo.getFullPath();
		this.originalFileName = fileInfo.getOriginalFileName();
		this.fileSize = fileInfo.getFileSize();
	}

	/**
	 * @return the fullPath
	 */
	public final String getFullPath() {
		return fullPath;
	}

	/**
	 * @param fullPath the fullPath to set
	 */
	public final void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	/**
	 * @return the originalFileName
	 */
	public final String getOriginalFileName() {
		return originalFileName;
	}

	/**
	 * @param originalFileName the originalFileName to set
	 */
	public final void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	/**
	 * @return the fileSize
	 */
	public final Long getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize the fileSize to set
	 */
	public final void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * @return the key
	 */
	public final String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public final void setKey(String key) {
		this.key = key;
	}	
}
