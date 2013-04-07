package org.opentides.service.impl;

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
        
}
