package org.opentides.service;

import java.util.List;

import org.opentides.bean.Tag;

/**
 * 
 * @author AJ
 *
 */
public interface TagService extends BaseCrudService<Tag> {

	public List<Tag> createTags(String[] csTags);
	
}
