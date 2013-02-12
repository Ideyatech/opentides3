/*
   Licensed to the Apache Software Foundation (ASF) under one
   or more contributor license agreements.  See the NOTICE file
   distributed with this work for additional information
   regarding copyright ownership.  The ASF licenses this file
   to you under the Apache License, Version 2.0 (the
   "License"); you may not use this file except in compliance
   with the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing,
   software distributed under the License is distributed on an
   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
   KIND, either express or implied.  See the License for the
   specific language governing permissions and limitations
   under the License.    
 */
package org.opentides.web.controller;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.opentides.annotation.FormBind;
import org.opentides.bean.BaseEntity;
import org.opentides.bean.MessageResponse;
import org.opentides.bean.SearchResults;
import org.opentides.service.BaseCrudService;
import org.opentides.util.NamingUtil;
import org.opentides.util.StringUtil;
import org.opentides.web.json.ResponseView;
import org.opentides.web.json.Views;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Base class for all controllers that wanted to support CRUD
 * functionalities. This version is designed for Spring 3.
 * 
 * @author allantan
 */
public abstract class BaseCrudController<T extends BaseEntity> {
	
    private static Logger _log = Logger.getLogger(BaseCrudController.class);
		
	@Value("#{applicationSettings.pageSize}")
	private Integer pageSize = 20;
	
	@Value("#{applicationSettings.linksCount}")
	private Integer numLinks = 10;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private BeanFactory beanFactory;
	
	// contains the class type of the bean    
    private Class<T> entityBeanType;

    protected BaseCrudService<T> service;

	protected Validator formValidator;
	
	protected String notificationLocation = "top-right";
	
	protected String singlePage = "";
	
	/**
	 * Attaches the validator if available.
	 * @param request
	 * @param binder
	 * @throws Exception
	 */
	@InitBinder
	protected final void initBinder(WebDataBinder binder) throws Exception {		
		if ((formValidator != null) && (binder.getTarget() != null) &&
				formValidator.supports(binder.getTarget().getClass()) )
			binder.setValidator(formValidator);
	}
	
	/**
	 * Handles all binding errors and return as json object for
	 * display to the user.
	 * 
	 * @param e
	 * @param request
	 * @return
	 */
    @ExceptionHandler(BindException.class)
    public @ResponseBody Map<String, Object> handleException(BindException e, HttpServletRequest request) {
    	Map<String, Object> response = new HashMap<String,Object>();
    	List<MessageResponse> messages = new ArrayList<MessageResponse>();
    	messages.addAll(convertErrorMessage("modal-body", e.getBindingResult(), request.getLocale()));
    	response.put("messages", messages);
    	return response;    		
    }
    
