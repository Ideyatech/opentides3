package org.opentides.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.opentides.annotation.Valid;
import org.opentides.bean.AjaxUpload;
import org.opentides.service.FileInfoService;
import org.opentides.service.FileUploadService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Default controller for uploading and downloading of files.
 * API calls are patterned and extended after Google Drive API. 
 * 
 * Currently, the supported functions are:
 * 
 * insert      : POST /files
 * get         : GET  /files/{fileId}
 * update      : PUT  /files/{fileId}
 *             : POST /files/{fileId}
 * info        : GET  /files/{fileId}/info
 * get version : GET  /files/{fileId}/v/{version}
 * list        : GET  /files (?)
 * delete      : DELETE /files/{fileId}
 * archive     : POST /files/{fileId}/archive
 * restore     : POST /files/{fileId}/restore
 * purge       : POST /files/purge
 *           
 * Open Questions:
 *    - How do we restrict file access per user or user group?
 *    
 * @author Ronielson
 * @author allanctan
 * 
 */

@RequestMapping("/files")
@Controller
public class FileController {
	
	@Autowired
	protected BeanFactory beanFactory;
	
	@Autowired
	protected MessageSource messageSource;
	
	@Autowired
	protected FileUploadService fileUploadService;
	
	@Autowired
	protected FileInfoService fileInfoService;

	private Map<String, String> contentTypes = new HashMap<>();
	
	@Autowired(required = false)
	private Map<String, String> additionalContentType;
	
	private static final Logger _log = Logger.getLogger(BaseCrudController.class);
	
	public FileController(){
		contentTypes.put(".pdf", "application/pdf");
		contentTypes.put(".doc", "application/msword");
		contentTypes.put(".odt", "application/vnd.oasis.opendocument.text");
		contentTypes.put(".xls", "application/vnd.ms-excel");
		contentTypes.put(".png", "image/png");
		contentTypes.put(".gif", "image/gif");
		contentTypes.put(".jpeg","image/jpeg");
		contentTypes.put(".jpg", "image/jpeg");
		contentTypes.put(".gz",  "application/x-gzip");
		contentTypes.put(".zip", "application/zip");
	}

