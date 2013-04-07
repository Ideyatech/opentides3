package org.opentides.service.impl;

import org.opentides.bean.Comment;
import org.opentides.service.CommentService;
import org.springframework.stereotype.Service;


/**
 * @author AJ
 *
 */
@Service(value="commentService")
public class CommentServiceImpl extends BaseCrudServiceImpl<Comment>
                implements CommentService {
        
}