    /**
     * This is the method handler when user requests to search via json.
     * @param command
     * @param bindingResult
     * @param uiModel
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseView(Views.SearchView.class)
    public final @ResponseBody SearchResults<T> searchJson(
    		@ModelAttribute("searchCommand") T command, 
    		BindingResult bindingResult, Model uiModel,
			HttpServletRequest request, HttpServletResponse response) {
    	preSearchAction(command, bindingResult, uiModel, request, response);
    	SearchResults<T> results = search(command, request);
    	postSearchAction(command, results, bindingResult, uiModel, request, response);
    	return results;
    }
    
    /**
     * This is the entry point of a CRUD page which loads the search page.
     * 
     * @param uiModel
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public final String loadHtml( @ModelAttribute("searchCommand") T command, 
    		BindingResult bindingResult, Model uiModel,
    		HttpServletRequest request, HttpServletResponse response) {
    	uiModel.asMap().clear();
    	uiModel.addAttribute("formCommand", BeanUtils.instantiate(this.entityBeanType));    	
    	uiModel.addAttribute("searchCommand", command);    	
    	if (request.getParameterMap().size() > 0) {
        	preSearchAction(command, bindingResult, uiModel, request, response);
        	SearchResults<T> results = search(command, request);
        	postSearchAction(command, results, bindingResult, uiModel, request, response);
    		uiModel.addAttribute("results",results);
    	} else {
    		onLoadAction(command, bindingResult, uiModel, request, response);    		
    	}
        return singlePage;
    }
       
    /**
     * Saves the form.
     * 
     * @param command
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public final @ResponseBody Map<String, Object> create(
    			@FormBind(name="formCommand") T command, 
    			BindingResult bindingResult, Model uiModel,
    			HttpServletRequest request, HttpServletResponse response) {
    	Map<String, Object> model = new HashMap<String,Object>();
    	List<MessageResponse> messages = new ArrayList<MessageResponse>();
   		preCreateAction(command, bindingResult, uiModel, request, response);
        service.save(command);
   		postCreateAction(command, bindingResult, uiModel, request, response);    		
       	messages.addAll(buildSuccessMessage(command, "add", request.getLocale()));
        model.put("command", command);
        model.put("messages", messages);
        return model;
    }
    
    @RequestMapping(value="{id}", method = RequestMethod.PUT, produces = "application/json")
    public final @ResponseBody Map<String, Object> update(
    			@FormBind(name="formCommand") T command, 
    			@PathVariable("id") Long id,
    			BindingResult bindingResult, Model uiModel,
    			HttpServletRequest request, HttpServletResponse response) {
    	Map<String, Object> model = new HashMap<String,Object>();
    	List<MessageResponse> messages = new ArrayList<MessageResponse>();
   		preUpdateAction(command, bindingResult, uiModel, request, response);
        service.save(command);
   		postUpdateAction(command, bindingResult, uiModel, request, response);    		
       	messages.addAll(buildSuccessMessage(command, "update", request.getLocale()));
        model.put("command", command);
        model.put("messages", messages);
        return model;
    }
        
    @RequestMapping(value="{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseView(Views.FormView.class)
    public final @ResponseBody T get(@PathVariable("id") Long id, Model uiModel,
			HttpServletRequest request, HttpServletResponse response) {    	
		T command = null;
    	if (id>0) {
    		command = service.load(id);    		
    	} else {
    		command = BeanUtils.instantiate(this.entityBeanType);
    	}
    	return command;
    }
 
    @RequestMapping(value="{id}", method = RequestMethod.DELETE, produces = "application/json")
    public final @ResponseBody Map<String, Object> delete(@PathVariable("id") Long id, 
    		T command, BindingResult bindingResult, Model uiModel,
			HttpServletRequest request, HttpServletResponse response) {
    	Map<String, Object> model = new HashMap<String,Object>();
    	List<MessageResponse> messages = new ArrayList<MessageResponse>();    	
    	if (id >= 0) {
    		try {
    			preDeleteAction(id, bindingResult, uiModel, request, response);
    			service.delete(id);
    			postDeleteAction(id, bindingResult, uiModel, request, response);
    	    	messages.addAll(buildSuccessMessage(command, "delete", request.getLocale()));
    	    	model.put("messages", messages);    	    	
    	    	return model;    			
    		} catch (Exception e) {
    			String message = "Failed to delete "+this.entityBeanType+" with id = ["+id+"]";
    			_log.error(message,e);
    			throw new DataRetrievalFailureException(message, e);
    		}
    	} else {
			String message = "Invalid id = ["+id+"] for delete operation of "+this.entityBeanType;
			_log.error(message);
			throw new DataRetrievalFailureException(message);    		
    	}
    }
    
    /**
     * Override this method to perform action when page is initially loaded.
     * 
     * @param command
     */

    protected void onLoadAction(
    		T command, 
    		BindingResult bindingResult, Model uiModel,
			HttpServletRequest request, HttpServletResponse response) {
    	
    }
    
    /**
     * Override this method to perform pre-processing on new data being saved.
     * 
     * @param command
     */

