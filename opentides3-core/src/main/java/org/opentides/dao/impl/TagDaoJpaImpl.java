package org.opentides.dao.impl;

import org.opentides.bean.Tag;
import org.opentides.dao.TagDao;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author AJ
 *
 */
@Repository(value="tagDao")
public class TagDaoJpaImpl extends BaseEntityDaoJpaImpl<Tag, Long> implements
                TagDao {

}
