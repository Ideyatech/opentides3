package org.opentides.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.opentides.web.json.Views;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "FILE_INFO")
public class FileInfo extends BaseEntity {

	private static final long serialVersionUID = -6814443831130229864L;

	@JsonView(Views.SearchView.class)
	@Column(name = "FILENAME", nullable = false)
	private String filename;

	@Column(name = "FULL_PATH", length = 2000)
	private String fullPath;

	@Column(name = "FILE_SIZE", nullable = false)
	private Long fileSize;

	@Column(name = "ORIGINAL_FILENAME")
	private String originalFileName;

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public final String getFileName() {
		return this.filename;
	}

	public final void setFileName(String fileName) {
		this.filename = fileName;
	}

	public final String getFullPath() {
		return this.fullPath;
	}

	public final void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public final Long getFileSize() {
		return this.fileSize;
	}

	public final void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Long getFileSizeInKB() {
		return Long.valueOf(this.fileSize.longValue() / 1024L);
	}

	public String getOriginalFileName() {
		return this.originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}
	
}