package org.opentides.web.controller;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.opentides.annotation.Valid;
import org.opentides.bean.BaseEntity;
import org.opentides.bean.Comment;
import org.opentides.bean.FileInfo;
import org.opentides.bean.impl.Commentable;
import org.opentides.service.BaseCrudService;
import org.opentides.service.CommentService;
import org.opentides.service.UserService;
import org.opentides.service.impl.DefaultFileUploadServiceImpl;
import org.opentides.util.NamingUtil;
import org.opentides.web.validator.CommentValidator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Base class for all controllers that wanted to support comments.
 * 
 * Remember: Command must implement Commentable
 * 
 * @author AJ
 */
public abstract class BaseCommentController<T extends BaseEntity> {
	
	protected BaseCrudService<T> service;
	
	@Autowired
	protected CommentService commentService;
	
	@Autowired
	protected DefaultFileUploadServiceImpl fileUploadService;
	
	@Autowired
	protected UserService userService;
	
	@Autowired
	protected BeanFactory beanFactory;

	@Autowired
	protected MessageSource messageSource;
	
	protected Class<T> entityBeanType;

	@Autowired
	protected CommentValidator commentValidator;
	
	private TransactionTemplate transactionTemplate;
	
	@RequestMapping(method = RequestMethod.GET, value="/delete")
	public @ResponseBody Map<String, Object>
	deleteComment(HttpServletRequest request) {

		Map<String, Object> model = new HashMap<String, Object>();
		
		String commentId = request.getParameter("commentId");
		
		commentService.delete(commentId);
		
		model.put("deleted", true);
		return model;
		
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public final @ResponseBody Map<String, Object>
		sendComment(@Valid final Comment command, BindingResult result, final HttpServletRequest request) {
		
		Map<String, Object> model = new HashMap<String, Object>();
		
		final String commentableId = request.getParameter("commentableId");
		
		if(result.hasGlobalErrors()) {
			model.put("sent", false);
			return model;
		}
		
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@SuppressWarnings("unchecked")
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				
				if(command.getFile() != null && !command.getFile().isEmpty()) {
					
					FileInfo attachment = fileUploadService.upload(command.getFile(), new FileInfo());
					command.addFile(attachment);
					
				}

				command.setAuthor(userService.getCurrentUser());
				
				commentService.save(command);
				
				Commentable commentable = (Commentable) service.load(commentableId);
				commentable.getComments().add(command);

				service.save((T) commentable);

			}
		});

		model.put("id", command.getId());
		model.put("author", command.getAuthor().getCompleteName());
		model.put("authorId", command.getAuthor().getId());
		model.put("text", command.getText());
		model.put("timestamp", command.getPrettyCreateDate());
		
		model.put("sent", true);
		return model;
	}
	
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

		// initialize transaction template.
		PlatformTransactionManager txManager = (PlatformTransactionManager) beanFactory
				.getBean("transactionManager");
		this.transactionTemplate = new TransactionTemplate(txManager);
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder){
		binder.setValidator(commentValidator);
	}
	
}
