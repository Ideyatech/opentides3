package org.opentides.web.listener;

import java.util.List;

import org.opentides.bean.SystemCodes;
import org.opentides.service.SystemCodesService;
import org.opentides.web.event.UserGroupChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class UserGroupChangeEventListener implements ApplicationListener<UserGroupChangeEvent>{

	@Autowired
	private SystemCodesService systemCodesService;
	
	@Override
	public void onApplicationEvent(UserGroupChangeEvent event) {
		SystemCodes tempSysCode = new SystemCodes();
		tempSysCode.setValue(event.getOldUserGroupName());
		tempSysCode.setExactMatch(true);
		List<SystemCodes> result = systemCodesService.findByExample(tempSysCode);
		if(result != null){
			for(SystemCodes userGroupSysCode: result){
				if(userGroupSysCode.getCategory().startsWith("USERGROUP")){
					userGroupSysCode.setValue(event.getNewUserGroupName());
					systemCodesService.save(userGroupSysCode);
					break;
				}
			}
		}
	}

}
