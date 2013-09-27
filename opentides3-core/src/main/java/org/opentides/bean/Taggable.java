package org.opentides.bean;

import java.util.List;

/**
 * 
 * @author AJ
 *
 */
public interface Taggable {
	
	public List<Tag> getTags();
	public void setTags(List<Tag> tags);	
}