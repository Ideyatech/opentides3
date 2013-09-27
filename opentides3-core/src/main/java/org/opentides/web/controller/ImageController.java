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
import org.opentides.bean.FileInfo;
import org.opentides.bean.ImageInfo;
import org.opentides.bean.MessageResponse;
import org.opentides.service.FileUploadService;
import org.opentides.service.ImageInfoService;
import org.opentides.util.CrudUtil;
import org.opentides.util.ImageUtil;
import org.opentides.web.validator.PhotoValidator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
 * @author AJ, allanctan
 */
@RequestMapping("/image")
@Controller
public class ImageController {
	
	private static Logger _log = Logger.getLogger(ImageController.class);

	@Autowired
	protected ImageInfoService imageInfoService;
	
	@Autowired
	protected PhotoValidator photoValidator;
	
	@Autowired
	@Qualifier("defaultFileUploadService")
	protected FileUploadService fileUploadService;
	
	@Autowired
	protected BeanFactory beanFactory;

	@Autowired
	protected MessageSource messageSource;
	
	protected String uploadPage = "";
	
	protected String adjustPhoto = "";	

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
			barray = ImageUtil.loadImage(info.getFullPath(), c);			
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
	
	
	@RequestMapping(method = RequestMethod.GET, value="/upload")
	public String displayUploadForm(){
		return uploadPage;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/adjust")
	public String displayAdjustForm(){
		return adjustPhoto;
	}	
	
	@RequestMapping(method = RequestMethod.POST, value="/upload", produces = "application/json")
	public @ResponseBody Map<String, Object>
		processUpload(@Valid @ModelAttribute("image") AjaxUpload image,
		BindingResult result, HttpServletRequest request) {
		
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
		imageInfoService.save(imageInfo);
		
		messages.addAll(CrudUtil.buildSuccessMessage(imageInfo, "upload-photo", request.getLocale(), messageSource));
		model.put("messages", messages);
		return model;
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder){
		binder.setValidator(photoValidator);
	}
	
}
