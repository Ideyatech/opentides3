/**
 * 
 */
package org.opentides.job;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.atmosphere.cpr.MetaBroadcaster;
import org.opentides.bean.SyncMessage;
import org.opentides.context.ApplicationContextProvider;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author allantan
 *
 */
public class NotifyDevices implements Runnable {

	private static Logger _log = Logger.getLogger(NotifyDevices.class);
	
	private static Set<String> lookups = Collections.synchronizedSet(new HashSet<String>()); 

	private static Future<?> futureTask = null;

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	@SuppressWarnings("deprecation")
	public void run() {		
		synchronized(lookups) {
			for (String lookup:lookups) {
				_log.info("Trying to notify devices with lookup " + lookup
						+ ". Retrieving Broadcaster...");		
				
				try {
		    		ObjectMapper mapper = new ObjectMapper();
					SyncMessage syncCommand = new SyncMessage();
					syncCommand.setCommand("getVersion");			    		
					String jsonString = mapper.writeValueAsString(syncCommand);
					MetaBroadcaster.getDefault().broadcastTo(lookup, jsonString);					
				} catch (JsonProcessingException e) {
					_log.error("Failed to convert to JSON.", e);
				}
			}
			lookups.clear();
		}
	}

	
	/**
	 * Notifies the device to check for updates. 
	 * Useful when there are server side changes to be pushed to device.
	 * 
	 * @param lookup
	 */
	public static void notifySync(String lookup) {
		synchronized(lookups) {
			lookups.add(lookup);
			if ( futureTask == null || futureTask.isDone() || futureTask.isCancelled() ) {	
				ThreadPoolTaskScheduler scheduler = (ThreadPoolTaskScheduler) 
						ApplicationContextProvider.getApplicationContext().getBean("scheduler");
				Date scheduledTime = new Date(new Date().getTime() + 10000); // after 10 secs.
				NotifyDevices nd = new NotifyDevices();
				futureTask = scheduler.schedule(nd, scheduledTime);
			}
		}		
	}
}
