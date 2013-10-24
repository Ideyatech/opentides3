package org.opentides.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.opentides.annotation.Valid;
import org.opentides.bean.AjaxUpload;
import org.opentides.bean.BaseEntity;
import org.opentides.bean.FileInfo;
import org.opentides.bean.ImageInfo;
import org.opentides.bean.MessageResponse;
import org.opentides.bean.MessageResponse.Type;
import org.opentides.bean.ImageUploadable;
import org.opentides.service.BaseCrudService;
import org.opentides.service.FileUploadService;
import org.opentides.service.ImageInfoService;
import org.opentides.util.CrudUtil;
import org.opentides.util.ImageUtil;
import org.opentides.util.NamingUtil;
import org.opentides.util.StringUtil;
import org.opentides.web.validator.ImageValidator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Generic class for handling Image functionalities.
 * 
 * @author AJ
 * @author allanctan
 * @author gino
 */
@RequestMapping("/image")
@Controller
public class ImageController {
	
	private static Logger _log = Logger.getLogger(ImageController.class);

	@Autowired
	protected ImageInfoService imageInfoService;
	
	@Autowired
	protected ImageValidator imagwValidator;
	
	@Autowired
	@Qualifier("defaultFileUploadService")
	protected FileUploadService fileUploadService;
	
	@Autowired
	protected BeanFactory beanFactory;

	@Autowired
	protected MessageSource messageSource;
	
	@Value("#{applicationSettings.imageUploadPage}")
	protected String uploadPage = "";
	
	@Value("#{applicationSettings.imageAdjustPage}")
	protected String adjustPhoto = "";	

	/**
	 * 
	 * @param modelMap
	 * @param id
	 * @param c
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "{id}", method = RequestMethod.GET, produces = "image/png")
	public String load(
			ModelMap modelMap,
			@PathVariable("id") Long id,
			@RequestParam(value="c", required=false) String c,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
				
		byte[] barray = null;
		OutputStream outputStream = response.getOutputStream();

		try {
			ImageInfo info = imageInfoService.load(id);
			if(info == null) {
				barray = ImageUtil.getDefaultImage();
			} else {
				barray = ImageUtil.loadImage(getImagePath(info), c);
			}
			if (barray != null) {
				response.setContentType("image/png");
				response.setHeader("Cache-Control", "public");
				outputStream.write(barray);
			}
		} catch (Exception e) {
			_log.error("Failed to load image with PhotoInfo id ["+id+"], command["+c+"].", e);
		} finally {
			outputStream.flush();
			outputStream.close();
		}		
		return null;		
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = "image/png")
	public String loadEmpty(HttpServletRequest request, HttpServletResponse response) throws IOException {
		byte [] byteArray = ImageUtil.getDefaultImage();
		response.setContentType("image/png");
		response.setHeader("Cache-Control", "public");
		
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			outputStream.write(byteArray);
		} catch (Exception e) {
			_log.error("Failed to load default image.", e);
		} finally {
			outputStream.flush();
			outputStream.close();
		}	
		
		return null;
	}
	
	/**
	 * Get the correct image path. 
	 * @param imageInfo
	 * @return
	 */
	private String getImagePath(ImageInfo imageInfo) {
		if(!StringUtil.isEmpty(imageInfo.getCommand())) {
			int idx = imageInfo.getFullPath().lastIndexOf(".");
			return imageInfo.getFullPath().substring(0, idx) + "_" + imageInfo.getCommand() +".png";
		}
		return imageInfo.getFullPath();
	}
	
