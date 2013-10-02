package org.opentides.bean;

import java.util.List;

/**
 * 
 * @author AJ
 *
 */
public interface Commentable {
	
	public List<Comment> getComments();
	public void setComments(List<Comment> comments);

}
