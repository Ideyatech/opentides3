package org.opentides.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.opentides.web.json.Views;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * Entity representing a Tag.
 * 
 * @author AJ
 * @author gino
 *
 */
@Entity
@Table(name = "TAG")
public class Tag extends BaseEntity {
	
	private static final long serialVersionUID = 7593186662964727295L;
	
	public Tag() {
		
	}
	
	public Tag(String tagText) {
		this.tagText = tagText;
	}

	/**
	 * The actual tag
	 */
	@JsonView(Views.FormView.class)
	@Column(name = "TAG_TEXT", nullable = false)
	private String tagText;
	
	@SuppressWarnings("rawtypes")
	@Column(name = "TAGGABLE_CLASS", nullable = false)
	private Class taggableClass;
	
	@Column(name = "TAGGABLE_ID")
	private Long taggableId;
	
	public String getTagText() {
		return tagText;
	}
	
	public void setTagText(String text) {
		this.tagText = text;
	}
	
	/**
	 * @return the taggableClass
	 */
	@SuppressWarnings("rawtypes")
	public Class getTaggableClass() {
		return taggableClass;
	}

	/**
	 * @param taggableClass the taggableClass to set
	 */
	@SuppressWarnings("rawtypes")
	public void setTaggableClass(Class taggableClass) {
		this.taggableClass = taggableClass;
	}

	/**
	 * @return the taggableId
	 */
	public Long getTaggableId() {
		return taggableId;
	}

	/**
	 * @param taggableId the taggableId to set
	 */
	public void setTaggableId(Long taggableId) {
		this.taggableId = taggableId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((tagText == null) ? 0 : tagText.hashCode());
		result = prime * result
				+ ((taggableClass == null) ? 0 : taggableClass.hashCode());
		result = prime * result
				+ ((taggableId == null) ? 0 : taggableId.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		Tag other = (Tag) obj;
		if (tagText == null) {
			if (other.tagText != null)
				return false;
		} else if (!tagText.equals(other.tagText))
			return false;
		if (taggableClass == null) {
			if (other.taggableClass != null)
				return false;
		} else if (!taggableClass.getName().equals(other.taggableClass.getName()))
			return false;
		if (taggableId == null) {
			if (other.taggableId != null)
				return false;
		} else if (!taggableId.equals(other.taggableId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Tag [tagText=" + tagText + "]";
	}
	
}
