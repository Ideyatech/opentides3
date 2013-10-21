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

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.opentides.annotation.FormBind;
import org.opentides.bean.BaseEntity;
import org.opentides.bean.MessageResponse;
import org.opentides.bean.MessageResponse.Type;
import org.opentides.bean.SearchResults;
import org.opentides.bean.Tag;
import org.opentides.bean.Taggable;
import org.opentides.exception.DataAccessException;
import org.opentides.service.BaseCrudService;
import org.opentides.service.SystemCodesService;
import org.opentides.service.TagService;
import org.opentides.util.CacheUtil;
import org.opentides.util.CrudUtil;
import org.opentides.util.NamingUtil;
import org.opentides.util.StringUtil;
import org.opentides.web.json.ResponseView;
import org.opentides.web.json.Views;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
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
 * Base class for all controllers that wanted to support CRUD functionalities.
 * This version is designed for Spring 3.
 * 
 * @author allantan
 */
public abstract class BaseCrudController<T extends BaseEntity> {

	private static Logger _log = Logger.getLogger(BaseCrudController.class);

	/**
	 * This is the page size for the search results table. This defaults to 20 results per table.
	 */
	@Value("#{applicationSettings.pageSize}")
	protected Integer pageSize = 20;

	/**
	 * 
	 */
	@Value("#{applicationSettings.linksCount}")
	protected Integer numLinks = 10;

	/**
	 * 
	 */
	@Autowired
	protected MessageSource messageSource;

	/**
	 * 
	 */
	@Autowired
	protected BeanFactory beanFactory;

	/**
	 * 
	 */
	@Autowired
	protected SystemCodesService systemCodesService;
	
	/**
	 * 
	 */
	@Autowired
	protected TagService tagService;

	/**
	 * This attribute contains the class type of the bean.
	 */
	protected Class<T> entityBeanType;

	/**
	 * 
	 */
	protected BaseCrudService<T> service;

	/**
	 * 
	 */
	protected Validator formValidator;

	/**
	 * 
	 */
	protected String singlePage = "";

	// single TransactionTemplate shared amongst all methods in this instance
	private TransactionTemplate transactionTemplate;

	/**
	 * Method that attaches the autowired form validator to the binder
	 * 
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) throws Exception {
		if ((formValidator != null) && (binder.getTarget() != null)
				&& formValidator.supports(binder.getTarget().getClass()))
			binder.setValidator(formValidator);
	}

	/**
	 * Handles all binding errors and return as json object for display to the
	 * user.
	 * 
	 * @param e
	 * @param request
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public @ResponseBody
	Map<String, Object> handleBindException(Exception ex,
			HttpServletRequest request) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		List<MessageResponse> messages = new ArrayList<MessageResponse>();
		if (ex instanceof BindException) {
			BindException e = (BindException) ex;
			messages.addAll(CrudUtil.convertErrorMessage(e.getBindingResult(),
					request.getLocale(), messageSource));
			if (_log.isDebugEnabled())
				_log.debug("Bind error encountered.", e);
		} else {
			if (!request.getHeader("Accept").contains("application/json")) {
				throw ex;
			}
			MessageResponse message = new MessageResponse(Type.error,
					new String[] { "error.uncaught-exception" },
					new Object[] { ex.getMessage() });
			message.setMessage(messageSource.getMessage(message,
					request.getLocale()));
			messages.add(message);
			_log.error(ex, ex);
		}
		response.put("messages", messages);
		return response;
	}

	/**
	 * This is the method handler when user requests to search via JSON.
	 * The JSON returned contains the attributes that are annotated with
	 * {@link Views.SearchView}. The implemented <code>preSearch</code> and <code>postSearch</code>
	 * of the child class will also be called accordingly.
	 * 
	 * @param command
	 * @param bindingResult
	 * @param uiModel
	 * @param request
	 * @param response
	 * @return <code>SearchResults</code> containing the results
	 */
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	@ResponseView(Views.SearchView.class)
	public final @ResponseBody
	SearchResults<T> search(@ModelAttribute("searchCommand") T command,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest request, HttpServletResponse response) {
		preSearch(command, bindingResult, uiModel, request, response);
		SearchResults<T> results = search(command, request);
		postSearch(command, results, bindingResult, uiModel, request, response);
		return results;
	}

