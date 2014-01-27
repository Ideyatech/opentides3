package org.opentides.dao;

import java.util.List;

import org.opentides.bean.Tag;

/**
 * 
 * @author AJ
 *
 */
public interface TagDao extends BaseEntityDao<Tag, Long> {

	public Tag loadByText(String text);
	
	/**
	 * Find all Tags attached to the Taggable class with the given ID
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Tag> findByTaggableClassAndId(Class clazz, Long id);
	
	/**
	 * Find all Tags attached to the Taggable class with the given ID and matching the given tags
	 * 
	 * @param clazz
	 * @param id
	 * @param tags
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Tag> findByTaggableClassIdTagTexts(Class clazz, Long id, List<String> tags);

}