	/**
	 * This method handles uploading of files.
	 * Returns the corresponding fileId for the newly uploaded file
	 * 
	 * insert      : POST /files/ 
	 * 
	 * Question: What if developer wants to upload to a specific location.
	 * 
	 * @param file
	 * @param destination
	 * @param result
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Map<String, Object> processUpload(
			@Valid @ModelAttribute("file") AjaxUpload file,
			@ModelAttribute("dest") String destination,
			BindingResult result, HttpServletRequest request) {
		_log.debug("Initiating file upload...");
		return null;
	}
	
	/**
	 * This method handles downloading of files based on the given file id.
	 * 
	 * get         : GET  /files/{fileId}
	 * @param id
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET, value="/{fileId}")
	public void downloadLatestFile(@PathVariable("fileId") Long fileId, 
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		_log.debug("Initiating download["+fileId+"]...");		 
	}

	/**
	 * This method handles updating of files based on the given file id.
	 * By default, all file updates are versioned. 
	 * However, an override flag is available to overwrite the file.
	 * 
	 * update      : PUT  /files/{fileId}
	 *             : POST /files/{fileId}
	 * @param id
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(method = {RequestMethod.PUT, RequestMethod.POST}, value="/{fileId}")
	public void updateFile(
			@Valid @ModelAttribute("file") AjaxUpload file,
			@PathVariable("fileId") Long fileId,
			@ModelAttribute("dest") String destination,
			@ModelAttribute("overwrite") Boolean overwrite,
			HttpServletRequest request, HttpServletResponse response) {
		_log.debug("Updating file["+fileId+"]...");
	}
	
	/**
	 * This method returns the file info details of a given file id.
	 * This can be used to identify the file version and access permission.
	 * 
	 * info        : GET  /files/{fileId}/info
	 * 
	 * @param fileId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value="/{fileId}/info")
	public @ResponseBody Map<String, Object> getInfo(@PathVariable("fileId") Long fileId, 
			HttpServletRequest request, HttpServletResponse response) {
		_log.debug("Retrieving file info["+fileId+"]...");
		return null;		
	}
	 
	/**
	 * This method returns a specific versioned file for download based on the
	 * given file id and version.
	 * 
	 * @param fileId
	 * @param version
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET, value="/{fileId}/v/{version}")
	public void downloadVersionedFile(@PathVariable("fileId") Long fileId, 
			@PathVariable("version") Long version, 
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		_log.debug("Initiating download["+fileId+"] of version ["+version+"]...");
	}
	
	/**
	 * This method deletes the files for the given fileId.
	 * Delete also removes all version of the fileId. 
	 * However, delete does not physically delete the file 
	 * until a purge is executed.
	 * 
   	 * delete      : DELETE /files/{fileId}
	 * 
	 * @param fileId
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.DELETE, value="/{fileId}")
	public void delete(@PathVariable("fileId") Long fileId, 
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		_log.debug("Deleting file["+fileId+"]...");
	}


	/**
	 * This method archives the files for the given fileId.
	 * 
	 * archive     : POST /files/{fileId}/archive
	 * 
	 * @param fileId
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.GET}, value="/{fileId}/archive")
	public void archive(@PathVariable("fileId") Long fileId, 
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		_log.debug("Archiving file["+fileId+"]...");
	}
	
	/**
	 * This method restores the files for the given fileId.
	 * The file could be previously deleted or archived.
	 * 
	 * restore     : POST /files/{fileId}/restore
	 * 
	 * @param fileId
	 */
	@RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.GET}, value="/{fileId}/restore")
	public void restore(@PathVariable("fileId") Long fileId, 
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		_log.debug("Restoring file["+fileId+"]...");
	}

	/**
	 * This method purges all files that has been deleted.
	 * A flag to include archived files is available.
	 * 
	 * purge       : POST /files/purge
	 * 
	 * @param fileId
	 */
	@RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.GET}, value="/purge")
	public void purge(@ModelAttribute("includeArchived") Boolean includeArchived, 
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		_log.debug("Purging files...");
	}
	
/*
	@RequestMapping(method = RequestMethod.POST, value="/upload/{id}", produces = "application/json")
	public @ResponseBody Map<String, Object> processUpload(
			@Valid @ModelAttribute("file") AjaxUpload file,
			@PathVariable("id") fileId,
			BindingResult result, HttpServletRequest request) {

		Map<String, Object> model = new HashMap<String, Object>();
		List<MessageResponse> messages = new ArrayList<MessageResponse>();
		
		FileInfo fileInfo = fileUploadService.upload(file.getAttachment());
		fileInfoService.save(fileInfo);
				
		messages.addAll(CrudUtil.buildSuccessMessage(fileInfo, "upload-file", request.getLocale(), messageSource));
		model.put("messages", messages);
		model.put("attachmentId", fileInfo.getId());
		model.put("attachmentName", fileInfo.getFilename());
		
		return model;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/upload", produces = "application/json")
	public @ResponseBody Map<String, Object> processUpload(@Valid @ModelAttribute("file") AjaxUpload file,
			@PathVariable("id", )
		BindingResult result, HttpServletRequest request) {

		Map<String, Object> model = new HashMap<String, Object>();
		List<MessageResponse> messages = new ArrayList<MessageResponse>();
		
		FileInfo fileInfo = fileUploadService.upload(file.getAttachment());
		fileInfoService.save(fileInfo);
				
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
	
*/


	@PostConstruct
	public void afterPropertiesSet(){
		if(additionalContentType!=null){
			contentTypes.putAll(contentTypes);
		}
	}

	public Map<String, String> getAdditionalContentType() {
		return additionalContentType;
	}

	public void setAdditionalContentType(Map<String, String> additionalContentType) {
		this.additionalContentType = additionalContentType;
	}
	
	public Map<String, String> getContentTypes() {
		return contentTypes;
	}
	
	public void setContentTypes(Map<String, String> contentTypes){
		this.contentTypes = contentTypes;
	}

}
