package org.opentides.bean;

import java.util.List;

import org.opentides.web.json.Views;
import org.opentides.web.json.serializer.TagsSerializer;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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
	@JsonSerialize(using = TagsSerializer.class)
	@JsonView(Views.FormView.class)
	public List<Tag> getTags();
	
	/**
	 * Set the tags of the entity
	 * 
	 * @param tags the List of tags to set
	 */
	public void setTags(List<Tag> tags);	
}