package org.opentides.bean.impl;

import java.util.List;

import org.opentides.bean.Tag;

/**
 * 
 * @author AJ
 *
 */
public interface Taggable {
	
	public List<Tag> getTags();
	public void setTags(List<Tag> tags);
	
	public String getCsTags();
	public void setCsTags(String csTags);

}