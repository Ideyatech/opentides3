package org.opentides.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "TAG")
public class Tag extends BaseEntity {

	private static final long serialVersionUID = -7643692652937089079L;
	
	@Column(name = "TEXT", length = 2000)
	private String text;

	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
}
