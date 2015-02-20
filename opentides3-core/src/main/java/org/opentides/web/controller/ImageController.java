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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.opentides.annotation.Valid;
import org.opentides.bean.AjaxUpload;
import org.opentides.bean.BaseEntity;
import org.opentides.bean.FileInfo;
import org.opentides.bean.ImageInfo;
import org.opentides.bean.ImageUploadable;
import org.opentides.bean.MessageResponse;
import org.opentides.bean.MessageResponse.Type;
import org.opentides.exception.DataAccessException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Generic class for handling Image functionalities. To extend this controller,
 * simply extend and implement doLoad method.
 * 
 * @author AJ
 * @author allanctan
 * @author gino
 */
@RequestMapping("/image")
@Controller
public class ImageController {

	private static final Logger _log = Logger.getLogger(ImageController.class);

	@Autowired
	protected ImageInfoService imageInfoService;

	@Autowired
	protected ImageValidator imageValidator;

	@Autowired
	protected FileUploadService defaultFileUploadService;

	@Autowired
	protected BeanFactory beanFactory;

	@Autowired
	protected MessageSource messageSource;

	@Value("#{applicationSettings.imageUploadPage}")
	protected String uploadPage = "";

	@Value("#{applicationSettings.imageAdjustPage}")
	protected String adjustPhoto = "";

	@Value("#{applicationSettings['defaultImageLocation']}")
	private String defaultImageLocation;

	/**
	 * Loads the default image
	 * 
	 * @param request
	 * @return
	 */
	private final byte[] defaultImage(HttpServletRequest request) {
		if (StringUtil.isEmpty(defaultImageLocation)) {
			return ImageUtil.getDefaultImage();
		} else {
			HttpSession session = request.getSession();
			ServletContext sc = session.getServletContext();
			InputStream is = sc.getResourceAsStream(defaultImageLocation);
			byte[] barray;

			try {
				barray = IOUtils.toByteArray(is);
			} catch (IOException e) {
				_log.error("Failed to load default image ["
						+ defaultImageLocation + "].", e);
				return ImageUtil.getDefaultImage();
			}

			return barray;
		}
	}

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
			@RequestParam(value = "c", required = false) String c,
			@RequestParam(value = "replaceOld", required = false) Boolean replaceOld,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		ImageInfo info = imageInfoService.load(id);

		byte[] barray = null;
		OutputStream outputStream = response.getOutputStream();

		try {
			if (info == null) {
				barray = defaultImage(request);
			} else {
				if (StringUtil.isEmpty(c)
						&& !StringUtil.isEmpty(info.getCommand())) {
					c = info.getCommand();
				}

				boolean replaceCache = replaceOld != null ? replaceOld : false;
				barray = ImageUtil.loadImage(info.getFullPath(), c,
						replaceCache);
			}
			if (barray != null) {
				response.setContentType("image/png");
				response.setHeader("Cache-Control", "no-cache");
				outputStream.write(barray);
			}
		} catch (Exception e) {
			_log.error("Failed to load image with PhotoInfo id [" + id
					+ "], command[" + c + "].", e);
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
	public String loadEmpty(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		response.setContentType("image/png");
		response.setHeader("Cache-Control", "no-cache");

		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			outputStream.write(defaultImage(request));
		} catch (Exception e) {
			_log.error("Failed to load default image.", e);
		} finally {
			outputStream.flush();
			outputStream.close();
		}

		return null;
	}

