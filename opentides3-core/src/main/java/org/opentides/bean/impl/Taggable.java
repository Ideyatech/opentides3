package org.opentides.bean.impl;

import java.util.Set;

import org.opentides.bean.Tag;

/**
 * 
 * @author AJ
 *
 */
public interface Taggable {
	
	public Set<Tag> getTags();
	public void setTags(Set<Tag> tags);

}