    protected void preCreateAction(T command, 
    		BindingResult bindingResult, Model uiModel,
			HttpServletRequest request, HttpServletResponse response) {
        preCreateAction(command);
    }

    protected void preCreateAction(T command) {
    }

    /**
     * Override this method to perform post-processing on new data being saved.
     * 
     * @param command
     */

    protected void postCreateAction(T command, 
    		BindingResult bindingResult, Model uiModel,
			HttpServletRequest request, HttpServletResponse response) {
        postCreateAction(command);
    }

    protected void postCreateAction(T command) {
    }

    /**
     * Override this method to perform pre-processing on data being updated.
     * 
     * @param command
     */

    protected void preUpdateAction(T command, 
    		BindingResult bindingResult, Model uiModel,
			HttpServletRequest request, HttpServletResponse response) {
        preUpdateAction(command);
    }

    protected void preUpdateAction(T command) {
    }

    /**
     * Override this method to perform post-processing on data being updated.
     * 
     * @param command
     */

    protected void postUpdateAction(T command, 
    		BindingResult bindingResult, Model uiModel,
			HttpServletRequest request, HttpServletResponse response) {
        postUpdateAction(command);
    }

    protected void postUpdateAction(T command) {
    }

    /**
     * Override this method to perform pre-processing on data being deleted.
     * 
     * @param command
     */

    protected void preDeleteAction(Long id,
    		BindingResult bindingResult, Model uiModel,
			HttpServletRequest request, HttpServletResponse response) {
        preDeleteAction(id);
    }

    protected void preDeleteAction(Long id) {
    }

    /**
     * Override this method to perform post-processing on data being deleted.
     * 
     * @param command
     */

    protected void postDeleteAction(Long id,
    		BindingResult bindingResult, Model uiModel,
			HttpServletRequest request, HttpServletResponse response) {
        postDeleteAction(id);
    }

    protected void postDeleteAction(Long id) {
    }

    /**
     * Override this method to perform pre-processing on data search. 
     * 
     * @param command
     *            criteria used for search
     */

    protected void preSearchAction(T command, 
    		BindingResult bindingResult, Model uiModel,
			HttpServletRequest request, HttpServletResponse response) {
        preSearchAction(command);
    }

    protected void preSearchAction(T command) {
    }

    /**
     * Override this method to perform post-processing on data search.
     * 
     * @param command
     */

    protected SearchResults<T> postSearchAction(T command, SearchResults<T> result,
    		BindingResult bindingResult, Model uiModel,
			HttpServletRequest request, HttpServletResponse response) {
        return postSearchAction(result);
    }

    protected SearchResults<T> postSearchAction(SearchResults<T> result) {
        return result;
    }
    
    @SuppressWarnings("unchecked")
	@PostConstruct
    public void afterPropertiesSet() throws Exception {
		try {
	        this.entityBeanType = (Class<T>) ((ParameterizedType) getClass()
	                .getGenericSuperclass()).getActualTypeArguments()[0];	        
		} catch (ClassCastException cc) {
			// if dao is extended from the generic dao class
			this.entityBeanType = (Class<T>) ((ParameterizedType) getClass().getSuperclass()
	                .getGenericSuperclass()).getActualTypeArguments()[0];
		}
		
		// try setting up service and validator by convention
		String attributeName = NamingUtil.toAttributeName(this.entityBeanType.getSimpleName());
        String serviceBean   = attributeName + "Service";
        String validatorBean = attributeName + "Validator";
        
        if (this.service==null && beanFactory.containsBean(serviceBean))
        	this.service = (BaseCrudService<T>) beanFactory.getBean(serviceBean);
        if (this.formValidator==null && beanFactory.containsBean(validatorBean))
			this.formValidator = (Validator) beanFactory.getBean(validatorBean);        
        if (StringUtil.isEmpty(this.singlePage)) {
        	this.singlePage = "app/"+ 
        			NamingUtil.toElementName(this.entityBeanType.getSimpleName()) +"-crud";
        }
        Assert.notNull(service, this.getClass().getSimpleName() + 
        					" is not associated with a service class [" + serviceBean +
        					"]. Please check your configuration.");
	}
    
