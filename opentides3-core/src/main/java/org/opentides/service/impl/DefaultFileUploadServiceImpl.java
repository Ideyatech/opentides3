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
	public FileInfo upload(MultipartFile multipartFile, String destination) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFilename(multipartFile.getOriginalFilename());
        fileInfo.setFileSize(Long.valueOf(multipartFile.getSize()));
        fileInfo.setOriginalFileName(multipartFile.getOriginalFilename());
        
        File directory = FileUtil.createDirectory(destination);
        String subdir = (new StringBuilder())
                .append(directory.getAbsoluteFile()).append(File.separator)
                .append(DateUtil.convertShortDate(new Date())).toString();
        File subDirectory = FileUtil.createDirectory(subdir);
        String filePath = (new StringBuilder())
                .append(subDirectory.getAbsoluteFile())
                .append(File.separator)
                .append(multipartFile.getOriginalFilename()).toString();
        
        Long fileCnt = Long.valueOf(1L);
        
        while (fileInfoService.getFileInfoByFullPath(filePath) != null) {
            
            String newFilePath;
            newFilePath = (new StringBuilder())
                    .append(subDirectory.getAbsoluteFile())
                    .append(File.separator).append(fileCnt.toString())
                    .append("_")
                    .append(multipartFile.getOriginalFilename()).toString();
            fileCnt++;
            filePath = newFilePath;
        }

        File uploadFile = new File(filePath);
        fileInfo.setFullPath(filePath);
        
        try {
            FileUtil.copyMultipartFile(multipartFile, uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
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

}
