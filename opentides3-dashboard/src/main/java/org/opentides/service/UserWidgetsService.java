package org.opentides.service;

import java.util.List;

import org.opentides.bean.UserWidgets;
import org.opentides.bean.Widget;
import org.opentides.bean.user.BaseUser;
import org.opentides.bean.user.UserAuthority;
import org.opentides.bean.user.UserGroup;

/**
 * 
 * @author opentides
 *
 */
public interface UserWidgetsService extends BaseCrudService<UserWidgets> {
	
	/**
	 * 
	 * @param userId
	 * @param selectedWidgets
	 */
	public void addUserWidgets(long userId, String selectedWidgets);
	
	/**
	 * Add user widgets based on user id and widget object
	 * @param userId - Id of a user
	 * @param widget - Widget Object
	 */
	public void addUserWidgets(long userId, Widget widget);
	
	
	/**
	 * Retrieve all widgets of a user based on the given status
	 * @param userId - the user
	 * @param widgetStatus - array's of widget status
	 * @return List of UserWidgets object
	 */
	public List<UserWidgets> findUserWidgets(long userId, Integer... widgetStatus);
	
	/**
	 * Retrieve a user widget based on name
	 * @param widgetName - the widget name
	 * @param user - base user object
	 * @return single UserWidgets object
	 */
	public UserWidgets findSpecificUserWidgets(BaseUser user, String widgetName);
	
	/**
	 * Update row and column of a user widget
	 * @param userWidgets - UserWidgets object
	 * @param column - int column position
	 * @param row - int row position
	 * @return 
	 */
	public void updateUserWidgetsOrder(UserWidgets userWidgets, int column, int row);
	
	/**
	 * Change the status of our user widgets
	 * @param userWidgets - UserWidgets object
	 * @param status - Integer status (remove, show or minimize)
	 */
	public void updateUserWidgetsStatus(UserWidgets userWidgets, Integer status);
	
	/**
	 * Count user widget installed based on userid and column number
	 * @param column - the column to count widgets
	 * @param userId - specific user
	 */
	public long countUserWidgetsColumn(Integer column, long userId);
	
	/**
	 * Removes the user widgets for all widget with the given access roles and usergroup.
	 * Note that list of access roles pertains to all of the usergroups's access roles, including non-widget
	 * specific roles. The method will be responsible in weeding out non-widget-specific access roles.
	 * 
	 * @param userId
	 * @param userAccessRoles
	 */
	public void removeUserGroupWidgetsWithAccessCodes(UserGroup userGroup, List<UserAuthority> userAccessRoles);
	
	/**
	 * Automatically add Widgets that is visible by default to all users within the User Group.
	 * @param userGroup
	 * @param userAccessRoles
	 */
	public void setupUserGroupWidgets(UserGroup userGroup, List<UserAuthority> userAccessRoles);
}
