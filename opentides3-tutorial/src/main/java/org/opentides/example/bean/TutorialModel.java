package org.opentides.example.bean;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.opentides.annotation.Auditable;
import org.opentides.bean.BaseEntity;
import org.opentides.bean.Comment;
import org.opentides.bean.SystemCodes;
import org.opentides.bean.Tag;
import org.opentides.bean.impl.Commentable;

public class TutorialModel extends BaseEntity implements Commentable{

	private static final long serialVersionUID = 5908631314966954092L;
	
	private Object attribute;
	private Date expirationDate;
	private Object attribute2;
	private Boolean active;
	private Object gender;
	private List<Tag> tags;
	private SystemCodes element;
	private List<Comment> comments = new ArrayList<Comment>();
	
	public TutorialModel(){
		
	}

	public Object getAttribute() {
		return attribute;
	}

	public void setAttribute(Object attribute) {
		this.attribute = attribute;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Object getAttribute2() {
		return attribute2;
	}

	public void setAttribute2(Object attribute2) {
		this.attribute2 = attribute2;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Object getGender() {
		return gender;
	}

	public void setGender(Object gender) {
		this.gender = gender;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public SystemCodes getElement() {
		return element;
	}

	public void setElement(SystemCodes element) {
		this.element = element;
	}

	@Override
	public List<Comment> getComments() {
		return comments;
	}

	@Override
	public void setComments(List<Comment> comments) {
		this.comments = comments;
		
	}

	
}
