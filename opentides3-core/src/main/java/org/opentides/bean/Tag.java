package org.opentides.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "OT_TAG")
public class Tag extends BaseEntity {
	
	private static final long serialVersionUID = 7593186662964727295L;

	public Tag() {
	}
	
	public Tag(String text) {
		this.text = text;
	}
	
	@Column(name = "TEXT", length = 2000, unique=true)
	private String text;

	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
}
