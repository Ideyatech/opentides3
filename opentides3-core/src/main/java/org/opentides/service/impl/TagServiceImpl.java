package org.opentides.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.opentides.bean.Tag;
import org.opentides.dao.TagDao;
import org.opentides.service.TagService;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author AJ
 *
 */
@Service(value="tagService")
public class TagServiceImpl extends BaseCrudServiceImpl<Tag>
                implements TagService {
	
	private static Logger _log = Logger.getLogger(TagServiceImpl.class);
	
	@Autowired
	public void setTagDao(TagDao tagDao) {
		this.dao = tagDao;
	}
	
	@Override
	public List<Tag> createTags(String csTags[]) {
		List<String> items = Arrays.asList( csTags );
		List<Tag> tags = new ArrayList<Tag>();
		
		for (String item : items) {
			
			TagDao dao = (TagDao) getDao();
			Tag existingTag = dao.loadByText(item);
			
			if(!(StringUtil.isEmpty(item) || "".equals(item))) {
				if(existingTag != null) {
					_log.debug("Saving existing TAG: " + existingTag.getTagText());
					tags.add(existingTag);
				} else {
					Tag tag = new Tag();
					tag.setTagText(item);
					save(tag);
					tags.add(tag);
					_log.debug("Creating new TAG: " + tag.getTagText());
				}
			}
			
		}
		
		return tags;
	}
	
	@Override
	public void saveAllTags(Collection<Tag> tags) {
		getDao().saveAllEntityModel(tags);
	}
        
}
