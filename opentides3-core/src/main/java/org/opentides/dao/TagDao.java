package org.opentides.dao;

import org.opentides.bean.Tag;

/**
 * 
 * @author AJ
 *
 */
public interface TagDao extends BaseEntityDao<Tag, Long> {

	public Tag loadByText(String text);

}