package org.opentides.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opentides.annotation.Valid;
import org.opentides.bean.BaseEntity;
import org.opentides.bean.FileInfo;
import org.opentides.bean.MessageResponse;
import org.opentides.bean.PhotoInfo;
import org.opentides.bean.impl.Photoable;
import org.opentides.service.BaseCrudService;
import org.opentides.service.PhotoInfoService;
import org.opentides.service.impl.DefaultFileUploadServiceImpl;
import org.opentides.util.ImageUtil;
import org.opentides.util.NamingUtil;
import org.opentides.util.StringUtil;
import org.opentides.web.validator.PhotoValidator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Base class for all controllers that wanted to support Photo functionalities.
 * Refer to UserPhotoController for how to use.
 * 
 * Remember: Command must implement Photoable
 * 
 * As much as you do not understand the code below, so do I.
 * 
 * @author ajalbaniel
 */
public abstract class BasePhotoController<T extends BaseEntity> {
	
	@Autowired
	protected PhotoInfoService photoInfoService;
	
	@Autowired
	protected PhotoValidator photoValidator;
	
	@Autowired
	protected DefaultFileUploadServiceImpl fileUploadService;
	
	protected String uploadPage = "";
	protected String adjustPhoto = "";

	@Autowired
	protected BeanFactory beanFactory;

	@Autowired
	protected MessageSource messageSource;
	
	protected Class<T> entityBeanType;
	
	private TransactionTemplate transactionTemplate;
	
	protected BaseCrudService<T> service;
	
	@ModelAttribute("command") 
	protected abstract T getPhotoable(Long id);
	