	/**
	 * Display the upload form.
	 * 
	 * @param modelMap
	 * @param id the ID of the current {@link ImageInfo} object
	 * @param classId the ID of the entity holding the image
	 * @param className the name of the class
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value="/upload")
	public String displayUploadForm(ModelMap modelMap, 
			@RequestParam(value="imageId", required = false) Long id,
			@RequestParam(value="classId") Long classId,
			@RequestParam(value="className") String className){
		modelMap.put("imageId", id);
		modelMap.put("className", className);
		modelMap.put("classId", classId);
		return uploadPage;
	}
	
	/**
	 * Display the image adjust form.
	 * 
	 * @param id the ID of the current {@link ImageInfo} object
	 * @param classId the ID of the entity holding the image
	 * @param className the name of the class
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value="/adjust")
	public String displayAdjustForm(@RequestParam(value = "imageId") Long id,
			@RequestParam(value = "classId") Long classId,
			@RequestParam(value = "className") String className,
			ModelMap modelMap) {
		modelMap.put("imageId", id);
		modelMap.put("className", className);
		modelMap.put("classId", classId);
		return adjustPhoto;
	}
	
	/**
	 * Crop the image. Set the request parameter replaceOriginal to true to also replace the original
	 * with the cropped one. Setting it to false will only create a cached image of the file. 
	 * (Check {@link ImageUtil#loadImage(String, String) ImageUtil.loadImage} )
	 *  
	 * @param id ID of the ImageInfo object
	 * @param topLeftX the x-coordinate of the top left most point of the cropping rectangle
	 * @param topLeftY the y-coordinate of the top left most point of the cropping rectangle
	 * @param bottomRightX the x-coordinate of the bottom right most point of the cropping rectangle
	 * @param bottomRightYthe y-coordinate of the bottom right most point of the cropping rectangle
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value="/adjust")
	public @ResponseBody Map<String, Object> adjustImage(@RequestParam(value = "imageId") Long id,
			@RequestParam("x1") Integer topLeftX,
			@RequestParam("y1") Integer topLeftY,
			@RequestParam("x2") Integer bottomRightX,
			@RequestParam("y2") Integer bottomRightY,
			@RequestParam("rw") Integer resizedWidth,
			@RequestParam(value = "replaceOriginal", required = false) boolean replaceOriginal,
			ModelMap modelMap,
			HttpServletRequest request) {
		
		Map<String, Object> model = new HashMap<String, Object>();
		List<MessageResponse> messages = new ArrayList<MessageResponse>();
		
		int newWidth = Math.abs(bottomRightX - topLeftX);
		int newHeight = Math.abs(bottomRightY - topLeftY);
		
		ImageInfo currentImage = imageInfoService.load(id);
		try {
			String command = ImageUtil.createCropCommand(newWidth, newHeight, topLeftX, topLeftY);
			ImageUtil.cropImage(getImagePath(currentImage), command, resizedWidth, replaceOriginal);
			String newCommand = (!StringUtil.isEmpty(currentImage.getCommand()) ? currentImage.getCommand() + "_" : "" )
					+ command;
			currentImage.setCommand(newCommand);
			imageInfoService.save(currentImage);
			messages.addAll(CrudUtil.buildSuccessMessage(currentImage, "upload-photo", request.getLocale(), messageSource));
		} catch (IOException e) {
			_log.error("Error encountered while cropping Image.", e);
			MessageResponse errorMessage = new MessageResponse(Type.error,
					new String[] { "message.adjust-photo-failed" },
					new Object[] { e.getMessage() });
			messages.add(errorMessage);
		}
		
		model.put("messages", messages);
		return model;
	}
	
	/**
	 * Process image upload. 
	 * 
	 * <p>
	 * This method can automatically attach the image to an entity if the parameters className
	 * and id were provided. The ImageUploadable class needs to have a service class implementing {@link BaseCrudService}
	 * since it will search via convention the required service. 
	 * <p>
	 * 
	 * <p>
	 * This will return a map containing the list of MessageResponse objects. The MessageResponse will
	 * contain error messages if there are validation problems or upload problems. Otherwise it
	 * will just return a success message.
	 * </p>
	 * 
	 * @param image
	 * @param className the class name of the ImageUploadable entity. 
	 * @param id the ID of the ImageUploadable entity
	 * @param isPrimary if the image is the primary photo of the entity
	 * @param result contains any validation errors
	 * @param request
	 * @return JSON format of a map containing the list of MessageResponse objects and the id of the ImageInfo. 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.POST, value="/upload", produces = "application/json")
	public @ResponseBody Map<String, Object>
		processUpload(@Valid @ModelAttribute("image") AjaxUpload image,
			@RequestParam(value = "className", required = false) String className, 
			@RequestParam(value = "classId", required = false) Long id,
			@RequestParam(value = "isPrimary", required = false) boolean isPrimary,
			BindingResult result, HttpServletRequest request) {
		
		BaseEntity entity = null;
		BaseCrudService service = null;
		if(!StringUtil.isEmpty(className) && id != null) {
			String attributeName = NamingUtil.toAttributeName(className);
			String serviceBean = attributeName + "Service";
			
			service = (BaseCrudService) beanFactory.getBean(serviceBean);
			Assert.notNull(service, "Entity " + attributeName
					+ " is not associated with a service class [" + serviceBean
					+ "]. Please check your configuration.");
			
			entity = service.load(id);
			Assert.notNull(entity, "No " + className + " object found for the given ID [" + id + "]");
			Assert.isAssignable(ImageUploadable.class, entity.getClass(), "Object is not ImageUploadable");
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		List<MessageResponse> messages = new ArrayList<MessageResponse>();
		
		if(result.hasErrors()) {
			messages.addAll(CrudUtil.convertErrorMessage(result,
					request.getLocale(), messageSource));
			model.put("messages", messages);
			return model;
		}
				
		FileInfo f = fileUploadService.upload(image.getAttachment());
		ImageInfo imageInfo = new ImageInfo(f);
		imageInfo.setIsPrimary(isPrimary);
		imageInfoService.save(imageInfo);
		
		if(entity != null) {
			//Attach to entity
			ImageUploadable imageUploadable = (ImageUploadable)entity;
			if(isPrimary) {
				if(imageUploadable.getPhotos() != null) {
					for(ImageInfo io : imageUploadable.getPhotos()) {
						io.setIsPrimary(false);
						imageInfoService.save(io);
					}
				}
			}
			imageUploadable.addPhoto(imageInfo);
			service.save(entity);
		}
		
		messages.addAll(CrudUtil.buildSuccessMessage(imageInfo, "upload-photo", request.getLocale(), messageSource));
		model.put("messages", messages);
		model.put("imageId", imageInfo.getId());
		return model;
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder){
		binder.setValidator(imagwValidator);
	}
	
}