	/**
	 * This is the entry point of a CRUD page which loads the search page.
	 * 
	 * @param command
	 * @param bindingResult
	 * @param uiModel
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public final String searchHtml(@ModelAttribute("searchCommand") T command,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest request, HttpServletResponse response) {
		uiModel.addAttribute("formCommand",
				BeanUtils.instantiate(this.entityBeanType));
		uiModel.addAttribute("searchCommand", command);
		if (request.getParameterMap().size() > 0) {
			preSearch(command, bindingResult, uiModel, request, response);
			SearchResults<T> results = search(command, request);
			postSearch(command, results, bindingResult, uiModel, request,
					response);
			uiModel.addAttribute("results", results);
		} else {
			onLoadSearch(command, bindingResult, uiModel, request, response);
		}
		uiModel.addAttribute("mode", "search");
		uiModel.addAttribute("search", "ot3-search");
		uiModel.addAttribute("form", "ot3-form hide");
		uiModel.addAttribute("add", "ot3-add");
		uiModel.addAttribute("update", "ot3-update");
		uiModel.addAttribute("method", "post");

		return singlePage;
	}

	/**
	 * 
	 * This is the method handler that persists/create new data. The <code>command</code> object is
	 * binded to the form with the name <code>formCommand</code> from the view. It returns
	 * a map consisting of the command object and the success messages.
	 * 
	 * @param command
	 * @param bindingResult
	 * @param uiModel
	 * @param request
	 * @param response
	 * @return Map containing the following:<br />
	 * 			command - the command object<br />
	 * 			messages - list of MessageResponse
	 */
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public final @ResponseBody
	Map<String, Object> create(@FormBind(name = "formCommand") T command,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> model = new HashMap<String, Object>();
		List<MessageResponse> messages = new ArrayList<MessageResponse>();
		createTx(command, bindingResult, uiModel, request, response);
		messages.addAll(CrudUtil.buildSuccessMessage(command, "add", request.getLocale(), messageSource));
		model.put("command", command);
		model.put("messages", messages);
		return model;
	}

	/**
	 * This is the method handler that updates persisted data. The <code>command</code> object is
	 * binded to the form with the name <code>formCommand</code> from the view. The  
	 * data is loaded from the database through the path variable <code>id</code>. It returns
	 * a map consisting of the command object and the success messages.
	 * 
	 * @param command
	 * @param id
	 * @param bindingResult
	 * @param uiModel
	 * @param request
	 * @param response
	 * @return Map containing the following:<br />
	 * 			command - the command object<br />
	 * 			messages - list of {@link MessageResponse }
	 */
	@RequestMapping(value = "{id}", method = { RequestMethod.PUT,
			RequestMethod.POST }, produces = "application/json")
	public final @ResponseBody
	Map<String, Object> update(@FormBind(name = "formCommand") T command,
			@PathVariable("id") Long id, BindingResult bindingResult,
			Model uiModel, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> model = new HashMap<String, Object>();
		List<MessageResponse> messages = new ArrayList<MessageResponse>();
		updateTx(command, bindingResult, uiModel, request, response);
		messages.addAll(CrudUtil.buildSuccessMessage(command, "update",
				request.getLocale(), messageSource));
		model.put("command", command);
		model.put("messages", messages);
		return model;
	}