	@RequestMapping(method = RequestMethod.GET)
	public final String displayImage(ModelMap modelMap,
			@ModelAttribute("command") T command,
			@RequestParam(value="size", required=false) String size,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		@SuppressWarnings("deprecation")
		String defaultImage = request.getRealPath("") + "/base/img/default-photo.png";
		
		byte[] barray = null;
		OutputStream outputStream = response.getOutputStream();

		try {
			Photoable photoable = (Photoable) command;
			if (photoable != null && photoable.getPhotos() != null && !photoable.getPhotos().isEmpty()) {
				
				PhotoInfo photoInfo = photoable.getPhotos().get(photoable.getPhotos().size()-1);
				
				String filePath = photoInfo.getFullPath();
				
				try {
					if("l".equals(size)) {
						barray = ImageUtil.loadImage(ImageUtil.buildFileName(filePath, "_l.png"));
					} else if("m".equals(size)) {
						barray = ImageUtil.loadImage(ImageUtil.buildFileName(filePath, "_m.png"));
					} else if("s".equals(size)) {
						barray = ImageUtil.loadImage(ImageUtil.buildFileName(filePath, "_s.png"));
					} else if("xs".equals(size)) {
						barray = ImageUtil.loadImage(ImageUtil.buildFileName(filePath, "_xs.png"));
					} else if("original".equals(size)) {
						barray = ImageUtil.loadImage(filePath);
					} else {
						barray = ImageUtil.loadImage(ImageUtil.buildFileName(filePath, "_l.png"));
					}
				} catch (Exception e) {
					
					if("l".equals(size)) {
						barray = ImageUtil.loadImage(ImageUtil.buildFileName(defaultImage, "_l.png"));
					} else if("m".equals(size)) {
						barray = ImageUtil.loadImage(ImageUtil.buildFileName(defaultImage, "_m.png"));
					} else if("s".equals(size)) {
						barray = ImageUtil.loadImage(ImageUtil.buildFileName(defaultImage, "_s.png"));
					} else if("xs".equals(size)) {
						barray = ImageUtil.loadImage(ImageUtil.buildFileName(defaultImage, "_xs.png"));
					} else if("original".equals(size)) {
						barray = ImageUtil.loadImage(defaultImage);
					} else {
						barray = ImageUtil.loadImage(ImageUtil.buildFileName(defaultImage, "_l.png"));
					}
					
				}
				
				if (barray != null) {
					response.setContentType("image/jpeg");
					response.setHeader("Cache-Control", "public");
					outputStream.write(barray);
				}
				
			} else {
				
				if("l".equals(size)) {
					barray = ImageUtil.loadImage(ImageUtil.buildFileName(defaultImage, "_l.png"));
				} else if("m".equals(size)) {
					barray = ImageUtil.loadImage(ImageUtil.buildFileName(defaultImage, "_m.png"));
				} else if("s".equals(size)) {
					barray = ImageUtil.loadImage(ImageUtil.buildFileName(defaultImage, "_s.png"));
				} else if("xs".equals(size)) {
					barray = ImageUtil.loadImage(ImageUtil.buildFileName(defaultImage, "_xs.png"));
				} else if("original".equals(size)) {
					barray = ImageUtil.loadImage(defaultImage);
				} else {
					barray = ImageUtil.loadImage(ImageUtil.buildFileName(defaultImage, "_l.png"));
				}
				
				response.setContentType("image/png");
				response.setHeader("Cache-Control", "public");
				outputStream.write(barray);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			outputStream.flush();
			outputStream.close();
		}
		
		return null;
		
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/upload")
	public final String displayUploadForm(){
		return uploadPage;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/adjust")
	public final String displayAdjustForm(){
		return adjustPhoto;
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value="/upload", produces = "application/json")
	public final @ResponseBody Map<String, Object>
		processUpload(@Valid @ModelAttribute("command") final T command,
		BindingResult result, final HttpServletRequest request) {
		
		Map<String, Object> model = new HashMap<String, Object>();
		final List<MessageResponse> messages = new ArrayList<MessageResponse>();
		
		if(result.hasErrors()) {
			messages.addAll(convertErrorMessage(result,
					request.getLocale()));
			model.put("messages", messages);
			return model;
		}
		
		if (Photoable.class.isAssignableFrom(command.getClass())) { // ensure that the command implements Photoable.
			
			transactionTemplate.execute(new TransactionCallbackWithoutResult() {
				@SuppressWarnings("unchecked")
				protected void doInTransactionWithoutResult(TransactionStatus status) {

					Photoable photoable = (Photoable) command;
					FileInfo f = new PhotoInfo();
					f = fileUploadService.upload(photoable.getPhoto(), new PhotoInfo());
					
					try {
						ImageUtil.createPhotoThumbnails(f.getFullPath());
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					photoable.addPhoto((PhotoInfo) f);
					
					messages.addAll(buildSuccessMessage(command, "upload-photo", request.getLocale()));
					
					service.save((T) photoable);
				}
			});
			
		} else {
			System.out.println("Could not process upload : "
					+ command.getClass().getSimpleName()
					+ " does not implement Photoable");
		}
		
		model.put("messages", messages);
		return model;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/adjust", produces = "application/json")
	public final @ResponseBody Map<String, Object>
		processAdjust(final HttpServletRequest request, @ModelAttribute("command") final T command) {
		
		Map<String, Object> model = new HashMap<String, Object>();
		final List<MessageResponse> messages = new ArrayList<MessageResponse>();
		
		if (Photoable.class.isAssignableFrom(command.getClass())) { // ensure that the command implements Photoable.
			
			Photoable photoable = (Photoable) command;
			PhotoInfo photoInfo = photoable.getPhotos().get(photoable.getPhotos().size()-1);
			
			int x = Integer.parseInt(request.getParameter("x")); // X Coordinate
			int y = Integer.parseInt(request.getParameter("y")); // Y2 Coordinate
			int x2 = Integer.parseInt(request.getParameter("x2")); // X2 Coordinate 
			int y2 = Integer.parseInt(request.getParameter("y2")); // Y2 Coordinate
			int rw = Integer.parseInt(request.getParameter("rw")); // Resized width of Image
			
			try {
				ImageUtil.adjustPhotoThumbnails(photoInfo.getFullPath(), x, y, x2, y2, rw);
				messages.addAll(buildSuccessMessage(command, "adjust-photo", request.getLocale()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else {
			System.out.println("Could not process adjust : "
					+ command.getClass().getSimpleName()
					+ " does not implement Photoable");
		}
		
		model.put("messages", messages);
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
		
		if (StringUtil.isEmpty(this.uploadPage)) {
			this.uploadPage = "app/"
					+ NamingUtil.toElementName(this.entityBeanType
							.getSimpleName()) + "-photo-upload";
		}
		if (StringUtil.isEmpty(this.adjustPhoto)) {
			this.adjustPhoto = "app/"
				+ NamingUtil.toElementName(this.entityBeanType
						.getSimpleName()) + "-photo-adjust";
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
	 * Converts the binding error messages to list of MessageResponse
	 * 
	 * @param bindingResult
	 */
	protected List<MessageResponse> convertErrorMessage(
			BindingResult bindingResult, Locale locale) {
		List<MessageResponse> errorMessages = new ArrayList<MessageResponse>();
		if (bindingResult.hasErrors()) {
			for (ObjectError error : bindingResult.getAllErrors()) {
				MessageResponse message = null;
				if (error instanceof FieldError) {
					FieldError ferror = (FieldError) error;
					message = new MessageResponse(MessageResponse.Type.error,
							error.getObjectName(), ferror.getField(),
							error.getCodes(), error.getArguments());
				} else
					message = new MessageResponse(MessageResponse.Type.error,
							error.getObjectName(), null, error.getCodes(),
							error.getArguments());
				message.setMessage(messageSource.getMessage(message, locale));
				errorMessages.add(message);
			}
		}
		return errorMessages;
	}

	/**
	 * Builds success message by convention. Success messages are displayed as
	 * notifications only.
	 * 
	 * Standard convention in order of resolving message is: (1)
	 * message.<className>.
	 * <code>-success (e.g. message.system-codes.add-success)
	 *     (2) message.add-success (generic message)
	 * 
	 * @param elementClass
	 * @param object
	 * @param code
	 * @param locale
	 * @return
	 */
	protected List<MessageResponse> buildSuccessMessage(BaseEntity object,
			String code, Locale locale) {
		List<MessageResponse> messages = new ArrayList<MessageResponse>();
		Assert.notNull(object);
		String prefix = "message."
				+ NamingUtil.toElementName(object.getClass().getSimpleName());
		String codes = prefix + "." + code + "-success,message." + code
				+ "-success";
		MessageResponse message = new MessageResponse(
				MessageResponse.Type.notification, codes.split("\\,"), null);
		message.setMessage(messageSource.getMessage(message, locale));
		messages.add(message);
		return messages;
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder){
		binder.setValidator(photoValidator);
	}
	
}
