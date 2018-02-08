package org.opentides.web.event;

import org.springframework.context.ApplicationEvent;

public class UserGroupChangeEvent extends ApplicationEvent {

	private static final long serialVersionUID = 6898291525365280615L;
	
	private String oldUserGroupName;
	
	private String newUserGroupName;

	public UserGroupChangeEvent(Object source, String oldUserGroupName, String newUserGroupName) {
		super(source);
		this.newUserGroupName = newUserGroupName;
		this.oldUserGroupName = oldUserGroupName;
		
	}

	public String getOldUserGroupName() {
		return oldUserGroupName;
	}

	public String getNewUserGroupName() {
		return newUserGroupName;
	}

}
