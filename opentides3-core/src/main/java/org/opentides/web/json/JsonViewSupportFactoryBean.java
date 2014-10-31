package org.opentides.web.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
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
		HandlerMethodReturnValueHandlerComposite returnValueHandlers = adapter
				.getReturnValueHandlers();
		List<HandlerMethodReturnValueHandler> handlers = new ArrayList<HandlerMethodReturnValueHandler>(
				returnValueHandlers.getHandlers());
		decorateHandlers(handlers);
		adapter.setReturnValueHandlers(handlers);
	}

	private void decorateHandlers(List<HandlerMethodReturnValueHandler> handlers) {
		for (HandlerMethodReturnValueHandler handler : handlers) {
			if (handler instanceof RequestResponseBodyMethodProcessor) {
				ViewInjectingReturnValueHandler decorator = new 
						ViewInjectingReturnValueHandler(handler);
				int index = handlers.indexOf(handler);
				handlers.set(index, decorator);
                _log.info("JsonView decorator support wired up for @ResponseBody.");
				break;
			}
		}
	}

}