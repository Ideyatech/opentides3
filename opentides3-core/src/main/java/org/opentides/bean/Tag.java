package org.opentides.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "TAG")
public class Tag extends BaseEntity {
	
	private static final long serialVersionUID = 7593186662964727295L;

	@Column(name = "TAG_TEXT", unique=true)
	private String tagText;

	public String getTagText() {
		return tagText;
	}
	
	public void setTagText(String text) {
		this.tagText = text;
	}
	
}
