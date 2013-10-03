package org.opentides.bean;

import java.util.List;

/**
 * Interface for entities that can be added with tags.
 *  
 * @author AJ
 */
public interface Taggable {
	
	/**
	 * Get the list of tags attached to the entity.
	 * 
	 * @return list of tags
	 */
	public List<Tag> getTags();
	
	/**
	 * Set the tags of the entity
	 * 
	 * @param tags the List of tags to set
	 */
	public void setTags(List<Tag> tags);	
}