package org.opentides.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.opentides.bean.user.BaseUser;

@Entity
@Table(name = "COMMENT")
public class Comment extends BaseEntity {
	
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
	
}
