package org.opentides.web.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

/**
 * Modified Spring 3.1's internal Return value handlers, and wires up a
 * decorator to add support for @JsonView
 * 
 * @author martypitt
 * 
 */

public class JsonViewSupportFactoryBean implements InitializingBean {

	@Autowired
	private RequestMappingHandlerAdapter adapter;
	
	private static final Logger _log = Logger.getLogger(JsonViewSupportFactoryBean.class);

	@Override
	public void afterPropertiesSet() throws Exception {
		List<HandlerMethodReturnValueHandler> handlers = adapter.getReturnValueHandlers();
		adapter.setReturnValueHandlers(decorateHandlers(handlers));
	}

	private List<HandlerMethodReturnValueHandler> decorateHandlers(List<HandlerMethodReturnValueHandler> handlers) {
		List<HandlerMethodReturnValueHandler> decorated = new ArrayList<HandlerMethodReturnValueHandler>();
		
		for (HandlerMethodReturnValueHandler handler : handlers) {
			if (handler instanceof RequestResponseBodyMethodProcessor) {
				ViewInjectingReturnValueHandler decorator = new 
						ViewInjectingReturnValueHandler(handler);
				decorated.add(decorator);
                _log.info("JsonView decorator support wired up for @ResponseBody.");
			} else {
				decorated.add(handler);				
			}
		}
		return decorated;
	}

}