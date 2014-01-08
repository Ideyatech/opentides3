package org.opentides.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opentides.annotation.Valid;
import org.opentides.bean.AjaxUpload;
import org.opentides.bean.BaseEntity;
import org.opentides.bean.FileInfo;
import org.opentides.bean.MessageResponse;
import org.opentides.bean.Uploadable;
import org.opentides.service.BaseCrudService;
import org.opentides.service.FileInfoService;
import org.opentides.service.FileUploadService;
import org.opentides.util.CrudUtil;
import org.opentides.util.NamingUtil;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author Ronielson
 *
 */

@RequestMapping("/file")
@Controller
public class FileController {

	@Value("#{applicationSettings.imageUploadPage}")
	protected String uploadPage = "";
	
	@Autowired
	protected BeanFactory beanFactory;
	
	@Autowired
	protected MessageSource messageSource;
	
	@Autowired
	@Qualifier("defaultFileUploadService")
	protected FileUploadService fileUploadService;
	
	@Autowired
	protected FileInfoService fileInfoService;
	

	private Map<String, String> contentTypes = new HashMap<>();
	
	@Autowired(required = false)
	private Map<String, String> additionalContentType;
	
	public FileController(){
		contentTypes.put(".pdf", "application/pdf");
		contentTypes.put(".doc", "application/msword");
		contentTypes.put(".odt", "application/vnd.oasis.opendocument.text");
		contentTypes.put(".xls", "application/vnd.ms-excel");
		contentTypes.put(".png", "image/png");
		contentTypes.put(".gif", "image/gif");
		contentTypes.put(".jpeg", "image/jpeg");
		contentTypes.put(".jpg", "image/jpeg");
		contentTypes.put(".gz", "application/x-gzip");
		contentTypes.put(".zip", "application/zip");
	}
	
	@PostConstruct
	public void afterPropertiesSet(){
		if(additionalContentType!=null){
			contentTypes.putAll(contentTypes);
		}
	}
	
	/**
	 * Display the upload form
	 * @param modelMap
	 * @param id
	 * @param classId
	 * @param className
	 * @return
	 */
	//not yet finished
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
	
	public Map<String, String> getContentTypes() {
		return contentTypes;
	}
	
	public void setContentTypes(Map<String, String> contentTypes){
		this.contentTypes = contentTypes;
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.POST, value="/upload", produces = "application/json")
	public @ResponseBody Map<String, Object>
	processUpload(@Valid @ModelAttribute("file") AjaxUpload file,
		@RequestParam(value = "className", required = false) String className, 
		@RequestParam(value = "classId", required = false) Long id,
		BindingResult result, HttpServletRequest request){
		
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
			Assert.isAssignable(Uploadable.class, entity.getClass(), "Object is not Uploadable");
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		List<MessageResponse> messages = new ArrayList<MessageResponse>();
		
		if(result.hasErrors()) {
			messages.addAll(CrudUtil.convertErrorMessage(result,
					request.getLocale(), messageSource));
			model.put("messages", messages);
			return model;
		}
		
		
		FileInfo fileInfo = fileUploadService.upload(file.getAttachment());
		fileInfoService.save(fileInfo);
		
		if(entity != null) {
			//Attach to entity
			Uploadable uploadable = (Uploadable) entity;
			
			uploadable.addFile(fileInfo);
			service.save(entity);
		}
		
		messages.addAll(CrudUtil.buildSuccessMessage(fileInfo, "upload-file", request.getLocale(), messageSource));
		model.put("messages", messages);
		model.put("attachmentId", fileInfo.getId());
		model.put("attachmentName", fileInfo.getFilename());
		
		return model;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/download/{id}")
	public void downloadFile(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) throws IOException{
		FileInfo fileInfo = fileInfoService.load(id);
		
		byte[] b = fileInfoService.convertToByteArray(fileInfo);
		
		if(b!=null){
			response.setHeader("Content-disposition", "attachment; filename = \"" + fileInfo.getFilename() + "\"");
			int idx = fileInfo.getFilename().lastIndexOf(".");
			String extension = "";
			if (idx > 0){
				extension = fileInfo.getFilename().substring(idx);
			}
			if (!StringUtil.isEmpty(extension)){
				response.setContentType(contentTypes.get(extension));
			}
			
			response.getOutputStream().write(b);
		}
	}

	public Map<String, String> getAdditionalContentType() {
		return additionalContentType;
	}

	public void setAdditionalContentType(Map<String, String> additionalContentType) {
		this.additionalContentType = additionalContentType;
	}

}
