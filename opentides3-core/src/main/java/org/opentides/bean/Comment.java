package org.opentides.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.opentides.bean.impl.Uploadable;
import org.opentides.bean.user.BaseUser;
import org.springframework.web.multipart.MultipartFile;

import com.ocpsoft.pretty.time.PrettyTime;

@Entity
@Table(name = "COMMENT")
public class Comment extends BaseEntity implements Uploadable {
	
	private static final long serialVersionUID = -7263338041829245226L;
	
	@Column(name = "TEXT", length = 2000)
	private String text;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="AUTHOR_ID", nullable=false)
	private BaseUser author;
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public BaseUser getAuthor() {
		return author;
	}
	
	public void setAuthor(BaseUser author) {
		this.author = author;
	}
	
	public String getPrettyCreateDate(){
		PrettyTime prettyTime = new PrettyTime();
		return prettyTime.format(getCreateDate());
	}
	
	// Uploadable requirements
	
	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "COMMENT_FILE", 
			joinColumns = { @JoinColumn(name = "COMMENT_ID", referencedColumnName = "ID") }, 
			inverseJoinColumns = @JoinColumn(name = "FILE_ID")
	)
	private List<FileInfo> files;
	private transient MultipartFile file;
	
	@Override
	public List<FileInfo> getFiles() {
		return files;
	}
	
	@Override
	public void setFiles(List<FileInfo> files) {
		this.files = files;
	}
	
	@Override
	public MultipartFile getFile() {
		return file;
	}
	
	@Override
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	public void addFile(FileInfo fileInfo){
		synchronized (fileInfo) {
			if (files == null){
				files = new ArrayList<FileInfo>();
			}
			files.add(fileInfo);
		}
	}
	
	// End of Uploadable requirements
	
}
