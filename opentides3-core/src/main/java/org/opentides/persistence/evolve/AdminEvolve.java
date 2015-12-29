/**
 * 
 */
package org.opentides.persistence.evolve;

import org.opentides.service.UserGroupService;
import org.opentides.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author allantan
 *
 */
@Component("adminEvolve")
public class AdminEvolve extends Evolver {
	
	@Autowired
	private UserGroupService userGroupService;
	
	@Autowired
	private UserService userService;

	/* (non-Javadoc)
	 * @see org.opentides.persistence.evolve.Evolver#execute()
	 */
	@Override
	public void execute() {
		userGroupService.setupAdminGroup();
		userService.setupAdminUser();
	}

	/* (non-Javadoc)
	 * @see org.opentides.persistence.evolve.Evolver#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Setup admin user and usergroup";
	}

	/* (non-Javadoc)
	 * @see org.opentides.persistence.evolve.Evolver#getVersion()
	 */
	@Override
	public int getVersion() {
		return 0;
	}

}
