package org.opentides.service;

import java.util.Collection;
import java.util.List;

import org.opentides.bean.Tag;
import org.opentides.bean.Taggable;

/**
 * 
 * @author AJ
 *
 */
public interface TagService extends BaseCrudService<Tag> {

	public List<Tag> createTags(String[] csTags);
	
	/**
	 * Save all tags.
	 * @param tags
	 */
	public void saveAllTags(Collection<Tag> tags);
	
	/**
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Tag> findByTaggableClassAndId(Class clazz, Long id);
	
	@SuppressWarnings("rawtypes")
	public List<Tag> findByTaggableClassIdTagTexts(Class clazz, Long id, List<String> tags);
	
	/**
	 * Process the tags in the taggable object.
	 * 
	 * @param taggable
	 * @param id
	 * @param clazz
	 */
	@SuppressWarnings("rawtypes")
	public void preProcessTags(Taggable taggable, Long id, Class clazz);
	
}