	/**
	 * Display the upload form.
	 * 
	 * @param modelMap
	 * @param id
	 *            the ID of the current {@link ImageInfo} object
	 * @param classId
	 *            the ID of the entity holding the image
	 * @param className
	 *            the name of the class
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/upload")
	public String displayUploadForm(ModelMap modelMap,
			@RequestParam(value = "imageId", required = false) Long id,
			@RequestParam(value = "classId") Long classId,
			@RequestParam(value = "className") String className) {
		modelMap.put("attachmentId", id);
		modelMap.put("className", className);
		modelMap.put("classId", classId);
		modelMap.put("date", System.currentTimeMillis());
		return uploadPage;
	}

	/**
	 * Display the image adjust form.
	 * 
	 * @param id
	 *            the ID of the current {@link ImageInfo} object
	 * @param classId
	 *            the ID of the entity holding the image
	 * @param className
	 *            the name of the class
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/adjust")
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
	 * Crop the image. Set the request parameter replaceOriginal to true to also
	 * replace the original with the cropped one. Setting it to false will only
	 * create a cached image of the file. (Check
	 * {@link ImageUtil#loadImage(String, String) ImageUtil.loadImage} )
	 * 
	 * @param id
	 *            ID of the ImageInfo object
	 * @param topLeftX
	 *            the x-coordinate of the top left most point of the cropping
	 *            rectangle
	 * @param topLeftY
	 *            the y-coordinate of the top left most point of the cropping
	 *            rectangle
	 * @param bottomRightX
	 *            the x-coordinate of the bottom right most point of the
	 *            cropping rectangle
	 * @param bottomRightYthe
	 *            y-coordinate of the bottom right most point of the cropping
	 *            rectangle
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/adjust")
	public @ResponseBody Map<String, Object> adjustImage(
			@RequestParam(value = "imageId") Long id,
			@RequestParam("x1") Integer topLeftX,
			@RequestParam("y1") Integer topLeftY,
			@RequestParam("x2") Integer bottomRightX,
			@RequestParam("y2") Integer bottomRightY,
			@RequestParam("rw") Integer resizedWidth,
			@RequestParam(value = "replaceOriginal", required = false) boolean replaceOriginal,
			ModelMap modelMap, HttpServletRequest request) {

		Map<String, Object> model = new HashMap<String, Object>();
		List<MessageResponse> messages = new ArrayList<MessageResponse>();

		int newWidth = Math.abs(bottomRightX - topLeftX);
		int newHeight = Math.abs(bottomRightY - topLeftY);

		ImageInfo currentImage = imageInfoService.load(id);
		try {
			String command = ImageUtil.createCropCommand(newWidth, newHeight,
					topLeftX, topLeftY);
			ImageUtil.cropImage(currentImage.getFullPath(), command,
					resizedWidth, replaceOriginal);
			currentImage.setCommand(command);
			imageInfoService.save(currentImage);
			messages.addAll(CrudUtil.buildSuccessMessage(currentImage,
					"upload-photo", request.getLocale(), messageSource));
			model.put("imageId", currentImage.getId());
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
	 * This method can automatically attach the image to an entity if the
	 * parameters className and id were provided. The ImageUploadable class
	 * needs to have a service class implementing {@link BaseCrudService} since
	 * it will search via convention the required service.
	 * <p>
	 * 
	 * <p>
	 * This will return a map containing the list of MessageResponse objects.
	 * The MessageResponse will contain error messages if there are validation
	 * problems or upload problems. Otherwise it will just return a success
	 * message.
	 * </p>
	 * 
	 * @param image
	 * @param className
	 *            the class name of the ImageUploadable entity.
	 * @param classId
	 *            the ID of the ImageUploadable entity
	 * @param isPrimary
	 *            if the image is the primary photo of the entity
	 * @param result
	 *            contains any validation errors
	 * @param request
	 * @return JSON format of a map containing the list of MessageResponse
	 *         objects and the id of the ImageInfo.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.POST, value = "/", produces = "application/json")
	public @ResponseBody Map<String, Object> processUpload(
			@Valid @ModelAttribute("image") AjaxUpload image,
			@RequestParam(value = "className", required = false) String className,
			@RequestParam(value = "classId", required = false) Long classId,
			@RequestParam(value = "isPrimary", required = false) boolean isPrimary,
			@RequestParam(value = "folderName", required = false) String folderName,
			BindingResult result, HttpServletRequest request) {

		Map<String, Object> model = new HashMap<String, Object>();
		List<MessageResponse> messages = new ArrayList<MessageResponse>();

		if (result.hasErrors()) {
			messages.addAll(CrudUtil.convertErrorMessage(result,
					request.getLocale(), messageSource));
			model.put("messages", messages);
			return model;
		}

		FileInfo f = defaultFileUploadService.upload(image.getAttachment());

		ImageInfo imageInfo = new ImageInfo(f);
		imageInfo.setIsPrimary(isPrimary);
		imageInfoService.save(imageInfo);

		if (!StringUtil.isEmpty(className) && classId != null) {
			String attributeName = NamingUtil.toAttributeName(className);
			String serviceBean = attributeName + "Service";

			BaseCrudService service = (BaseCrudService) beanFactory
					.getBean(serviceBean);
			Assert.notNull(service, "Entity " + attributeName
					+ " is not associated with a service class [" + serviceBean
					+ "]. Please check your configuration.");

			BaseEntity entity = service.load(classId);
			Assert.notNull(entity, "No " + className
					+ " object found for the given ID [" + classId + "]");
			Assert.isAssignable(ImageUploadable.class, entity.getClass(),
					"Object is not ImageUploadable");

			// Attach to entity
			ImageUploadable imageUploadable = (ImageUploadable) entity;

			if (isPrimary) {
				if (imageUploadable.getImages() != null) {
					for (ImageInfo io : imageUploadable.getImages()) {
						io.setIsPrimary(false);
						imageInfoService.save(io);
					}
				}
			}

			imageUploadable.addImage(imageInfo);
			service.save(entity);
			model.put("uplodable", entity);
		}

		messages.addAll(CrudUtil.buildSuccessMessage(imageInfo, "upload-photo",
				request.getLocale(), messageSource));
		model.put("messages", messages);
		model.put("attachmentId", imageInfo.getId());
		model.put("attachmentName", imageInfo.getOriginalFileName());
		return model;
	}

	/**
	 * Deletes the image {@link ImageInfo} and detaches it from the associated
	 * entity.
	 * 
	 * <p>
	 * It expects that {@code className} and {@code classId} pertaining to the
	 * owning entity are passed as form arguments.
	 * </p>
	 * 
	 * <p>
	 * We are using MultiValueMap to get the passed form body since by default
	 * Tomcat will only parse arguments in the form style when the HTTP method
	 * is POST. This will ensure compatibility without the need to modify server
	 * configuration.
	 * </p>
	 * 
	 * @param id
	 *            - the id of the ImageInfo to be deleted
	 * @param formData
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public @ResponseBody Map<String, Object> deleteImageInfo(
			@PathVariable("id") Long id,
			@RequestBody MultiValueMap<String, String> formData,
			HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		List<MessageResponse> messages = new ArrayList<MessageResponse>();
		if (id > 0) {
			String className = formData.getFirst("className");
			String classId = formData.getFirst("classId");
			if (!StringUtil.isEmpty(classId) && !StringUtil.isEmpty(className)) {
				_log.debug("Processing image for entity " + className
						+ " with id [" + classId + "]");
				ImageInfo imageInfo = imageInfoService.load(id);

				// load the owning entity using its corresponding service
				String attributeName = NamingUtil.toAttributeName(className);
				String serviceBean = attributeName + "Service";

				BaseCrudService service = (BaseCrudService) beanFactory
						.getBean(serviceBean);
				Assert.notNull(service, "Entity " + attributeName
						+ " is not associated with a service class ["
						+ serviceBean + "]. Please check your configuration.");

				BaseEntity entity = service.load(classId);
				Assert.notNull(entity, "No " + className
						+ " object found for the given ID [" + classId + "]");
				Assert.isAssignable(ImageUploadable.class, entity.getClass(),
						"Object is not ImageUploadable");

				// remove image for the entity and delete record
				ImageUploadable imageUploadable = (ImageUploadable) entity;
				imageUploadable.getImages().remove(imageInfo);
				imageInfoService.delete(id);

				messages.addAll(CrudUtil.buildSuccessMessage(imageInfo,
						"delete", request.getLocale(), messageSource));
				model.put("messages", messages);
			}

			return model;
		} else {
			String message = "Invalid id = [" + id
					+ "] for delete operation of "
					+ ImageInfo.class.getSimpleName();
			_log.error(message);
			throw new DataAccessException(message);
		}
	}

	@InitBinder(value = "formCommand")
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(imageValidator);
	}

}
