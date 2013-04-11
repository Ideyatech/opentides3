package org.opentides.dao.impl;

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
}
