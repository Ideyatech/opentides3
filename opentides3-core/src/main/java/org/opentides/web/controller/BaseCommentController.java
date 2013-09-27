package org.opentides.web.controller;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.opentides.annotation.Valid;
import org.opentides.bean.BaseEntity;
import org.opentides.bean.Comment;
import org.opentides.bean.Commentable;
import org.opentides.bean.FileInfo;
import org.opentides.service.BaseCrudService;
import org.opentides.service.CommentService;
import org.opentides.service.FileUploadService;
import org.opentides.service.UserService;
import org.opentides.util.NamingUtil;
import org.opentides.web.validator.CommentValidator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Base class for all controllers that want to support comments. Command
 * object should implement the {@link Commentable} interface.
 * 
 * @author AJ
 */
public abstract class BaseCommentController<T extends BaseEntity> {
	
	protected Class<T> entityBeanType;

	protected BaseCrudService<T> service;
	
	@Autowired
	protected CommentService commentService;
	
	@Autowired
	@Qualifier(value="defaultFileUploadService")
	protected FileUploadService fileUploadService;
	
	@Autowired
	protected UserService userService;
	
	@Autowired
	protected BeanFactory beanFactory;

	@Autowired
	protected MessageSource messageSource;
	
	@Autowired
	protected CommentValidator commentValidator;
	
	/**
	 * This method is used to remove a comment from a Commentable entity.
	 * It expects two parameters, <code>commentableId</code> and <code>commentId</code>.
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value="/delete")
	public @ResponseBody Map<String, Object>
	deleteComment(HttpServletRequest request) {

		Map<String, Object> model = new HashMap<String, Object>();
		
		String commentableId = request.getParameter("commentableId");
		String commentId = request.getParameter("commentId");
		
		Commentable commentable = (Commentable) service.load(commentableId);
		Comment comment = (Comment) commentService.load(commentId);
		
		commentable.getComments().remove(comment);
		
		service.save((T) commentable);
		
		model.put("deleted", true);
		
		return model;
	}
	
	/**
	 * This is the method handler to save a comment. It expects a <code>commentableId</code>
	 * parameter which is the id of the entity that the said comment is attached to.
	 * 
	 * @param command
	 * @param result
	 * @param request
	 * @return Map containing the following:<br />
	 * 			- id<br />
	 * 			- author<br />
	 * 			- authorId<br />
	 * 			- text<br />
	 * 			- timestamp<br />
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Map<String, Object>
		sendComment(@Valid Comment command, BindingResult result, HttpServletRequest request) {
		
		Map<String, Object> model = new HashMap<String, Object>();
		
		String commentableId = request.getParameter("commentableId");
		
		if(result.hasGlobalErrors()) {
			model.put("sent", false);
			return model;
		}
		
		if(command.getFile() != null && !command.getFile().isEmpty()) {
//			FileInfo attachment = fileUploadService.upload(command.getFile(), new FileInfo());
//			command.addFile(attachment);
		}

		command.setAuthor(userService.getCurrentUser());
		
		commentService.save(command);
		
		Commentable commentable = (Commentable) service.load(commentableId);
		commentable.getComments().add(command);

		service.save((T) commentable);
		
		model.put("id", command.getId());
		model.put("author", command.getAuthor().getCompleteName());
		model.put("authorId", command.getAuthor().getId());
		model.put("text", command.getText());
		model.put("timestamp", command.getPrettyCreateDate());
		
		model.put("sent", true);
		return model;
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
		
		// try setting up service by convention
		String attributeName = NamingUtil.toAttributeName(this.entityBeanType
				.getSimpleName());
		String serviceBean = attributeName + "Service";

		if (this.service == null && beanFactory.containsBean(serviceBean))
			this.service = (BaseCrudService<T>) beanFactory
					.getBean(serviceBean);
		
		Assert.notNull(service, this.getClass().getSimpleName()
				+ " is not associated with a service class [" + serviceBean
				+ "]. Please check your configuration.");

	}
	
	/**
	 * Method that attaches the autowired comment validator to the binder
	 * 
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder){
		binder.setValidator(commentValidator);
	}
	
}
