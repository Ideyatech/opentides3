package org.opentides.bean;

import com.ocpsoft.pretty.time.PrettyTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.opentides.bean.user.BaseUser;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "COMMENT")
public class Comment extends BaseEntity implements Uploadable {
	private static final long serialVersionUID = -7263338041829245226L;

	@Column(name = "TEXT", length = 2000)
	private String text;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AUTHOR_ID", nullable = false)
	private BaseUser author;

	@Column(name = "EXCLUSIVE", columnDefinition = "bit(1) DEFAULT false")
	private Boolean exclusive;

	@OneToMany(cascade = { javax.persistence.CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinTable(name = "COMMENT_FILE", joinColumns = { @JoinColumn(name = "COMMENT_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "FILE_ID") })
	private List<FileInfo> files;
	private transient MultipartFile file;

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public BaseUser getAuthor() {
		return this.author;
	}

	public void setAuthor(BaseUser author) {
		this.author = author;
	}

	public String getPrettyCreateDate() {
		PrettyTime prettyTime = new PrettyTime();
		return prettyTime.format(getCreateDate());
	}

	public final Boolean getExclusive() {
		return this.exclusive;
	}

	public final void setExclusive(Boolean exclusive) {
		this.exclusive = exclusive;
	}

	public List<FileInfo> getFiles() {
		return this.files;
	}

	public MultipartFile getFile() {
		return this.file;
	}

	public void setFiles(List<FileInfo> files) {
		this.files = files;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public void addFile(FileInfo fileInfo) {
		synchronized (fileInfo) {
			if (this.files == null) {
				this.files = new ArrayList();
			}
			this.files.add(fileInfo);
		}
	}
}