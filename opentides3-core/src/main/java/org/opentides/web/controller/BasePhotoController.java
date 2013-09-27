package org.opentides.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.opentides.service.FileUploadService;
import org.opentides.service.PhotoInfoService;
import org.opentides.util.CrudUtil;
import org.opentides.util.ImageUtil;
import org.opentides.util.NamingUtil;
import org.opentides.util.StringUtil;
import org.opentides.web.validator.PhotoValidator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Base class for all controllers that want to support photo functionalities. Command
 * object should implement the {@link Photoable} interface.
 * Refer to UserPhotoController for how to use.
 * 
 * @author AJ
 */
public abstract class BasePhotoController<T extends BaseEntity> {
	
	@Autowired
	protected PhotoInfoService photoInfoService;
	
	@Autowired
	protected PhotoValidator photoValidator;
	
	@Autowired
	@Qualifier("defaultFileUploadService")
	protected FileUploadService fileUploadService;
	
	protected String uploadPage = "";
	protected String adjustPhoto = "";

	@Autowired
	protected BeanFactory beanFactory;

	@Autowired
	protected MessageSource messageSource;
	
	protected Class<T> entityBeanType;
	
	protected BaseCrudService<T> service;
	
	/**
	 * This method handlers loads the command object given the 
	 * passed parameter {@code id}
	 * 
	 * @param id
	 * @return the command object
	 */
	@ModelAttribute("command") 
	protected abstract T getPhotoable(Long id);
	
	@RequestMapping(method = RequestMethod.GET)
	public String displayImage(ModelMap modelMap,
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
	
	/**
	 * This displays the page for uploading a photo.
	 * 
	 * @return the upload page
	 */
	@RequestMapping(method = RequestMethod.GET, value="/upload")
	public String displayUploadForm(){
		return uploadPage;
	}
	
	/**
	 * This displays the form used to adjust the uploaded photo.
	 * 
	 * @return the from for adjusting a photo
	 */
	@RequestMapping(method = RequestMethod.GET, value="/adjust")
	public String displayAdjustForm(){
		return adjustPhoto;
	}
	
	
	/**
	 * The method handler is responsible for processing the command object.
	 * It saves the uploaded photo and creates the corresponsing thumbnails
	 * of sizes L, M, S and XS. It also populates the {@link PhotoInfo }
	 * of the uploaded photo.
	 * 
	 * @param command object the implements the {@link Photoable} interface
	 * @param result
	 * @param request
	 * @return Map containing the following:<br />
	 * 			messages - list of {@code MessageResponse }
	 * 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST, value="/upload", produces = "application/json")
	public @ResponseBody Map<String, Object>
		processUpload(@Valid @ModelAttribute("command") T command,
		BindingResult result, HttpServletRequest request) {
		
		Map<String, Object> model = new HashMap<String, Object>();
		List<MessageResponse> messages = new ArrayList<MessageResponse>();
		
		if(result.hasErrors()) {
			messages.addAll(CrudUtil.convertErrorMessage(result,
					request.getLocale(), messageSource));
			model.put("messages", messages);
			return model;
		}
		
		if (Photoable.class.isAssignableFrom(command.getClass())) { // ensure that the command implements Photoable.
			
			Photoable photoable = (Photoable) command;
			FileInfo f = fileUploadService.upload(photoable.getPhoto(), new PhotoInfo());
			
			try {
				ImageUtil.createPhotoThumbnails(f.getFullPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			photoable.addPhoto((PhotoInfo) f);
			
			messages.addAll(CrudUtil.buildSuccessMessage(command, "upload-photo", request.getLocale(), messageSource));
			
			service.save((T) photoable);
			
		} else {
			System.out.println("Could not process upload : "
					+ command.getClass().getSimpleName()
					+ " does not implement Photoable");
		}
		
		model.put("messages", messages);
		return model;
	}
	
	/**
	 * The method handler is responsible for processing the thumbnails of the adjusted 
	 * command object.
	 *  
	 * @param request containing the parameters {@code x} coordinate, {@code y} coordinate, 
	 * {@code x2} coordinate, {@code y2} coordinate and the {@code rw} resized width of the photo 
	 * @param command object the implements the {@link Photoable} interface
	y * @return Map containing the following:<br />
	 * 			messages - list of {@code MessageResponse }
	 */
	@RequestMapping(method = RequestMethod.POST, value="/adjust", produces = "application/json")
	public @ResponseBody Map<String, Object>
		processAdjust(HttpServletRequest request, @ModelAttribute("command") T command) {
		
		Map<String, Object> model = new HashMap<String, Object>();
		List<MessageResponse> messages = new ArrayList<MessageResponse>();
		
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
				messages.addAll(CrudUtil.buildSuccessMessage(command, "adjust-photo", request.getLocale(), messageSource));
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
	
	/**
	 * This is a post construct that set ups the service for the
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

	}
	
	/**
	 * Method that attaches the autowired photo validator to the binder
	 * 
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder){
		binder.setValidator(photoValidator);
	}
	
}
