package org.opentides.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opentides.bean.Tag;
import org.opentides.service.TagService;
import org.springframework.stereotype.Service;


/**
 * @author AJ
 *
 */
@Service(value="tagService")
public class TagServiceImpl extends BaseCrudServiceImpl<Tag>
                implements TagService {

	@Override
	public List<Tag> createTags(String csTags[]) {
		List<String> items = Arrays.asList( csTags );
		List<Tag> tags = new ArrayList<Tag>();
		
		for (String item : items) {
			tags.add(new Tag(item));
		}
		return tags;
	}
        
}
