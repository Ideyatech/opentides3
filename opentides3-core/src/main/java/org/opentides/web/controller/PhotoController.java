package org.opentides.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.opentides.bean.BaseEntity;
import org.opentides.bean.PhotoInfo;
import org.opentides.bean.Photoable;
import org.opentides.service.BaseCrudService;
import org.opentides.service.PhotoInfoService;
import org.opentides.util.DateUtil;
import org.opentides.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * 
 * @author AJ
 *
 */
@Controller
public abstract class PhotoController<T extends BaseEntity> {
	
	@Autowired
	protected PhotoInfoService photoInfoService;
	
	protected String uploadPath = (new StringBuilder()).append(File.separator)
			.append("uploads").toString();
	
	protected String uploadPage = "";

	protected BaseCrudService<T> service;
	
	@ModelAttribute("command") 
	protected abstract T getPhotoable();
	
	@RequestMapping(method = RequestMethod.GET, value="/upload")
	public final String displayUploadForm(ModelMap modelMap){
		
		return uploadPage;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST, value="/upload")
	public final String uploadPhoto(ModelMap modelMap, @ModelAttribute("command") T command) {
		
		if (Photoable.class.isAssignableFrom(command.getClass())) { // ensure that the command implements Photoable.
			
			Photoable photoable = (Photoable) command;
			
			PhotoInfo p = uploadPhoto(photoable.getPhoto());
			
			photoable.addPhoto(p);

			service.save((T) photoable);
		}

		modelMap.addAttribute("command", command);
		
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
			
			while (photoInfoService.getPhotoInfoByFullPath(filePath) != null) {
				Long fileCnt = Long.valueOf(1L);
				String newFilePath;
				newFilePath = (new StringBuilder())
						.append(subDirectory.getAbsoluteFile())
						.append(File.separator).append(fileCnt.toString())
						.append("_")
						.append(photo.getOriginalFilename()).toString();

				filePath = newFilePath;
			}

			File uploadFile = new File(filePath);
			photoInfo.setFullPath(uploadFile.getAbsolutePath());
			try {
				FileUtil.copyMultipartFile(photo, uploadFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			/*
			 * Create thumbnails for this photo
			 */
			//ImageUtil.createPhotoThumbnails(fileInfo.getFullPath());
			
			return photoInfo;

	}
	
}
