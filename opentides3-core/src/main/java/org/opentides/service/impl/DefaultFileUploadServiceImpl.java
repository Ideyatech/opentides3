package org.opentides.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.opentides.bean.FileInfo;
import org.opentides.service.FileInfoService;
import org.opentides.service.FileUploadService;
import org.opentides.util.DateUtil;
import org.opentides.util.FileUtil;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Default file upload implementation
 * 
 * @author AJ
 *
 */
@Service(value="defaultFileUploadService")
@Transactional
public class DefaultFileUploadServiceImpl implements FileUploadService {

	@Value("${fileupload.basedir}")
	private String uploadPath;
	
	@Autowired
	private FileInfoService fileInfoService;
	
	@Override
	@Transactional
	public FileInfo upload(MultipartFile file, String fileId) {
		FileInfo fileInfo;
/*		
		if(StringUtil.isEmpty(fileId)) {
			// if fileId is null, create a random ID
			FileInfo exist = null;
			String newFileId = "";
			// ensure id is unique and non-existing in the database
			do {
				newFileId = StringUtil.generateRandomString(6);
				exist = fileInfoService.getLatestFileInfoByFileId(newFileId);				
			} while (exist != null);
			fileId = newFileId;
			fileInfo = new FileInfo(1l, fileId);
		} else {
			// file Id has been provided, ensure proper versioning
			FileInfo exist = fileInfoService.getLatestFileInfoByFileId(fileId);
			if (exist == null) 
				// non-existing, this is version 1
				fileInfo = new FileInfo(1l, fileId);
			else
				// existing, increment the version
				fileInfo = new FileInfo(fileInfo.getFileVersion()+1,fileId);			
		}
				
		fileInfo.setFilename(file.getOriginalFilename());
		fileInfo.setFileSize(Long.valueOf(file.getSize()));
		fileInfo.setOriginalFileName(file.getOriginalFilename());
		
		// build the directory to save the file
		String directory = new StringBuilder(uploadPath).append(File.separator)
				.append(DateUtil.convertShortDate(new Date())).toString();
		File fileDirectory = FileUtil.createDirectory(directory);
		
		// if file exist, append a number
		
		if (fileInfoService.getFileInfoByFullPath(path))
		File directory = FileUtil.createDirectory(destination);
		String subdir = 
		File subDirectory = 
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
*/
		return null;
	}
	
	@Override
	@Transactional
	public FileInfo upload(File file, String destination) {
		FileInfo fileInfo = new FileInfo();
		fileInfo.setFilename(file.getName());
		fileInfo.setFileSize(Long.valueOf(file.length()));
		fileInfo.setOriginalFileName(file.getName());
		
		File directory = FileUtil.createDirectory(destination);
		String subdir = (new StringBuilder())
				.append(directory.getAbsoluteFile()).append(File.separator)
				.append(DateUtil.convertShortDate(new Date())).toString();
		File subDirectory = FileUtil.createDirectory(subdir);
		String filePath = (new StringBuilder())
				.append(subDirectory.getAbsoluteFile())
				.append(File.separator)
				.append(file.getName()).toString();
		
		Long fileCnt = Long.valueOf(1L);
		
		while (fileInfoService.getFileInfoByFullPath(filePath) != null) {
			
			String newFilePath;
			newFilePath = (new StringBuilder())
					.append(subDirectory.getAbsoluteFile())
					.append(File.separator).append(fileCnt.toString())
					.append("_")
					.append(file.getName()).toString();
			fileCnt++;
			filePath = newFilePath;
		}

		File uploadFile = new File(filePath);
		fileInfo.setFullPath(filePath);
		
		//TODO Copy file to disk
		/*try {
			FileUtil.copyFile(file, uploadFile);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		return fileInfo;
	}

	@Override
	public FileInfo upload(MultipartFile multipartFile, String destination,
			boolean retainFilename) {
		return upload(multipartFile, destination);
	}
	
	@Override
	@Transactional
	public FileInfo upload(MultipartFile multipartFile) {
		return upload(multipartFile, uploadPath);
	}
	
	@Override
	public FileInfo upload(File file, String destination,
			boolean retainFilename) {
		return upload(file, destination);
	}
	
	@Override
	@Transactional
	public FileInfo upload(File file) {
		return upload(file, uploadPath);
	}
	
}
