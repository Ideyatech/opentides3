package org.opentides.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.opentides.web.json.Views;

import com.fasterxml.jackson.annotation.JsonView;


@Entity
@Table(name = "TAG")
public class Tag extends BaseEntity {
	
	private static final long serialVersionUID = 7593186662964727295L;
	
	public Tag() {
		
	}
	
	public Tag(String tagText) {
		this.tagText = tagText;
	}

	@JsonView(Views.FormView.class)
	@Column(name = "TAG_TEXT")
	private String tagText;

	public String getTagText() {
		return tagText;
	}
	
	public void setTagText(String text) {
		this.tagText = text;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((tagText == null) ? 0 : tagText.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tag other = (Tag) obj;
		if (tagText == null) {
			if (other.tagText != null)
				return false;
		} else if (!tagText.equals(other.tagText))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Tag [tagText=" + tagText + "]";
	}
	
}
