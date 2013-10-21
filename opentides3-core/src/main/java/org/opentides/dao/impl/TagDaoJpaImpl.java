package org.opentides.dao.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opentides.bean.Tag;
import org.opentides.dao.TagDao;
import org.opentides.util.StringUtil;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author AJ
 *
 */
@Repository(value="tagDao")
public class TagDaoJpaImpl extends BaseEntityDaoJpaImpl<Tag, Long> implements
                TagDao {

	@Override
	public Tag loadByText(String text) {
		if (StringUtil.isEmpty(text))
			return null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("text", text);
		List<Tag> result = findByNamedQuery("jpql.tag.findByText",
				params);
		if (result == null || result.size() == 0) {
			return null;
		} else {
			return result.get(0);
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<Tag> findByTaggableClassAndId(Class clazz, Long id) {
		Map<String, Object> params = new HashMap<>();
		params.put("taggableClass", clazz);
		params.put("taggableId", id);
		
		return findByNamedQuery("jpql.tag.findByTaggableClassAndId", params);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<Tag> findByTaggableClassIdTagTexts(Class clazz, Long id,
			List<String> tags) {
		Map<String, Object> params = new HashMap<>();
		params.put("taggableClass", clazz);
		params.put("taggableId", id);
		params.put("tagTexts", tags);
		
		return findByNamedQuery("jpql.tag.findByTaggableClassIdTagTexts", params);
	}
}