	/**
	 * This is the method handler that loads the entity using the path variable
	 * <code>id</code>.
	 * 
	 * @param id
	 * @param uiModel
	 * @param request
	 * @param response
	 * @return command object
	 */
	@RequestMapping(value = "{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseView(Views.FormView.class)
	public final @ResponseBody
	T get(@PathVariable("id") Long id, Model uiModel,
			HttpServletRequest request, HttpServletResponse response) {
		T command = null;
		if (id > 0) {
			command = service.load(id);
		} else {
			command = formBackingObject(request, response);
		}
		return command;
	}

	/**
	 * @param id
	 * @param uiModel
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public String getHtml(@PathVariable("id") Long id, Model uiModel,
			HttpServletRequest request, HttpServletResponse response) {
		T command = null;
		if (id > 0) {
			command = service.load(id);
			uiModel.addAttribute("add", "ot3-add hide");
			uiModel.addAttribute("update", "ot3-update");
			uiModel.addAttribute("method", "put");
		} else {
			command = formBackingObject(request, response);
			uiModel.addAttribute("update", "ot3-update hide");
			uiModel.addAttribute("add", "ot3-add");
			uiModel.addAttribute("method", "post");
		}
		uiModel.addAttribute("formCommand", command);
		uiModel.addAttribute("mode", "form");
		uiModel.addAttribute("search", "ot3-search hide");
		uiModel.addAttribute("form", "ot3-form");

		// load default search page settings
		onLoadSearch(null, null, uiModel, request, response);
		return singlePage;
	}

	/**
	 * This is the method handler that deletes persisted data. It
	 * deletes the data with the path variable <code>id</code> from the database.
	 * 
	 * @param id
	 * @param command
	 * @param bindingResult
	 * @param uiModel
	 * @param request
	 * @param response
	 * @return Map containing the following:<br />
	 * 			{@value} command - the command object<br />
	 * 			{@value} messages - list of MessageResponse
	 */
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = "application/json")
	public final @ResponseBody
	Map<String, Object> delete(@PathVariable("id") Long id, T command,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> model = new HashMap<String, Object>();
		List<MessageResponse> messages = new ArrayList<MessageResponse>();
		if (id >= 0) {
			try {
				deleteTx(id, bindingResult, uiModel, request, response);
				messages.addAll(CrudUtil.buildSuccessMessage(command, "delete",
						request.getLocale(), messageSource));
				model.put("messages", messages);
				return model;
			} catch (Exception e) {
				String message = "Failed to delete " + this.entityBeanType
						+ " with id = [" + id + "]";
				_log.error(message, e);
				throw new DataAccessException(message, e);
			}
		} else {
			String message = "Invalid id = [" + id
					+ "] for delete operation of " + this.entityBeanType;
			_log.error(message);
			throw new DataAccessException(message);
		}
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private final T formBackingObject(HttpServletRequest request,
			HttpServletResponse response) {
		Method addForm = CacheUtil.getNewFormBindMethod(this.getClass());
		Object target;
		try {
			target = (addForm != null) ? addForm.invoke(this, request)
					: BeanUtils.instantiateClass(this.entityBeanType);
		} catch (Exception e) {
			_log.error("Failed to invoke FormBind method.", e);
			target = BeanUtils.instantiateClass(this.entityBeanType);
		}
		return (T) target;
	}

	/**
	 * Override this method to perform action when page is initially loaded.
	 * 
	 * @param command
	 * @param bindingResult
	 * @param uiModel
	 * @param request
	 * @param response
	 */
	protected void onLoadSearch(T command, BindingResult bindingResult,
			Model uiModel, HttpServletRequest request,
			HttpServletResponse response) {

	}

	/**
	 * @param command
	 * @param bindingResult
	 * @param uiModel
	 * @param request
	 * @param response
	 */
	private final void createTx(final T command, final BindingResult bindingResult,
			final Model uiModel, final HttpServletRequest request,
			final HttpServletResponse response) {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				preProcessTaggableEntities(command);
				preCreate(command, bindingResult, uiModel, request, response);
				service.save(command);
				postProcessTaggableEntities(command);
				postCreate(command, bindingResult, uiModel, request, response);
			}
		});
		
	}
	
	/**
	 * Check if entity is an instance of {@link Taggable}. If so, save the tags.
	 * @param command
	 */
	private void preProcessTaggableEntities(T command) {
		if (Taggable.class.isAssignableFrom(command.getClass())) {
			Taggable taggable = (Taggable)command;
			tagService.preProcessTags(taggable, command.getId(), this.entityBeanType);
		}
	}
	
	private void postProcessTaggableEntities(T command) {
		if (Taggable.class.isAssignableFrom(command.getClass())) {
			Taggable taggable = (Taggable)command;
			if(taggable.getTags() != null && !taggable.getTags().isEmpty()) {
				for(Tag tag : taggable.getTags()) {
					if(tag.getTaggableId() == null) {
						tag.setTaggableId(command.getId());
						tagService.save(tag);
					}
				}
			}
		}
	}

	/**
	 * Override this method to perform pre-processing on new data being saved.
	 * 
	 * @param command
	 * @param bindingResult
	 * @param uiModel
	 * @param request
	 * @param response
	 */
	protected void preCreate(T command, BindingResult bindingResult,
			Model uiModel, HttpServletRequest request,
			HttpServletResponse response) {
		preCreate(command);
	}
	
	
	/**
	 * Override this method to perform pre-processing on new data being saved.
	 * 
	 * @param command
	 */
	protected void preCreate(T command) {
	}

	/**
	 * Override this method to perform post-processing on new data being saved.
	 * 
	 * @param command
	 * @param bindingResult
	 * @param uiModel
	 * @param request
	 * @param response
	 */
	protected void postCreate(T command, BindingResult bindingResult,
			Model uiModel, HttpServletRequest request,
			HttpServletResponse response) {
		postCreate(command);
	}

	/**
	 * Override this method to perform post-processing on new data being saved.
	 * 
	 * @param command
	 */
	protected void postCreate(T command) {
	}

	/**
	 * Contain pre and post update within one transaction.
	 * 
	 * @param command
	 * @param bindingResult
	 * @param uiModel
	 * @param request
	 * @param response
	 */
	private final void updateTx(final T command, final BindingResult bindingResult,
			final Model uiModel, final HttpServletRequest request,
			final HttpServletResponse response) {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				preProcessTaggableEntities(command);
				preUpdate(command, bindingResult, uiModel, request, response);
				service.save(command);
				postUpdate(command, bindingResult, uiModel, request, response);
			}
		});
	}

	/**
	 * Override this method to perform pre-processing on data being updated.
	 * 
	 * @param command
	 */
	protected void preUpdate(T command, BindingResult bindingResult,
			Model uiModel, HttpServletRequest request,
			HttpServletResponse response) {
		preUpdate(command);
	}

	/**
	 * Override this method to perform pre-processing on data being updated.
	 * 
	 * @param command
	 */
	protected void preUpdate(T command) {
	}

	/**
	 * Override this method to perform post-processing on data being updated.
	 * 
	 * @param command
	 */

	protected void postUpdate(T command, BindingResult bindingResult,
			Model uiModel, HttpServletRequest request,
			HttpServletResponse response) {
		postUpdate(command);
	}

	/**
	 * Override this method to perform post-processing on data being updated.
	 * 
	 * @param command
	 */
	protected void postUpdate(T command) {
	}

	/**
	 * @param id
	 * @param bindingResult
	 * @param uiModel
	 * @param request
	 * @param response
	 */
	private final void deleteTx(final Long id, final BindingResult bindingResult,
			final Model uiModel, final HttpServletRequest request,
			final HttpServletResponse response) {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				preDelete(id, bindingResult, uiModel, request, response);
				service.delete(id);
				postDelete(id, bindingResult, uiModel, request, response);
			}
		});		
	}

	/**
	 * Override this method to perform pre-processing on data being deleted.
	 * 
	 * @param command
	 */
	protected void preDelete(Long id, BindingResult bindingResult,
			Model uiModel, HttpServletRequest request,
			HttpServletResponse response) {
		preDelete(id);
	}

	/**
	 * Override this method to perform pre-processing on data being deleted.
	 *  
	 * @param id
	 */
	protected void preDelete(Long id) {
	}

	/**
	 * Override this method to perform post-processing on data being deleted.
	 * 
	 * @param command
	 */
	protected void postDelete(Long id, BindingResult bindingResult,
			Model uiModel, HttpServletRequest request,
			HttpServletResponse response) {
		postDelete(id);
	}

	/**
	 * Override this method to perform post-processing on data being deleted.
	 * 
	 * @param id
	 */
	protected void postDelete(Long id) {
	}

	/**
	 * Override this method to perform pre-processing on data search.
	 * 
	 * @param command
	 *            criteria used for search
	 */

	protected void preSearch(T command, BindingResult bindingResult,
			Model uiModel, HttpServletRequest request,
			HttpServletResponse response) {
		preSearch(command);
	}

	/**
	 * Override this method to perform pre-processing on data search.
	 * 
	 * @param command
	 * @return SearchResults
	 */
	protected void preSearch(T command) {
	}

	/**
	 * Override this method to perform post-processing on data search.
	 * 
	 * @param command
	 * @return SearchResults
	 */
	protected SearchResults<T> postSearch(T command, SearchResults<T> result,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest request, HttpServletResponse response) {
		return postSearch(result);
	}

	/**
	 * Override this method to perform post-processing on data search.
	 * 
	 * @param result
	 * @return SearchResults
	 */
	protected SearchResults<T> postSearch(SearchResults<T> result) {
		return result;
	}

	/**
	 * This is a post construct that set ups the service and validator for the
	 * child service class.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		try {
			this.entityBeanType = (Class<T>) ((ParameterizedType) getClass()
					.getGenericSuperclass()).getActualTypeArguments()[0];
		} catch (ClassCastException cc) {
			// if dao is extended from the generic dao class
			this.entityBeanType = (Class<T>) ((ParameterizedType) getClass()
					.getSuperclass().getGenericSuperclass())
					.getActualTypeArguments()[0];
		}
		Assert.notNull(this.entityBeanType,
				"Unable to retrieve entityBeanType for "
						+ this.getClass().getSimpleName());
		// try setting up service and validator by convention
		String attributeName = NamingUtil.toAttributeName(this.entityBeanType
				.getSimpleName());
		String serviceBean = attributeName + "Service";
		String validatorBean = attributeName + "Validator";

		if (this.service == null && beanFactory.containsBean(serviceBean))
			this.service = (BaseCrudService<T>) beanFactory
					.getBean(serviceBean);
		if (this.formValidator == null
				&& beanFactory.containsBean(validatorBean))
			this.formValidator = (Validator) beanFactory.getBean(validatorBean);
		if (StringUtil.isEmpty(this.singlePage)) {
			this.singlePage = "app/"
					+ NamingUtil.toElementName(this.entityBeanType
							.getSimpleName()) + "-crud";
		}
		Assert.notNull(service, this.getClass().getSimpleName()
				+ " is not associated with a service class [" + serviceBean
				+ "]. Please check your configuration.");

		// initialize transaction template.
		PlatformTransactionManager txManager = (PlatformTransactionManager) beanFactory
				.getBean("transactionManager");
		this.transactionTemplate = new TransactionTemplate(txManager);
	}

	/**
	 * This is the method handler that performs search requests for a given command object.
	 * It can start searching from a specific page given by the parameter <code>p</code>.
	 * If no page is given, it defaults to the first page of the results.
	 * 
	 * @param command
	 * @param request
	 * @return SSearchResults
	 */
	protected final SearchResults<T> search(T command,
			HttpServletRequest request) {
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
				results.addResults(service.findByExample(command, start, total));
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
	 * 
	 * @param command
	 * @return the count
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
	 * @return the service
	 */
	public final BaseCrudService<T> getService() {
		return service;
	}

}
