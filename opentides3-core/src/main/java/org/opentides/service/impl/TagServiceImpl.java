package org.opentides.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opentides.bean.Tag;
import org.opentides.dao.TagDao;
import org.opentides.dao.UserDao;
import org.opentides.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author AJ
 *
 */
@Service(value="tagService")
public class TagServiceImpl extends BaseCrudServiceImpl<Tag>
                implements TagService {
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
			
			
			if(existingTag != null) {
				System.out.println("Tag is already existing!");
				tags.add(existingTag);
			} else {
				System.out.println("Creating new tag!");
				tags.add(new Tag(item));
			}
			
		}
		
		return tags;
	}
        
}
