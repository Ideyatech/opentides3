package org.opentides.dao.impl;

import org.opentides.bean.Comment;
import org.opentides.dao.CommentDao;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author AJ
 *
 */
@Repository(value="commentDao")
public class CommentDaoJpaImpl extends BaseEntityDaoJpaImpl<Comment, Long> implements
                CommentDao {

}