    protected final SearchResults<T> search(T command, HttpServletRequest request) {
        SearchResults<T> results = new SearchResults<T>(pageSize, numLinks);
        int page = StringUtil.convertToInt(request.getParameter("p"), 1);
        long startTime = System.currentTimeMillis();
        results.setCurrPage(page);
        results.setTotalResults(this.countAction(command));
        int start = results.getStartIndex();
        int total = results.getPageSize();
        if (pageSize > 0) {
            if (command == null) {
                // no command, let's search everything
                results.addResults(service.findAll(start, total));
            } else {
                // let's do a query by example
                results.addResults(service.findByExample(command,
                        start, total));
            }
        } else {
            if (command == null) {
                // no command, let's search everything
                results.addResults(service.findAll());
            } else {
                // let's do a query by example
                results.addResults(service.findByExample(command));
            }
        }
        results.setSearchTime(System.currentTimeMillis() - startTime);
        return results;
    }
    /**
     * Performs record matching count, used for search.
     * @param command
     * @return
     */
    protected long countAction(T command) {
        if (command == null) {
            // no command, let's search everything
            return service.countAll();
        } else {
            return service.countByExample(command);
        }
    }

	/**
	 * Converts the binding error messages to list of MessageResponse
	 * @param bindingResult
	 */
	protected List<MessageResponse> convertErrorMessage(String elementClass, BindingResult bindingResult, Locale locale) {
		List<MessageResponse> errorMessages = new ArrayList<MessageResponse>();
		if (bindingResult.hasErrors()) {
			for (ObjectError error:bindingResult.getAllErrors()) {
				MessageResponse message = new MessageResponse(elementClass, MessageResponse.Type.error,
						error.getObjectName(), error.getCodes(), error.getArguments());
				message.setMessage(messageSource.getMessage(message, locale));
				errorMessages.add(message);
			}
		}
		return errorMessages;
	}
	
	/**
	 * Builds success message by convention. Default location to top-right.
	 * 
	 * Standard convention in order of resolving message is:
	 *     (1) message.<className>.<code>-success (e.g. message.system-codes.add-success)
	 *     (2) message.add-success (generic message)
	 * 
	 * @see buildSuccessMessage(String elementClass, BaseEntity object, String code, Locale locale)
	 * 
	 * @param object
	 * @param code
	 * @param locale
	 * @return
	 */
	protected List<MessageResponse> buildSuccessMessage(BaseEntity object, String code, Locale locale) {
		return this.buildSuccessMessage(notificationLocation, object, code, locale);
	}
	
	/**
	 * Builds success message by convention.
	 * Success messages are displayed as notifications only.
	 * 
	 * Standard convention in order of resolving message is:
	 *     (1) message.<className>.<code>-success (e.g. message.system-codes.add-success)
	 *     (2) message.add-success (generic message)
	 *     
	 * @param elementClass
	 * @param object
	 * @param code
	 * @param locale
	 * @return
	 */
	protected List<MessageResponse> buildSuccessMessage(String elementClass, BaseEntity object, String code, Locale locale) {
		List<MessageResponse> messages = new ArrayList<MessageResponse>();
		Assert.notNull(object);
		String prefix = "message." + NamingUtil.toElementName(object.getClass().getSimpleName());
		String codes = prefix+"."+code+"-success,message."+code+"-success";
		MessageResponse message = new MessageResponse(elementClass, MessageResponse.Type.notification, codes.split("\\,"), null);
		message.setMessage(messageSource.getMessage(message, locale));
    	messages.add(message);
	    return messages;
	}
	
	/**
	 * @return the service
	 */
	public final BaseCrudService<T> getService() {
		return service;
	}    
    
}
