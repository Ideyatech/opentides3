package org.opentides.web.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opentides.bean.BaseEntity;
import org.opentides.bean.PhotoInfo;
import org.opentides.bean.Photoable;
import org.opentides.service.BaseCrudService;
import org.opentides.service.PhotoInfoService;
import org.opentides.util.DateUtil;
import org.opentides.util.FileUtil;
import org.opentides.util.ImageUtil;
import org.opentides.util.NamingUtil;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


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
public abstract class PhotoController<T extends BaseEntity> {
	
	@Autowired
	protected PhotoInfoService photoInfoService;
	
	protected String uploadPath = (new StringBuilder()).append(File.separator)
			.append("uploads").toString();
	
	protected String uploadPage = "";
	protected String cropPage = "";

	@Autowired
	protected BeanFactory beanFactory;
	
	protected Class<T> entityBeanType;

	protected Validator formValidator;
	
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
						barray = ImageUtil.loadImage(filePath);
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
	
	@RequestMapping(method = RequestMethod.GET, value="/crop")
	public final String displayCropForm(){
		return cropPage;
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value="/upload")
	public final String processUpload(@ModelAttribute("command") final T command) {
		
		if (Photoable.class.isAssignableFrom(command.getClass())) { // ensure that the command implements Photoable.
			
			transactionTemplate.execute(new TransactionCallbackWithoutResult() {
				@SuppressWarnings("unchecked")
				protected void doInTransactionWithoutResult(TransactionStatus status) {

					Photoable photoable = (Photoable) command;
					PhotoInfo p = uploadPhoto(photoable.getPhoto());
					
					photoable.addPhoto(p);
					
					service.save((T) photoable);
				}
			});
			
		} else {
			System.out.println("Could not process upload : "
					+ command.getClass().getSimpleName()
					+ " does not implement Photoable");
		}
		
		return cropPage;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/crop")
	public final String processCrop(final HttpServletRequest request, @ModelAttribute("command") final T command) {
		
		if (Photoable.class.isAssignableFrom(command.getClass())) { // ensure that the command implements Photoable.
			
			transactionTemplate.execute(new TransactionCallbackWithoutResult() {
				@SuppressWarnings("unchecked")
				protected void doInTransactionWithoutResult(TransactionStatus status) {

					Photoable photoable = (Photoable) command;
					PhotoInfo photoInfo = photoable.getPhotos().get(photoable.getPhotos().size()-1);
					
					System.out.println(request);
					System.out.println(request.getParameter("x"));
					System.out.println(request.getParameter("x2"));
					System.out.println(request.getParameter("y"));
					System.out.println(request.getParameter("y2"));
					System.out.println(photoInfo);
					
					service.save((T) photoable);
				}
			});
			
		} else {
			System.out.println("Could not process crop : "
					+ command.getClass().getSimpleName()
					+ " does not implement Photoable");
		}
		
		return uploadPage;
	}
	
	public PhotoInfo uploadPhoto(CommonsMultipartFile photo) {

		PhotoInfo photoInfo = new PhotoInfo();
		photoInfo.setFilename(photo.getOriginalFilename());
		photoInfo.setFileSize(Long.valueOf(photo.getSize()));
		photoInfo.setOriginalFileName(photo.getOriginalFilename());
		
		File directory = FileUtil.createDirectory(uploadPath);
		String subdir = (new StringBuilder())
				.append(directory.getAbsoluteFile()).append(File.separator)
				.append(DateUtil.convertShortDate(new Date())).toString();
		File subDirectory = FileUtil.createDirectory(subdir);
		String filePath = (new StringBuilder())
				.append(subDirectory.getAbsoluteFile())
				.append(File.separator)
				.append(photo.getOriginalFilename()).toString();
		
		Long fileCnt = Long.valueOf(1L);
		while (photoInfoService.getPhotoInfoByFullPath(filePath) != null) {
			
			String newFilePath;
			newFilePath = (new StringBuilder())
					.append(subDirectory.getAbsoluteFile())
					.append(File.separator).append(fileCnt.toString())
					.append("_")
					.append(photo.getOriginalFilename()).toString();
			fileCnt++;
			filePath = newFilePath;
		}

		File uploadFile = new File(filePath);
		photoInfo.setFullPath(uploadFile.getAbsolutePath());
		
		try {
			FileUtil.copyMultipartFile(photo, uploadFile);
			ImageUtil.createPhotoThumbnails(photoInfo.getFullPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return photoInfo;

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
		if (StringUtil.isEmpty(this.uploadPage)) {
			this.uploadPage = "app/"
					+ NamingUtil.toElementName(this.entityBeanType
							.getSimpleName()) + "-photo-upload";
		}
		if (StringUtil.isEmpty(this.cropPage)) {
			this.cropPage = "app/"
				+ NamingUtil.toElementName(this.entityBeanType
						.getSimpleName()) + "-photo-crop";
		}
		Assert.notNull(service, this.getClass().getSimpleName()
				+ " is not associated with a service class [" + serviceBean
				+ "]. Please check your configuration.");

		// initialize transaction template.
		PlatformTransactionManager txManager = (PlatformTransactionManager) beanFactory
				.getBean("transactionManager");
		this.transactionTemplate = new TransactionTemplate(txManager);
	}
	
}
