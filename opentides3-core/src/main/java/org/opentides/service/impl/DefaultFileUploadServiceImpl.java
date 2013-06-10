package org.opentides.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.opentides.bean.FileInfo;
import org.opentides.service.FileInfoService;
import org.opentides.service.FileUploadService;
import org.opentides.util.DateUtil;
import org.opentides.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Default file upload implementation
 * 
 * @author AJ
 *
 */
@Service
public class DefaultFileUploadServiceImpl implements FileUploadService {

	private String uploadPath = (new StringBuilder()).append(File.separator)
			.append("uploads").toString();
	
	@Autowired
	private FileInfoService fileInfoService;
	
	public FileInfo upload(MultipartFile file, FileInfo fileInfo) {

		fileInfo.setFilename(file.getOriginalFilename());
		fileInfo.setFileSize(Long.valueOf(file.getSize()));
		fileInfo.setOriginalFileName(file.getOriginalFilename());
		
		File directory = FileUtil.createDirectory(uploadPath);
		String subdir = (new StringBuilder())
				.append(directory.getAbsoluteFile()).append(File.separator)
				.append(DateUtil.convertShortDate(new Date())).toString();
		File subDirectory = FileUtil.createDirectory(subdir);
		String filePath = (new StringBuilder())
				.append(subDirectory.getAbsoluteFile())
				.append(File.separator)
				.append(file.getOriginalFilename()).toString();
		
		Long fileCnt = Long.valueOf(1L);
		
		while (fileInfoService.getFileInfoByFullPath(filePath) != null) {
			
			String newFilePath;
			newFilePath = (new StringBuilder())
					.append(subDirectory.getAbsoluteFile())
					.append(File.separator).append(fileCnt.toString())
					.append("_")
					.append(file.getOriginalFilename()).toString();
			fileCnt++;
			filePath = newFilePath;
		}

		File uploadFile = new File(filePath);
		fileInfo.setFullPath(filePath);
		
		try {
			FileUtil.copyMultipartFile(file, uploadFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return fileInfo;

	}
	
}
